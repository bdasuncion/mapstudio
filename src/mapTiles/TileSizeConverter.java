package mapTiles;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

public class TileSizeConverter {

	int fromWidth;
	int fromHeight;
	int toWidth;
	int toHeight;
	int widthInTiles;
	int heightInTiles;
	BufferedImage tileSet;
	BufferedImage convertedImage;
	BufferedImage resizedTileSet[];
	
	public TileSizeConverter(int fromW, int fromH, int toW, int toH,
			int wInTiles, int hInTiles, BufferedImage[] tiles) {
		fromWidth = fromW;
		fromHeight = fromH;
		toWidth = toW;
		toHeight = toH;
		widthInTiles = wInTiles;
		heightInTiles = hInTiles;
		
		if(fromWidth == toWidth && fromHeight == toHeight) {
			int nTiles = wInTiles*hInTiles;
			resizedTileSet = new BufferedImage[nTiles];
			for(int i = 0; i<nTiles; i++) {
				resizedTileSet[i] = tiles[i];
			}
		}
		
		buildImage(tiles);
	}
	
	public BufferedImage[] getnewTiles() {
		return resizedTileSet;
	}
	
	public BufferedImage getConvertedImage() {
		return convertedImage;
	}
	
	public void convert() {
		if(fromWidth == toWidth && fromHeight == toHeight)
			return;
		convertImage();
		convertToTiles();
	}
	
	private void buildImage(BufferedImage[] tiles) {
		int type;
		IndexColorModel cm;
		
		type = tiles[0].getType();
		cm = (IndexColorModel)tiles[0].getColorModel();
		
		tileSet = new BufferedImage(widthInTiles*fromWidth,
				heightInTiles*fromHeight,type, cm);
		
		int ARGB[] = new int[fromWidth*fromHeight];
		for(int i = 0; i<heightInTiles; i++) {
			int y = i*fromHeight;
			for(int j = 0; j<widthInTiles; j++) {
				int x = j*fromWidth;
				tiles[widthInTiles*i + j].getRGB(0, 0, fromWidth, fromHeight, ARGB, 0, fromWidth);
				tileSet.setRGB( x, y, fromWidth, fromHeight, ARGB, 0, fromWidth);
			}
		}
	}
	
	private void convertImage() {
		IndexColorModel cm;
		int imgWidth, imgHeight, type;
		if(tileSet.getWidth()%toWidth != 0) {
			imgWidth = tileSet.getWidth() - 
			(tileSet.getWidth()%toWidth) + toWidth;
		} else {
			imgWidth = tileSet.getWidth();
		}
		
		if(tileSet.getHeight()%toHeight != 0) {
			imgHeight = tileSet.getHeight() - 
			(tileSet.getHeight()%toHeight) + toHeight;
		} else {
			imgHeight = tileSet.getHeight();
		}
		
		cm = (IndexColorModel)tileSet.getColorModel();
		type = tileSet.getType();
		
		convertedImage = new BufferedImage(imgWidth, imgHeight, type, cm);
		
		int ARGB[] = new int[tileSet.getWidth()*tileSet.getHeight()];
		
		tileSet.getRGB(0,0,tileSet.getWidth(),tileSet.getHeight(), ARGB, 0,tileSet.getWidth());
		int setX, setY;
		setX = (tileSet.getWidth() - imgWidth)/2;
		setY = (tileSet.getHeight() - imgHeight)/2;
		
		convertedImage.setRGB(setX, setY, tileSet.getWidth(), tileSet.getHeight(), ARGB, 0, tileSet.getWidth());
	}
	
	private void convertToTiles() {
		IndexColorModel cm = (IndexColorModel)convertedImage.getColorModel();
		int type = convertedImage.getType();
		int ntiles = convertedImage.getWidth()/toWidth*
			convertedImage.getHeight()/toHeight;
		resizedTileSet = new BufferedImage[ntiles];
		
		int ARGB[] = new int[toWidth*toHeight];
		
		for(int i = 0, idx = 0; i<convertedImage.getHeight()/toHeight; i++) {
			int y = i*toHeight;
			for(int j = 0; j<convertedImage.getWidth()/toWidth; j++, idx++) {
				int x = j*toWidth;
				
				resizedTileSet[idx] = new BufferedImage(toWidth, toHeight, type, cm);
				convertedImage.getRGB(x, y, toWidth, toHeight, ARGB, 0, toWidth);
				resizedTileSet[idx].setRGB(0, 0, toWidth, toHeight, ARGB, 0, toWidth);
			}
		}
		
		widthInTiles = convertedImage.getWidth()/toWidth;
		heightInTiles = convertedImage.getHeight()/toHeight;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}
}
