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
    }
    
    private void initializeCollision(int tileCount) {
    	for (int i = 0; i < tileCount/4; ++i) {
    		collision.add(new CollisionInfo());
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
		
		//System.out.println("STARTIDX: " + startIdx + " W " + totalWidth + " H " + totalHeight);
		//BufferedImage[] tiles = new BufferedImage[totalWidth*totalHeight];
		
		for (int i = 0; i < totalHeight; ++i) {
			for (int j = 0; j < totalWidth; ++j) {
				//System.out.println("IDX: " + ((startIdx + j) + (i*widthInTiles)));
				selectedTiles.add(getTileSet().get((startIdx + j) + (i*widthInTiles)));
			}
		}
		
		Vector<CollisionInfo> selectedCollision = new Vector<CollisionInfo>();
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				//System.out.println("IDX: " + ((startIdx + j) + (i*widthInTiles)));
				selectedCollision.add(getCollision().get((x + j) + (i*width)));
			}
		}
		
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
		
		for (int i = 0; i < heightInTiles/4; ++i) {
			for (int j = 0; j < widthInTiles/2; ++j) {
				CollisionInfo collisionTemp = collision.get(j + i*(widthInTiles/2));
				CollisionInfo collisionTemp2 = collision.get(j + (heightInTiles/2 - 1 - i)*(widthInTiles/2));
				collision.set(j + (heightInTiles/2 - 1 - i)*(widthInTiles/2), collisionTemp);
				collision.set(j + i*(widthInTiles/2), collisionTemp2);
			}
		}
		for (CollisionInfo collisionInfo: getCollision()) {
			collisionInfo.setVflip(!collisionInfo.isVflip());
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
		
		for (int i = 0; i < heightInTiles/2; ++i) {
			for (int j = 0; j < widthInTiles/4; ++j) {
				CollisionInfo collisionTemp = collision.get(j + i*(widthInTiles/2));
				CollisionInfo collisionTemp2 = collision.get((widthInTiles/2 - 1 - j) + i*(widthInTiles/2));
				collision.set((widthInTiles/2 - 1 - j) + i*(widthInTiles/2), collisionTemp);
				collision.set(j + i*(widthInTiles/2), collisionTemp2);
			}
		}
		for (CollisionInfo collisionInfo: getCollision()) {
			collisionInfo.setHflip(!collisionInfo.isHflip());
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
		//getTileSet().clear();
		
		Vector<TileInfo> tiles = getTileSet();
		
		tiles.clear();
		int tileIdxSet = tileIdxStart;
		if (read != null) {
			for (int i = 0; i < read.getTile().length; ++i) {
				if (tiles.size() >= i) {
				    tiles.add(new TileInfo(8, 8));
				}
				tiles.get(i).setTile(read.getTile()[i]);
				tiles.get(i).setName(formatName(getFileName(), i));
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
	
	public static String formatName(String baseName, int currentCount) {
		return baseName + "_" + currentCount;
	}
}
