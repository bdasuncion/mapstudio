package mapTileSet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.event.MouseInputAdapter;

import infoObjects.CollisionInfo;
import infoObjects.TileSetInfo;
import interfaces.TileSetting;
import mapTileSet.TileSetPanel.TileScaleListener;

public class TileSetCollisionSetting extends TileSetPanel {

	CollisionSettingDialog collisionSet;
	public TileSetCollisionSetting(int tileW, int tileH, TileSetting ts) {
		super(tileW, tileH, ts);
		// TODO Auto-generated constructor stub
		collisionSet = new CollisionSettingDialog(null);
	}
	
	protected void setMouseActions() {
		this.addMouseListener(new TileSetLayerCollisionSettingListener());
		this.addMouseMotionListener(new TileSetLayerCollisionSettingListener());
		this.addMouseWheelListener(new TileScaleListener());		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		TileSetInfo tileSetInfo = getTileSetInfo();
		if (tileSetInfo == null) {
			return;
		}
		Graphics2D g2D =(Graphics2D)g;
		
		g2D.setStroke(new BasicStroke(0.2F));
		g2D.setColor(Color.WHITE);
		
		
		int width = tileSetInfo.getWidthInTiles()/2, height = tileSetInfo.getHeightInTiles()/2;
		int tileWidth = 16, tileHeight = 16;
		
		for (int i = 0; i <= width; ++i) {
			g2D.drawLine(tileWidth*scale*i, 0, tileWidth*scale*i, height*scale*tileHeight);
		}
		
		for (int i = 0; i <= height; ++i) {
			g2D.drawLine(0, tileHeight*scale*i, width*scale*tileWidth, tileHeight*scale*i);
		}
		
		g2D.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, tileHeight*scale));
		
		int x = 0, y = 0;
		for (int i = 0; i < getTileSetInfo().getHeightInTiles()/2; ++i) {
			for (int j = 0; j < getTileSetInfo().getWidthInTiles()/2; ++j) {
				x = getSnapToWidth()*j;
				CollisionInfo collisionInfo = getTileSetInfo().getCollision().
						get(j + i*(getTileSetInfo().getWidthInTiles()/2));
				//g2D.draw(new Rectangle((x + collisionInfo.getX())*scale, (y + collisionInfo.getY())*scale,
				//		collisionInfo.getWidth()*scale, collisionInfo.getHeight()*scale));
				
				g2D.setColor(new Color(255, 255, 255, 190));
				g2D.fill(new Rectangle((x + collisionInfo.getX())*scale, (y + collisionInfo.getY())*scale,
						collisionInfo.getWidth()*scale, collisionInfo.getLength()*scale));
			}
			x = 0;
			y += getSnapToHeight();
		}
	}
	
	private class TileSetLayerCollisionSettingListener extends MouseInputAdapter  {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = getxHighlight()/(scale*getSnapToWidth());
			int y = getyHighlight()/(scale*getSnapToHeight());
			TileSetInfo tileSetInfo = getTileSetInfo();
			collisionSet.setCollisionInfo(tileSetInfo.getCollision().get(x + (y*(tileSetInfo.getWidthInTiles()/2))));
			collisionSet.setVisible(true);
			repaint();
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			setxHighlight((e.getX()/(getSnapToWidth()*scale))*
					(getSnapToWidth()*scale));
			setyHighlight((e.getY()/(getSnapToHeight()*scale))*
					(getSnapToHeight()*scale));
			
			if (getTileSetInfo() != null) {
				BufferedImage tile = getTileSetInfo().getTileSet().get(0).getTileImage();
				if (getxHighlight() >= scale*getTileSetInfo().getWidthInTiles()*tile.getWidth()) {
					setxHighlight(scale*getTileSetInfo().getWidthInTiles()*tile.getWidth() 
							- scale*getSnapToWidth());
				}
				
				if (getyHighlight() >= scale*getTileSetInfo().getHeightInTiles()*tile.getHeight()) {
					setyHighlight(scale*getTileSetInfo().getHeightInTiles()*tile.getHeight() 
							- scale*getSnapToHeight());
				}
			}
			repaint();
		}
	}
}
