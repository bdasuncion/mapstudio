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
       
        mapView.setPreferredSize(new Dimension(width, height));
        //mapView.setSize(mapView.getPreferredSize());
        
		JScrollPane scrollView = new JScrollPane(mapView);
		
		scrollView.setViewportView(mapView);

		add(scrollView, BorderLayout.CENTER);
		
		mapView.revalidate();
		scrollView.revalidate();
		mapView.repaint();
		
		setLocation(0, 200);		
		pack();
		setVisible(true);

		scrollView.revalidate();
		mapView.revalidate();
		mapView.repaint();
		
		mapView.setBorder(BorderFactory.createLineBorder(Color.RED));
		scrollView.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		//scrollView.getViewport().setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		//repaint();
		
		System.out.println("MAP SIZE:" + mapView.getPreferredSize());
		System.out.println("MAP W:" + mapView.getWidth() + " H:"+ mapView.getHeight());
		System.out.println("SCROLL SIZE:" + scrollView.getPreferredSize());
		System.out.println("SCROLL W:" + scrollView.getWidth() + " H:"+ scrollView.getHeight());
		
		//mapView.setSize(mapView.getPreferredSize());
		//mapView.revalidate();

		SwingUtilities.invokeLater(() -> {
			//mapView.setPreferredSize(new Dimension(width, height));
			//mapView.revalidate();
			//repaint();
		    //System.out.println("MapView size: " + mapView.getWidth() + " x " + mapView.getHeight());
		    //System.out.println("Scroll size: " + scrollView.getWidth() + " x " + scrollView.getHeight());
		    //System.out.println("VIEW: " + scrollView.getViewport().getView());
		    
			System.out.println("Viewport: " + scrollView.getViewport());
		    System.out.println("Viewport view: " + scrollView.getViewport().getView());
		    System.out.println("Viewport size: " + scrollView.getViewport().getViewSize());
		    System.out.println("MapView actual size: " + mapView.getWidth() + " x " + mapView.getHeight());
		});
	}
	
	public MapCanvasWxH getMapView() {
		return mapView;
	}
	
}
