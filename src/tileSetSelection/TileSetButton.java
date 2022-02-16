package tileSetSelection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import infoObjects.TileSetInfo;
import interfaces.TileSetting;

public class TileSetButton extends JButton implements ActionListener{
	
	TileSetButtonDisplay tileSetDisplay;
	TileSetInfo tileSetInfo;
	TileSetting tileSetting;
	
	/*public TileSetButton(TileSetButtonDisplay ts, MapTileSet mts) {
		super();
		tileSet = ts;
		this.setSize(tileSet.getWidth(), tileSet.getHeight());
		this.add(tileSet);
		this.addActionListener(this);
		this.setVisible(true);
		mapTileSet = mts;
		this.repaint();
	}*/
	
	public TileSetButton(TileSetInfo ts, TileSetting mts) {
		super();
		tileSetInfo = ts;
		this.setSize(tileSetInfo.getWidthInTiles(), tileSetInfo.getHeightInTiles());
		
		tileSetDisplay = new TileSetButtonDisplay(tileSetInfo);
		
		this.add(tileSetDisplay);
		this.addActionListener(this);
		this.setVisible(true);
		tileSetting = mts;
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		//mapTileSet.setTiles(tileSet);
		tileSetting.setTiles(tileSetInfo);
		tileSetting.setTileSetName(tileSetDisplay.getTileName());
	}

	public TileSetInfo getTileSetInfo() {
		return tileSetInfo;
	}

	public void setTileSetInfo(TileSetInfo tileSetInfo) {
		this.tileSetInfo = tileSetInfo;
	}

	public TileSetButtonDisplay getTileSetDisplay() {
		return tileSetDisplay;
	}
}
