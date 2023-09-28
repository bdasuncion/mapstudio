package mapTileSet;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import infoObjects.TileSetInfo;
import interfaces.TileSetting;
import tools.ImageTools;
//this class handles the data of the tiles from the Map Canvas
//it stores the data so it can be formatted for output
public class TileSetPanel extends JPanel implements TileSetting {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3590480880234397160L;

	private int widthInTiles;
	private int heightInTiles;
	private int tileWidth = 16;
	private int tileHeight = 16;
	protected int highlighterWidth = 16;
	protected int highlighterHeight = 16;
	private int snapToWidth = 16;
	private int snapToHeight = 16;
	protected int scale = 1;
		
	private int xHighlight;
	private int yHighlight;
	
	private BufferedImage highlighter;
	private String tileSetName;
	
	private TileSetInfo tileSetInfo;
	
	private TileSetSelectionListener tileSetSelectionListener;
	
	TileSetting tileSetting;
	
	protected boolean drawTiles = true;
	
	public TileSetPanel(int tileW, int tileH, TileSetting ts) {
		tileSetName = "CUSTOM TILES";
		//this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK | 
		//		AWTEvent.MOUSE_MOTION_EVENT_MASK | 
		//		AWTEvent.MOUSE_EVENT_MASK);
		
		tileSetting = ts;
				
		tileWidth = tileW;
		tileHeight = tileH;
		
		widthInTiles = 1;
		heightInTiles = 1;
		//index = 0;
		
		tileSetSelectionListener = new TileSetSelectionListener();
		//this.createHighlighter();
		setMouseActions();
	}
	
	protected void setMouseActions() {
		this.addMouseListener(tileSetSelectionListener);
		this.addMouseMotionListener(tileSetSelectionListener);
		this.addMouseWheelListener(new TileScaleListener());		
	}
	
	/*private void createHighlighter() {
		byte R[],G[],B[],A[];
		R = new byte[16];
		G = new byte[16];
		B = new byte[16];
		A = new byte[16];
		
		A[0] = (byte)120;
		R[0] = G[0] = (byte)(0);
		B[0] = (byte)255;
		
		IndexColorModel highlightPalette = new IndexColorModel(4,16, R,G,B,A);
		highlighter = new BufferedImage(tileWidth,tileHeight, BufferedImage.TYPE_BYTE_BINARY, highlightPalette);
	}*/
	
	public int getWidthInTiles() {
		return widthInTiles;
	}
	
	public int getHeightInTiles() {
		return heightInTiles;
	}
	
	public void flipVertical() {	
		this.repaint();
	}
	
	public void flipHorizontal() {
		this.repaint();
	}
	
	@Override
	public void setTiles(TileSetInfo ts) {
		tileSetInfo = ts;
		
		xHighlight = 0;
		yHighlight = 0;
		highlighterWidth = 0;//tileSetInfo.getWidthInTiles()*tileWidth;
		highlighterHeight = 0;//tileSetInfo.getHeightInTiles()*tileHeight;
		
		TileSetInfo tileset = tileSetInfo.getTileSet(0, 0,
				tileSetInfo.getWidthInTiles()/2,tileSetInfo.getHeightInTiles()/2);    	

    	tileSetting.setTiles(tileset);
    	
		setPrefferedSize();
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (tileSetInfo == null || !drawTiles) {
			return;
		}
		Graphics2D g2D =(Graphics2D)g;
		
		int i, x = 0, y = 0;
		BufferedImage disp;
		
		for(i = 0; i<tileSetInfo.getTileSet().size(); i++) {
			disp = g2D.getDeviceConfiguration().createCompatibleImage(tileWidth, tileHeight);
			disp = (BufferedImage)tileSetInfo.getTileSet().get(i).getTileImage();
			g2D.drawImage(disp, disp.getWidth()*x*scale, disp.getHeight()*y*scale, 
					disp.getWidth()*scale,disp.getHeight()*scale, this);
			
			x++;
			if(x >= tileSetInfo.getWidthInTiles()) {
				x = 0;
				y++;
			}
			if(y >= tileSetInfo.getHeightInTiles()) {
				y = 0;
			}
		}
		
		//disp = highlighter;
		//g2D.drawImage(disp, xHighlight, yHighlight, 
		//		highlighterWidth*scale,highlighterHeight*scale, this);
		g2D.setColor(new Color(0, 0, 255, 120));
		g2D.fill(new Rectangle(xHighlight, yHighlight, highlighterWidth*scale,highlighterHeight*scale));
		//xHighlightPrev = xHighlight;
		//yHighlightPrev = yHighlight;
		
		//g2D.setStroke(new BasicStroke(3));
		//g2D.setColor(Color.WHITE);
		//g2D.draw(new Rectangle(xHighlight, yHighlight, highlighterWidth*scale, highlighterHeight*scale));
		
		g2D.drawString(tileSetInfo.getFileName(), tileWidth*tileSetInfo.getWidthInTiles()*scale + 16,
				16);
		
		g2D.drawString("Press v to flip vertical", tileWidth*tileSetInfo.getWidthInTiles()*scale + 16,
				40);
		g2D.drawString("Press h to flip horizontal", tileWidth*tileSetInfo.getWidthInTiles()*scale + 16,
				60);
	}

