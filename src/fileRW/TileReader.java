package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import tools.ImageTools;
/* 
File Structure
Header
pixel size: 	 			4 bytes
number of tiles in length:	4 bytes
number of tiles in width:	4 bytes
number of 8x8 tiles:		4 bytes

Palette			
size:					4 bytes per element
number of elements:		16(pixel size: 4 bits) or 256 entries(pixel size: 8 bits)
Image data
size:					1 byte per element
number of elements:		(8*8*number of tile)/2(pixel size: 4 bits) or 8*8*number of tile(pixel size: 8 bits)	
*/
public class TileReader
{
	private byte[] data;
	private FileInputStream fileInStream;
	private BufferedInputStream fileInBuffer;
	private DataInputStream fileDataInStream;
	private IndexColorModel tilePalette;
	private BufferedImage tiles[];
	private int pixelSize =0;
	private int numberOfTiles =0;
	private int widthInTiles;
	private int heightInTiles;
	private int i = 0;
	
	public TileReader() {
	}
	
	public TileReader(InputStream inStream) {
		try {
			data = new byte[inStream.available()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
		    inStream.read(data, 0, inStream.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.readHeader();
		this.readPalette();
		this.readImageData();
	}
	
	public void read(File f) {
		try {
			fileInStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		fileInBuffer = new BufferedInputStream(fileInStream);
		fileDataInStream = new DataInputStream(fileInBuffer);
		
		try {
			data = new byte[fileInBuffer.available()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			fileDataInStream.readFully(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.readHeader();
		this.readPalette();
		this.readImageData();
	}
	
	private void readHeader() {
		pixelSize = (int)(data[0]&0xFF|(data[1]<<8)&0xFF00|(data[2]<<16)&0xFF0000|(data[3]<<24)&0xFF000000);
		widthInTiles = (int)(data[4]&0xFF|(data[5]<<8)&0xFF00|(data[6]<<16)&0xFF0000|(data[7]<<24)&0xFF000000);
		heightInTiles = (int)(data[8]&0xFF|(data[9]<<8)&0xFF00|(data[10]<<16)&0xFF0000|(data[11]<<24)&0xFF000000);
		numberOfTiles = (int)(data[12]&0xFF|(data[13]<<8)&0xFF00|(data[14]<<16)&0xFF0000|(data[15]<<24)&0xFF000000);
	}
	
	private void readPalette() {
		int numberOfColours = (int)java.lang.Math.pow(2,pixelSize);
		byte R[] = new byte[numberOfColours];
		byte G[] = new byte[numberOfColours];
		byte B[] = new byte[numberOfColours];
		
		int idx;
		for(i = 16,idx = 0; idx<numberOfColours; i+=4,idx++)
		{
			R[idx] = data[i];
			G[idx] = data[i+1];
			B[idx] = data[i+2];
		}
		
		tilePalette = new IndexColorModel(pixelSize,numberOfColours,R,G,B);
	}
	
	private void readImageData() {
		tiles = new BufferedImage[numberOfTiles];
		int tileWidth = 8;
		int tileHeight = 8;
		int sizeInBytes = tileWidth*tileHeight*pixelSize/8;
		for(int idx = 0;idx<numberOfTiles;idx++)
		{
			byte[] imageData = new byte[sizeInBytes];
			for(int j = 0; j<sizeInBytes; j++,i++)
			{
				imageData[j] = data[i];
			}
			tiles[idx] = ImageTools.createBufferedImage(tileWidth, tileHeight, tilePalette, imageData);
		}
	}
	
	public IndexColorModel getPalette() {
		return tilePalette;
	}
	
	public BufferedImage[] getTile() {
		return tiles;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}
}