package mapTileSet;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import infoObjects.CollisionInfo;

public class CollisionSettingDialog extends JDialog implements ActionListener {
	
	private JSpinner xPosition;
	private JSpinner width;
	private JSpinner yPosition;
	private JSpinner height;
	private JButton okButton,cancelButton;
	
	private CollisionInfo collisionInfo;
	
	private static String COMMAND_OK = "OK";
	private static String COMMAND_CANCEL = "CANCEL";
	
	//private static int MAX_VALUE = 15;
	//private static int MIN_VALUE = 0;
    public CollisionSettingDialog(JFrame owner) {
    	super(owner, true);
    	
    	SpinnerNumberModel xModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE, CollisionInfo.XY_MAX_VALUE, 1);
    	SpinnerNumberModel yModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE, CollisionInfo.XY_MAX_VALUE, 1);
    	SpinnerNumberModel widthModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE,CollisionInfo.WIDTHHEIGHT_MAX_VALUE,1);
    	SpinnerNumberModel heightModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE,CollisionInfo.WIDTHHEIGHT_MAX_VALUE,1);
    	
    	xPosition = new JSpinner(xModel);
    	xPosition.setSize(15,10);
    	yPosition = new JSpinner(yModel);
    	yPosition.setSize(15,10);
    	width = new JSpinner(widthModel);
    	width.setSize(15,10);
    	height = new JSpinner(heightModel);
    	height.setSize(15,10);
    	
    	JPanel panelX = new JPanel();
    	panelX.add(new JLabel("X"));
    	panelX.add(xPosition);
    	
    	JPanel panelY = new JPanel();
    	panelY.add(new JLabel("Y"));
    	panelY.add(yPosition);
    	
    	JPanel panelWidth = new JPanel();
    	panelWidth.add(new JLabel("width"));
    	panelWidth.add(width);
    	
    	JPanel panelHeight = new JPanel();
    	panelHeight.add(new JLabel("height"));
    	panelHeight.add(height);
    	
    	okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
    	
		JPanel panelButtons;
		panelButtons = new JPanel();
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		
    	JPanel panelNewFile = new JPanel();
		panelNewFile.setLayout(new GridLayout(3,2));
		panelNewFile.add(panelX);
		panelNewFile.add(panelY);
		panelNewFile.add(panelWidth);
		panelNewFile.add(panelHeight);
		panelNewFile.add(panelButtons);
    	
		this.add(panelNewFile);
		this.setSize(300,200);
		this.setResizable(false);
		this.pack();
    }
    
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals(COMMAND_OK)) {
			if (collisionInfo != null) {
				int widthValue = (int)width.getValue(), heightValue = (int) height.getValue();
				
				if (widthValue > CollisionInfo.WIDTHHEIGHT_MAX_VALUE - (int) xPosition.getValue()) {
					widthValue = CollisionInfo.WIDTHHEIGHT_MAX_VALUE - (int) xPosition.getValue();
				}
				
				if (heightValue > CollisionInfo.WIDTHHEIGHT_MAX_VALUE - (int) yPosition.getValue()) {
					heightValue = CollisionInfo.WIDTHHEIGHT_MAX_VALUE - (int) yPosition.getValue();
				}
				
				collisionInfo.setX((int) xPosition.getValue());
				collisionInfo.setY((int) yPosition.getValue());
				collisionInfo.setWidth(widthValue);
				collisionInfo.setHeight(heightValue);	
			}
		} else if (e.getActionCommand().contentEquals(COMMAND_CANCEL)) {
			
		}
		
		collisionInfo = null;
		this.setVisible(false);
	}


	protected void setCollisionInfo(CollisionInfo collisionInfo) {
		this.collisionInfo = collisionInfo;
		xPosition.setValue(collisionInfo.getX());
		yPosition.setValue(collisionInfo.getY());
		width.setValue(collisionInfo.getWidth());
		height.setValue(collisionInfo.getHeight());
	}
	
	
}
