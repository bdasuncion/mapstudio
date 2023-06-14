package mapCanvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import infoObjects.ActorInfo;
import infoObjects.EventInfo;
import infoObjects.MaskInfo;
import infoObjects.SpriteMaskBoundsInfo;
import infoObjects.SpriteMaskInfo;
import interfaces.MaskImportFunctions;
import tools.ImageTools;

public class SpriteMaskSettingDialog extends JDialog implements MaskImportFunctions, ActionListener {
	JSpinner heightSpinner;
	SpinnerNumberModel heightModel;
	JComboBox maskSelection;
	JLabel name;
	JComboBox type;
	JButton okButton;
	JButton cancelButton;
	JButton deleteButton;
	
	MaskImageDisplay maskDisplay;
	SpriteMaskInfo spriteMaskInfo = null;
	MaskInfo maskInfo = null;
	Vector<SpriteMaskInfo> spriteMasks;
	int x,y,snapToWidth,snapToHeight;
	
	private static final String MASKTYPE = "MASKTYPE";
	private static final String OK = "OK";
	private static final String CANCEL = "CANCEL";
	private static final String DELETE = "DELETE";
	
	Vector<MaskInfo> masks;
	
	public SpriteMaskSettingDialog(JFrame owner, Vector<MaskInfo> masks) {
		super(owner, true);
		heightModel = new SpinnerNumberModel(SpriteMaskInfo.MINHEIGHT, SpriteMaskInfo.MINHEIGHT, SpriteMaskInfo.MAXHEIGHT, 8);
		heightSpinner = new JSpinner(heightModel);
		
		String types[] = new String[MaskInfo.MaskType.MaskTypeCount.ordinal()];
		
		for (int i = 0; i < SpriteMaskInfo.types.length; ++i) {
			types[i] = SpriteMaskInfo.types[i];
		}
    	
    	type = new JComboBox<String>(types);
    	
    	this.masks = masks;
    	
		maskSelection = new JComboBox<MaskInfo>(new DefaultComboBoxModel<MaskInfo>(masks));
		maskDisplay = new MaskImageDisplay();
		JPanel maskImagePanel = new JPanel();
		JPanel controlPanel = new JPanel();
		
		maskDisplay.setPreferredSize(new Dimension(128,128));
		maskImagePanel.add(maskDisplay);
		
		
		maskSelection.setActionCommand(MASKTYPE);
		maskSelection.addActionListener(this);
		maskSelection.setSelectedIndex(0);
		controlPanel.add(maskSelection);
		controlPanel.add(heightSpinner);
		
		okButton = new JButton("OK");
    	okButton.setActionCommand(OK);
    	okButton.addActionListener(this);
    	
    	cancelButton = new JButton("Cancel");
    	cancelButton.setActionCommand(CANCEL);
    	cancelButton.addActionListener(this);
    	
    	deleteButton = new JButton("Delete");
    	deleteButton.setActionCommand(DELETE);
    	deleteButton.addActionListener(this);
    	deleteButton.setVisible(false);
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	buttonPanel.add(deleteButton);
    	
		JPanel mainPanel = new JPanel();
		mainPanel.add(maskImagePanel);
		mainPanel.add(controlPanel);
		JPanel allPanel = new JPanel();
		allPanel.setLayout(new GridLayout(2, 1));
		allPanel.add(mainPanel);
		allPanel.add(buttonPanel);
		
		add(allPanel);
		
		this.setSize(300,250);
		this.setResizable(false);
		this.pack();
	}
	
	public void setSpriteMaskList(int x, int y, int snapToWidth, int snapToHeight, Vector<SpriteMaskInfo> spriteMasks) {
		this.spriteMaskInfo = null;
		this.spriteMasks = spriteMasks;
    	this.x = x;
    	this.y = y;
    	this.snapToWidth = snapToWidth;
    	this.snapToHeight = snapToHeight;
    	deleteButton.setVisible(false);
    	for (SpriteMaskInfo spriteMaskInfo: spriteMasks) {
    		SpriteMaskBoundsInfo bounds = spriteMaskInfo.getBounds();
    		if (x >= bounds.getStartX() && x <= bounds.getEndX() &&
    			y >= bounds.getStartY() && y <= bounds.getEndY()) {
    			this.spriteMaskInfo = spriteMaskInfo;
    			heightSpinner.setValue(spriteMaskInfo.getZ());
    			
    		
    			for (int i = 0; i < maskSelection.getItemCount(); ++i) {
    				if (maskSelection.getItemAt(i).toString().contentEquals(spriteMaskInfo.toString())) {
    					maskSelection.setSelectedIndex(i);
    					deleteButton.setVisible(true);
    					break;
    				}
    			}
    			return;
    		}
		}
    	
    	heightSpinner.setValue(SpriteMaskInfo.MINHEIGHT);
    	maskSelection.setSelectedIndex(0);
	}

	@Override
	public void onImportMask() {
	
	}
	
	 public class MaskImageDisplay extends JPanel {
		 BufferedImage maskImage;
		 
		 public void setDisplay(BufferedImage d) {
			 maskImage = d;
		 }
		 
		 @Override
		 public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2D =(Graphics2D)g;
			if (maskImage == null) {
				return;
			}
			
			BufferedImage disp;
			disp = g2D.getDeviceConfiguration().
					createCompatibleImage(maskImage.getWidth()*3, maskImage.getHeight()*3);
			
			disp = maskImage;
			
			g2D.drawImage(disp,  0, 0, maskImage.getWidth()*3, maskImage.getHeight()*3, this);
		 }
	 }
	 
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals(MASKTYPE)) {
			maskInfo = (MaskInfo) maskSelection.getSelectedItem();
			maskDisplay.setDisplay(maskInfo.getMask());
			maskDisplay.repaint();
		} else if (e.getActionCommand().contentEquals(OK)) {
			int startX = (x/snapToWidth)*snapToWidth;
			int startY = (y/snapToHeight)*snapToHeight;
			boolean isNew = false;
			
			if (spriteMaskInfo == null) {
				spriteMaskInfo = new SpriteMaskInfo();
				isNew = true;
			}
			
			spriteMaskInfo.setType(maskInfo.getType());
			spriteMaskInfo.setZ((int) heightModel.getValue());
			spriteMaskInfo.setName(maskInfo.getName());
			spriteMaskInfo.setMask(maskInfo.getMask());
			spriteMaskInfo.setPositionFromUpperLeftCorner(startX, startY);
			if (isNew) {
				this.spriteMasks.add(spriteMaskInfo);
			}
			setVisible(false);
		} else if (e.getActionCommand().contentEquals(CANCEL)) {
			setVisible(false);
		}  else if (e.getActionCommand().contentEquals(DELETE)) {
			this.spriteMasks.remove(spriteMaskInfo);
			setVisible(false);
		}
	}
}
