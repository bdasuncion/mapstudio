package tools;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


public class ImageTools
{
	public ImageTools()
	{
		
	}
	
	public static BufferedImage createBufferedImage(int width, int height, IndexColorModel cm, byte[] imageData) {
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		BufferedImage image = new BufferedImage(width,height,type, cm);
		
		DataBuffer data = image.getData().getDataBuffer();
		
		for(int i = 0; i<imageData.length; i++ )
		{
			data.setElem(i, (byte)imageData[i]);
		}
		
		int pixelSize = cm.getPixelSize();
		
		WritableRaster wRaster = Raster.createPackedRaster(data, width, height, pixelSize, new Point(0,0));
		
		image.setData(wRaster);
		
		return image;
	}
	
	public static BufferedImage createEmptyImage(int width, int height, IndexColorModel cm)
	{
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		BufferedImage image = new BufferedImage(width,height,type, cm);
		
		int pixelSize = cm.getPixelSize();
		
		DataBuffer data = image.getData().getDataBuffer();
		for(int i = 0; i<width*height*pixelSize/8; i++ )
		{
			data.setElem(i, (byte)0);
		}
		
		WritableRaster wRaster = Raster.createPackedRaster(data, width, height, pixelSize, new Point(0,0));
		
		image.setData(wRaster);
		return image;
	}
	
	public static IndexColorModel createColorModel() {
		byte R[],G[],B[],A[];
		
		R = new byte[16];
		G = new byte[16];
		B = new byte[16];
		A = new byte[16];

		R[0] = G[0] = B[0] = (byte)(255);
		A[0] = (byte)0;
		A[1] = (byte)255;
		
		IndexColorModel cm = new IndexColorModel(4,16,R,G,B,A);
		return cm;
	}
	
