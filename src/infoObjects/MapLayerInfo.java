package infoObjects;

import java.awt.image.BufferedImage;
import java.util.Vector;

import tools.ImageTools;

public class MapLayerInfo {
    int widthInTiles;
    int heightInTiles;
    Vector<TileInfo> tiles;
    BufferedImage mapDisplay;
    
    public MapLayerInfo(int width, int height) {
    	tiles = new Vector<TileInfo>();
    	
    	widthInTiles = width;
    	heightInTiles = height;
    	
    	for (int i = 0; i < widthInTiles*heightInTiles; ++i) {
    		tiles.add(new TileInfo(8, 8));
    		tiles.lastElement().setName("EMPTY");
    	}
    	
    	mapDisplay = new BufferedImage(widthInTiles*8, heightInTiles*8, BufferedImage.TYPE_INT_ARGB);
    }

	public int getWidthInTiles() {
		return widthInTiles;
	}

	public void setWidthInTiles(int widthInTiles) {
		this.widthInTiles = widthInTiles;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public void setHeightInTiles(int heightInTiles) {
		this.heightInTiles = heightInTiles;
	}

	public Vector<TileInfo> getTiles() {
		return tiles;
	}

	public void setTiles(Vector<TileInfo> tiles) {
		this.tiles = tiles;
	}
	
    public BufferedImage getMapDisplay() {
		return mapDisplay;
	}

	public void setMapDisplay(BufferedImage mapDisplay) {
		this.mapDisplay = mapDisplay;
	}

	public void setTileAt(int x, int y, TileInfo ti) {
    	if (x >= widthInTiles || y >= heightInTiles) {
    		return;
    	}
    	//System.out.println("SET TILES " + x + " " + y);
    	tiles.get(x + y*widthInTiles).set(ti);
    	
    	int ARGB[] = new int[ti.getWidth()*ti.getHeight()];
    	int tileWidth = ti.getWidth(), tileHeight = ti.getHeight();
		ImageTools.makeImageBGTransparet(ti.getTileImage()).getRGB(0, 0, tileWidth, tileHeight, ARGB, 0, tileWidth);
		mapDisplay.setRGB( x*tileWidth, y*tileHeight, tileWidth, tileHeight, ARGB, 0, tileWidth);
    } 
}
