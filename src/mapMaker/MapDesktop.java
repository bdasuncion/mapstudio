package mapMaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import debugTools.PanelDisplay;
import infoObjects.CollisionInfo;
import infoObjects.MapInfo;
import infoObjects.TileInfo;
import infoObjects.TileSetInfo;
import mapCanvas.ModeSetting;
import mapCanvas.MapCanvasBlocks;
import mapCanvas.MapCanvasWxH;
import mapCanvas.MapScale;
import mapCanvas.VisibleLayer;
import mapInternalFrames.LayerOptionsInternalFrame;
import mapInternalFrames.MapCanvasInternalFrame;
import mapInternalFrames.TileInternalFrame;
import mapInternalFrames.TileSetInternalFrame;
import mapPalette.MapPalette;
import mapPalette.PaletteChooser;
import mapTileSet.TileSetPanel;
import mapTileSet.TileSetControlPanel;
import mapTiles.MapTilesInVRAM;
import tileSetSelection.TileSetSelection;
import tools.ImageTools;



public class MapDesktop extends JDesktopPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 331881428319863344L;
	private MapCanvasWxH mapView;
	private MapTilesInVRAM mapTilesInVRAM;
	//private TileSetPanel mapTileSet;
	//private MapPalette mapPalette;
	private PaletteChooser paletteChooser;
	
	private TileSetSelection tileSetCollection;
	
	private TileInternalFrame TileFrame;
	private MapCanvasInternalFrame mapFrame;
	private TileSetInternalFrame tileSetFrame;
	private JInternalFrame mapPaletteFrame;
	private JInternalFrame tilesetButtonsFrame;
	private LayerOptionsInternalFrame layerOptionFrame;
	
	private int tileWidth = 8;
	private int tileHeight = 8;
	private int collisionWidth = 8;
	private int collisionHeight = 8;
	private MapInfo mapInfo;
	
	public MapDesktop(JFrame owner, int width, int height,
			int tileW, int tileH) {
		this.setBackground(Color.GRAY);
		
		tileWidth = tileW;
		tileHeight = tileH;
		
		mapInfo = new MapInfo(width/8, height/8);
		
		TileFrame = new TileInternalFrame(tileWidth, tileHeight, mapInfo.getTileReference());
		mapTilesInVRAM = TileFrame.getMapTilesPanel();
		
		tileSetCollection = new TileSetSelection(mapInfo);
		JPanel tileSetCollectionPanel = new JPanel();
		tileSetCollectionPanel.setLayout(new BorderLayout());
		tileSetCollectionPanel.setBackground(Color.LIGHT_GRAY);
		tileSetCollectionPanel.add(tileSetCollection, BorderLayout.CENTER);
		
		tilesetButtonsFrame = new JInternalFrame("Tile set", true, true);
		tilesetButtonsFrame.setSize(400,75);
		tilesetButtonsFrame.add(tileSetCollectionPanel);
		tilesetButtonsFrame.setVisible(true);
		tilesetButtonsFrame.setLocation(500, 0);
		
		mapFrame = new MapCanvasInternalFrame(mapInfo, width, height, tileWidth, tileHeight);
		mapView = mapFrame.getMapView();

		tileSetFrame = new TileSetInternalFrame(owner, tileWidth, tileHeight, mapView, mapView);
		
		mapPaletteFrame = new JInternalFrame("Palette", false, true);
		mapPaletteFrame.setLayout(new GridLayout(2,1));

		layerOptionFrame = new LayerOptionsInternalFrame(mapFrame.getMapView(), 
				mapFrame.getMapView(), mapFrame.getMapView(), tileSetCollection);
		
		this.add(TileFrame);
		this.add(mapFrame);
		this.add(tileSetFrame);
		this.add(layerOptionFrame);
		this.add(mapPaletteFrame);
		this.add(tilesetButtonsFrame);
		
		tileSetCollection.createEraser(tileSetFrame);
		
		this.setVisible(true);
	}
	
	public void importTiles(BufferedImage tiles[]) {
		for (BufferedImage tile: tiles) {
		    mapInfo.getTileReference().add(new TileInfo(tile));
		}
		mapTilesInVRAM.importTiles(tiles);
	}
	
	public void importTiles(int width, int height, BufferedImage tiles[],
			String tileName) {
		tileSetCollection.importTiles(width, height, tileName, tiles, tileSetFrame, tileName);
	}
	
	public void importTileSet(TileSetInfo tileSet) {
		tileSetCollection.importTiles(tileSet, tileSetFrame);
	}
	
	public void importTextGlyph(BufferedImage tile[]) {
		mapTilesInVRAM.importTextGlyph(tile);
	}
	
	public void importCollisionMap(Vector<CollisionInfo> collisionMap) {
		mapInfo.setCollisionTiles(collisionMap);
	}
	
	public MapCanvasWxH getMapCanvas() {
		return mapView;
	}
	
	public MapTilesInVRAM getMapTilesInVRAM() {
		return mapTilesInVRAM;
	}
	
	public PaletteChooser getPaletteChooser() {
		return paletteChooser;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public TileSetSelection getTileSetCollection() {
		return tileSetCollection;
	}

	public int getCollisionHeight() {
		return collisionHeight;
	}

	public int getCollisionWidth() {
		return collisionWidth;
	}
	
	public MapInfo getMapInfo() {
		return mapInfo;
	}
}