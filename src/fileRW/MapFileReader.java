package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import mapBlock.Map32x32Tiles;
import mapBlock.MapWxH;
import tileSetSelection.TileSetButtonDisplay;
import tools.ImageTools;

/*
File Structure
Header:
Map Width				4 bytes(width in map blocks)
Map Height				4 bytes(height in map blocks)
Map Layers				4 bytes(including collision Layer)
Map Block Width		4 bytes(width in tiles)
Map Block Height		4 bytes(height in tiles)
tile Width				4 bytes
tile Height			4 bytes
collision Width				4 bytes
collision Height			4 bytes
Pixel Size				4 bytes
number of Tiles		4 bytes
number of Palettes		4 bytes(max 16 for 4-bit tiles, 1 for 8-bit tiles)



Data-Palette:
Size:					4 bytes per element
Number of Elements:	16 for 4-bit pixels, 256 for 8-bit pixels

Data-Palette Used of Tiles:
Size:					1 byte per element
Number of Elements:	number of Tiles

Data-Tiles:
Size:					1 byte per element(4-bit or 8-bit pixels)
Number of Elements:	64 elements per tile(8x8 tiles)


Interleaved
Data-Index of Tiles in Map:(Map Width)*(Map Height)
Size:					2 bytes per element
Number of Elements:	1024 elements per map block(32x32 tiles per map block)

Data-Horizontal flip of tiles:(Map Width)*(Map Height)
Size:					1 byte per element
Number of Elements:	1024 elements per map block(32x32 tiles per map block)

Data-Vertical flip of tiles:(Map Width)*(Map Height)
Size:					1 byte per element
Number of Elements:	1024 elements per map block(32x32 tiles per map block)
*/

public class MapFileReader
{
	private byte[] data;
	private Vector<BufferedImage> vramBlock1;
	private Vector<BufferedImage> collisionTiles;
	private Vector<Integer> paletteBankIndex;
	private Vector<IndexColorModel> paletteBank;
	private Vector<Map32x32Tiles> Layer[];
	private MapWxH mapWxHLayers[];
	private Vector<BufferedImage[]> tileSetCollection;
	private Vector<int[]> tileDimCollection;
	private Vector<String> nameCollection;
	private int mapWidth;
	private int mapHeight;
	private int mapWidthInBlocks;
	private int mapHeightInBlocks;
	private int mapLayers;
	private int mapBlockWidth;
	private int mapBlockHeight;
	private int tileWidth;
	private int tileHeight;
	private int collisionWidth;
	private int collisionHeight;
	private int pixelSize;
	private int numberOfTiles;
	private int numberOfPalettes;
	private int numberOfTileSets;
	private FileInputStream fileIn;
	private BufferedInputStream fileInBuffer;
	private DataInputStream fileDataIn;
	private int idx = 0;
	
