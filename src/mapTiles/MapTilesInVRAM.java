package mapTiles;
//This class displays the tiles that have been copied to VRAM

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.JPanel;

import tools.ImageTools;
import fileRW.TileReader;
import infoObjects.TileInfo;


public class MapTilesInVRAM extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3452109485265450268L;
	private Vector<BufferedImage> vramBlock1;
	private Vector<BufferedImage> collisionTiles;
	private Vector<Integer> paletteBankIndex;

	private BufferedImage getImage;
	private int getImageIndex;
	private int getPaletteIndex;
	
	private Vector<BufferedImage> getImages;
	private Vector<Integer> getImageIndices;
	private Vector<Integer> getPaletteIndices;
	private int numImages = 1;
	
	private IndexColorModel palette[];
	private int PixelSize;
	private int ImageIndexTaken;
	private int PaletteIndexTaken = 0;
	private int scale = 1;
	
	private BufferedImage highlighter;
	private boolean inBounds = false;
	private boolean isClicked = false;
	private boolean isRightClicked = false;
	private boolean collisionLayerActive = false;
	private int xHighlight;
	private int yHighlight;
	private int xHighlightPrev;
	private int yHighlightPrev;
	private int maxTilesIn16KMem = 0;
	private int tileWidth = 8;
	private int tileHeight = 8;
	private int collisionWidth = 8;
	private int collisionHeight = 8;
	
	//Maximum number of tiles displayed in width and height
	private static int MAX_WIDTH_DISPLAY = 16;
	private static int MAX_HEIGHT_DISPLAY = 32;
	
	private Vector<TileInfo> tileReference;
	
	public MapTilesInVRAM(int pixelSize, int tileW, int tileH, Vector<TileInfo> tr) {
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
		tileReference = tr; 
		
		tileWidth = tileW;
		tileHeight = tileH;
		
		if(pixelSize == 4) {
			palette = new IndexColorModel[16];
			PixelSize = 4;
			//512 4BPP 8x8 tile in 16K memory
			vramBlock1 = new Vector<BufferedImage>(512);
			paletteBankIndex = new Vector<Integer>(512);
			maxTilesIn16KMem = 512;
		} else {
			palette = new IndexColorModel[1];
			PixelSize = 8;	
			//256 8BPP 8x8 tile in 16K memory
			vramBlock1 = new Vector<BufferedImage>(256);
			paletteBankIndex = new Vector<Integer>(256);
			maxTilesIn16KMem = 256;
		}
		collisionTiles = new Vector<BufferedImage>(16);
		//this.setCollisionTiles();
		
		ImageIndexTaken = 1;
		byte R[],G[],B[],A[];
		
		int NumberOfColours = (int) java.lang.Math.pow(2, PixelSize);
		
		R = new byte[NumberOfColours];
		G = new byte[NumberOfColours];
		B = new byte[NumberOfColours];
		A = new byte[NumberOfColours];
		
		R[0] = G[0] = B[0] = (byte)(255);
		
		for(int i = 0; i<palette.length; i++)
		{
			palette[i] = new IndexColorModel(PixelSize,NumberOfColours, R,G,B);
		}
		
		for(int i= 0; i<maxTilesIn16KMem; i++)
		{
			vramBlock1.add(new BufferedImage(tileWidth,tileHeight,BufferedImage.TYPE_BYTE_BINARY, palette[0]));
			paletteBankIndex.add(new Integer(0));
		}
		
		A[0] = (byte)120;
		R[0] = G[0] = (byte)(0);

		IndexColorModel highlightPalette = new IndexColorModel(PixelSize,NumberOfColours, R,G,B,A);
		highlighter = new BufferedImage(tileWidth,tileHeight, BufferedImage.TYPE_BYTE_BINARY, highlightPalette);
		
		getImages = new Vector<BufferedImage>(1);
		getImageIndices = new Vector<Integer>(1);
		getPaletteIndices = new Vector<Integer>(1);
		
		this.setNumberOfImages(1);

		this.setVisible(true);
	}
	
	public void importTiles(BufferedImage[] tiles) {
		int size = 0;
		for(int i = 0; i<tiles.length; i++) {
			if(this.checkImageEmpty(tiles[i]) == true)
				continue;
			if(vramBlock1.size()>ImageIndexTaken + size)
				vramBlock1.set(ImageIndexTaken + size, tiles[i]);
			else
				vramBlock1.add(ImageIndexTaken + size, tiles[i]);
			size++;
		}
		if(size == 0)
			return;
		this.checkPalette((IndexColorModel) tiles[0].getColorModel(), tiles.length);
		ImageIndexTaken += size;
		this.repaint();
	}
	
	public void importTextGlyph(BufferedImage[] tiles) {
		int size = 0;
		if(ImageIndexTaken<=1)
		    ImageIndexTaken = 2;
		for(int i = 0; i<tiles.length; i++)
		{
			if(vramBlock1.size()>ImageIndexTaken + size)
				vramBlock1.set(ImageIndexTaken + size, tiles[i]);
			else
				vramBlock1.add(ImageIndexTaken + size, tiles[i]);
			size++;
		}
		if(size == 0)
			return;
		this.checkPalette((IndexColorModel) tiles[0].getColorModel(), tiles.length);
		ImageIndexTaken += size;
		this.repaint();
	}
	
	/*public void setCollisionTiles() {
	    File f;
	    TileReader reader;
	    InputStream stream = this.getClass().getResourceAsStream("/res/collisiontiles.tile");
	    if(stream == null) {
	    	f = new File("res/collisiontiles.tile");
	    	reader = new TileReader(f);
	    }
	    else {
	    	reader = new TileReader(stream);
	    }
	    
	    BufferedImage[] tiles = reader.getTile();

	    for(int i = 0; i<tiles.length; i++) {
	    	//collisionTiles.add(ImageTools.resizeImage(tiles[i], tileWidth, tileHeight));
	    	collisionTiles.add(ImageTools.resizeImage(tiles[i], collisionWidth, collisionHeight));
	    }
	}*/
	
	public void setCollisionLayerActive(boolean act) {
		collisionLayerActive = act;
	}
	
	public boolean checkImageEmpty(BufferedImage img) {
		DataBuffer data = img.getData().getDataBuffer();
		
		for(int i = 0; i<data.getSize(); i++) {
			if(data.getElem(i) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public void checkPalette(IndexColorModel palIn, int numOfTiles) {
		if(PaletteIndexTaken == 0) {
			palette[PaletteIndexTaken] = palIn;
			for(int x = 0; x<numOfTiles; x++) {
				paletteBankIndex.set(ImageIndexTaken + x, new Integer(0));
			}
			
			PaletteIndexTaken++;
		} else {
			int i = 0,j = 0, equal = 0;
			for(i = 0; i<PaletteIndexTaken; i++) {
				equal = 0;
				for(j = 0; j<palIn.getMapSize(); j++) {
					if(palIn.getRed(j) != palette[i].getRed(j) ||
						palIn.getBlue(j) != palette[i].getBlue(j) ||
						palIn.getGreen(j) != palette[i].getGreen(j) )
						continue;
					equal++;
				}
				if(equal == 16) {
					break;
				}
			}
			if(equal != 16) {
				palette[PaletteIndexTaken] = palIn;
				for(int x = 0; x<numOfTiles; x++) {
					paletteBankIndex.set(ImageIndexTaken + x, new Integer(PaletteIndexTaken));			
				}
				PaletteIndexTaken++;				
			} else {
				for(int x = 0; x<numOfTiles; x++) {
					paletteBankIndex.set(ImageIndexTaken + x, new Integer(i));
				}				
			}
		}
		
	}
	
	public int getPaletteIndexTaken()
	{
		return PaletteIndexTaken;
	}
	
	public int getImageIndexTaken()
	{
		return ImageIndexTaken;
	}
	
	public IndexColorModel[] getPalette()
	{
		return palette;
	}
	
	public Vector<BufferedImage> getTiles()
	{
		return vramBlock1;
	}
	
	public Vector<Integer> getTilePaletteIndices()
	{
		return paletteBankIndex;
	}
	
	public IndexColorModel getPaletteAt(int x)
	{
		return palette[x];
	}
	
	public void setPaletteBank(Vector pal)
	{
		for(int i = 0; i<pal.size(); i++ )
		{
			palette[i] = (IndexColorModel) pal.get(i);
			PaletteIndexTaken++;
		}
	}
	
	public void setTiles(Vector tiles)
	{
		for(int i = 0; i<tiles.size(); i++ )
		{
			vramBlock1.set(i, (BufferedImage) tiles.get(i));
		}
		ImageIndexTaken = tiles.size();
	}
	
	public void setTilePaletteIndices(Vector paletteIdx)
	{
		for(int i = 0; i<paletteIdx.size(); i++ )
		{
			paletteBankIndex.set(i, (Integer) paletteIdx.get(i));
		}		
	}
	
	public void setImageIndexTaken(int imageTake)
	{
		ImageIndexTaken = imageTake;
	}
	
//Multi Image	
	public boolean newImagesClicked()
	{
		return isRightClicked;
	}
	
	public void setNumberOfImages(int num)
	{
		getImages.clear();
		getImageIndices.clear();
		getPaletteIndices.clear();
		for(int i = 0; i<num; i++)
		{
			getImages.add(ImageTools.createEmptyImage(8, 8));
			getImageIndices.add(new Integer(0));
			getPaletteIndices.add(new Integer(0));
		}
		
		numImages = num;
	}
	
	public Vector getImages()
	{
		isRightClicked = false;
		return getImages;
	}
	
	public Vector getImageIndices()
	{
		isRightClicked = false;
		return getImageIndices;
	}
	
	public Vector getPaletteIndices()
	{
		isRightClicked = false;
		return getPaletteIndices;
	}
//Single Image	
	public boolean newImageClicked()
	{
		return isClicked;
	}
	
	public BufferedImage getNewImage()
	{
		isClicked = false;
		return getImage;
	}
	
	public int getImageIndex()
	{
		isClicked = false;
		return getImageIndex;
	}	
	
	public int getPaletteIndex()
	{
		isClicked = false;
		return getPaletteIndex;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		BufferedImage disp;
		
		int x = 0,y = 0;
		
		disp = g2D.getDeviceConfiguration().createCompatibleImage(tileWidth*scale, tileHeight*scale);
		if(collisionLayerActive == false) {
			for(int i = 0; i<tileReference.size(); i++ ) {
				if(i%MAX_WIDTH_DISPLAY == 0 && i>0) {
					x = 0;
					y++;
				}
				
				
				disp = (BufferedImage)tileReference.get(i).getTileImage();
				
				g2D.drawImage(disp, disp.getWidth()*x*scale, disp.getHeight()*y*scale, 
						disp.getWidth()*scale,disp.getHeight()*scale, this);
				x++;
			}			
		} else {
			for(int i = 0; i<collisionTiles.size(); i++ ) {
				if(i%MAX_WIDTH_DISPLAY == 0 && i>0) {
					x = 0;
					y++;
				}
				
				disp = (BufferedImage)collisionTiles.get(i);
				
				g2D.drawImage(disp, disp.getWidth()*x*scale, disp.getHeight()*y*scale, 
						disp.getWidth()*scale,disp.getHeight()*scale, this);
				x++;
			}
		}

		int drawWidth;
		int drawHeight;
		
		if(collisionLayerActive != true) {
			drawWidth = tileWidth*scale;
			drawHeight = tileHeight*scale;
		} else {
			drawWidth = collisionWidth*scale;
			drawHeight = collisionHeight*scale;
		}
		
		g2D.drawImage(highlighter, xHighlight, yHighlight, 
				drawWidth, drawHeight, this);
		xHighlightPrev = xHighlight;
		yHighlightPrev = yHighlight;
	}
	public void processMouseEvent(MouseEvent e)
	{
		int xPosition,yPosition;
		if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1)
		{
			if(collisionLayerActive == false)
			{
				xPosition = (e.getX()/scale)/(tileWidth);
				yPosition = (e.getY()/scale)/(tileHeight);
				
				getImage = (BufferedImage)vramBlock1.get(xPosition +(yPosition*MAX_WIDTH_DISPLAY));
				getPaletteIndex = ((Integer)paletteBankIndex.get(xPosition +(yPosition*MAX_WIDTH_DISPLAY))).intValue();
				getImageIndex = xPosition +(yPosition*MAX_WIDTH_DISPLAY);				
			}
			else
			{
				xPosition = (e.getX()/scale)/(collisionWidth);
				yPosition = (e.getY()/scale)/(collisionHeight);
				
				getImage = (BufferedImage)collisionTiles.get(xPosition +(yPosition*MAX_WIDTH_DISPLAY));
				getPaletteIndex = 0;
				getImageIndex = xPosition +(yPosition*MAX_WIDTH_DISPLAY);				
			}

			isClicked = true;		
		}
		else if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3)
		{
			if(collisionLayerActive == true)
				return;
			
			xPosition = (e.getX()/scale)/(tileWidth);
			yPosition = (e.getY()/scale)/(tileHeight);
			
			for(int i = 0; i<numImages; i++)
			{
				getImages.set(i, (BufferedImage)vramBlock1.get(xPosition + (yPosition*MAX_WIDTH_DISPLAY)+ i));
				getPaletteIndices.set(i, (Integer)paletteBankIndex.get(xPosition +(yPosition*MAX_WIDTH_DISPLAY) + i));
				getImageIndices.set(i, new Integer(xPosition + (yPosition*MAX_WIDTH_DISPLAY)+ i));
				
			}
			isRightClicked = true;
		}
	}
	
	public void processMouseWheelEvent(MouseWheelEvent e)
	{
		if(e.getWheelRotation()>0 && scale > 1)
		{
			scale--;
		}
		else if((e.getWheelRotation() < 0))
		{
			scale++;
		}
		this.repaint();
	}
	
	public void processMouseMotionEvent(MouseEvent e)
	{	
		if(collisionLayerActive != true)
		{
			if(e.getX()<(MAX_WIDTH_DISPLAY*tileWidth*scale) && e.getY()<(MAX_HEIGHT_DISPLAY*tileHeight*scale))
			{
				inBounds = true;
				xHighlight = (e.getX()/(tileWidth*scale))*(tileWidth*scale);
				yHighlight = (e.getY()/(tileHeight*scale))*(tileHeight*scale);
				
				if(xHighlightPrev != xHighlight || yHighlightPrev != yHighlight)
					this.repaint();
			}
			else
				inBounds = false;
		}else
		{
			if(e.getX()<(MAX_WIDTH_DISPLAY*collisionWidth*scale) && e.getY()<(MAX_HEIGHT_DISPLAY*collisionHeight*scale))
			{
				inBounds = true;
				xHighlight = (e.getX()/(collisionWidth*scale))*(collisionWidth*scale);
				yHighlight = (e.getY()/(collisionHeight*scale))*(collisionHeight*scale);
				
				if(xHighlightPrev != xHighlight || yHighlightPrev != yHighlight)
					this.repaint();
			}
			else
				inBounds = false;			
		}
			
	}
}