	public String getTileSetName() {
		return tileSetName;
	}

	@Override
	public void setTileSetName(String tileSetName) {
		this.tileSetName = tileSetName;
	}

	protected class TileScaleListener extends MouseInputAdapter {
		public void mouseWheelMoved(MouseWheelEvent e) {
        	scaleDown();
        	if(e.getWheelRotation() > 0 && scale > 1) {
    			scale--;
    		} else if((e.getWheelRotation() < 0)) {
    			scale++;
    		}
        	
        	setPrefferedSize();
        	
        	scaleUp();
    		repaint();
        }
		
		 private void scaleDown() {
    		xHighlight = xHighlight/(snapToWidth*scale);
    		yHighlight = yHighlight/(snapToHeight*scale);
	    }
	        
	    private void scaleUp() {
        	xHighlight = xHighlight*(snapToWidth*scale);
			yHighlight = yHighlight*(snapToHeight*scale);
        }
	        
	}
	
    protected class TileSetSelectionListener extends MouseInputAdapter {
    	private int startX = 0;
    	private int startY = 0;
    	public void mousePressed(MouseEvent e) {
    		startX = e.getX();
    		startY = e.getY();
    		xHighlight = normalizeX(startX);
    		yHighlight = normalizeY(startY);
    		
    		highlighterWidth = snapToWidth;
    		highlighterHeight = snapToHeight;
    		repaint();
    	}
    	
    	@Override
    	public void mouseDragged(MouseEvent e) {
            setHighliterX(startX, e.getX());
    		
    		setHighliterY(startY, e.getY());
        	
    		limitHighlighterToMax();
        	repaint();
        }

        public void mouseReleased(MouseEvent e) {
    		setHighliterX(startX, e.getX());
    		
    		setHighliterY(startY, e.getY());
    		
    		limitHighlighterToMax();
        	repaint();
        	
        	/*System.out.println("X " + xHighlight/(scale*snapToWidth) + 
        			" Y " + yHighlight/(scale*snapToHeight));
        	System.out.println("WIDTH " + highlighterWidth/(snapToWidth)+
        			" HEIGHT " + highlighterHeight/(snapToHeight));*/
        	
        	TileSetInfo tileset = tileSetInfo.getTileSet(xHighlight/(scale*snapToWidth), 
        			yHighlight/(scale*snapToHeight),
        			highlighterWidth/(snapToWidth),
        			highlighterHeight/(snapToHeight));
        	
        	tileSetting.setTiles(tileset);
        }
        
