package mapTileSet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import infoObjects.TileSetInfo;
import interfaces.TileSetManipulation;
import interfaces.TileSetPanelDisplay;
import interfaces.TileSetting;

public class TileSetControlPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 32053756636055977L;
	//private MapTileSetPanel tileSet;
	private TileSetResizeDialog tileSizeDialog;
	private boolean tileSetChanged;
	private boolean tileSetResized = false;
	private TileSetManipulation tileSetManipulation;
	private TileSetPanelDisplay tileSetPanelDisplay;
	
	JButton showLayerPanel;
	
	public final static String LAYER_COMMAND = "LAYER";
	public final static String LAYERCHANGEALL_COMMAND = "SET ALL";
	public final static String COLLISION_COMMAND = "COLLISION";
	public final static String SETTILE_COMMAND = "SETTILE";
		
	public TileSetControlPanel(JFrame parent, TileSetManipulation tsm, TileSetPanelDisplay tspd) {
		super();
		this.setLayout(new GridLayout(0,1));
		
		tileSetManipulation = tsm;
		tileSetPanelDisplay = tspd;
		//tileSet = new MapTileSetPanel(tileW, tileH, ts);
		
		//tileSizeDialog = new TileSetResizeDialog(parent);
		//tileSizeDialog.setVisible(false);
		
		//JPanel buttonPanel = new JPanel();
		JButton flipVertical = new JButton("Flip Vertical");
		flipVertical.setActionCommand("vertical");
		flipVertical.addActionListener(this);
		JButton flipHorizontal = new JButton("Flip Horizontal");
		flipHorizontal.setActionCommand("horizontal");
		flipHorizontal.addActionListener(this);
		JButton showCollisionPanel = new JButton("Collision");
		showCollisionPanel.setActionCommand(COLLISION_COMMAND);
		showCollisionPanel.addActionListener(this);
		showLayerPanel = new JButton("Layer");
		showLayerPanel.setActionCommand(LAYER_COMMAND);
		showLayerPanel.addActionListener(this);
		JButton showTileSetPanel = new JButton("Tile Set");
		showTileSetPanel.setActionCommand(SETTILE_COMMAND);
		showTileSetPanel.addActionListener(this);
		
		JPanel viewControls = new JPanel();
		viewControls.add(showCollisionPanel);
		viewControls.add(showLayerPanel);
		viewControls.add(showTileSetPanel);
		add(viewControls);
		/*add(showCollisionPanel);
		add(flipHorizontal);
		add(showLayerPanel);
		add(showTileSetPanel);*/
		//pack();
		
		
		
	//	this.add(frameView);
	//	this.add(buttonPanel);
		//this.add(frameView, BorderLayout.CENTER);
		//this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if(action == "vertical") {
			this.flipTileSetVertical();
		}
		
		else if(action == "horizontal") {
			this.flipTileSetHorizontal();
		} else if (action.contains(COLLISION_COMMAND)) {
			tileSetPanelDisplay.showCollisionSet();
			showLayerPanel.setText(LAYER_COMMAND);
		} else if (action.contains(LAYER_COMMAND)) {
			showLayerPanel.setText(LAYERCHANGEALL_COMMAND);
			tileSetPanelDisplay.showLayerSet();
		} else if (action.contains(SETTILE_COMMAND)) {
			tileSetPanelDisplay.showTileSet();
			showLayerPanel.setText(LAYER_COMMAND);
		} else {
			this.resizeTileSet();
		}
		
		tileSetChanged = true;
	}
	
	public void flipTileSetHorizontal() {
		tileSetManipulation.flipTilesHorizontal();
	}
	
	public void flipTileSetVertical() {
		tileSetManipulation.flipTilesVertical();
	}
	
	public void resizeTileSet() {
		tileSizeDialog.setVisible(true);
		if(tileSizeDialog.getOKStaus() == false)
			return;
		
		tileSetResized = true;
		/*TODO
		 * check this?
		 * */
		//tileSet.resizeTileSet(tileSizeDialog.getTileWidth(), tileSizeDialog.getTileHeight());
		//tileSet.repaint();
	}
	
	public boolean isTileSetChanged() {
		if(tileSetChanged == true) {
			tileSetChanged = false;
			return true;
		}
		return false;
	}
	
	public boolean isTileSetResized() {
		if(tileSetResized == true)
		{
			tileSetResized = false;
			return true;
		}
		return false;
	}
	
	//public MapTileSetPanel getMapTileSet() {
		//return tileSet;
	//}
	
	public int getTileSetSize() {
		return tileSizeDialog.getTileWidth()*tileSizeDialog.getTileHeight();
	}
}