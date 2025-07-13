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
	MapShiftAndResizeDialog shiftResize;
	private static final String COMMAND_RELOAD = "RELOAD";
	private static final String COMMAND_EDIT = "EDIT";
	
	public MapExtraControls(MapControls mc, TileSelectionControl tc ) {
		super();
		mapControls = mc;
		tileSetControls = tc;
		shiftResize = new MapShiftAndResizeDialog(mc, null);
		shiftResize.setVisible(false);
		shiftResize.setModal(true);
		
		reset = new JButton("RELOAD ASSETS");
		reset.setActionCommand(COMMAND_RELOAD);
		reset.addActionListener(this);
		JPanel resetPanel = new JPanel();
		resetPanel.add(reset);
		add(resetPanel);
		
		JButton editTilesConfigurationButton = new JButton("EDIT");
		editTilesConfigurationButton.setActionCommand(COMMAND_EDIT);
		editTilesConfigurationButton.addActionListener(this);
		resetPanel.add(editTilesConfigurationButton);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase(COMMAND_RELOAD)) {
			mapControls.reloadMap();
			tileSetControls.reloadTileSelection();
		} else if (e.getActionCommand().equalsIgnoreCase(COMMAND_EDIT)) {
			System.out.println("EDIT MAP");
			shiftResize.setVisible(true);
		}
	}
}
