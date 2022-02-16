package infoObjects;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import tools.ImageTools;

public class TileInfo {
    private int width;
    private int height;
    private int setToLayer = NO_SET_LAYER;
    private int index = NO_SET_INDEX;
    private int palletteIndex = 0;
    private boolean hflip = false;
    private boolean vflip = false;
	private BufferedImage tile;
	private String name;
    
    public static int NO_SET_LAYER = -1;
    //public static int NO_SET_INDEX = -1;
    public static int NO_SET_INDEX = 0;
    
    public TileInfo(int w, int h) {
    	width = w;
    	height = h;
    	tile = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
    	setToLayer = 3;
    }
    
    public TileInfo(BufferedImage t) {
    	tile = t;
    	width = tile.getWidth();
    	height = tile.getHeight();
    	setToLayer = 3;
    }
    
    public TileInfo(TileInfo ti) {
    	set(ti);
    }
    
    public void set(TileInfo ti) {
    	width = ti.getWidth();
    	height = ti.getHeight();
    	index = ti.getIndex();
    	setToLayer = ti.getSetToLayer();
    	setHflip(ti.isHflip());
    	setVflip(ti.isVflip());
    	name = ti.getName();
    	tile = ImageTools.copyBufferedImage(ti.getTileImage());
    	palletteIndex = ti.getPalletteIndex();
    }
    
    public BufferedImage getTileImage() {
    	return tile;
    }
    
    public void setTile(BufferedImage tile) {
		this.tile = ImageTools.copyBufferedImage(tile);
		if (hflip) {
			this.tile.setData(ImageTools.imageFlipHorizontal(this.tile));	
		}
		
		if (vflip) {
			this.tile.setData(ImageTools.imageFlipVertical(this.tile));	
		}
		
		if (isEmptyImage()) {
			setIndex(0);
		}
	}

	public int getSetToLayer() {
		return setToLayer;
	}

	public void setSetToLayer(int setToLayer) {
		this.setToLayer = setToLayer;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean isEmptyImage() {
		DataBuffer data = tile.getData().getDataBuffer();
		//boolean isEmpty = true;
		
		for(int i = 0; i<data.getSize(); i++) {
			if(data.getElem(i) != 0) {
				return false;
			}
		}
		return true;
	}

	public boolean isHflip() {
		return hflip;
	}

	public void setHflip(boolean hflip) {
		if (hflip != this.hflip) {
			tile.setData(ImageTools.imageFlipHorizontal(tile));	
		}
		this.hflip = hflip;
	}

	public boolean isVflip() {
		return vflip;
	}

	public void setVflip(boolean vflip) {
		if (vflip != this.vflip) {
			tile.setData(ImageTools.imageFlipVertical(tile));	
		}
		this.vflip = vflip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPalletteIndex() {
		return palletteIndex;
	}

	public void setPalletteIndex(int palletteIndex) {
		this.palletteIndex = palletteIndex;
	}
}
