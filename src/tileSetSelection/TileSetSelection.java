package tileSetSelection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import infoObjects.MapInfo;
import infoObjects.TileSetInfo;
import interfaces.MapControls;
import interfaces.TileSelectionControl;
import interfaces.TileSetting;
import tools.ImageTools;

public class TileSetSelection extends JPanel implements TileSelectionControl {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5287326833651429101L;
	//private Vector<TileSetButtonDisplay> tileSetCollection;
	private int indexTaken = 1;
	//private int paletteIndexTaken = 0;
	//private Vector<TileSetInfo> tileSets;
	private MapInfo mapInfo;
	
	private static final String ERASER_TILES = "ERASER";
	//public TileSetSelection(Vector<TileSetInfo> ts)
	public TileSetSelection(MapInfo mi) {
		super();
		
		mapInfo = mi;
		//tileSetCollection = new Vector<TileSetButtonDisplay>(1);
		this.setVisible(true);
	}
	
	public void createEraser(TileSetting mts) {
		BufferedImage[] emptyTile = new BufferedImage[4];
		for (int i = 0; i < 4; ++i) {
			emptyTile[i] = ImageTools.createEraserTile(8, 8);
		}
		
		TileSetInfo tileSetInfo = new TileSetInfo(2, 2, emptyTile, 0, "ERASER");
		mapInfo.getTileSets().add(tileSetInfo);
		
		indexTaken = mapInfo.getTileSets().lastElement().getTileIdxEnd();
		
		this.add(new TileSetButton(tileSetInfo, mts));
		this.repaint();
	}
	
	public void importTiles(int setWidth, int setHeight, String tileName, BufferedImage[] tiles, 
			TileSetting mts, String name) {	
		TileSetInfo tileSetInfo = new TileSetInfo(setWidth, setHeight, tiles, indexTaken, name);
		if (mapInfo.addTileSet(tileSetInfo)) {
		
			indexTaken = mapInfo.getTileSets().lastElement().getTileIdxEnd();
			
			this.add(new TileSetButton(tileSetInfo, mts));
			this.repaint();
		}
	}
	
	public void importTiles(TileSetInfo tileSetInfo, TileSetting  mts) {
		if (mapInfo.addTileSet(tileSetInfo)) {
			
			indexTaken = mapInfo.getTileSets().lastElement().getTileIdxEnd();
			
			this.add(new TileSetButton(tileSetInfo, mts));
			this.repaint();
		}
	}
	
	
	//public int getNumberOfTileSets() {
	//	return tileSetCollection.size();
	//}
	
	//public TileSetButtonDisplay getTileSetAt(int i) {
	//	return tileSetCollection.elementAt(i);
	//}

	//public Vector<TileSetButtonDisplay> getTileSetCollection() {
	//	return tileSetCollection;
	//}

	@Override
	public void reloadTileSelection() {
		for (int i = 1; i < this.getComponentCount(); ++i) {
			TileSetButton tileSetButton = (TileSetButton) this.getComponent(i);
			TileSetInfo tileSet = mapInfo.getTileSets().get(i);
			tileSetButton.getTileSetDisplay().setFromTileSetInfo(tileSet);
			tileSetButton.getTileSetDisplay().setName(tileSet.getFileName());
		} 
		((TileSetButton) this.getComponent(0)).doClick();
		repaint();
	}
}
