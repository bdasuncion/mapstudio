package mapInternalFrames;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import infoObjects.MapInfo;
import interfaces.TileSetPanelDisplay;
import mapCanvas.MapCanvasWxH;

public class MapCanvasInternalFrame extends JInternalFrame {
	private MapCanvasWxH mapView;
	private static final int minTileWidth = 16;
	private static final int minTileheight = 16;
	public MapCanvasInternalFrame(MapInfo mi, int width, int height, int tileWidth, int tileHeight) {
		super("", true, true);
		
		if (width%minTileWidth > 0) {
			width = ((width/minTileWidth)+ 1 )* minTileWidth;
		}
		
		if (height%minTileheight > 0) {
			height = ((height/minTileheight)+ 1 )* minTileheight;
		}
		
        StringBuilder mapTitle = new StringBuilder();
		
		mapTitle.append("Map ");
		mapTitle.append(width);
		mapTitle.append("x");
		mapTitle.append(height);
		mapTitle.append(" ");
		mapTitle.append("Tile ");
		mapTitle.append(tileWidth);
		mapTitle.append("x");
		mapTitle.append(tileHeight);
		
		setTitle(mapTitle.toString());
		
        mapView = new MapCanvasWxH(mi, width,height,tileWidth,tileHeight);
		
		JScrollPane scrollView = new JScrollPane(mapView);
		
		setSize(1000,1000);
		getContentPane().add(scrollView);
		setVisible(true);
		pack();
		setLocation(0, 200);
	}
	
	public MapCanvasWxH getMapView() {
		return mapView;
	}
}
