package mapInternalFrames;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import infoObjects.TileSetInfo;
import interfaces.TileSetDetailsSetting;
import interfaces.TileSetManipulation;
import interfaces.TileSetPanelDisplay;
import interfaces.TileSetting;
import mapTileSet.TileSetPanel;
import mapTileSet.TileSetCollisionSetting;
import mapTileSet.TileSetControlPanel;
import mapTileSet.TileSetLayerSettingsPanel;

public class TileSetInternalFrame extends JInternalFrame implements TileSetPanelDisplay,
TileSetting, TileSetDetailsSetting {
	private TileSetControlPanel tileSetPanel;
	private TileSetPanel mapTileSet;
	private TileSetCollisionSetting tileSetCollisionSetting;
	private TileSetLayerSettingsPanel tileSetLayerSettingsPanel;
	private boolean layerDisplayed = false;
	JScrollPane frameView;
	TileSetInfo tileSetInfo;
	
    public TileSetInternalFrame(JFrame parent , int tileW, int tileH, TileSetting ts, TileSetManipulation tsm) {
    	super("Tile Set to Map", true, true);
    	
    	tileSetPanel = new TileSetControlPanel(parent, tsm, this);
    	
    	mapTileSet =  new TileSetPanel(tileW, tileH, ts);
    	tileSetLayerSettingsPanel =  new TileSetLayerSettingsPanel(tileW, tileH, ts);
    	tileSetCollisionSetting = new TileSetCollisionSetting(tileW, tileH, ts);
		//mapTileSet = tileSetPanel.getMapTileSet();
		
    	frameView = new JScrollPane();
		frameView.setViewportView(mapTileSet);
		
		add(frameView, BorderLayout.CENTER);
		add(tileSetPanel, BorderLayout.SOUTH);
		
    	setSize(400,400);
		//add(mainTileSetPanel);
		setVisible(true);
		pack();
		setLocation(400, 100);
    }
    
    /*public TileSetPanel getTileSetPanel() {
    	return mapTileSet;
    }*/

	@Override
	public void showTileSet() {
		mapTileSet.setScale(((TileSetPanel)frameView.getViewport().getView()).getScale());
		frameView.setViewportView(mapTileSet);
		layerDisplayed = false;
		repaint();
	}

	@Override
	public void showLayerSet() {
		if (layerDisplayed) {
			tileSetLayerSettingsPanel.setAllTilesLayer();
			mapTileSet.setTiles(tileSetInfo);
		}
		tileSetLayerSettingsPanel.setScale(((TileSetPanel)frameView.getViewport().getView()).getScale());
		frameView.setViewportView(tileSetLayerSettingsPanel);
		layerDisplayed = true;
		repaint();
	}

	@Override
	public void showCollisionSet() {
		tileSetCollisionSetting.setScale(((TileSetPanel)frameView.getViewport().getView()).getScale());
		frameView.setViewportView(tileSetCollisionSetting);
		layerDisplayed = false;
		repaint();
	}

	@Override
	public void setTiles(TileSetInfo ts) {
		tileSetInfo = ts;
		setTitle("count: " 
	+ ts.getTileSet().size() + " pallete:" + ts.getPaletteIdx() + " idx:" + ts.getTileIdxStart() + "-" +
				ts.getTileIdxEnd());
		mapTileSet.setTiles(ts);
    	tileSetLayerSettingsPanel.setTiles(ts);
    	tileSetCollisionSetting.setTiles(ts);
	}

	@Override
	public void setTileSetName(String tileSetName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInformation(int tileCount, int paletteIdx, String tileSetName) {
		// TODO Auto-generated method stub
		
	}
}