	public static BufferedImage createEmptyImage(int width, int height) {
		byte R[],G[],B[],A[];
		
		R = new byte[16];
		G = new byte[16];
		B = new byte[16];
		A = new byte[16];

		R[0] = G[0] = B[0] = (byte)(255);
		A[0] = (byte)255;
		
		IndexColorModel cm = new IndexColorModel(4,16,R,G,B,A);
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY, cm);
		return image;
	}
	
	public static BufferedImage createEraserTile(int width, int height) {
		byte R[],G[],B[],A[];
		
		R = new byte[16];
		G = new byte[16];
		B = new byte[16];
		A = new byte[16];
		
		for(int i = 0; i < 16; i++) {
			R[i] = (byte)0;
			G[i] = (byte)0;
			B[i] = (byte)0;
			A[i] = (byte)255;
		}
		A[0] = (byte)0;
		
		IndexColorModel cm = new IndexColorModel(4,16,R,G,B,A);
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY, cm);
		return image;
	}
	
	public static BufferedImage makeImageBGTransparet(BufferedImage image)
	{
		IndexColorModel cm = (IndexColorModel)image.getColorModel();
		byte R[], G[], B[], A[];
		
		R = new byte[cm.getMapSize()];
		G = new byte[cm.getMapSize()];
		B = new byte[cm.getMapSize()];
		A = new byte[cm.getMapSize()];
		
		for(int i = 0; i<cm.getMapSize(); i++)
		{
			R[i] = (byte)cm.getRed(i);
			G[i] = (byte)cm.getGreen(i);
			B[i] = (byte)cm.getBlue(i);
			A[i] = (byte)255;
		}
		
		A[0] = (byte)0;
		
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		
		IndexColorModel newcm = new IndexColorModel(cm.getPixelSize(), cm.getMapSize(), R, G, B, A);
		
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type, newcm);
		
		newImage.setData(image.getData());
		
		return newImage;
	}
	
	public static BufferedImage makeImageTranslucent(BufferedImage image)
	{
		IndexColorModel cm = (IndexColorModel)image.getColorModel();
		byte R[], G[], B[], A[];
		
		R = new byte[cm.getMapSize()];
		G = new byte[cm.getMapSize()];
		B = new byte[cm.getMapSize()];
		A = new byte[cm.getMapSize()];
		
		for(int i = 0; i<cm.getMapSize(); i++)
		{
			R[i] = (byte)cm.getRed(i);
			G[i] = (byte)cm.getGreen(i);
			B[i] = (byte)cm.getBlue(i);
			A[i] = (byte)128;
		}
		
		A[0] = (byte)0;
		
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		
		IndexColorModel newcm = new IndexColorModel(cm.getPixelSize(), cm.getMapSize(), R, G, B, A);
		
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type, newcm);
		
		newImage.setData(image.getData());
		
		return newImage;
	}
	
	public static WritableRaster imageShiftRight(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<height;i++)
		{
			int idx = 1;
			rgbValCurrent = image.getRGB(0, i);
			for(int j = 0; j<width; j++, idx++)
			{
				if(j == width - 1)
					idx = 0;
				rgbValNext = image.getRGB(idx, i);
				image.setRGB(idx, i, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageShiftLeft(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<height;i++)
		{
			int idx = width - 2;
			rgbValCurrent = image.getRGB(31, i);
			for(int j = width - 1; j>=0; j--, idx--)
			{
				if(j == 0)
					idx = 31;
				rgbValNext = image.getRGB(idx, i);
				image.setRGB(idx, i, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageShiftUp(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<width;i++)
		{
			int idx = height - 2;
			rgbValCurrent = image.getRGB(i, 31);
			for(int j = height - 1; j>=0; j--, idx--)
			{
				if(j == 0)
					idx = 31;
				rgbValNext = image.getRGB(i, idx);
				image.setRGB(i, idx, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageShiftDown(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<width;i++)
		{
			int idx = 1;
			rgbValCurrent = image.getRGB(i, 0);
			for(int j = 0; j<height; j++, idx++)
			{
				if(j == height - 1)
					idx = 0;
				rgbValNext = image.getRGB(i, idx);
				image.setRGB(i, idx, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageFlipHorizontal(BufferedImage image)
	{
		int width,height, rgbVal = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<height;i++) {
			for(int j = 0; j<width/2; j++) {
				rgbVal = image.getRGB((width - 1) - j, i);
				image.setRGB((width - 1) - j, i, image.getRGB(j, i));
				image.setRGB(j, i, rgbVal);
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageFlipVertical(BufferedImage image) {
		int width,height, rgbVal = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<width;i++) {
			for(int j = 0; j<height/2; j++) {
				rgbVal = image.getRGB(i, (height - 1) - j);
				image.setRGB(i, (height - 1) - j, image.getRGB(i, j));
				image.setRGB(i, j, rgbVal);
			}	
		}
		return image.getRaster();
	}
	
	public static BufferedImage resizeImage(BufferedImage orgImg, int newW, int newH) {
		BufferedImage resized = createEmptyImage(newW,newH,(IndexColorModel)orgImg.getColorModel());
		
		int rgbOrg[] = new int[orgImg.getWidth()*orgImg.getHeight()];
		orgImg.getRGB(0, 0, orgImg.getWidth(), orgImg.getHeight(), rgbOrg, 0, orgImg.getWidth());
		
		int i,j,x,y;
		
		x = 0;
		for(i = 0; i<newW/orgImg.getWidth(); i++) {
			y = 0;
			for(j = 0; j<newH/orgImg.getHeight(); j++) {
				resized.setRGB(x, y, orgImg.getWidth(), orgImg.getHeight(), rgbOrg, 0, orgImg.getWidth());	
				y += orgImg.getHeight();
			}
			x += orgImg.getWidth();
		}
		return resized;
	}
	
	public static BufferedImage copyBufferedImage(BufferedImage img) {
		
		DataBuffer dataBuff = img.getData().getDataBuffer();
		byte dataBytes[] = new byte[dataBuff.getSize()];
		
		for(int i = 0;i<dataBuff.getSize(); i++) {
			dataBytes[i] = (byte) (dataBuff.getElem(i)&0xff);
		}
		
		return createBufferedImage(img.getWidth(), img.getHeight(), 
				(IndexColorModel)img.getColorModel(), dataBytes);
	}
	
	public static BufferedImage fill(BufferedImage image) {
		IndexColorModel cm = (IndexColorModel)image.getColorModel();
		int rgb[] = new int[cm.getMapSize()];
		cm.getRGBs(rgb);
		for (int i = 0; i < image.getHeight(); ++i) {
			for (int j = 0; j < image.getWidth(); ++j) {
				image.setRGB(j, i, rgb[1]);
			}
		}
		
		return image;
	}
}