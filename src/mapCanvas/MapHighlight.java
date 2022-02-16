package mapCanvas;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

public class MapHighlight extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5058007340930380073L;
	private Vector tileSet;
	private int scale = 2;
	private int tileSetWidth = 0;
	private int tileSetHeight = 0;
	
	private int xTileSetPosition = 0;
	private int yTileSetPosition = 0;
	private int xTileSetPositionPrev = 0;
	private int yTileSetPositionPrev = 0;
	
	private static final int pixelWidth = 8;
	private static final int pixelHeight = 8;

	
	public MapHighlight()
	{
		this.enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		tileSet = new Vector(0);
		this.setVisible(true);
	}
	
	public void setTileSet(Vector tiles, int tileWidth, int tileHeight)
	{
		tileSet = new Vector(tiles);
		tileSetWidth = tileWidth;
		tileSetHeight = tileHeight;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		
		int x = 0,y = 0;
	
		BufferedImage disp;
		for(int i = 0; i<tileSet.size(); i++)
		{
			disp = g2D.getDeviceConfiguration().createCompatibleImage(
					pixelWidth, pixelHeight);
			disp = (BufferedImage)tileSet.get(i);
			g2D.drawImage(disp, disp.getWidth()*x*scale + xTileSetPosition, 
					disp.getHeight()*y*scale + yTileSetPosition,
					disp.getWidth()*scale,disp.getHeight()*scale, this);
			
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
	
	public void processMouseMotionEvent(MouseEvent e)
	{	
		xTileSetPosition = (e.getX()/(pixelWidth*scale))*(pixelWidth*scale);
		yTileSetPosition = (e.getY()/(pixelHeight*scale))*(pixelHeight*scale);
			
		if(xTileSetPositionPrev != xTileSetPosition || yTileSetPositionPrev != yTileSetPosition)
			this.repaint();	
	}
}