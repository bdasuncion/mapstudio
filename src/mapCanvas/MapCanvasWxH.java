package mapCanvas;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import infoObjects.ActorInfo;
import infoObjects.CollisionInfo;
import infoObjects.EventInfo;
import infoObjects.EventTransferMapInfo;
import infoObjects.MapInfo;
import infoObjects.TileInfo;
import infoObjects.TileSetInfo;
import interfaces.MapControls;
import interfaces.MapViewSettings;
import interfaces.ModeSelectionInterface;
import interfaces.TileSetManipulation;
import interfaces.TileSetting;
import tools.ImageTools;

import mapBlock.MapWxH;

public class MapCanvasWxH extends JPanel implements TileSetting, TileSetManipulation, KeyListener,
    MapViewSettings, ModeSelectionInterface, MapControls {
	private static final long serialVersionUID = 1L;

	private boolean collisionLayerVisible = false;
	private boolean gridVisible = true;
	private boolean layer0Visible = false;
	private boolean layer1Visible = false;
	private boolean layer2Visible = true;
	private boolean layer3Visible = true;
	private boolean actorsVisible = false;
	private boolean eventsVisible = false;
	
	private int scale = 1;
	
	private int xTileHighlightPosition = 0;
	private int yTileHighlightPosition = 0;
	private int xTileSetPositionPrev = 0;
	private int yTileSetPositionPrev = 0;
	
	private int mapWidth = 256;
	private int mapHeight = 256;
	private int tileWidth = 8;
	private int tileHeight = 8;
	private int collisionWidth = 8;
	private int collisionHeight = 8;
//	used to determine how large the array of the map will be
//	together with tile width and height, the size of the map
//	is determined
	private int mapWidthInTiles = 32;
	private int mapHeightInTiles = 32;
	
	private int gridWidth = 16;
	private int gridHeight = 16;
	
	private MapInfo mapInfo;
	private TileSetInfo setTiles;
	
	TileSetting tileSettingMouseAdapter;
	EventSetting eventSettingMouseAdapter;
	ActorSetting actorSettingMouseAdapter;
	
	private ActorSettingDialog actorSettingDialog;
	private EventSettingDialog eventSettingDialog;
	
	public MapCanvasWxH(MapInfo mi, int widthInPixels, int heightInPixels, 
	int widthOfTile, int heightOfTile) {
		//this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
		this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		tileWidth = widthOfTile;
		tileHeight = heightOfTile;
		
		mapInfo = mi;
		if(widthInPixels%widthOfTile == 0)
			mapWidth = widthInPixels;
		else
			mapWidth = widthInPixels + (widthOfTile - widthInPixels%widthOfTile);
		
		if(heightInPixels%heightOfTile == 0)
			mapHeight = heightInPixels;
		else
			mapHeight = heightInPixels + (heightOfTile - heightInPixels%heightOfTile);
				
		mapWidthInTiles = mapWidth/tileWidth;
		mapHeightInTiles = mapHeight/tileHeight;
		
		this.addKeyListener(this);
		setFocusable(true);
		
		this.setPreferredSize(new Dimension(mapWidth*scale, mapHeight*scale));
		this.revalidate();
		this.setVisible(true);
		
		tileSettingMouseAdapter = new TileSetting();
		actorSettingMouseAdapter = new ActorSetting();
		eventSettingMouseAdapter = new EventSetting();
		
		this.addMouseListener(tileSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
		
		actorSettingDialog = new ActorSettingDialog(null);
		eventSettingDialog = new EventSettingDialog(null);
	}
	
	public void setScale(int s) {
		scale = s;
		this.setPreferredSize(new Dimension(mapWidth*scale, mapHeight*scale));
		this.revalidate();
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
	
		g2D.fill(new Rectangle(0, 0, mapWidth*scale, mapHeight*scale));
		BufferedImage display = g2D.getDeviceConfiguration().createCompatibleImage(
				mapWidth, mapHeight);
	
		g2D.scale(scale, scale);
		
		if(layer3Visible == true && mapInfo != null && 
			mapInfo.getMapLayers().get(3).getMapDisplay() != null) {
				display = mapInfo.getMapLayers().get(3).getMapDisplay();
				
				g2D.drawImage(display, 0, 0, display.getWidth(),display.getHeight(), this);
		}
		
		if(layer2Visible == true && mapInfo != null && 
				mapInfo.getMapLayers().get(2).getMapDisplay() != null) {
				display = mapInfo.getMapLayers().get(2).getMapDisplay();
				
				g2D.drawImage(display, 0, 0, display.getWidth(),display.getHeight(), this);
		}
		
		if(layer1Visible == true && mapInfo != null && 
			mapInfo.getMapLayers().get(1).getMapDisplay() != null) {
				display = mapInfo.getMapLayers().get(1).getMapDisplay();
				
				g2D.drawImage(display, 0, 0, display.getWidth(),display.getHeight(), this);
		}
		
		if(layer0Visible == true && mapInfo != null && 
			mapInfo.getMapLayers().get(0).getMapDisplay() != null) {
				display = mapInfo.getMapLayers().get(0).getMapDisplay();
				
				g2D.drawImage(display, 0, 0, display.getWidth(),display.getHeight(), this);
		}
		
		if(collisionLayerVisible == true) {
			drawCollision(g2D, display);
		}
		
		if (gridVisible) {
		    drawGrid(g2D, display);
		}
		
		if (actorsVisible) {
			drawActors(g2D, display);
		}
		
		if (eventsVisible) {
		    drawEvents(g2D, display);
		}
		
		drawMapStamp(g2D, display);
		
		xTileSetPositionPrev = xTileHighlightPosition;
		yTileSetPositionPrev = yTileHighlightPosition;
		
		g2D.dispose();
	}
	
	private void drawCollision(Graphics2D g2D, BufferedImage display) {
		int collisionX = 0;
		int collisionY = 0;
		
		for (int i = 0; i < getMapHeightInTiles()/2; ++i) {
			for (int j = 0; j < getMapWidthInTiles()/2; ++j) {
				collisionX = 16*j;
				CollisionInfo collisionInfo = mapInfo.getCollisionTiles().
						get(j + i*(getMapWidthInTiles()/2));
				//g2D.draw(new Rectangle((x + collisionInfo.getX())*scale, (y + collisionInfo.getY())*scale,
				//		collisionInfo.getWidth()*scale, collisionInfo.getHeight()*scale));
				
				g2D.setColor(new Color(255, 255, 255, 190));
				g2D.fill(new Rectangle((collisionX + collisionInfo.getX()), (collisionY + collisionInfo.getY()),
						collisionInfo.getWidth(), collisionInfo.getHeight()));
			}
			collisionX = 0;
			collisionY += 16;
		}
	}
	
	private void drawMapStamp(Graphics2D g2D, BufferedImage display) {
		int x = 0, y = 0;
		if (setTiles != null) {
			for(int i = 0; i < setTiles.getTileSet().size(); ++i) {
				display = setTiles.getTileSet().get(i).getTileImage();
				
				g2D.drawImage(display, display.getWidth()*x + xTileHighlightPosition, 
						display.getHeight()*y + yTileHighlightPosition,
						display.getWidth(),display.getHeight(), this);
				x++;
				if (x >= setTiles.getWidthInTiles()) {
					x = 0;
					y++;
				}
				if (y >= setTiles.getHeightInTiles()) {
					y = 0;
				}
			}
			
			g2D.setStroke(new BasicStroke(0.2f));
			g2D.setColor(Color.RED);
			g2D.drawLine(xTileHighlightPosition, yTileHighlightPosition, 
					xTileHighlightPosition + setTiles.getWidthInTiles()*display.getWidth(), yTileHighlightPosition);
			g2D.drawLine(xTileHighlightPosition, yTileHighlightPosition + setTiles.getHeightInTiles()*display.getHeight(), 
					xTileHighlightPosition + setTiles.getWidthInTiles()*display.getWidth(), 
					yTileHighlightPosition + setTiles.getHeightInTiles()*display.getHeight());
			g2D.drawLine(xTileHighlightPosition, yTileHighlightPosition, xTileHighlightPosition, 
					yTileHighlightPosition + setTiles.getHeightInTiles()*display.getHeight());
			g2D.drawLine(xTileHighlightPosition + setTiles.getWidthInTiles()*display.getWidth(), 
					yTileHighlightPosition, 
					xTileHighlightPosition  + setTiles.getWidthInTiles()*display.getWidth(), 
					yTileHighlightPosition + setTiles.getHeightInTiles()*display.getHeight());
		}

	}
	
	private void drawGrid(Graphics2D g2D, BufferedImage display) {
		g2D.setStroke(new BasicStroke(0.2f));
		g2D.setColor(Color.WHITE);
		
		for (int i = 0; i <= display.getWidth()/gridWidth; ++i) {
			g2D.drawLine(gridWidth*i, 0, gridWidth*i, display.getHeight());
		}
		
		for (int i = 0; i <= display.getHeight()/gridHeight; ++i) {
			g2D.drawLine(0, gridHeight*i, display.getWidth(), gridHeight*i);
		}
	}
	
	private void drawActors(Graphics2D g2D, BufferedImage display) {
		for (ActorInfo actor : mapInfo.getActors()) {
			g2D.setColor(new Color(255, 0, 0, 75));
			g2D.fillOval(actor.getX() - 8, actor.getY() - 8, 16, 16);
			g2D.setColor(Color.BLUE);
			g2D.setFont(new Font(g2D.getFont().getFontName(), Font.ITALIC, 6));
		    g2D.drawString(actor.getType(), actor.getX() - 8, actor.getY());
		}
	}
	
	private void drawEvents(Graphics2D g2D, BufferedImage display) {
		
		for (EventInfo event : mapInfo.getEvents()) {
			g2D.setColor(Color.CYAN);
			g2D.setStroke(new BasicStroke(0.2f));
			g2D.setFont(new Font(g2D.getFont().getFontName(), Font.PLAIN, 6));
			g2D.drawString(event.getType(), event.getX(), event.getY() + 6);
			g2D.setStroke(new BasicStroke(1.0f));
			g2D.setColor(Color.RED);
		    g2D.drawRect(event.getX(), event.getY(), event.getWidth(), event.getHeight());
		    if (event.getType().contentEquals(EventInfo.TYPE_TRANSFER)) {
		    	drawEventMapTransfer(g2D, display, (EventTransferMapInfo) event);
		    }
		}
	}
	
	private void drawEventMapTransfer(Graphics2D g2D, BufferedImage display, EventTransferMapInfo info) {
		g2D.setColor(Color.CYAN);
		g2D.setStroke(new BasicStroke(0.2f));
		g2D.setFont(new Font(g2D.getFont().getFontName(), Font.PLAIN, 6));
		g2D.drawString(info.getTransferToMap() + ":(" + info.getTransferToX()+ ", " + info.getTransferToY() + ")", 
			info.getX(), info.getY() + 12);
	}
	/*TODO
	 * get tile dimension from MapWxH Layer so even if
	 * collision dimension is different from tile dimension, this part
	 * will still get thye correct results
	 * */
	
	/*public void processMouseMotionEvent(MouseEvent e) {	
		//int highlightW = Layer[activeLayerIndex].getWidthOfTile();
		//int highlightH = Layer[activeLayerIndex].getWidthOfTile();
		int highlightW = 16;
		int highlightH = 16;
		
		xTileSetPosition = (e.getX()/(highlightW*scale))*(highlightW);
		yTileSetPosition = (e.getY()/(highlightH*scale))*(highlightH);
		
		if(xTileSetPositionPrev != xTileSetPosition || yTileSetPositionPrev != yTileSetPosition) {
			this.repaint();
		}
	}*/
	
	public void processMouseWheelEvent(MouseWheelEvent e) {
    	if(e.getWheelRotation() > 0 && scale > 1) {
			scale--;
		} else if((e.getWheelRotation() < 0)) {
			scale++;
		}
   
    	this.setPreferredSize(new Dimension(mapWidth*scale, mapHeight*scale));
		this.revalidate();
		repaint();
    }

	public int getMapHeightInTiles() {
		return mapHeightInTiles;
	}

	public int getMapWidthInTiles() {
		return mapWidthInTiles;
	}

	public MapInfo getMapInfo() {
		return mapInfo;
	}

	@Override
	public void setTiles(TileSetInfo ts) {
		setTiles = ts;
		this.requestFocus();
		this.repaint();
	}

	@Override
	public void setTileSetName(String tileSetName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flipTilesVertical() {
		setTiles.flipVertical();
		repaint();
	}

	@Override
	public void flipTilesHorizontal() {
		setTiles.flipHorizontal();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (setTiles != null && (e.getKeyChar() == 'V' || e.getKeyChar() == 'v')) {
			setTiles.flipVertical();
		}
		if (setTiles != null && (e.getKeyChar() == 'H' || e.getKeyChar() == 'h')) {
			setTiles.flipHorizontal();
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setViewGrid(boolean v) {
		gridVisible = v;
		repaint();
	}

	@Override
	public void setViewLayer0(boolean v) {
		layer0Visible = v;
		repaint();
	}

	@Override
	public void setViewLayer1(boolean v) {
		layer1Visible = v;
		repaint();
	}

	@Override
	public void setViewLayer2(boolean v) {
		layer2Visible = v;
		repaint();
	}

	@Override
	public void setViewLayer3(boolean v) {
		layer3Visible = v;
		repaint();
	}

	@Override
	public void setViewLayerCollision(boolean v) {
		collisionLayerVisible = v;
		repaint();
	}
	
	@Override
	public void setViewLayerActors(boolean v) {
		actorsVisible = v;
		repaint();
		
	}
	
	@Override
	public void setViewLayerEvents(boolean v) {
		eventsVisible = v;
		repaint();
	}
	
	protected class TileSetting extends MouseInputAdapter {
		//@Override
		int xTileSetPostion;
		int yTileSetPostion;
		public void mousePressed(MouseEvent e) {
			//if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileSetPostion = normalizeX(e.getX());//*(setW);
				yTileSetPostion = normalizeY(e.getY());//*(setH);
				
				//System.out.println("MAP SET TILE TO MAP:" + xTileSetPostion + " " + yTileSetPostion);
				mapInfo.setTileSetToMap(xTileSetPostion*2, yTileSetPostion*2, setTiles);

				xTileHighlightPosition = normalizeX(e.getX())*(snapToWidth);
				yTileHighlightPosition = normalizeY(e.getY())*(snapToHeight);
				
				repaint();
			//}
		}
		
		@Override
    	public void mouseDragged(MouseEvent e) {
			int dragX = normalizeX(e.getX());
			int dragY = normalizeY(e.getY());
			
			int differenceX = (xTileSetPostion- dragX);
			int differenceY = (yTileSetPostion - dragY);
			int widthCheck = differenceX < 0 ? -differenceX : differenceX;
			int heightCheck = differenceY < 0 ? -differenceY: differenceY;

			if (widthCheck >= (setTiles.getWidthInTiles()/2) || heightCheck >= (setTiles.getHeightInTiles()/2)) {
				xTileSetPostion = dragX;
				yTileSetPostion = dragY;
				mapInfo.setTileSetToMap(xTileSetPostion*2, yTileSetPostion*2, setTiles);
			}
			
			repaint();
        }
		
	   private int normalizeX(int x) {
		   int snapToWidth = 16;
		   return (x/(snapToWidth*scale));
	    }
	   
	   private int normalizeY(int y) {
		   int snapToHeight = 16;
		   return (y/(snapToHeight*scale));
	    }
		//@Override
		public void mouseReleased(MouseEvent e) {
			//if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileHighlightPosition = (e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (e.getY()/(snapToHeight*scale));//*(setH);
				//System.out.println("MOUSE RELEASED:" + xTileHighlightPosition + " " + yTileHighlightPosition);
			//}
        }
		
		public void mouseMoved(MouseEvent e) {
			//if (e.getID() != MouseEvent.MOUSE_DRAGGED) {
				int highlightW = 16;
				int highlightH = 16;
				
				xTileHighlightPosition = (e.getX()/(highlightW*scale))*(highlightW);
				yTileHighlightPosition = (e.getY()/(highlightH*scale))*(highlightH);
				
				if(xTileSetPositionPrev != xTileHighlightPosition || yTileSetPositionPrev != yTileHighlightPosition) {
					repaint();
				}
			//}
		}
	}
	
	private class EventSetting extends MouseInputAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileHighlightPosition = (e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (e.getY()/(snapToHeight*scale));//*(setH);
				
				eventSettingDialog.setEventList((xTileHighlightPosition*snapToWidth), 
						(yTileHighlightPosition*snapToHeight), mapInfo.getEvents());
				eventSettingDialog.setVisible(true);
				repaint();
			}
		}
	}
	
	private class ActorSetting extends MouseInputAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileHighlightPosition = (e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (e.getY()/(snapToHeight*scale));//*(setH);
				
				actorSettingDialog.setActorList((xTileHighlightPosition*snapToWidth) + (snapToWidth/2), 
						(yTileHighlightPosition*snapToHeight) + (snapToHeight/2), mapInfo.getActors());

				actorSettingDialog.setVisible(true);
				repaint();
			}
		}
	}

	@Override
	public void setModeTile() {
		// TODO Auto-generated method stub
		this.removeMouseListener(eventSettingMouseAdapter);
		this.removeMouseListener(actorSettingMouseAdapter);
		this.addMouseListener(tileSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}

	@Override
	public void setModeEvent() {
		// TODO Auto-generated method stub
		this.removeMouseListener(tileSettingMouseAdapter);
		this.removeMouseListener(actorSettingMouseAdapter);
		this.removeMouseMotionListener(tileSettingMouseAdapter);
		this.addMouseListener(eventSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}

	@Override
	public void setModeActor() {
		// TODO Auto-generated method stub
		this.removeMouseListener(tileSettingMouseAdapter);
		this.removeMouseListener(eventSettingMouseAdapter);
		this.removeMouseMotionListener(tileSettingMouseAdapter);
		this.addMouseListener(actorSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}

	@Override
	public void reloadMap() {
		mapInfo.reset();
		repaint();
	}
}