	/*TODO
	 * an error occurs when there is no tile data
	 * */
	public MapFileReader(File f)
	{
		try {
			fileIn = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		fileInBuffer = new BufferedInputStream(fileIn);
		fileDataIn = new DataInputStream(fileInBuffer);
	
		try {
			data = new byte[fileInBuffer.available()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			fileDataIn.readFully(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.readHeader();
		
		vramBlock1 = new Vector<BufferedImage>();
		collisionTiles = new Vector<BufferedImage>();
		paletteBankIndex = new Vector<Integer>();
		paletteBank = new Vector<IndexColorModel>();
		tileSetCollection = new Vector<BufferedImage[]>();
		tileDimCollection = new Vector<int[]>();
		
		mapWxHLayers = new MapWxH[mapLayers];
		mapWidth = mapBlockWidth*tileWidth;
		mapHeight = mapBlockHeight*tileHeight;
		
		mapWxHLayers[0] = new MapWxH(mapWidth, mapHeight,
				collisionWidth, collisionHeight);
		for(int i = 1; i<mapLayers; i++)
		{
			mapWxHLayers[i] = new MapWxH(mapWidth, mapHeight,
					tileWidth, tileHeight);
		}
		
		this.readPalette();
		this.readPaletteIndex();
		this.readTiles();
		this.readWxHBGLayers();
		this.readTileSet();
		
		try {
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		collisionTiles = null;
	}
	
	private void readHeader()
	{
		mapWidthInBlocks = (int)(data[0]|data[1]>>8|data[2]>>16|data[3]>>24);
		mapHeightInBlocks = (int)(data[4]|data[5]>>8|data[6]>>16|data[7]>>24);
		mapLayers = (int)(data[8]|data[9]>>8|data[10]>>16|data[11]>>24);
		mapBlockWidth = (int)(data[12]|data[13]>>8|data[14]>>16|data[15]>>24);
		mapBlockHeight = (int)(data[16]|data[17]>>8|data[18]>>16|data[19]>>24);
		tileWidth = (int)(data[20]|data[21]>>8|data[22]>>16|data[23]>>24);	
		tileHeight = (int)(data[24]|data[25]>>8|data[26]>>16|data[27]>>24);	
		collisionWidth = (int)(data[28]|data[29]>>8|data[30]>>16|data[31]>>24);
		collisionHeight = (int)(data[32]|data[33]>>8|data[34]>>16|data[35]>>24);
		pixelSize = (int)(data[36]|data[37]>>8|data[38]>>16|data[39]>>24);
		numberOfTiles = (int)(data[40]|data[41]>>8|data[42]>>16|data[43]>>24);
		numberOfPalettes = (int)(data[44]|data[45]>>8|data[46]>>16|data[47]>>24);
		
		idx  = 48;
	}
	
	private void readPalette() {
		int numberOfColours = (int)java.lang.Math.pow(2,pixelSize);
		byte R[], G[], B[];
		R = new byte[numberOfColours];
		G = new byte[numberOfColours];
		B = new byte[numberOfColours];
		
		for(int i = 0; i<numberOfPalettes; i++) {
			for( int j = 0; j<numberOfColours; idx+=4, j++) {
				R[j] = data[idx];
				G[j] = data[idx+1];
				B[j] = data[idx+2];
			}
			
			paletteBank.add(new IndexColorModel(pixelSize,numberOfColours,R,G,B));
		}
	}
	
	private void readPaletteIndex() {
		for(int i = 0; i<numberOfTiles; i++, idx++) {
			paletteBankIndex.add(new Integer(data[idx]));
		}
	}
	
	private void readTiles()
	{
		byte imageDataBytes[] = new byte[tileWidth*tileHeight*pixelSize/8];
		
		if(numberOfTiles<2)
			return;
		for(int i = 0; i<numberOfTiles; i++) {	
			for(int j = 0; j<tileWidth*tileHeight*pixelSize/8; j++, idx++) {
				imageDataBytes[j] = data[idx];
			}
			int idxPal = ((Integer)paletteBankIndex.get(i)).intValue();
			IndexColorModel pal = (IndexColorModel) paletteBank.get(idxPal);
			vramBlock1.add(ImageTools.createBufferedImage(tileWidth, tileHeight, pal, imageDataBytes));
		}
	}
	
	private void readBGLayers() {
	    //this.setCollisionTiles();
	    this.readCollisionLayer();
	    int i = 1;
	    for(i = 1; i<mapLayers; i++) {
	    	this.readBGBlocks(i);
	    }
	}
	
	/*private void setCollisionTiles() { 
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
	    for(int i = 0; i<tiles.length; i++)  {
	    	collisionTiles.add(tiles[i]);
	    }
	}*/
	
	private void readCollisionLayer()
	{
	    int i = 0;
		
	    for(i = 0; i<mapWidthInBlocks*mapHeightInBlocks; i++)
	    {
	    	Map32x32Tiles mapBlock = new Map32x32Tiles();
			for(int j = 0; j<mapBlockWidth*mapBlockHeight; j++)
			{
			    int tileIdx = data[idx]|(data[idx+1]>>8);
			    int hflip = 0;
			    int vflip = 0;
			    int paletteBankIdx = 0;
			    idx+=4;
			    mapBlock.setIndexOfTile(tileIdx, j);
			    mapBlock.setHorizontalFlip(hflip, j);
			    mapBlock.setVerticalFlip(vflip, j);
			    mapBlock.setPaletteBank(paletteBankIdx, j);	
			    int x ,y;
			    y = (j/mapBlockWidth)*tileHeight;
			    x = (j%mapBlockWidth)*tileWidth;
								
			    BufferedImage imgSet = ImageTools.createBufferedImage(tileWidth, 
				    tileHeight,
				    (IndexColorModel) ((BufferedImage)collisionTiles.get(tileIdx)).getColorModel(), 
				    this.getDataBytes((BufferedImage)collisionTiles.get(tileIdx)));
							
			    mapBlock.setTileAt(ImageTools.makeImageBGTransparet(imgSet), x, y);
			}
			
			Layer[0].add(mapBlock);
	    }	    
	}
	
	private void readBGBlocks(int layer)
	{
		int i = 0;

		for(i = 0; i<mapWidthInBlocks*mapHeightInBlocks; i++)
		{
			Map32x32Tiles mapBlock = new Map32x32Tiles();
			for(int j = 0; j<mapBlockWidth*mapBlockHeight; j++)
			{
				int tileIdx = data[idx]|(data[idx+1]>>8);
				int hflip = data[idx+2];
				int vflip = data[idx+3];
				int paletteBankIdx = ((Integer)paletteBankIndex.get(tileIdx)).intValue();
				idx+=4;
				mapBlock.setIndexOfTile(tileIdx, j);
				mapBlock.setHorizontalFlip(hflip, j);
				mapBlock.setVerticalFlip(vflip, j);
				mapBlock.setPaletteBank(paletteBankIdx, j);	
				int x ,y;
				y = (j/mapBlockWidth)*tileHeight;
				x = (j%mapBlockWidth)*tileWidth;
							
				BufferedImage imgSet = ImageTools.createBufferedImage(tileWidth, 
						tileHeight,
						(IndexColorModel) ((BufferedImage)vramBlock1.get(tileIdx)).getColorModel(), 
						this.getDataBytes((BufferedImage)vramBlock1.get(tileIdx)));
			
				if(hflip == 1)
					imgSet.setData(ImageTools.imageFlipHorizontal(imgSet));
				if(vflip == 1)
					imgSet.setData(ImageTools.imageFlipVertical(imgSet));
					
				mapBlock.setTileAt(ImageTools.makeImageBGTransparet(imgSet), x, y);
			}
			
			Layer[layer].add(mapBlock);
		}
	}
	
	private void readWxHBGLayers()
	{
	    //this.setWxHCollisionTiles();
	    this.readWxHCollisionLayer();
	    int i = 1;
	    for(i = 1; i<mapLayers; i++)
	    {
	    	this.readWxHBGBlocks(i);
	    }
	}

	/*private void setWxHCollisionTiles()
	{ 
	    File f;
	    TileReader reader;
	    InputStream stream = this.getClass().getResourceAsStream("/res/collisiontiles.tile");
	    if(stream == null)
	    {
			f = new File("res/collisiontiles.tile");
			reader = new TileReader();
			reader.read(f);
	    }
	    else
	    {
	    	reader = new TileReader(stream);
	    }
	    
	    BufferedImage[] tiles = reader.getTile();
	    for(int i = 0; i<tiles.length; i++)
	    {
	    	collisionTiles.add(ImageTools.resizeImage(tiles[i], collisionWidth, collisionHeight));
	    }
	}*/
	
	private void readWxHCollisionLayer()
	{
	    int i = 0;
		
	    MapWxH mapBlock = new MapWxH(mapWidth, mapHeight, collisionWidth, collisionHeight);
	    int collisionBlockwidth = mapWidth/collisionWidth;
	    int collisionBlockHeight = mapHeight/collisionHeight;
	    for(i = 0; i<collisionBlockwidth*collisionBlockHeight; i++)
	    {
		    int tileIdx = data[idx]|(data[idx+1]>>8);
		    int hflip = 0;
		    int vflip = 0;
		    int paletteBankIdx = 0;
		    idx+=4;
		    mapBlock.setIndexFromTilesAt(tileIdx, i);
		    mapBlock.setFlipHorizontalAt(hflip, i);
		    mapBlock.setFlipVerticalAt(vflip, i);
		    mapBlock.setPaletteBankAt(paletteBankIdx, i);	
		    int x ,y;
		    y = (i/collisionBlockwidth)*collisionHeight;
		    x = (i%collisionBlockwidth)*collisionWidth;
							
		    BufferedImage imgSet = ImageTools.createBufferedImage(collisionWidth, 
		    	collisionHeight,
			    (IndexColorModel) ((BufferedImage)collisionTiles.get(tileIdx)).getColorModel(), 
			    this.getDataBytes((BufferedImage)collisionTiles.get(tileIdx)));
		    mapBlock.setTileAt(ImageTools.makeImageBGTransparet(imgSet), x, y);
	    }
	    mapWxHLayers[0] = mapBlock;
	}
	/*
	 * TODO*/
	private void readWxHBGBlocks(int layer)
	{
		MapWxH mapBlock = new MapWxH(mapWidth, mapHeight, tileWidth, tileHeight);
		
		for(int j = 0; j<mapBlockWidth*mapBlockHeight; j++)
		{
			int tileIdx = data[idx]|(data[idx+1]>>8);
			int hflip = data[idx+2];
			int vflip = data[idx+3];
			int paletteBankIdx = ((Integer)paletteBankIndex.get(tileIdx)).intValue();
			idx+=4;
			mapBlock.setIndexFromTilesAt(tileIdx, j);
			mapBlock.setFlipHorizontalAt(hflip, j);
			mapBlock.setFlipVerticalAt(vflip, j);
			mapBlock.setPaletteBankAt(paletteBankIdx, j);	
			int x ,y;
			y = (j/mapBlockWidth)*tileHeight;
			x = (j%mapBlockWidth)*tileWidth;
						
			BufferedImage imgSet;
			if(numberOfTiles>1)
				imgSet = ImageTools.createBufferedImage(tileWidth, tileHeight,
					(IndexColorModel) ((BufferedImage)vramBlock1.get(tileIdx)).getColorModel(), 
					this.getDataBytes((BufferedImage)vramBlock1.get(tileIdx)));
			else
				imgSet = ImageTools.createEmptyImage(tileWidth, tileHeight);
		
			if(hflip == 1)
				imgSet.setData(ImageTools.imageFlipHorizontal(imgSet));
			if(vflip == 1)
				imgSet.setData(ImageTools.imageFlipVertical(imgSet));
				
			mapBlock.setTileAt(ImageTools.makeImageBGTransparet(imgSet), x, y);
		}
		
		mapWxHLayers[layer] = mapBlock;
	}
	
	private byte[] getDataBytes(BufferedImage img)
	{
		DataBuffer dataBuff = img.getData().getDataBuffer();
		byte dataBytes[] = new byte[dataBuff.getSize()];
		for(int i = 0;i<dataBuff.getSize(); i++)
		{
			dataBytes[i] = (byte) (dataBuff.getElem(i)&0xff);
		}
		
		return dataBytes;
	}
	
	private void readTileSet()
	{
		numberOfTileSets = (int)(data[idx]|data[idx+1]>>8|data[idx+2]>>16|data[idx+3]>>24);
		//Additional +4 because of resered data
		idx += 8;
		int idxImage = 1;
		
		nameCollection = new Vector<String>();
		for(int i = 0; i<numberOfTileSets; i++)
		{
			this.readName();
			int widthInTiles = (int)(data[idx]|data[idx+1]>>8);
			int heightInTiles = (int)(data[idx+2]|data[idx+3]>>8);
			int tileDim[] = new int[2];
			tileDim[0] = widthInTiles;
			tileDim[1] = heightInTiles;
			
			tileDimCollection.add(tileDim);
			
			int numTilesInSet = widthInTiles*heightInTiles;
			idx += 4;
			
			BufferedImage tileSetImgs[] = new BufferedImage[numTilesInSet];
			for(int j = 0; j<numTilesInSet; j++)
			{
				if((int)data[idx] == 0)
				{
					tileSetImgs[j] = vramBlock1.get(0);
				}else
				{
					tileSetImgs[j] = vramBlock1.get(idxImage);
					idxImage++;
				}
				idx++;
			}
			tileSetCollection.add(tileSetImgs);
		}
	}
	
	public int getMapWidthInBlocks()
	{
		return mapWidthInBlocks;
	}
	
	public int getMapHeightInBlocks()
	{
		return mapHeightInBlocks;
	}
	
	public Vector getPaletteBank()
	{
		return paletteBank;
	}
	
	public Vector getPaletteBankIndex()
	{
		return paletteBankIndex;
	}
	
	public Vector getTiles()
	{
		return vramBlock1;
	}
	
	public Vector[] getLayers()
	{
		return Layer;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public MapWxH[] getMapWxHLayers() {
		return mapWxHLayers;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getNumberOfTileSets() {
		return numberOfTileSets;
	}

	public Vector<BufferedImage[]> getTileSetCollection() {
		return tileSetCollection;
	}

	public Vector<int[]> getTileDimCollection() {
		return tileDimCollection;
	}
	
	public void readName()
	{
		int nameLength = (int)(data[idx]|data[idx+1]>>8);
		byte name[] = new byte[nameLength];
		idx += 2;
		for(int i = 0; i<nameLength; i++)
		{
			name[i] = data[idx + i];
		}
		idx += nameLength;
		nameCollection.add(new String(name));
	}

	public Vector<String> getNameCollection() {
		return nameCollection;
	}	
}