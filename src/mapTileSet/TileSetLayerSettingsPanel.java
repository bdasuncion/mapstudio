package mapTileSet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import infoObjects.TileInfo;
import infoObjects.TileSetInfo;
import interfaces.TileSetting;

public class TileSetLayerSettingsPanel extends TileSetPanel {

	static final int LAYER_MAX = 3;
	static final int DO_NOT_SET = -1;
	public TileSetLayerSettingsPanel(int tileW, int tileH, TileSetting ts) {
		super(tileW, tileH, ts);
		highlighterHeight = 0;
		highlighterWidth = 0;
	}
	
	@Override
	protected void setMouseActions() {
		this.addMouseWheelListener(new TileScaleListener());
		this.addMouseListener(new TileSetLayerControlListener());
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
		
		
		int width = tileSetInfo.getWidthInTiles(), height = tileSetInfo.getHeightInTiles();
		int tileWidth = tileSetInfo.getTileSet().get(0).getWidth(), 
			tileHeight = tileSetInfo.getTileSet().get(0).getHeight();
		
		for (int i = 0; i <= width; ++i) {
			g2D.drawLine(tileWidth*scale*i, 0, tileWidth*scale*i, height*scale*tileHeight);
		}
		
		for (int i = 0; i <= height; ++i) {
			g2D.drawLine(0, tileHeight*scale*i, width*scale*tileWidth, tileHeight*scale*i);
		}
		
		//g2D.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, tileHeight*scale));
		g2D.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 6*scale));
		g2D.setColor(Color.BLUE);
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				g2D.drawString(Integer.toString(tileSetInfo.getTileSet().get(j + (i*width)).getSetToLayer()), 
					(2 + tileWidth*j)*scale, (tileHeight*i + tileHeight - 1)*scale);
				//g2D.drawString(Integer.toString(tileSetInfo.getTileSet().get(j + (i*width)).getIndex()), 
				//		(2 + tileWidth*j)*scale, (tileHeight*i + tileHeight - 1)*scale);
			}
		}
		
	}
	
	public void setAllTilesLayer() {
		for (TileInfo tileInfo : getTileSetInfo().getTileSet()) {
			int setToLayer = tileInfo.getSetToLayer() + 1;
			tileInfo.setSetToLayer(setToLayer > LAYER_MAX ? DO_NOT_SET : setToLayer );
		}
		repaint();
	}
	
	private class TileSetLayerControlListener extends MouseInputAdapter {	
		@Override
		public void mousePressed(MouseEvent e) {

			int x = e.getX();
    		int y = e.getY();
    		
    		int tileWidth = getTileSetInfo().getTileSet().get(0).getWidth();
    		int tileHeight = getTileSetInfo().getTileSet().get(0).getHeight();
    		
    		int width = getTileSetInfo().getWidthInTiles(), height = getTileSetInfo().getHeightInTiles();
   
    		
    		if (x > width*scale*tileWidth || y > height*scale*tileHeight) {
    			return;
    		}
    		
    		int idx = (x/(tileWidth*scale)) + ((y/(tileHeight*scale))*width);
    		TileInfo tileInfo = getTileSetInfo().getTileSet().get(idx);
    		
    		int setToLayer = tileInfo.getSetToLayer() + 1;
    		
    		tileInfo.setSetToLayer(setToLayer > LAYER_MAX ? DO_NOT_SET : setToLayer );
    		repaint();
    	}
	}

}
