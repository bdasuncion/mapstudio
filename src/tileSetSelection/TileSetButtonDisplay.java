package tileSetSelection;
//this class handles the preview of the tiles to be
//set on the Map Canvas
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.util.Vector;

import javax.swing.JPanel;

import infoObjects.TileInfo;
import infoObjects.TileSetInfo;

public class TileSetButtonDisplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int widthInTiles;
	private int heightInTiles;
	private int tileWidth = 8;
	private int tileHeight = 8;
	//private Vector<BufferedImage> tileCollection;
	//private Vector<Integer> index;
	//private int firstIndex = 0;
	//private int lastIndex = 0;
	int paletteBankIndex = 0;
	private BufferedImage tiledDisplay;
	int dispWidth = 48;
	int dispHeight = 48;
	private String tileName;
	
	private TileSetInfo tileSetInfo;
	
	public TileSetButtonDisplay(TileSetInfo tsi) {
		setFromTileSetInfo(tsi);
	}
	
	public void setFromTileSetInfo(TileSetInfo tsi) {
		tileSetInfo = tsi;
		
		tileName = "TEMPORARY_NAME";
		
		widthInTiles = tileSetInfo.getWidthInTiles();
		heightInTiles = tileSetInfo.getHeightInTiles();
		
		TileInfo tileInfo = tsi.getTileSet().get(0);
		tileWidth = tileInfo.getTileImage().getWidth();
		tileHeight = tileInfo.getTileImage().getHeight();
		
		tiledDisplay = new BufferedImage(widthInTiles*tileWidth,
				heightInTiles*tileHeight, tileInfo.getTileImage().getType(), 
				(IndexColorModel)tileInfo.getTileImage().getColorModel());
		
		//System.out.println("FILE NAME " + tileSetInfo.getFileName());
		//System.out.println("TILE SIZE " + tileSetInfo.getTileSet().size());
		BufferedImage[] tiles = new BufferedImage[tileSetInfo.getTileSet().size()];
		for (int i = 0; i < tileSetInfo.getTileSet().size(); ++i) {
			tiles[i] = tileSetInfo.getTileSet().get(i).getTileImage();
		}
		
		//System.out.println("heightInTiles " + heightInTiles + " widthInTiles " + widthInTiles);
		int ARGB[] = new int[tileWidth*tileHeight];
		for(int i = 0; i<heightInTiles; i++) {
			for(int j = 0; j<widthInTiles; j++) {
				int x = j*tileWidth;
				int y = i*tileHeight;
				if ((widthInTiles*i + j) < tileSetInfo.getTileSet().size()) {
					tiles[widthInTiles*i + j].getRGB(0, 0, tileWidth, tileHeight, ARGB, 0, tileWidth);
					tiledDisplay.setRGB( x, y, tileWidth, tileHeight, ARGB, 0, tileWidth);
				}
			}
		}
		
		this.setSize(tiledDisplay.getWidth()*2, tiledDisplay.getHeight()*2);
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		this.setVisible(true);
		this.repaint();
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}
	
	public int getTileHeight() {
		return tileHeight;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public BufferedImage getTiledDisplay() {
		return tiledDisplay;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		BufferedImage disp;
		disp = g2D.getDeviceConfiguration().
			createCompatibleImage(dispWidth,dispHeight);
		disp = tiledDisplay;
		g2D.drawImage(disp, 0, 0, dispWidth,dispHeight, this);
		g2D.dispose();
		this.setPreferredSize(new Dimension(dispWidth,dispHeight));
		this.revalidate();
	}

	public String getTileName() {
		return tileName;
	}

	public void setTileName(String tileName) {
		this.tileName = tileName;
	}
}
