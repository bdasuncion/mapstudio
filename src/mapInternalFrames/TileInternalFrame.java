package mapInternalFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import mapTiles.MapTilesInVRAM;
import infoObjects.TileInfo;

public class TileInternalFrame extends JInternalFrame{
	JPanel mainTilePanel;
	MapTilesInVRAM mapTilesInVRAM;
    public TileInternalFrame(int tileWidth, int tileHeight, Vector<TileInfo> tr) {
    	
    	super("Tiles", true, true);
    	
    	mapTilesInVRAM = new MapTilesInVRAM(4, tileWidth, tileHeight, tr);
		mainTilePanel = new JPanel();
		mainTilePanel.setLayout(new BorderLayout());
		mainTilePanel.setBackground(Color.LIGHT_GRAY);
		mainTilePanel.add(mapTilesInVRAM, BorderLayout.CENTER);
		
		setSize(400,400);
		add(mainTilePanel);
		setVisible(true);
		pack();
    }
    
    public MapTilesInVRAM getMapTilesPanel() {
    	return mapTilesInVRAM;
    }
}
