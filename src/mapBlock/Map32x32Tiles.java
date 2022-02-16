package mapBlock;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class Map32x32Tiles
{
//	private Vector tiles8x8;
	private BufferedImage block256x256;
	private Vector<Integer> indexFromTiles;
	private Vector<Integer> flipHorizontal;
	private Vector<Integer> flipVertical;
	private Vector<Integer> paletteBank;
	private static final int widthInTiles = 32;
	private static final int heightInTiles = 32;
	private static final int widthOfTile = 8;
	private static final int heightOfTile = 8;
	
	public Map32x32Tiles()
	{	
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
		
		block256x256 = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setTileAt(BufferedImage img, int x, int y)
	{
		int ARGB[] = new int[64];
		img.getRGB(0, 0, 8, 8, ARGB, 0, 8);
		block256x256.setRGB( x, y, 8, 8, ARGB, 0, 8);
	}
	
	public void setIndexOfTile(int tileIndex, int i)
	{
		indexFromTiles.set(i, new Integer(tileIndex));
	}
	
	public void setHorizontalFlip(int hFlip, int i)
	{
		flipHorizontal.set(i,new Integer(hFlip));
	}
	
	public void setVerticalFlip(int vFlip, int i)
	{
		flipVertical.set(i,new Integer(vFlip));
	}
	
	public void setPaletteBank(int paletteIdx, int i)
	{
		paletteBank.set(i,new Integer(paletteIdx));
	}
	
	public BufferedImage getMapBlock()
	{
		return block256x256;
	}
	
	public int getIndexOfTileAt(int i)
	{
		return ((Integer)indexFromTiles.get(i)).intValue();
	}
	
	public int getHorizontalFlipAt(int i)
	{
		return ((Integer)flipHorizontal.get(i)).intValue();
	}
	
	public int getVerticalFlipAt(int i)
	{
		return ((Integer)flipVertical.get(i)).intValue();
	}
	
	public int getPaletteBankAt(int i)
	{
		return ((Integer)paletteBank.get(i)).intValue();
	}
	
	public int getSizeOfMap()
	{
		return widthInTiles*heightInTiles;
	}
	
	public int getWidthOfMap()
	{
		return widthInTiles;
	}
	
	public int getHeightOfMap()
	{
		return heightInTiles;
	}
	
	public int getWidthOfTile()
	{
		return widthOfTile;
	}
	
	public int getHeightOfTile()
	{
		return heightOfTile;
	}
}