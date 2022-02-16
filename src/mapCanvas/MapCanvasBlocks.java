package mapCanvas;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import mapBlock.Map32x32Tiles;
import tools.ImageTools;

public class MapCanvasBlocks extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1568254708513450178L;
	private Vector<Map32x32Tiles> Layer[];
	private Vector<BufferedImage> tileSet;
	private Vector<Integer> tileActiveSet;
	private Vector<Integer> tileIdxSet;
	private Vector<Integer> paletteBankIndexSet;
	private Vector<Integer> hFlipSet;
	private Vector<Integer> vFlipSet;
	private Vector<BufferedImage> tileHighlight;

	private boolean collisionLayerVisible = false;
	private boolean layer0Visible = false;
	private boolean layer1Visible = false;
	private boolean layer2Visible = true;
	private boolean layer3Visible = true;
	
	private int activeLayerIndex = 4;
	private int scale = 1;
	
	private int mapBlockWidth = 1;
	private int mapBlockHeight = 1;
	
	private int tileSetWidth = 0;
	private int tileSetHeight = 0;
	
	private int xTileSetPosition = 0;
	private int yTileSetPosition = 0;
	private int xTileSetPositionPrev = 0;
	private int yTileSetPositionPrev = 0;
	
	private static final int tileWidth = 8;
	private static final int tileHeight = 8;
	private static final int mapWidthInTiles = 32;
	private static final int mapHeightInTiles = 32;
	
	public MapCanvasBlocks(int widthInPixels, int heightInPixels)
	{
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
		//modify this
		mapBlockWidth = widthInPixels/256;
		if(widthInPixels%256 > 0)
			mapBlockWidth += 1;
		mapBlockHeight = heightInPixels/256;
		if(heightInPixels%256 > 0)
			mapBlockHeight += 1;
		
		Layer = new Vector[5];
		
		Layer[0] = new Vector<Map32x32Tiles>(mapBlockWidth*mapBlockHeight);
		
		for(int i = 0; i<mapBlockWidth*mapBlockHeight; i++)
		{
			Layer[0].add(new Map32x32Tiles());
		}
		
		Layer[1] = new Vector<Map32x32Tiles>(mapBlockWidth*mapBlockHeight);
		
		for(int i = 0; i<mapBlockWidth*mapBlockHeight; i++)
		{
			Layer[1].add(new Map32x32Tiles());
		}
		
		Layer[2] = new Vector<Map32x32Tiles>(mapBlockWidth*mapBlockHeight);
		
		for(int i = 0; i<mapBlockWidth*mapBlockHeight; i++)
		{
			Layer[2].add(new Map32x32Tiles());
		}
		
		Layer[3] = new Vector<Map32x32Tiles>(mapBlockWidth*mapBlockHeight);
		
		for(int i = 0; i<mapBlockWidth*mapBlockHeight; i++)
		{
			Layer[3].add(new Map32x32Tiles());
		}
		
		Layer[4] = new Vector<Map32x32Tiles>(mapBlockWidth*mapBlockHeight);
		
		for(int i = 0; i<mapBlockWidth*mapBlockHeight; i++)
		{
			Layer[4].add(new Map32x32Tiles());
		}
		
		tileSet = new Vector<BufferedImage>(0);
		tileActiveSet = new Vector<Integer>(0);
		tileHighlight = new Vector<BufferedImage>(0);
		tileIdxSet = new Vector<Integer>(0);
		paletteBankIndexSet = new Vector<Integer>(0);
		hFlipSet = new Vector<Integer>(0);
		vFlipSet = new Vector<Integer>(0);
		
		this.setPreferredSize(new Dimension(mapWidthInTiles*tileWidth*mapBlockWidth*scale, mapHeightInTiles*tileHeight*mapBlockHeight*scale));
		this.revalidate();
		this.setVisible(true);
	}
	
	public int getContainerWidth()
	{
		return mapBlockWidth*scale*tileWidth*mapWidthInTiles;
	}
	
	public int getContainerHeight()
	{
		return mapBlockHeight*scale*tileHeight*mapHeightInTiles;
	}
	
	public int getWidth()
	{
		return mapBlockWidth*scale*tileWidth*mapWidthInTiles;
	}
	
	public int getHeight()
	{
		return mapBlockHeight*scale*tileHeight*mapHeightInTiles;
	}
	
	public int getMapWidthInTiles()
	{
		return mapBlockWidth;
	}
	
	public int getMapHeightInTiles()
	{
		return mapBlockHeight;
	}
	
	public Vector<Map32x32Tiles>[] getLayers()
	{
		return Layer;
	}
	
	public void setLayers(Vector<Map32x32Tiles> layers[])
	{
		for(int i = 0; i<layers.length; i++)
		{
			Layer[i].clear();
			for(int j = 0; j<layers[i].size(); j++)
			{
				Layer[i].add(layers[i].get(j));
			}
		}
	}
	
	public void setTileSet(
	Vector<BufferedImage> tiles, Vector<Integer> activeTiles, 
	Vector<Integer> tileidx, Vector<Integer> palBankIdx, 
	Vector<Integer> hFlipFlg, Vector<Integer> vFlipFlg, 
	int tileWidth, int tileHeight)
	{
		
		tileSetWidth = tileWidth;
		tileSetHeight = tileHeight;
		
		tileHighlight.clear();
		tileSet.clear();
		tileActiveSet.clear();
		tileIdxSet.clear();
		paletteBankIndexSet.clear();
		hFlipSet.clear();
		vFlipSet.clear();
		int i;
		for(i = 0; i<tiles.size(); i++)
		{
			tileHighlight.add(ImageTools.makeImageTranslucent((BufferedImage)tiles.get(i)));
			tileSet.add(ImageTools.makeImageBGTransparet((BufferedImage)tiles.get(i)));
			tileActiveSet.add(activeTiles.get(i));
			tileIdxSet.add(tileidx.get(i));
			paletteBankIndexSet.add(palBankIdx.get(i));
			hFlipSet.add(hFlipFlg.get(i));
			vFlipSet.add(vFlipFlg.get(i));	
		}
	}
	
	public void setActiveLayer(int i)
	{
		activeLayerIndex = i;
	}
	
	public int getActiveLayer()
	{
		return activeLayerIndex;
	}
	
	public void setCollisionLayerVisible(boolean a)
	{
		collisionLayerVisible = a;
	}
	
	public void setLayer0Visible(boolean a)
	{
		layer0Visible = a;
	}
	
	public void setLayer1Visible(boolean a)
	{
		layer1Visible = a;
	}
	
	public void setLayer2Visible(boolean a)
	{
		layer2Visible = a;
	}
	
	public void setLayer3Visible(boolean a)
	{
		layer3Visible = a;
	}
	
	public void setScale(int s)
	{
		scale = s;
		this.setPreferredSize(new Dimension(mapWidthInTiles*tileWidth*mapBlockWidth*scale, mapHeightInTiles*tileHeight*mapBlockHeight*scale));
		this.revalidate();
		this.repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
	
		g2D.fill(new Rectangle(0, 0, mapWidthInTiles*tileWidth*mapBlockWidth*scale,	mapHeightInTiles*tileHeight*mapBlockHeight*scale));
		
		int x = 0,y = 0;
		BufferedImage display = g2D.getDeviceConfiguration().createCompatibleImage(
				mapWidthInTiles*tileWidth, mapHeightInTiles*tileHeight);
	
		g2D.scale(scale, scale);
		
		if(layer3Visible == true)
		{
			for(int i = 0; i<Layer[4].size(); i++ )
			{
				Map32x32Tiles MapBlock = (Map32x32Tiles)Layer[4].get(i);
				display = MapBlock.getMapBlock();
				
				g2D.drawImage(display, x*mapWidthInTiles*tileWidth, y*mapHeightInTiles*tileHeight, display.getWidth(),display.getHeight(), this);
				
				x++;
				
				if(x >= mapBlockWidth)
				{
					x = 0;
					y++;
				}			
			}
		}
		
		
		x = 0;
		y = 0;
		
		if(layer2Visible == true)
		{
			for(int i = 0; i<Layer[3].size(); i++ )
			{
				Map32x32Tiles MapBlock = (Map32x32Tiles)Layer[3].get(i);
				display = MapBlock.getMapBlock();
				
				g2D.drawImage(display, x*MapBlock.getWidthOfMap()*8, y*MapBlock.getHeightOfMap()*8, display.getWidth(),display.getHeight(), this);
				
				x++;
				
				if(x == mapBlockWidth)
				{
					x = 0;
					y++;
				}			
			}
		}
		
		x = 0;
		y = 0;
		
		if(layer1Visible == true)
		{
			for(int i = 0; i<Layer[2].size(); i++ )
			{
				Map32x32Tiles MapBlock = (Map32x32Tiles)Layer[2].get(i);
				display = MapBlock.getMapBlock();
				
				g2D.drawImage(display, x*MapBlock.getWidthOfMap()*8, y*MapBlock.getHeightOfMap()*8, display.getWidth(),display.getHeight(), this);
				
				x++;
				
				if(x == mapBlockWidth)
				{
					x = 0;
					y++;
				}			
			}
		}
		
		x = 0;
		y = 0;
		
		if(layer0Visible == true)
		{
			for(int i = 0; i<Layer[1].size(); i++ )
			{
				Map32x32Tiles MapBlock = (Map32x32Tiles)Layer[1].get(i);
				display = MapBlock.getMapBlock();
				
				g2D.drawImage(display, x*MapBlock.getWidthOfMap()*8, y*MapBlock.getHeightOfMap()*8, display.getWidth(),display.getHeight(), this);
				
				x++;
				
				if(x == mapBlockWidth)
				{
					x = 0;
					y++;
				}			
			}
		}
		
		x = 0;
		y = 0;
		
		if(collisionLayerVisible == true)
		{
			for(int i = 0; i<Layer[0].size(); i++ )
			{
				Map32x32Tiles MapBlock = (Map32x32Tiles)Layer[0].get(i);
				display = MapBlock.getMapBlock();
				
				g2D.drawImage(display, x*MapBlock.getWidthOfMap()*8, y*MapBlock.getHeightOfMap()*8, display.getWidth(),display.getHeight(), this);
				
				x++;
				
				if(x == mapBlockWidth)
				{
					x = 0;
					y++;
				}			
			}
		}
		
		x = 0;
		y = 0;
		
		for(int i = 0; i<tileHighlight.size(); i++)
		{
			display = (BufferedImage)tileHighlight.get(i);
			
			g2D.drawImage(display, display.getWidth()*x + xTileSetPosition, 
					display.getHeight()*y + yTileSetPosition,
					display.getWidth(),display.getHeight(), this);
			

			
			x++;
			if(x>=tileSetWidth)
			{
				x = 0;
				y++;
			}
			if(y>=tileSetHeight)
			{
				y = 0;
			}
		}
		
		xTileSetPositionPrev = xTileSetPosition;
		yTileSetPositionPrev = yTileSetPosition;
		
		g2D.dispose();
	}
	
	public void processMouseEvent(MouseEvent e)
	{
		if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1)
		{
			xTileSetPosition = (e.getX()/(tileWidth*scale))*(tileWidth);
			yTileSetPosition = (e.getY()/(tileHeight*scale))*(tileHeight);
			
			BufferedImage disp;
			int x = 0,y = 0,i = 0;
			int mapWidthTotal = mapWidthInTiles*tileWidth*mapBlockWidth;
			int mapHeightTotal = mapHeightInTiles*tileHeight*mapBlockHeight;
			for(i = 0; i<tileSet.size(); i++)
			{
				if(((Integer)tileActiveSet.get(i)).intValue() == 0)
				{
					x++;
					if(x>=tileSetWidth)
					{
						x = 0;
						y++;
					}
					if(y>=tileSetHeight)
					{
						y = 0;
					}
					continue;
				}
				disp = (BufferedImage)tileSet.get(i);
				
				int mapPositionX = disp.getWidth()*x + xTileSetPosition;
				int mapPositionY = disp.getHeight()*y + yTileSetPosition;
				
				if(mapPositionX < mapWidthTotal && mapPositionY<mapHeightTotal)
				{
					int indexTileCollectX = mapPositionX/(mapWidthInTiles*tileWidth);
					int indexTileCollectY = mapPositionY/(mapHeightInTiles*tileHeight);
					
					Map32x32Tiles tiles = (Map32x32Tiles)Layer[activeLayerIndex].get(indexTileCollectX + indexTileCollectY*mapBlockWidth);
					
					int indexX = mapPositionX - indexTileCollectX*(mapWidthInTiles*tileWidth);
					int indexY = mapPositionY - indexTileCollectY*(mapHeightInTiles*tileHeight);
					
					indexX /= tileWidth;
					indexY /= tileHeight;
					
					int tileIdx = ((Integer)tileIdxSet.get(i)).intValue();
					int palIdx = ((Integer)paletteBankIndexSet.get(i)).intValue();
					int hflp = ((Integer)hFlipSet.get(i)).intValue();
					int vflp = ((Integer)vFlipSet.get(i)).intValue();
					
					tiles.setIndexOfTile(tileIdx, indexX + indexY*mapWidthInTiles);
					tiles.setPaletteBank(palIdx, indexX + indexY*mapWidthInTiles);
					tiles.setHorizontalFlip(hflp, indexX + indexY*mapWidthInTiles);
					tiles.setVerticalFlip(vflp, indexX + indexY*mapWidthInTiles);
					
					int setX = (xTileSetPosition + x*tileWidth)%(mapWidthInTiles*tileWidth);
					int setY = (yTileSetPosition + y*tileHeight)%(mapHeightInTiles*tileHeight);
					
					tiles.setTileAt(disp, setX, setY);
				}
				
				x++;
				if(x>=tileSetWidth)
				{
					x = 0;
					y++;
				}
				if(y>=tileSetHeight)
				{
					y = 0;
				}
			}	

			xTileSetPosition = (e.getX()/(tileWidth*scale))*(tileWidth);
			yTileSetPosition = (e.getY()/(tileHeight*scale))*(tileHeight);
			
			this.repaint();
		}
		else if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1 && activeLayerIndex == 0)
		{
			
		}
	}
	
	public void processMouseMotionEvent(MouseEvent e)
	{	
		xTileSetPosition = (e.getX()/(tileWidth*scale))*(tileWidth);
		yTileSetPosition = (e.getY()/(tileHeight*scale))*(tileHeight);
			
		if(xTileSetPositionPrev != xTileSetPosition || yTileSetPositionPrev != yTileSetPosition)
		{
			this.repaint();
		}
	}
}