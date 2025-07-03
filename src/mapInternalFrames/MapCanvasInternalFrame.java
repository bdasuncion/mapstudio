package mapInternalFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import infoObjects.MapInfo;
import interfaces.TileSetPanelDisplay;
import mapCanvas.MapCanvasWxH;

public class MapCanvasInternalFrame extends JInternalFrame {
	private MapCanvasWxH mapView;
	
	public MapCanvasInternalFrame(MapInfo mi, int width, int height, int tileWidth, int tileHeight) {
		super("", true, true);
		
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
        mapView.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		add(mapView, BorderLayout.CENTER);
		
		//setPreferredSize(new Dimension(200, 200));
		setVisible(true);		
		pack();
		setLocation(0, 200);
		
		mapView.revalidate();
		mapView.repaint();
		
		validate();
	}
	
	public MapCanvasWxH getMapView() {
		return mapView;
	}
	
}
