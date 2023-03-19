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
	private JSpinner length;
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
    	SpinnerNumberModel widthModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE,CollisionInfo.WIDTHLENGTH_MAX_VALUE,1);
    	SpinnerNumberModel lengthModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE,CollisionInfo.WIDTHLENGTH_MAX_VALUE,1);
    	SpinnerNumberModel heightModel = new SpinnerNumberModel(0,CollisionInfo.MIN_VALUE,CollisionInfo.HEIGHT_MAX_VALUE,1);
    	
    	
    	xPosition = new JSpinner(xModel);
    	xPosition.setSize(15,10);
    	yPosition = new JSpinner(yModel);
    	yPosition.setSize(15,10);
    	width = new JSpinner(widthModel);
    	width.setSize(15,10);
    	length = new JSpinner(lengthModel);
    	length.setSize(15,10);
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
    	
    	JPanel panelLength = new JPanel();
    	panelLength.add(new JLabel("length"));
    	panelLength.add(length);
    	
    	JPanel panelHeight = new JPanel();
    	panelHeight.add(new JLabel("height"));
    	panelHeight.add(height);
    	
    	okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
    	
		JPanel panelXY = new JPanel();
		panelXY.add(panelX);
		panelXY.add(panelY);
		
		JPanel panelButtons;
		panelButtons = new JPanel();
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		
		JPanel paneLWLH = new JPanel();
		paneLWLH.add(panelWidth);
		paneLWLH.add(panelLength);
		paneLWLH.add(panelHeight);
		
    	JPanel panelNewFile = new JPanel();
		panelNewFile.setLayout(new GridLayout(3,1));
		panelNewFile.add(panelXY);
		panelNewFile.add(paneLWLH);
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
				int widthValue = (int)width.getValue(), lengthValue = (int) length.getValue(), heightValue = (int)height.getValue();
				
				if (widthValue > CollisionInfo.WIDTHLENGTH_MAX_VALUE - (int) xPosition.getValue()) {
					widthValue = CollisionInfo.WIDTHLENGTH_MAX_VALUE - (int) xPosition.getValue();
				}
				
				if (lengthValue > CollisionInfo.WIDTHLENGTH_MAX_VALUE - (int) yPosition.getValue()) {
					lengthValue = CollisionInfo.WIDTHLENGTH_MAX_VALUE - (int) yPosition.getValue();
				}
				
				collisionInfo.setX((int) xPosition.getValue());
				collisionInfo.setY((int) yPosition.getValue());
				collisionInfo.setWidth(widthValue);
				collisionInfo.setLength(lengthValue);
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
		length.setValue(collisionInfo.getLength());
		height.setValue(collisionInfo.getHeight());
	}
	
	
}
