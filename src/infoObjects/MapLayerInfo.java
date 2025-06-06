package infoObjects;

import java.awt.image.BufferedImage;
import java.util.Enumeration;
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
    
    private Vector<TileInfo> copyTiles() {
    	Vector<TileInfo> tilesCopy = new Vector<TileInfo>();
        Enumeration<TileInfo> enu = this.tiles.elements();
        while (enu.hasMoreElements()) {
        	tilesCopy.add(new TileInfo(enu.nextElement()));
        }
        return tilesCopy;
    }
    
    public void shiftUp() {
    	System.out.println("SHIFT UP");
    	Vector<TileInfo> tilesCopy = copyTiles();
        
        int width = this.widthInTiles;
        int height = this.heightInTiles;
        
        for(int count = 0; count < 2; ++count) {
	        for (int i = 0; i < height; ++i) {
	        	int copyFromRow = (i + 1)%height;
	        	int copyToRow = i%height;
	        	for (int j = 0; j < width; ++j) {
	        		//this.tiles.set(width*i + j, tilesCopy.get(copyFromRow*width + j));
	        		setTileAt(j, copyToRow, tilesCopy.get(copyFromRow*width + j));
	        	}
	        }
        }
    }
    
    public void shiftDown() {
    	Vector<TileInfo> tilesCopy = copyTiles();
        
        int width = this.widthInTiles;
        int height = this.heightInTiles;
        
        for(int count = 0; count < 2; ++count) {
	        for (int i = 0; i < height; ++i) {
	        	int copyFromRow = i%height;
	        	int copyToRow = (i + 1)%height;
	        	for (int j = 0; j < width; ++j) {
	        		//this.tiles.set(width*currentRow + j, tilesCopy.get(copyFromRow*width + j));
	        		setTileAt(j, copyToRow, tilesCopy.get(copyFromRow*width + j));
	        	}
	        }
        }
    }
    
    public void shiftLeft() {
    	Vector<TileInfo> tilesCopy = copyTiles();
        
        int width = this.widthInTiles;
        int height = this.heightInTiles;
        
        for(int count = 0; count < 2; ++count) {
	        for (int i = 0; i < height; ++i) {
	        	for (int j = 0; j < width; ++j) {
	        		int copyFromColumn = (j + 1)%width;
	        		//this.tiles.set(width*i + j, tilesCopy.get(i*width + copyFromColumn));
	        		setTileAt(j, i, tilesCopy.get(i*width + copyFromColumn));
	        	}
	        }
        }
    }
    
    public void shiftRight() {
    	Vector<TileInfo> tilesCopy = copyTiles();
        
        int width = this.widthInTiles;
        int height = this.heightInTiles;
        
        for(int count = 0; count < 2; ++count) {
	        for (int i = 0; i < height; ++i) {
	        	for (int j = 0; j < width; ++j) {
	        		int copyToColumn = (j + 1)%width;
	        		int copyFromColumn = j%width;
	        		//this.tiles.set(width*i + copyToColumn, tilesCopy.get(i*width + copyFromColumn));
	        		setTileAt(copyToColumn, i, tilesCopy.get(i*width + copyFromColumn));
	        	}
	        }
        }
    }
    
    public void resize(int newWidth, int newHeight) {
    	Vector<TileInfo> tilesCopy = copyTiles();
        
        int currentWidth = this.widthInTiles;
        int currentHeight = this.heightInTiles;
        int width = Math.min(currentWidth, newWidth);
        int height = Math.min(currentHeight, newHeight);
        
        for (int i = 0; i < height; ++i) {
        	for (int j = 0; j < width; ++j) {
        		this.tiles.set(width*i + j, tilesCopy.get(i*width + j));
        	}
        }
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
