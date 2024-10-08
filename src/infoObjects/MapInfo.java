package infoObjects;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import tools.ImageTools;

public class MapInfo {
    int widthInTiles;
    int heightInTiles;
    Vector<TileInfo> tileReference;
    Vector<TileSetInfo> tileSets;

	Vector<MapLayerInfo> mapLayers;
	Vector<CollisionInfo> collisionTiles;
	Vector<PalletteInfo> paletteInfos;
	Map<String, String> tileSetNamesMap;
	Vector<String> tileSetNames;
	Vector<EventInfo> events;
	Vector<ActorInfo> actors;
	Vector<SpriteMaskInfo> spriteMasks;
	Vector<MaskInfo> masks;
	VramInfo vramInfo;
	private File saveFile;
	
	public static final int TILE_START_IDX = 1;
	public static final String ERASER = "EMPTY";
    
    public MapInfo(int width, int height) {
    	tileReference = new Vector<TileInfo>();
    	mapLayers = new Vector<MapLayerInfo>();
    	tileSets = new Vector<TileSetInfo>();
    	paletteInfos = new Vector<PalletteInfo>();
    	tileSetNamesMap = new HashMap<String, String>();
    	tileSetNames = new Vector<String>();
    	events = new Vector<EventInfo>();
    	actors =  new Vector<ActorInfo>();
    	spriteMasks = new Vector<SpriteMaskInfo>();
    	masks = MaskInfo.generateStandardMask();
    	vramInfo = new VramInfo();
    	
    	widthInTiles = width;
    	heightInTiles = height;
    	for (int i = 0; i < 4; ++i) {
    		mapLayers.add(new MapLayerInfo(width, height));
    	}
    	
    	collisionTiles = new Vector<CollisionInfo>();
    	
    	for (int i = 0; i < (widthInTiles*heightInTiles)/2; ++i ) {
    		collisionTiles.add(new CollisionInfo());
    	}
    }
    
    public Vector<TileInfo> getTileReference() {
    	return tileReference;
    }
    
    public Vector<TileSetInfo> getTileSets() {
		return tileSets;
	}
    
    private void setIndexToTileSet(TileSetInfo tileSet, String name, int index) {
    	for (TileInfo tile: tileSet.getTileSet()) {
    		if (tile.getName().contentEquals(name)) {
    			tile.setIndex(index);
    		}
    	}
    }
    
    public void setTileSetToMap(int x, int y, TileSetInfo tsi, boolean isSetTiles,  boolean isSetCollision) {
    	
    	if (tsi == null) {
    		return;
    	}
    	TileSetInfo tileSetForIndexSetting = null;
    	for (TileSetInfo tileSet : tileSets) {
    		if (tsi.getTileSet().size() > 0) { 
    			if (tsi.getTileSet().get(0).getName().contains(tileSet.getFileName())) {
    				tileSetForIndexSetting = tileSet;
    			}
    		}
    	}
    	
    	
    	int width = tsi.getWidthInTiles(), height = tsi.getHeightInTiles();
    	Vector<TileInfo> tiles = tsi.getTileSet();
    	for (int i = 0; i < height; ++i) {
    		for (int j = 0; j < width; ++j) {
    			TileInfo tileInfo = tiles.get(j + i*width);
    			if (tileInfo.getSetToLayer() != TileInfo.NO_SET_LAYER && isSetTiles) {
    				if (tileInfo.getIndex() == TileInfo.NO_SET_INDEX 
    					&& !tileInfo.getName().contentEquals(ERASER)) {
    					int index = vramInfo.setIndex(tileInfo.getName());
    					setIndexToTileSet(tileSetForIndexSetting, tileInfo.getName(), index);
    					tileInfo.setIndex(index);
    					System.out.println("ADDED:" + tileInfo.getName() + " " + tileInfo.getIndex());
    				}
    			    mapLayers.get(tileInfo.getSetToLayer()).setTileAt(x + j, y + i, tileInfo); 
    			}
    		}
    	}
    	
    	if (isSetCollision) {
    		setCollisionAt(x/2, y/2, tsi.getCollision().get(0));
    	}
    }

	public Vector<MapLayerInfo> getMapLayers() {
		return mapLayers;
	}

	public void setMapLayers(Vector<MapLayerInfo> mapLayers) {
		this.mapLayers = mapLayers;
	}
    
	public Vector<CollisionInfo> getCollisionTiles() {
		return collisionTiles;
	}

	public void setCollisionTiles(Vector<CollisionInfo> collisionTiles) {
		this.collisionTiles = collisionTiles;
	}
	
	 public void setCollisionAt(int x, int y, CollisionInfo ci) {
    	int collisionWidth = widthInTiles/2, collisionHeight = heightInTiles/2;
    	if (x >= collisionWidth || y >= collisionHeight) {
    		return;
    	}
    	
    	collisionTiles.get(x + y*collisionWidth).set(ci);
	 }
	 
