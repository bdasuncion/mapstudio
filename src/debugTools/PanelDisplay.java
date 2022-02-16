package debugTools;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

public class PanelDisplay  extends JPanel{
	
	Vector<BufferedImage> imgCollection;
	int numDisplay = 0;
	int widthInTiles = 0;
	int heightInTiles = 0;
	
	public PanelDisplay()
	{
		super();
		this.setVisible(true);
	}
	
	public void setDisplay(BufferedImage imgs[],int getNum,
			int wit, int hit)
	{
		int i;
		imgCollection = new Vector<BufferedImage>();
		numDisplay = getNum;
		for(i = 0; i<numDisplay; i++)
		{
			imgCollection.add(imgs[i]);
		}
		
		widthInTiles = wit;
		heightInTiles = hit;
		this.setVisible(true);
		this.repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		
		int w = 16;//imgCollection.get(0).getWidth();
		int h = 16;//imgCollection.get(0).getHeight();
		int wi = 0, hi = 0;
		int x = 0, y = 0;
		
		BufferedImage disp;
		disp = g2D.getDeviceConfiguration().
		createCompatibleImage(w,h);
		
		for(int i = 0; i<numDisplay; i++)
		{
			disp = imgCollection.get(i);
			x = wi*w*2 + 4;
			
			g2D.drawImage(disp, x, y, w*2, h*2, this);
			wi++;
			if(wi == widthInTiles)
			{
				wi = 0;
				hi++;
				y = hi*h*2 + 4;
			}
		}
		g2D.dispose();
	}
}
