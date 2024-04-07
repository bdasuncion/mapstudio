package infoObjects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import fileRW.TileReader;

public class TileSetInfo {
    private int widthInTiles;
    private int heightInTiles;
    private Vector<TileInfo> tileSet;
    private Vector<CollisionInfo> collision;
    private String fileName;
    int tileIdxStart;
    int tileIdxEnd;
    int paletteIdx = 0;
    
    public TileSetInfo(int width, int height, BufferedImage[] tiles, int idx, String name) {
    	widthInTiles = width;
    	heightInTiles = height; 
    	tileSet = new Vector<TileInfo>();
    	collision = new Vector<CollisionInfo>();
    	fileName = name;
    	
    	tileIdxStart = idx;
    	int idxSet = idx;
    	int nameIdx = 0;
    	for (BufferedImage tile : tiles) {
    		TileInfo tileInfo = new TileInfo(tile);
    		
    		tileInfo.setName(name + "_" + nameIdx);
    		++nameIdx;
    		if (!tileInfo.isEmptyImage()) {
    			tileInfo.setIndex(idxSet);
    		    ++idxSet;
    		} else {
    			tileInfo.setIndex(0);
    		}
    		
    		tileSet.add(tileInfo);
    	}
    	
    	//if (idxSet == 0) {
    	//	++idxSet;
    	//}
    	tileIdxEnd = idxSet;
    	
    	initializeCollision(tiles.length);
    }

    public TileSetInfo(int width, int height, Vector<TileInfo> tiles , Vector<CollisionInfo> collisionSet) {
    	widthInTiles = width;
    	heightInTiles = height;
    	tileSet = new Vector<TileInfo>();
    	collision = new Vector<CollisionInfo>();
    	
    	for (TileInfo tile: tiles) {
    		tileSet.add(new TileInfo(tile));
    	}
    	
    	for (CollisionInfo collisionInfo: collisionSet) {
    		collision.add(collisionInfo);
    	}
    	
    	for (int i = 0; i < tileSet.size(); ++i) {
    		if (tileSet.get(i).getIndex() > 0 || tileSet.size() == i - 1) {
    			tileIdxStart = tileSet.get(i).getIndex();
    			break;
    		}
    	}
    	
    	for (int i = tileSet.size() - 1; i >= 0; --i) {
    		if (tileSet.get(i).getIndex() > 0 || tileSet.size() == i - 1) {
    			tileIdxEnd = tileSet.get(i).getIndex();
    			break;
    		}
    	}
    	//tileIdxStart = tileSet.firstElement().getIndex();
    	//tileIdxEnd = tileSet.lastElement().getIndex() + 1;
    	
    	initializeCollision(tiles.size());
    	//initializeCollision(4);
    }
    
    private void initializeCollision(int tileCount) {
    	//for (int i = 0; i < tileCount/4; ++i) {
    		collision.add(new CollisionInfo());
    	//}
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

	public Vector<TileInfo> getTileSet() {
		return tileSet;
	}

	public void setTileSet(Vector<TileInfo> tileSet) {
		this.tileSet = tileSet;
	}

	public Vector<CollisionInfo> getCollision() {
		return collision;
	}

	public void setCollision(Vector<CollisionInfo> collision) {
		this.collision = collision;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTileIdxStart() {
		return tileIdxStart;
	}

	public int getTileIdxEnd() {
		return tileIdxEnd;
	}
	
	public TileSetInfo getTileSet(int x, int y, int width, int height) {
		Vector<TileInfo> selectedTiles = new Vector<TileInfo>();
		int totalWidth = width*2, totalHeight = height*2;
		
		int startIdx = (x*2) + (y*widthInTiles*2);
				
		for (int i = 0; i < totalHeight; ++i) {
			for (int j = 0; j < totalWidth; ++j) {
				selectedTiles.add(getTileSet().get((startIdx + j) + (i*widthInTiles)));
			}
		}
		
		Vector<CollisionInfo> selectedCollision = new Vector<CollisionInfo>();
		selectedCollision.add(getCollision().get(0));
		
		return  new TileSetInfo(totalWidth, totalHeight, selectedTiles, selectedCollision);
	}
	
	public void flipVertical() {
		for (int i = 0; i < heightInTiles/2; ++i) {
			for (int j = 0; j < widthInTiles; ++j) {
				TileInfo tileTemp = tileSet.get(j + i*widthInTiles);
				TileInfo tileTemp2 = tileSet.get(j + (heightInTiles - 1 - i)*widthInTiles);
				tileSet.set(j + (heightInTiles - 1 - i)*widthInTiles, tileTemp);
				tileSet.set(j + i*widthInTiles, tileTemp2);
			}
		}
		for (TileInfo tile: getTileSet()) {
			tile.setVflip(!tile.isVflip());
		}
	}
	
	public void flipHorizontal() {
		for (int i = 0; i < heightInTiles; ++i) {
			for (int j = 0; j < widthInTiles/2; ++j) {
				TileInfo tileTemp = tileSet.get(j + i*widthInTiles);
				TileInfo tileTemp2 = tileSet.get((widthInTiles - 1 - j) + i*widthInTiles);
				tileSet.set(((widthInTiles - 1 - j) + i*widthInTiles), tileTemp);
				tileSet.set(j + i*widthInTiles, tileTemp2);
			}
		}
		
		for (TileInfo tile: getTileSet()) {
			tile.setHflip(!tile.isHflip());
		}
	}

	public int getPaletteIdx() {
		return paletteIdx;
	}

	public void setPaletteIdx(int paletteIdx) {
		this.paletteIdx = paletteIdx;
		for (TileInfo tile : getTileSet()) {
			tile.setPalletteIndex(this.paletteIdx);
		}
	}
	
	public int reset(String parentDirectory, int tileIdxStart) {
		File tileFile = new File(parentDirectory + "\\" + getFileName() + ".tile");
		TileReader read = new TileReader();
		read.read(tileFile);
		
		Vector<TileInfo> tiles = getTileSet();
		
		tiles.clear();
		int tileIdxSet = tileIdxStart;
		if (read != null) {
			int row = 0, column = 0;
			for (int i = 0; i < read.getTile().length; ++i) {
				if (tiles.size() >= i) {
				    tiles.add(new TileInfo(8, 8));
				}
				tiles.get(i).setTile(read.getTile()[i]);
				tiles.get(i).setName(formatName(getFileName(), row, column));
				++column;
				if (column >= read.getWidthInTiles()) {
					column = 0;
					++row;
				}
				if (!tiles.get(i).isEmptyImage()) {
					tiles.get(i).setIndex(tileIdxSet);
					++tileIdxSet;
				} else {
					tiles.get(i).setIndex(0);
				}
			}
			
			setWidthInTiles(read.getWidthInTiles());
			setHeightInTiles(read.getHeightInTiles());
		}
		return tileIdxSet;
	}
	
	public static String formatName(String baseName, int row, int column) {
		return baseName + "_" + row + "_" + column;
	}
}
