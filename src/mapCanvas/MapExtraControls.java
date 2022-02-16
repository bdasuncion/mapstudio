package mapCanvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import interfaces.MapControls;
import interfaces.TileSelectionControl;

public class MapExtraControls extends JPanel implements ActionListener {
	MapControls mapControls;
	TileSelectionControl tileSetControls;
	JButton reset;
	public MapExtraControls(MapControls mc, TileSelectionControl tc ) {
		super();
		mapControls = mc;
		tileSetControls = tc;
		
		reset = new JButton("RELOAD ASSETS");
		reset.addActionListener(this);
		JPanel resetPanel = new JPanel();
		resetPanel.add(reset);
		add(resetPanel);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		mapControls.reloadMap();
		tileSetControls.reloadTileSelection();
	}
}
