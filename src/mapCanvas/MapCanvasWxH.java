package mapCanvas;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import infoObjects.ActorInfo;
import infoObjects.CollisionInfo;
import infoObjects.EventInfo;
import infoObjects.EventTransferMapInfo;
import infoObjects.MapInfo;
import infoObjects.SpriteMaskBoundsInfo;
import infoObjects.SpriteMaskInfo;
import infoObjects.TileInfo;
import infoObjects.TileSetInfo;
import interfaces.MapControls;
import interfaces.MapTileSettingModeSelection;
import interfaces.MapViewSettings;
import interfaces.ModeSelectionInterface;
import interfaces.TileSetManipulation;
import interfaces.TileSetting;
import tools.ImageTools;

import mapBlock.MapWxH;
import mapMaker.MapDesktop;

public class MapCanvasWxH extends JPanel implements TileSetting, TileSetManipulation, KeyListener,
    MapViewSettings, ModeSelectionInterface, MapControls, MapTileSettingModeSelection {
	private static final long serialVersionUID = 1L;

	private boolean collisionLayerVisible = false;
	private boolean gridVisible = false;
	private boolean layer0Visible = false;
	private boolean layer1Visible = false;
	private boolean layer2Visible = true;
	private boolean layer3Visible = true;
	private boolean actorsVisible = false;
	private boolean eventsVisible = false;
	private boolean spriteMasksVisible = false;
	
	private double scale = 0.50;
	
	private int xTileHighlightPosition = 0;
	private int yTileHighlightPosition = 0;
	private int xTileSetPositionPrev = 0;
	private int yTileSetPositionPrev = 0;
	
	private int mapWidth = 256;
	private int mapHeight = 256;
	public  static int tileWidth = 8;
	public  static int tileHeight = 8;
	private int collisionWidth = 8;
	private int collisionHeight = 8;
	private int collisionFilterMin = 0;
	private int collisionFilterMax = 0;
//	used to determine how large the array of the map will be
//	together with tile width and height, the size of the map
//	is determined
	private int mapWidthInTiles = 32;
	private int mapHeightInTiles = 32;
	
	private int gridWidth = 16;
	private int gridHeight = 16;
	
	private int mapMoveOffsetX = 0;
	private int mapMoveOffsetY = 0;
	
	private MapInfo mapInfo;
	private TileSetInfo setTiles;
	
	TileSetting tileSettingMouseAdapter;
	EventSetting eventSettingMouseAdapter;
	ActorSetting actorSettingMouseAdapter;
	SpriteMaskSetting spriteMaskSettingMouseAadapter;
	
	private ActorSettingDialog actorSettingDialog;
	private EventSettingDialog eventSettingDialog;
	private SpriteMaskSettingDialog spriteMaskSettingDialog;
	
	private boolean tileMode = true;
	private boolean heightMapMode = false;
	
	private int displayCoordinatesX = 0;
	private int displayCoordinatesY = 0;
	private boolean displayCoordinates = false;
	private boolean moveMap = false;
	private Point dragStart;
	
	public MapCanvasWxH(MapInfo mi, int widthInPixels, int heightInPixels, 
	int widthOfTile, int heightOfTile) {
		super();
		//this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
		this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		tileWidth = widthOfTile;
		tileHeight = heightOfTile;
		
		mapInfo = mi;
		mapWidth = widthInPixels;
		mapHeight = heightInPixels;
				
		mapWidthInTiles = mapWidth/tileWidth;
		mapHeightInTiles = mapHeight/tileHeight;
		
		this.addKeyListener(this);
		
		tileSettingMouseAdapter = new TileSetting();
		actorSettingMouseAdapter = new ActorSetting();
		eventSettingMouseAdapter = new EventSetting();
		spriteMaskSettingMouseAadapter = new SpriteMaskSetting();
		
		this.addMouseListener(tileSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
		
		actorSettingDialog = new ActorSettingDialog(null);
		eventSettingDialog = new EventSettingDialog(null);
		spriteMaskSettingDialog = new SpriteMaskSettingDialog(null, mapInfo.getMasks());
		
		setFocusable(true);
		requestFocusInWindow();
		setLayout(null);
		
		//add(new JLabel("SOMETHINTG"));
		
		setPreferredSize(new Dimension((int)(mapWidth*scale), (int)(mapHeight*scale)));
		System.out.println("Inside W: " + (mapWidth*scale) + " H:" + (mapHeight*scale));
		
		setPreferredSize();
		//this.repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int)(mapWidth*scale), (int)(mapHeight*scale));
	}
	
	public void setPreferredSize() {
		setPreferredSize(new Dimension((int)(mapWidth*scale), (int)(mapHeight*scale)));
		revalidate();
		repaint();
	}
	
	public void setScale(int s) {
		scale = s;
		this.setPreferredSize(new Dimension((int)(mapWidth*scale), (int)(mapHeight*scale)));
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        
		Graphics2D g2D =(Graphics2D)g;
		
		g2D.translate(mapMoveOffsetX, mapMoveOffsetY);
		
		g2D.scale(scale, scale);
		
		
		g2D.fill(new Rectangle(0, 0, (int)(mapWidth), (int)(mapHeight)));
		BufferedImage display = g2D.getDeviceConfiguration().createCompatibleImage(
				mapWidth, mapHeight);
		

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
		
		if (spriteMasksVisible) {
			drawSpriteMasks(g2D, display);
		}
		
		drawMapStamp(g2D, display);
		drawHeightMapStamp(g2D, display);
		drawCoordinates(g2D);
		xTileSetPositionPrev = xTileHighlightPosition;
		yTileSetPositionPrev = yTileHighlightPosition;
		
		g2D.dispose();
	}
	
	private void drawCoordinates(Graphics2D g2d) {
		if (displayCoordinates) {
			int coordBoxWidth = 70;
			int coordBoxHeigth = 20;
			int yOffset = 15;
			g2d.setColor(Color.black);
			int displayAtX = displayCoordinatesX;
			//if (displayAtX + coordBoxWidth >= mapWidth) {
			//	displayAtX -= coordBoxWidth;
			//}
			g2d.fillRect(displayAtX, displayCoordinatesY - yOffset, coordBoxWidth, coordBoxHeigth);
			g2d.setColor(Color.white);
			g2d.drawString("(" + displayCoordinatesX + ", " + displayCoordinatesY + ")", 
					displayAtX, displayCoordinatesY);
		}
		
	}

	private void drawSpriteMasks(Graphics2D g2D, BufferedImage display) {
		for (SpriteMaskInfo spriteMask: mapInfo.getSpriteMasks()) {
			SpriteMaskBoundsInfo bounds = spriteMask.getBounds();
			g2D.setColor(new Color(255, 0, 0, 255));
			g2D.drawRect(bounds.getStartX(), bounds.getStartY(), bounds.getEndX() - bounds.getStartX(), bounds.getEndY() - bounds.getStartY());
			g2D.drawImage(spriteMask.getMask(), bounds.getStartX(), bounds.getStartY() - spriteMask.getZ(), bounds.getEndX() - bounds.getStartX(), bounds.getEndY() - bounds.getStartY(), this);
			g2D.drawString(Integer.toString(spriteMask.getZ()), spriteMask.getX() - 8, bounds.getStartY() - spriteMask.getZ() + 16);
		}
	}

	private void drawCollision(Graphics2D g2D, BufferedImage display) {
		int collisionX = 0;
		int collisionY = 0;
		
		for (int i = 0; i < getMapHeightInTiles()/2; ++i) {
			for (int j = 0; j < getMapWidthInTiles()/2; ++j) {
				collisionX = 16*j;
				CollisionInfo collisionInfo = mapInfo.getCollisionTiles().
						get(j + i*(getMapWidthInTiles()/2));
				
				if (collisionInfo.getHeight() >= collisionFilterMin && collisionInfo.getHeight() <= collisionFilterMax) {
					g2D.setColor(new Color(0, 255, 0, 255));
					//g2D.fill(new Rectangle((collisionX + collisionInfo.getX()), (collisionY - collisionInfo.getHeight() + 16),
					//		collisionInfo.getWidth(), collisionInfo.getHeight()));
					//g2D.drawLine((collisionX + 8), (collisionY + 8), 
					//		(collisionX + 8), collisionY + 8 - collisionInfo.getHeight());
					
					g2D.setColor(new Color(128, 255, 128, 255));
					g2D.fill(new Rectangle((collisionX), (collisionY - collisionInfo.getHeight() + 16),
							collisionInfo.getWidth(), collisionInfo.getHeight()));
					
					g2D.setColor(new Color(128, 128, 128, 190));
					g2D.fill(new Rectangle((collisionX + collisionInfo.getX()), (collisionY - collisionInfo.getHeight()),
							collisionInfo.getWidth(), collisionInfo.getLength()));
					g2D.setColor(new Color(200, 0, 0, 190));
					g2D.drawString("" + collisionInfo.getHeight(), collisionX + collisionInfo.getX(), 
							collisionY - collisionInfo.getHeight() + 14);
				}
			}
			collisionX = 0;
			collisionY += 16;
		}
	}
	
	private void drawHeightMapStamp(Graphics2D g2D, BufferedImage display) {
		int x = 0, y = 0;
		int snapToWidth = 16, snapToHeight = 16;
		if (setTiles != null && heightMapMode) {
			
			if (setTiles.getCollision().size() > 0) {
				CollisionInfo collisionInfo = setTiles.getCollision().get(0);
				g2D.setColor(new Color(0, 255, 0, 255));
				g2D.drawLine((xTileHighlightPosition + 8), (yTileHighlightPosition + 8), 
						(xTileHighlightPosition + 8), yTileHighlightPosition + 8 - collisionInfo.getHeight());
				g2D.setColor(new Color(128, 128, 128, 190));
				g2D.fill(new Rectangle((xTileHighlightPosition + collisionInfo.getX()), 
						(yTileHighlightPosition - collisionInfo.getHeight()),
						collisionInfo.getWidth(), collisionInfo.getLength()));
				
				int indexX = xTileHighlightPosition/snapToWidth;
				int indexY = (yTileHighlightPosition/snapToHeight)*(mapWidthInTiles/2);
				//System.out.println(indexX + " " + indexY);
				CollisionInfo infoHighlight = mapInfo.getCollisionTiles().get(indexX + indexY);
				g2D.setColor(new Color(190, 128, 0, 220));
				g2D.fill(new Rectangle((xTileHighlightPosition + infoHighlight.getX()), 
						(yTileHighlightPosition - infoHighlight.getHeight()),
						snapToWidth, snapToHeight));
				
				int positionOffset = 12;
				for (int offsetX = -1; offsetX < 2; ++offsetX) {	
					int resultX = indexX + offsetX;
					if (offsetX == 0 || resultX > mapWidthInTiles || resultX < 0 || (resultX + indexY) < 0) {
						continue;
					}
					String label = "";
					if (offsetX == -1) {
						label = "L";
					} else {
						label = "R";
					}
					infoHighlight = mapInfo.getCollisionTiles().get(resultX + indexY);
					g2D.setColor(new Color(128, 0, 0, 190));
					g2D.fill(new Rectangle((xTileHighlightPosition + (snapToWidth*offsetX) + infoHighlight.getX()), 
							(yTileHighlightPosition - infoHighlight.getHeight()),
							snapToWidth, snapToHeight));
					g2D.setColor(new Color(0, 0, 0, 255));
					g2D.drawString(label, xTileHighlightPosition + (snapToWidth*offsetX) + infoHighlight.getX(), 
					(yTileHighlightPosition - infoHighlight.getHeight() + positionOffset));

				}
				for (int offsetY = -1; offsetY < 2; ++offsetY) {
					int resultY = indexY + (mapWidthInTiles/2)*offsetY;
					int checkOffsetY = (yTileHighlightPosition/snapToHeight) + offsetY;
					if (offsetY == 0 || checkOffsetY > mapHeightInTiles || checkOffsetY < 0 || (indexX + resultY) < 0) {
						continue;
					}
					String label = "";
					if (offsetY == -1) {
						label = "U";
					} else {
						label = "D";
					}
					infoHighlight = mapInfo.getCollisionTiles().get(indexX + resultY);
					g2D.setColor(new Color(128, 0, 0, 190));
					g2D.fill(new Rectangle((xTileHighlightPosition + infoHighlight.getX()), 
							(yTileHighlightPosition + (snapToHeight*offsetY) - infoHighlight.getHeight()),
							snapToWidth, snapToHeight));
					g2D.setColor(new Color(0, 0, 0, 255));
					g2D.drawString(label, xTileHighlightPosition + infoHighlight.getX(), 
							(yTileHighlightPosition + (snapToHeight*offsetY) - infoHighlight.getHeight() + positionOffset));

						//g2D.setColor(new Color(200, 200, 200, 190));
						//g2D.drawString("" + infoHighlight.getHeight(), xTileHighlightPosition + collisionInfo.getX(), 
						//		(yTileHighlightPosition - collisionInfo.getHeight() + 16));
				}
				
				g2D.setColor(new Color(200, 0, 0, 190));
				g2D.drawString("" + collisionInfo.getHeight(), xTileHighlightPosition + collisionInfo.getX(), 
						(yTileHighlightPosition - collisionInfo.getHeight() + 16));
			}
		}

	}
	
	private void drawMapStamp(Graphics2D g2D, BufferedImage display) {
		int x = 0, y = 0;
		if (moveMap) {
			return;
		}
		if (setTiles != null && tileMode) {
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
			g2D.setColor(new Color(0, 255, 0, 255));
			g2D.drawLine(actor.getX(), actor.getY(), actor.getX(), actor.getY() - actor.getZ());
			g2D.setColor(new Color(255, 255, 255, 128));
			g2D.fillOval(actor.getX() - 8, actor.getY() - 8 - actor.getZ(), 16, 16);
			g2D.setColor(Color.BLUE);
			g2D.setFont(new Font(g2D.getFont().getFontName(), Font.ITALIC, 6));
		    g2D.drawString(actor.getType(), actor.getX() - 8, actor.getY());
		    g2D.setColor(Color.RED);
		    g2D.setFont(new Font(g2D.getFont().getFontName(), Font.ITALIC, 7));
			g2D.drawString(Integer.toString(actor.getZ()), actor.getX() - 2, actor.getY() - actor.getZ());
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
		    g2D.drawRect(event.getX(), event.getY(), event.getWidth(), event.getLength());
	    	g2D.drawLine(event.getX() + event.getWidth()/2, event.getY() + event.getLength()/2, 
	    			event.getX() + event.getWidth()/2, event.getY() + event.getLength()/2 - event.getzOffset());
	    	g2D.drawRect(event.getX(), event.getY() - event.getzOffset(), event.getWidth(), event.getLength());
		    if (event.getType().contentEquals(EventInfo.TYPE_TRANSFER)) {
		    	drawEventMapTransfer(g2D, display, (EventTransferMapInfo) event);
		    }
		}
	}
	
	private void drawEventMapTransfer(Graphics2D g2D, BufferedImage display, EventTransferMapInfo info) {
		int displayX = info.getX();
		int offsetY = 6;
		String transferToMapName = info.getTransferToMap() + ":";
		int boxWidth = (int) ((transferToMapName.length()*3.5) + 5);
		if ((displayX + boxWidth) > mapWidth) {
			displayX -= (displayX + boxWidth) - mapWidth;
		}
		//g2D.setColor(Color.BLACK);
		//g2D.fillRect(displayX, info.getY(), boxWidth, offsetY*3);
		
		g2D.setColor(Color.CYAN);
		g2D.setStroke(new BasicStroke(0.2f));
		g2D.setFont(new Font(g2D.getFont().getFontName(), Font.PLAIN, 6));
		g2D.drawString(transferToMapName, displayX, info.getY() + offsetY*2);
		g2D.drawString("(" + info.getTransferToX()+ ", " + info.getTransferToY() + ", " + info.getTransferToZ() +  ")", 
				displayX, info.getY() + offsetY*3);
	}
	
	public void processMouseWheelEvent(MouseWheelEvent e) {
		System.out.println("SCROLL MOUSE WHEEL");
    	if(e.getWheelRotation() > 0 && scale > 0.5) {
			//scale -= 0.25;
    		scale -= 0.10;
		} else if((e.getWheelRotation() < 0)) {
			//scale += 0.25;
			scale += 0.10;
		}
   
    	this.setPreferredSize(new Dimension((int)(mapWidth*scale), (int)(mapHeight*scale)));
		this.revalidate();
		repaint();
    }
	
	/*@Override
	public void keyReleased(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_A) {

		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			
		}
		System.out.println("PRESSED");
		this.repaint();
	}*/

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
		/*if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			moveMap = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			System.out.println("CTRL:" + moveMap);
		}*/
		repaint();
	}

	//@Override
	//public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	//}

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
	
	@Override
	public void setViewLayerSpriteMask(boolean v) {
		spriteMasksVisible = v;
		repaint();
	}
	
	private void setDisplayCoordinates(int mouseX, int mouseY) {
		displayCoordinatesX = (int)((mouseX/scale) - (mapMoveOffsetX/scale));
		displayCoordinatesY = (int)((mouseY/scale) - (mapMoveOffsetY/scale));
		displayCoordinates = true;
	}
	
	protected class TileSetting extends MouseInputAdapter {
		//@Override
		int xTileSetPostion;
		int yTileSetPostion;
		int snapToWidth = 16;
		int snapToHeight = 16;
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				if (!moveMap) {
					xTileSetPostion = normalizeX(e.getX());//*(setW);
					yTileSetPostion = normalizeY(e.getY());//*(setH);
					
					mapInfo.setTileSetToMap(xTileSetPostion*2, yTileSetPostion*2, setTiles, tileMode, heightMapMode);
	
					xTileHighlightPosition = normalizeX(e.getX())*(snapToWidth);
					yTileHighlightPosition = normalizeY(e.getY())*(snapToHeight);
					//xTileHighlightPosition = normalizeX(e.getX());
					//yTileHighlightPosition = normalizeY(e.getY());
					displayCoordinates = false;
				} else {
					dragStart = e.getPoint();
				}
			} else if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3){
				setDisplayCoordinates(e.getX(), e.getY());
			}
			repaint();
		}
		
		@Override
    	public void mouseDragged(MouseEvent e) {
			int dragX = normalizeX(e.getX());
			int dragY = normalizeY(e.getY());
			
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (moveMap == false) {
					displayCoordinates = false;
					xTileSetPostion = dragX;
					yTileSetPostion = dragY;
					xTileHighlightPosition = dragX*snapToWidth;
					yTileHighlightPosition = dragY*snapToHeight;
					mapInfo.setTileSetToMap(xTileSetPostion*2, yTileSetPostion*2, setTiles, tileMode, heightMapMode);
				} else {
					Point dragEnd = e.getPoint();
	                mapMoveOffsetX += dragEnd.x - dragStart.x;
	                mapMoveOffsetY += dragEnd.y - dragStart.y;
	                dragStart = dragEnd;
	                repaint();
				}
			}
			
			repaint();
        }
		
	   private int normalizeX(int x) {
		   //return (int)((x - mapMoveOffsetX*scale)/(snapToWidth*scale));
		   return (int)((x - mapMoveOffsetX)/(snapToWidth*scale));
	    }
	   
	   private int normalizeY(int y) {
		   //return (int)((y - mapMoveOffsetY*scale)/(snapToHeight*scale));
		   return (int)((y - mapMoveOffsetY)/(snapToHeight*scale));
	    }
		//@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {				
				xTileHighlightPosition = (int)(e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (int)(e.getY()/(snapToHeight*scale));//*(setH);
				displayCoordinates = false;
			}
        }
		
		public void mouseMoved(MouseEvent e) {
			if (e.getID() != MouseEvent.MOUSE_DRAGGED) {
				int highlightW = 16;
				int highlightH = 16;
				
				//xTileHighlightPosition = (int)((e.getX() - (mapMoveOffsetX*scale))/(highlightW*scale))*(highlightW);
				//yTileHighlightPosition = (int)((e.getY() - (mapMoveOffsetY*scale))/(highlightH*scale))*(highlightH);
				xTileHighlightPosition = (int)((e.getX() - (mapMoveOffsetX))/(highlightW*scale))*(highlightW);
				yTileHighlightPosition = (int)((e.getY() - (mapMoveOffsetY))/(highlightH*scale))*(highlightH);
				
				if(xTileSetPositionPrev != xTileHighlightPosition || yTileSetPositionPrev != yTileHighlightPosition) {
					repaint();
				}
			}
		}
	}
	
	private class EventSetting extends MouseInputAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileHighlightPosition = (int)(e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (int)(e.getY()/(snapToHeight*scale));//*(setH);
				
				eventSettingDialog.setEventList((xTileHighlightPosition*snapToWidth), 
						(yTileHighlightPosition*snapToHeight), mapInfo.getEvents());
				eventSettingDialog.setVisible(true);
				repaint();
			} else if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3){
				setDisplayCoordinates(e.getX(), e.getY());
			}
		}
	}
	
	private class ActorSetting extends MouseInputAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileHighlightPosition = (int)(e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (int)(e.getY()/(snapToHeight*scale));//*(setH);
				
				actorSettingDialog.setActorList((xTileHighlightPosition*snapToWidth) + (snapToWidth/2), 
						(yTileHighlightPosition*snapToHeight) + (snapToHeight/2), mapInfo.getActors());

				actorSettingDialog.setVisible(true);
				repaint();
			} else if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3){
				setDisplayCoordinates(e.getX(), e.getY());
			}
		}
	}
	
	private class SpriteMaskSetting extends MouseInputAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				int snapToWidth = 16;
				int snapToHeight = 16;
				
				xTileHighlightPosition = (int)(e.getX()/(snapToWidth*scale));//*(setW);
				yTileHighlightPosition = (int)(e.getY()/(snapToHeight*scale));//*(setH);
				
				spriteMaskSettingDialog.setSpriteMaskList((xTileHighlightPosition*snapToWidth) + (snapToWidth/2), 
						(yTileHighlightPosition*snapToHeight) + (snapToHeight/2), snapToWidth, snapToHeight, 
						mapInfo.getSpriteMasks());
				spriteMaskSettingDialog.setVisible(true);
				repaint();
			} else if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3){
				setDisplayCoordinates(e.getX(), e.getY());
			}
		}
	}

	@Override
	public void setModeTile() {
		this.removeMouseListener(eventSettingMouseAdapter);
		this.removeMouseListener(actorSettingMouseAdapter);
		this.removeMouseListener(spriteMaskSettingMouseAadapter);
		this.addMouseListener(tileSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}

	@Override
	public void setModeEvent() {
		this.removeMouseListener(tileSettingMouseAdapter);
		this.removeMouseListener(actorSettingMouseAdapter);
		this.removeMouseListener(spriteMaskSettingMouseAadapter);
		this.removeMouseMotionListener(tileSettingMouseAdapter);
		this.addMouseListener(eventSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}

	@Override
	public void setModeActor() {
		this.removeMouseListener(tileSettingMouseAdapter);
		this.removeMouseListener(eventSettingMouseAdapter);
		this.removeMouseListener(spriteMaskSettingMouseAadapter);
		this.removeMouseMotionListener(tileSettingMouseAdapter);
		this.addMouseListener(actorSettingMouseAdapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}
	

	@Override
	public void setModeSpriteMask() {
		this.removeMouseListener(tileSettingMouseAdapter);
		this.removeMouseListener(eventSettingMouseAdapter);
		this.removeMouseMotionListener(tileSettingMouseAdapter);
		this.addMouseListener(spriteMaskSettingMouseAadapter);
		this.addMouseMotionListener(tileSettingMouseAdapter);
	}

	@Override
	public void reloadMap() {
		mapInfo.reset();
		this.setPreferredSize(new Dimension((int)(mapWidth*scale), (int)(mapHeight*scale)));
		this.setSize(this.getPreferredSize());
		this.revalidate();
		repaint();
		
		Container parent = this.getParent();
		if (parent != null) {
		    parent.revalidate();   // notify parent layout
		    parent.repaint();      // refresh visuals
		}
		
		SwingUtilities.invokeLater(() -> {
		    System.out.println("MapView actual size: " + getWidth() + " x " + getHeight());
		});
	}

	@Override
	public void setCollisionFilter(int min, int max) {
		collisionFilterMin = min;
		collisionFilterMax = max;
		repaint();
	}

	@Override
	public void setAsTileMode(boolean b) {
		tileMode = b;
	}

	@Override
	public void setAsMapHeightMode(boolean b) {
		heightMapMode = b;
	}

	@Override
	public void shiftLeft() {
		// TODO Auto-generated method stub
		System.out.println("LEFT");
		for (int i = 0; i < mapInfo.getMapLayers().size(); ++i) {
			mapInfo.getMapLayers().get(i).shiftLeft();
			mapInfo.getMapLayers().get(i).shiftLeft();
		}
		mapInfo.getCollisionLayer().shiftLeft();
		mapInfo.shiftLeft();
		mapInfo.shiftLeft();
		repaint();
	}

	@Override
	public void shiftRight() {
		// TODO Auto-generated method stub
		System.out.println("RIGHT");
		for (int i = 0; i < mapInfo.getMapLayers().size(); ++i) {
			mapInfo.getMapLayers().get(i).shiftRight();
			mapInfo.getMapLayers().get(i).shiftRight();
		}
		mapInfo.getCollisionLayer().shiftRight();
		mapInfo.shiftRight();
		mapInfo.shiftRight();
		repaint();
	}

	@Override
	public void shiftUp() {
		// TODO Auto-generated method stub
		System.out.println("UP");
		for (int i = 0; i < mapInfo.getMapLayers().size(); ++i) {
			mapInfo.getMapLayers().get(i).shiftUp();
			mapInfo.getMapLayers().get(i).shiftUp();
		}
		mapInfo.getCollisionLayer().shiftUp();
		mapInfo.shiftUp();
		mapInfo.shiftUp();
		repaint();
	}

	@Override
	public void shiftDown() {
		// TODO Auto-generated method stub
		System.out.println("DOWN");
		for (int i = 0; i < mapInfo.getMapLayers().size(); ++i) {
			mapInfo.getMapLayers().get(i).shiftDown();
			mapInfo.getMapLayers().get(i).shiftDown();
		}
		mapInfo.getCollisionLayer().shiftDown();
		mapInfo.shiftDown();
		mapInfo.shiftDown();
		repaint();
	}

	@Override
	public void resize(int newWidth, int newHeight) {

		int widthSet = newWidth;
		if (newWidth%MapDesktop.minTileWidth > 0) {
			widthSet = ((newWidth/MapDesktop.minTileWidth)+ 1 )* MapDesktop.minTileWidth;
		}
		
		int heightSet = newHeight;
		if (newHeight%MapDesktop.minTileheight > 0) {
			heightSet = ((newHeight/MapDesktop.minTileheight)+ 1 )* MapDesktop.minTileheight;
		}
		
		for (int i = 0; i < mapInfo.getMapLayers().size(); ++i) {
			mapInfo.getMapLayers().get(i).resize(widthSet, heightSet);
		}
				
		mapWidth = widthSet;
		mapHeight = heightSet;
		mapWidthInTiles = mapWidth/tileWidth;
		mapHeightInTiles = mapHeight/tileHeight;
		mapInfo.getCollisionLayer().resize(mapWidthInTiles/2, mapHeightInTiles/2);
		mapInfo.setWidthInTiles(mapWidthInTiles);
		mapInfo.setHeightInTiles(mapHeightInTiles);
		
		Container parent = getParent();
		
		while (parent != null) {
			if (parent instanceof JInternalFrame) {
				JInternalFrame internalFrameParent = (JInternalFrame) parent;
				internalFrameParent.setTitle("Map " + mapWidth + "x" + mapHeight + " Tile " + tileWidth + "x" + tileHeight);
				break;
			}
			parent = parent.getParent();
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			moveMap = !moveMap;
			System.out.println("CTRL:" + moveMap);
			if (!moveMap) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	
			} else {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));	
			}
			
		}
		
	}
}
