package mapInternalFrames;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

import interfaces.MapControls;
import interfaces.MapViewSettings;
import interfaces.ModeSelectionInterface;
import interfaces.TileSelectionControl;
import mapCanvas.ModeSetting;
import mapCanvas.MapExtraControls;
import mapCanvas.MapScale;
import mapCanvas.VisibleLayer;

public class LayerOptionsInternalFrame extends JInternalFrame {
	private ModeSetting activeLayer;
	private VisibleLayer visibleLayer;
	private MapExtraControls mapExtraControls;
	private MapScale mapScale;
	
	public LayerOptionsInternalFrame(MapViewSettings mapViewSettings, ModeSelectionInterface msi, 
			MapControls mc, TileSelectionControl tc) {
		super("Layer Options");
		activeLayer = new ModeSetting(msi);
		visibleLayer = new VisibleLayer(mapViewSettings);
		mapExtraControls = new MapExtraControls(mc, tc);
		
		//mapScale = new MapScale();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(activeLayer, BorderLayout.WEST);
		getContentPane().add(visibleLayer, BorderLayout.EAST);
		getContentPane().add(mapExtraControls, BorderLayout.SOUTH);
	
		//getContentPane().add(mapScale, BorderLayout.SOUTH);
		setVisible(true);
		pack();
		setLocation(500, 200);
	}
}
