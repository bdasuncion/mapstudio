package infoObjects;

import java.awt.image.IndexColorModel;

public class PalletteInfo {
	IndexColorModel pallette;
	String name;
	public PalletteInfo(String name, IndexColorModel pallette) {
		this.name = name;
		this.pallette = pallette;
	}
	
	public IndexColorModel getPallette() {
		return pallette;
	}
	public void setPallette(IndexColorModel pallette) {
		this.pallette = pallette;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