	 public boolean addTileSet(TileSetInfo tsi) {
		 if (!tileSetNamesMap.containsKey(tsi.getFileName())) {
			 tileSetNamesMap.put(tsi.getFileName(), tsi.getFileName());
			 tileSetNames.add(tsi.getFileName());
			 BufferedImage tile = tsi.getTileSet().get(0).getTileImage();
			 IndexColorModel tilePallete = (IndexColorModel) tile.getColorModel();
			 int highestIndex = 0;
			 for (TileInfo tileInfo: tsi.getTileSet()) {
				 if (tileInfo.getIndex() > 0) {
					 highestIndex = vramInfo.setIndex(tileInfo.getName(), tileInfo.getIndex());
				 }
			 }

			 boolean addPalette = true;
			 for(PalletteInfo paletteInfo: paletteInfos) {
				 if (!isDifferentPalletes(tilePallete, paletteInfo.getPallette())) {
					 addPalette = false;
					 break;
				 }
			 }
			 
			 if (addPalette) {
				 paletteInfos.add(new PalletteInfo(tsi.getFileName(), tilePallete));
			 }
			 
			 int palIdx = 0;
			 for(PalletteInfo paletteInfo: paletteInfos) {
				 if (!isDifferentPalletes(tilePallete, paletteInfo.getPallette())) {
					 break;
				 }
				 ++palIdx;
			 }
			 
			 tsi.setPaletteIdx(palIdx);
			 tileSets.add(tsi);
			 
			 return true;
		 }
		 return false;
	 }
	 
	 private boolean isDifferentPalletes(IndexColorModel firstPal, IndexColorModel secondPal) {
    	int[] rgb = new int[firstPal.getMapSize()];
		firstPal.getRGBs(rgb);
		
		int[] checkRgb = new int[secondPal.getMapSize()];
		secondPal.getRGBs(checkRgb);
		
    	for (int idx = 0; idx < rgb.length; ++idx) {
			if (rgb[idx] != checkRgb[idx]) {
				return true;
			}
		}
    	
    	return false;
	}

	public Vector<String> getTileSetNames() {
		return tileSetNames;
	}

	public void setTileSetNames(Vector<String> tileSetNames) {
		this.tileSetNames = tileSetNames;
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

	public Vector<EventInfo> getEvents() {
		return events;
	}

	public Vector<ActorInfo> getActors() {
		return actors;
	}
	
	public Vector<SpriteMaskInfo> getSpriteMasks() {
		return spriteMasks;
	}

	public Vector<MaskInfo> getMasks() {
		return masks;
	}

	public Vector<PalletteInfo> getPallete() {
		return paletteInfos;
	}

	public File getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
	}
	
	public void reset() {
		int tileIdx = TILE_START_IDX;
		boolean firstTile = true;
		paletteInfos.clear();
		//Vector<TileSetInfo> newTiles = new Vector<TileSetInfo>(getTileSets());
		//while(getTileSets().size() > 1) {
		//	getTileSets().remove(getTileSets().lastElement());
		//}
		for (TileSetInfo tileSet: getTileSets()) {
		//for (TileSetInfo tileSet: newTiles) {
			if (firstTile) {
				firstTile = false;
				continue;
			}
			tileIdx = tileSet.reset(getSaveFile().getParent(), tileIdx);
			//addTileSet(tileSet);
			BufferedImage tile = tileSet.getTileSet().get(0).getTileImage();
			 IndexColorModel tilePallete = (IndexColorModel) tile.getColorModel();
			 
			 boolean addPalette = true;
			 for(PalletteInfo paletteInfo: paletteInfos) {
				 if (!isDifferentPalletes(tilePallete, paletteInfo.getPallette())) {
					 addPalette = false;
					 break;
				 }
			 }
			 
			 if (addPalette) {
				 paletteInfos.add(new PalletteInfo(tileSet.getFileName(), tilePallete));
			 }
		}
		
		for ( MapLayerInfo mapLayer : getMapLayers()) {
			for (TileInfo tile : mapLayer.getTiles()) {
				setTileFromTileSet(tile);
			}
		}
		
		for ( MapLayerInfo mapLayer : getMapLayers()) {
			for (int i = 0; i < getHeightInTiles(); ++i) {
				for (int j = 0; j < getWidthInTiles(); ++j) {
					TileInfo setTile = mapLayer.getTiles().get(j + i*getWidthInTiles());
					if (setTile.getSetToLayer() >= 0) {
						mapLayer.setTileAt(j, i, setTile);
					}
				}
			}
		}
	}
	
	private void setTileFromTileSet(TileInfo tileFromLayer) {
		boolean firstSet = true;
		//boolean updated = false;
		for (TileSetInfo tileSet: getTileSets()){
			if (firstSet) {
				firstSet = false;
				continue;
			}
			for (TileInfo tile: tileSet.getTileSet()) {
				if (tileFromLayer.getName().equals(tile.getName())) {
					tileFromLayer.setIndex(tile.getIndex());
					tileFromLayer.setTile(tile.getTileImage());
					tileFromLayer.setPalletteIndex(tileSet.getPaletteIdx());
					return;
				}
			}
		}
		
		tileFromLayer = new TileInfo(8, 8);
		tileFromLayer.setName(ERASER);
		tileFromLayer.setTile(ImageTools.createEraserTile(8,8));
		tileFromLayer.setIndex(0);
		//tileFromLayer.setTile(ImageTools.createEmptyImage(8, 8));
	}
}