        private int normalizeX(int x) {
        	if (tileSetInfo != null) {
        	    BufferedImage tile = tileSetInfo.getTileSet().get(0).getTileImage();
        	    int maxWidth = tileSetInfo.getWidthInTiles()*tile.getWidth()*scale;
        	    int xRet = (x/(snapToWidth*scale))*(snapToWidth*scale);
        	    if (xRet >= maxWidth) {
        	    	xRet = ((maxWidth/(snapToWidth*scale) - 1))*(snapToWidth*scale);
        	    }
        	    return xRet;
        	}
        	return 0;
        }
        
        private int normalizeY(int y) {
        	if (tileSetInfo != null) {
        	    BufferedImage tile = tileSetInfo.getTileSet().get(0).getTileImage();
        	    int maxHeight = tileSetInfo.getHeightInTiles()*tile.getHeight()*scale;
        	    int yRet = (y/(snapToHeight*scale))*(snapToHeight*scale);
        	    if (yRet >= maxHeight) {
        	    	yRet = ((maxHeight/(snapToHeight*scale) - 1))*(snapToHeight*scale);
        	    }
        	    return yRet;
        	}
        	return 0;
        }
        
        private int getHighlightWidth(int dist) {
        	return (dist/(scale*snapToWidth) + 1)*(snapToWidth);
        }
        
        private int getHighlightHeight(int dist) {
        	return (dist/(scale*snapToHeight) + 1)*(snapToHeight);
        }
        
        private void limitHighlighterToMax() {
        	if (tileSetInfo != null) {
        	    BufferedImage tile = tileSetInfo.getTileSet().get(0).getTileImage();
        	    int maxWidth = tileSetInfo.getWidthInTiles()*tile.getWidth()*scale;
        	    int maxHeight = tileSetInfo.getHeightInTiles()*tile.getHeight()*scale;
        	    
        	    if ((xHighlight + (highlighterWidth*scale)) > maxWidth) {
        	    	highlighterWidth = (maxWidth - xHighlight)/scale;
        	    }
        	    
        	    if ((yHighlight + (highlighterHeight*scale)) > maxHeight) {
        	        highlighterHeight = (maxHeight - yHighlight)/scale;	
        	    }
        	}
        }
        
        private void setHighliterX(int startX, int endX) {
        	if (startX < endX) {
    			xHighlight = normalizeX(startX);
    		    highlighterWidth = getHighlightWidth(endX - xHighlight);
    		} else {
    			xHighlight = normalizeX(endX);
    			highlighterWidth = getHighlightWidth(startX - xHighlight);
    		}
        }
        
        private void setHighliterY(int startY, int endY) {
        	if (startY < endY) {
    			yHighlight = normalizeY(startY);
    		    highlighterHeight = getHighlightHeight(endY - yHighlight);
    		    
    		} else {
    			yHighlight = normalizeY(endY);
    			highlighterHeight = getHighlightHeight(startY - yHighlight);
    		}
        }
    }
    
    private void setPrefferedSize() {
    	if (tileSetInfo != null && tileSetInfo.getTileSet().size() > 0) {
			BufferedImage tile = tileSetInfo.getTileSet().get(0).getTileImage();
			setPreferredSize(new Dimension(tile.getWidth()*tileSetInfo.getWidthInTiles()*scale, 
					tile.getHeight()*tileSetInfo.getHeightInTiles()*scale));
			revalidate();
    	}
    }

	protected TileSetInfo getTileSetInfo() {
		return tileSetInfo;
	}

	protected int getxHighlight() {
		return xHighlight;
	}

	protected void setxHighlight(int xHighlight) {
		this.xHighlight = xHighlight;
	}

	protected int getyHighlight() {
		return yHighlight;
	}

	protected void setyHighlight(int yHighlight) {
		this.yHighlight = yHighlight;
	}

	protected int getSnapToWidth() {
		return snapToWidth;
	}

	protected void setSnapToWidth(int snapToWidth) {
		this.snapToWidth = snapToWidth;
	}

	protected int getSnapToHeight() {
		return snapToHeight;
	}

	protected void setSnapToHeight(int snapToHeight) {
		this.snapToHeight = snapToHeight;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
		setPrefferedSize();
	}
}