package mapCanvas;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import interfaces.ModeSelectionInterface;


public class ModeSetting extends JPanel implements ActionListener
{
/**
	 * 
	 */
	private static final long serialVersionUID = 3135690050228733767L;
	private int activeLayer = 1;
	private boolean isButtonPressed = false;
	
	ModeSelectionInterface modeSelection;
	
	private static String TILESET = "TILESET";
	private static String EVENTSET = "EVENTSET";
	private static String ACTORSET = "ACTORSET";
	public ModeSetting(ModeSelectionInterface msi) {
		super();
		
		modeSelection = msi;
		JRadioButton modeTileSet = new JRadioButton("Tile Set");
		modeTileSet.setActionCommand(TILESET);
		JRadioButton modeEventSet = new JRadioButton("Event Set");
		modeEventSet.setActionCommand(EVENTSET);
		JRadioButton modeActorSet = new JRadioButton("Actor Set");
		modeActorSet.setActionCommand(ACTORSET);
		
		modeTileSet.setSelected(true);

		modeTileSet.addActionListener(this);
		modeEventSet.addActionListener(this);
		modeActorSet.addActionListener(this);
		
		ButtonGroup group = new ButtonGroup();
		group.add(modeTileSet);
		group.add(modeEventSet);		
		group.add(modeActorSet);
		
		this.setLayout(new GridLayout(0, 1));
		
		this.add(new JLabel("Select Mode"));
		this.add(modeTileSet);
		this.add(modeEventSet);
		this.add(modeActorSet);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().matches(ACTORSET)) {
			modeSelection.setModeActor();
		} else if (e.getActionCommand().matches(TILESET)) {
			modeSelection.setModeTile();
		} else if (e.getActionCommand().matches(EVENTSET)) {
			modeSelection.setModeEvent();
		}
	}
}