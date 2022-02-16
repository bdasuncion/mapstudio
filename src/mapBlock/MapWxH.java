package mapBlock;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class MapWxH {

	private BufferedImage blockWxH;
	private Vector<Integer> indexFromTiles;
	private Vector<Integer> flipHorizontal;
	private Vector<Integer> flipVertical;
	private Vector<Integer> paletteBank;
	int widthInTiles = 0;
	int heightInTiles = 0;
	int widthOfTile = 0;
	int heightOfTile = 0;
	int widthInPixels = 0;//padded to the nearest tile width
	int heightInPixels = 0;//padded to the nearest tile height
	//Maybe a separate class should be made for collision tiles?
	int widthOfCollision = 0; //padded to the nearest 8
	int heightOfCollision = 0; //padded to the nearest 8
	
	public MapWxH(int widthOfMap, int heightOfMap, int widthTiles, int heightTiles)
	{	
		widthOfTile = widthTiles;
		heightOfTile = heightTiles;
		
		widthOfCollision = widthTiles;
		heightOfCollision = heightTiles;
		
		if(widthOfMap%widthOfTile > 0)
			widthInPixels = widthOfMap + (widthOfTile - widthOfMap%widthOfTile);
		else
			widthInPixels = widthOfMap;
		widthInTiles = widthInPixels/widthOfTile;
		
		//???
//		if(widthInTiles%8 > 0)
//			widthOfCollision = widthInTiles/8 + 1;
//		else
//			widthOfCollision = widthInTiles/8;
		
		if(heightOfMap%heightOfTile > 0)
			heightInPixels = heightOfMap + (heightOfTile - heightOfMap%heightOfTile);
		else
			heightInPixels = heightOfMap;
		heightInTiles = heightInPixels/heightOfTile;
		heightOfCollision = heightInTiles;
		
		indexFromTiles = new Vector<Integer>(widthInTiles*heightInTiles);
		flipHorizontal = new Vector<Integer>(widthInTiles*heightInTiles);
		flipVertical = new Vector<Integer>(widthInTiles*heightInTiles);
		paletteBank = new Vector<Integer>(widthInTiles*heightInTiles);
		
		for(int i = 0; i<widthInTiles*heightInTiles; i++)
		{
			indexFromTiles.add(new Integer(0));
			flipHorizontal.add(new Integer(0));
			flipVertical.add(new Integer(0));
			paletteBank.add(new Integer(0));
		}
		
		blockWxH = new BufferedImage(widthInPixels, heightInPixels, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setTileAt(BufferedImage img, int x, int y)
	{
		int ARGB[] = new int[widthOfTile*heightOfTile];
		img.getRGB(0, 0, widthOfTile, heightOfTile, ARGB, 0, widthOfTile);
		blockWxH.setRGB( x, y, widthOfTile, heightOfTile, ARGB, 0, widthOfTile);
	}

	public int getHeightInPixels() {
		return heightInPixels;
	}

	public void setHeightInPixels(int heightInPixels) {
		this.heightInPixels = heightInPixels;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public void setHeightInTiles(int heightInTiles) {
		this.heightInTiles = heightInTiles;
	}

	public int getHeightOfCollision() {
		return heightOfCollision;
	}

	public void setHeightOfCollision(int heightOfCollision) {
		this.heightOfCollision = heightOfCollision;
	}

	public int getHeightOfTile() {
		return heightOfTile;
	}

	public void setHeightOfTile(int heightOfTile) {
		this.heightOfTile = heightOfTile;
	}

	public int getWidthInPixels() {
		return widthInPixels;
	}

	public void setWidthInPixels(int widthInPixels) {
		this.widthInPixels = widthInPixels;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}

	public void setWidthInTiles(int widthInTiles) {
		this.widthInTiles = widthInTiles;
	}

	public int getWidthOfCollision() {
		return widthOfCollision;
	}

	public void setWidthOfCollision(int widthOfCollision) {
		this.widthOfCollision = widthOfCollision;
	}

	public int getWidthOfTile() {
		return widthOfTile;
	}

	public void setWidthOfTile(int widthOfTile) {
		this.widthOfTile = widthOfTile;
	}

	public BufferedImage getMapImage() {
		return blockWxH;
	}
	
	public void setMapImage(BufferedImage img) {
		blockWxH = img;
	}
	
	public void clear()
	{
		indexFromTiles.clear();
		flipHorizontal.clear();
		flipVertical.clear();
		paletteBank.clear();		
	}

	public Vector<Integer> getFlipHorizontal() {
		return flipHorizontal;
	}

	public void setFlipHorizontal(Vector<Integer> flipHorizontal) {
		this.flipHorizontal = flipHorizontal;
	}
	
	public void setFlipHorizontalAt(int flipHorizontal, int setAt) {
		this.flipHorizontal.set(setAt, new Integer(flipHorizontal));
	}

	public Vector<Integer> getFlipVertical() {
		return flipVertical;
	}

	public void setFlipVertical(Vector<Integer> flipVertical) {
		this.flipVertical = flipVertical;
	}
	
	public void setFlipVerticalAt(int flipVertical, int setAt) {
		this.flipVertical.set(setAt, new Integer(flipVertical));
	}

	public Vector<Integer> getIndexFromTiles() {
		return indexFromTiles;
	}

	public void setIndexFromTiles(Vector<Integer> indexFromTiles) {
		this.indexFromTiles = indexFromTiles;
	}

	public void setIndexFromTilesAt(int indexFromTiles, int setAt) {
		this.indexFromTiles.set(setAt, new Integer(indexFromTiles));
	}
	
	public Vector<Integer> getPaletteBank() {
		return paletteBank;
	}

	public void setPaletteBank(Vector<Integer> paletteBank) {
		this.paletteBank = paletteBank;
	}
	
	public void setPaletteBankAt(int paletteBank, int setAt) {
		this.paletteBank.set(setAt, new Integer(paletteBank));
	}

	public void setBlockWxH(BufferedImage blockWxH) {
		this.blockWxH = blockWxH;
	}
}
