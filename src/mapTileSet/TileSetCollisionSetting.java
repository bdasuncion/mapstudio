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
		collisionSet = new CollisionSettingDialog(null);
		drawTiles = false;
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
		
		g2D.setColor(new Color(255, 255, 255, 190));
		g2D.fill(new Rectangle(0, 0,
				tileWidth*scale, tileHeight*scale));
		
		g2D.setColor(new Color(0, 0, 255, 120));
		g2D.drawString("HEIGHT:" + tileSetInfo.getCollision().get(0).getHeight(), tileWidth*tileSetInfo.getWidthInTiles()*scale + 16,
				40);
		g2D.drawString("WIDTH:" + tileSetInfo.getCollision().get(0).getWidth(), tileWidth*tileSetInfo.getWidthInTiles()*scale + 16,
				55);
		g2D.drawString("LENGTH:" + tileSetInfo.getCollision().get(0).getLength(), tileWidth*tileSetInfo.getWidthInTiles()*scale + 16,
				70);
	}
	
	private class TileSetLayerCollisionSettingListener extends MouseInputAdapter  {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = getxHighlight()/(scale*getSnapToWidth());
			int y = getyHighlight()/(scale*getSnapToHeight());
			TileSetInfo tileSetInfo = getTileSetInfo();
			//collisionSet.setCollisionInfo(tileSetInfo.getCollision().get(x + (y*(tileSetInfo.getWidthInTiles()/2))));
			collisionSet.setCollisionInfo(tileSetInfo.getCollision().get(0));
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
