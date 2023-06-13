package mapCanvas;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

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
import infoObjects.EventTransferMapInfo;
import javafx.scene.control.ComboBox;

public class EventSettingDialog extends JDialog implements ActionListener, KeyListener {
	//JTextField nameField;
	//private JSpinner widthSpinner;
	//private JSpinner heightSpinner;
	private JButton okButton,cancelButton;
	//private JComboBox type;
	private BasicPanel basicPanel;
	private TransferPanel transferPanel;
	
	
	int x, y, zOffset, width, length;
	String name;
	Vector<EventInfo> evenInfos;
	
	static private final String OK = "OK";
	static private final String CANCEL = "CANCEL";
	
	EventInfo info = null;
	
    public EventSettingDialog(JFrame owner) {
    	super(owner, true);
    	
    	basicPanel = new BasicPanel();
    	
    	okButton = new JButton("OK");
    	okButton.setActionCommand(OK);
    	okButton.addActionListener(this);
    	
    	cancelButton = new JButton("Cancel");
    	cancelButton.setActionCommand(CANCEL);
    	cancelButton.addActionListener(this);
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	transferPanel = new TransferPanel();
    	
    	JPanel eventPanel = new JPanel();
    	eventPanel.setLayout(new GridLayout(3,1));
    	eventPanel.add(basicPanel);
    	eventPanel.add(transferPanel);
    	eventPanel.add(buttonPanel);
    	
    	this.add(eventPanel);
    	//this.setSize(300,400);
		this.setResizable(false);
		this.pack();
		
		setFocusable(true);
    }

    private void initFields() {
    	basicPanel.setName("");
    	basicPanel.setLength(EventInfo.MIN_VALUE);
    	basicPanel.setWidth(EventInfo.MIN_VALUE);
    	basicPanel.setZOffset(0);
    	transferPanel.setTransferToMap("");
    	transferPanel.setTransferToX(EventTransferMapInfo.XYMIN_VALUE);
    	transferPanel.setTransferToY(EventTransferMapInfo.XYMIN_VALUE);
    	transferPanel.setFaceDirectionOnTransfer(EventTransferMapInfo.DOWN);
    }
    
    public void setEventList(int x, int y, Vector<EventInfo> e) {
    	info = null;
    	evenInfos = e;
    	this.x = x;
    	this.y = y;
    	this.zOffset = 0;
    	initFields();
    	for (EventInfo eventInfo: evenInfos) {
			if (x >= eventInfo.getX() && x < (eventInfo.getX() + eventInfo.getWidth()) &&
				y >= eventInfo.getY() && y < (eventInfo.getY() + eventInfo.getLength())) {
				info = eventInfo;
				basicPanel.setType(eventInfo.getType());
				basicPanel.setWidth(eventInfo.getWidth());
				basicPanel.setLength(eventInfo.getLength());
				basicPanel.setZOffset(eventInfo.getzOffset());
				if (eventInfo.getType().contentEquals(EventInfo.TYPE_TRANSFER)) {
					transferPanel.settransferEventFields((EventTransferMapInfo) eventInfo);
				}
			}
		}
    	//nameField.requestFocus();
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals(OK)) {
			basicPanel.setEvent(info);
		}

		this.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		 /*if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			 if (e.getSource() == nameField) {
				 ((JSpinner.DefaultEditor)widthSpinner.getEditor()).getTextField().requestFocus();
			 } else if (e.getSource() == ((JSpinner.DefaultEditor)widthSpinner.getEditor()).getTextField()) {
				 ((JSpinner.DefaultEditor)heightSpinner.getEditor()).getTextField().requestFocus();
			 } else if (e.getSource() == ((JSpinner.DefaultEditor)heightSpinner.getEditor()).getTextField()) {
                 okButton.doClick();
			 }
         } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	 cancelButton.doClick();
         }*/
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class BasicPanel extends JPanel {
		//JTextField nameField;
		private JSpinner widthSpinner;
		private JSpinner lengthSpinner;
		private JSpinner zOffsetSpinner;
		private JComboBox type;
		SpinnerNumberModel widthModel;
		SpinnerNumberModel lengthModel;
		SpinnerNumberModel zOffsetModel;
		
		public BasicPanel() {
			super();
			
			/*nameField = new JTextField();
	    	nameField.setPreferredSize(new Dimension(100, 20));
	    	//nameField.addKeyListener(this);
	    	JPanel namePanel = new JPanel();
	    	namePanel.add(new JLabel("Name"));
	    	namePanel.add(nameField);*/
	    	
	    	widthModel = new SpinnerNumberModel(EventInfo.MIN_VALUE, EventInfo.MIN_VALUE,
	    			EventInfo.WIDTHHEIGHT_MAX, 8);
	    	lengthModel = new SpinnerNumberModel(EventInfo.MIN_VALUE, EventInfo.MIN_VALUE,
	    			EventInfo.WIDTHHEIGHT_MAX, 8);
	    	zOffsetModel = new SpinnerNumberModel(0, 0, EventInfo.ZOFFSET_MAX_VALUE, 8);
	    	
	    	widthSpinner = new JSpinner(widthModel);
	    	widthSpinner.setSize(15,10);
	    	//widthSpinner.getEditor().addKeyListener(this);
	    	lengthSpinner = new JSpinner(lengthModel);
	    	lengthSpinner.setSize(15,10);
	    	//heightSpinner.getEditor().addKeyListener(this);
	    	zOffsetSpinner = new JSpinner(zOffsetModel);
	    	zOffsetSpinner.setSize(15, 10);
	    	
	    	JPanel panelWidth = new JPanel();
	    	panelWidth.add(new JLabel("width"));
	    	panelWidth.add(widthSpinner);
	    	
	    	JPanel panelLength = new JPanel();
	    	panelLength.add(new JLabel("length"));
	    	panelLength.add(lengthSpinner);
	    	
	    	JPanel panelZOffset = new JPanel();
	    	panelLength.add(new JLabel("zOffset"));
	    	panelLength.add(zOffsetSpinner);
	    	
	    	JPanel widthHeightPanel = new JPanel();
	    	widthHeightPanel.add(panelWidth);
	    	widthHeightPanel.add(panelLength);
	    	widthHeightPanel.add(panelZOffset);
	    	
	    	String types[] = new String[2];
	    	types[0] = EventInfo.TYPE_NONE;
	    	types[1] = EventInfo.TYPE_TRANSFER;
	    	type = new JComboBox<String>(types);
	    	
	    	JPanel typePanel = new JPanel();
	    	typePanel.add(type);
	    	
	    	JPanel basicEventPanel = new JPanel();
	    	basicEventPanel.setLayout(new GridLayout(2,1));
	    	//basicEventPanel.add(namePanel);
	    	basicEventPanel.add(typePanel);
	    	basicEventPanel.add(widthHeightPanel);
	    	
	    	
	    	this.add(basicEventPanel);
		}
		
		public void setEvent(EventInfo eventInfo) {
			if (info != null) {
				if (!((String)type.getSelectedItem()).contentEquals(EventInfo.TYPE_NONE)) {
				    info.setType((String)type.getSelectedItem());
				    info.setLength((int)lengthSpinner.getValue());
					info.setWidth((int)widthSpinner.getValue());
					info.setzOffset((int)zOffsetSpinner.getValue());
					
					if (((String) type.getSelectedItem()).contentEquals(EventInfo.TYPE_TRANSFER) &&
							info.getType() != EventInfo.TYPE_TRANSFER) {
						info = new EventTransferMapInfo(info);
						info.setType((String) type.getSelectedItem());
					}
					transferPanel.setEventTransfer((EventTransferMapInfo) info);
					
				} else {
					evenInfos.remove(info);
				}
			} else {
				//Replace with EventInfo factory
				info = new EventTransferMapInfo();
				//info.setType(nameField.getText());
				info.setX(x);
				info.setY(y);
				info.setzOffset((int)zOffsetSpinner.getValue());
				info.setLength((int)lengthSpinner.getValue());
				info.setWidth((int)widthSpinner.getValue());
				info.setType((String)type.getSelectedItem());
				
				if (((String) type.getSelectedItem()).contentEquals(EventInfo.TYPE_TRANSFER) &&
						info.getType() != EventInfo.TYPE_TRANSFER) {
					info = new EventTransferMapInfo(info);
				} 
				
				transferPanel.setEventTransfer((EventTransferMapInfo) info);
			
				evenInfos.add(info);
			}	
		}
		
		public void setType(String t) {
			type.setSelectedItem(t);
		}
		
		public void setWidth(int width) {
			widthModel.setValue(width);
		}
		
		public void setLength(int height) {
			lengthModel.setValue(height);
		}
		
		public void setZOffset(int zOffset) {
			zOffsetModel.setValue(zOffset);
		}
	}
	
	private class TransferPanel extends JPanel {
		private JSpinner transferToX;
		private JSpinner transferToY;
		JTextField transferToMap;
		JComboBox faceDirectionOnTransfer;
		SpinnerNumberModel xTransferModel;
		SpinnerNumberModel yTransferModel;
		
		public TransferPanel() {
			super();
			
			xTransferModel = new SpinnerNumberModel(EventTransferMapInfo.XYMIN_VALUE, 
					EventTransferMapInfo.XYMIN_VALUE, EventTransferMapInfo.XYMAX_VALUE, 1);
			yTransferModel = new SpinnerNumberModel(EventTransferMapInfo.XYMIN_VALUE,
					EventTransferMapInfo.XYMIN_VALUE, EventTransferMapInfo.XYMAX_VALUE, 1);
			
			transferToX = new JSpinner(xTransferModel);
			transferToX.setSize(15,10);
			transferToY = new JSpinner(yTransferModel);
			transferToY.setSize(15,10);
			
			JPanel panelXTransfer = new JPanel();
			panelXTransfer.add(new JLabel("Go to X:"));
			panelXTransfer.add(transferToX);
	    	
	    	JPanel panelYTransfer = new JPanel();
	    	panelYTransfer.add(new JLabel("Go to Y:"));
	    	panelYTransfer.add(transferToY);
	    	
	    	JPanel transferXYPanel = new JPanel();
	    	transferXYPanel.add(panelXTransfer);
	    	transferXYPanel.add(panelYTransfer);
	    	
	    	transferToMap = new JTextField();
	    	transferToMap.setPreferredSize(new Dimension(80, 20));

	    	JPanel mapTransferPanel = new JPanel();
	    	mapTransferPanel.add(new JLabel("Transfer to:"));
	    	mapTransferPanel.add(transferToMap);
	    	
	    	String directions[] = new String[8];
	    	directions[0] = EventTransferMapInfo.DOWN;
	    	directions[1] = EventTransferMapInfo.DOWNRIGHT;
	    	directions[2] = EventTransferMapInfo.RIGHT;
	    	directions[3] = EventTransferMapInfo.UPRIGHT;
	    	directions[4] = EventTransferMapInfo.UP;
	    	directions[5] = EventTransferMapInfo.UPLEFT;
	    	directions[6] = EventTransferMapInfo.LEFT;
	    	directions[7] = EventTransferMapInfo.DOWNLEFT;
	    	
	    	faceDirectionOnTransfer  = new JComboBox(directions);
	    	
	    	JPanel directionPanel = new JPanel();
	    	directionPanel.add(new JLabel("Directions:"));
	    	directionPanel.add(faceDirectionOnTransfer);
	    	
	    	JPanel mapAndDirectionPanel = new JPanel();
	    	mapAndDirectionPanel.add(mapTransferPanel);
	    	mapAndDirectionPanel.add(directionPanel);
	    	
	    	JPanel transferPanel = new JPanel();
	    	transferPanel.setLayout(new GridLayout(2,1));
	    	transferPanel.add(transferXYPanel);
	    	transferPanel.add(mapAndDirectionPanel);
	    	
	    	this.add(transferPanel);
	    	//this.setSize(300,200);
		}
		
		public void setEventTransfer(EventTransferMapInfo info) {
			if (transferToMap.getText() != "") {
			    info.setTransferToMap(transferToMap.getText());
			    info.setTransferToX((int) transferToX.getValue());
			    info.setTransferToY((int) transferToY.getValue());
			    info.setFaceDirectionOnTransfer((String) faceDirectionOnTransfer.getSelectedItem());
			}
		}
		
		public void setTransferToMap(String map) {
			transferToMap.setText(map);
		}
		
		public void setTransferToX(int x) {
			transferToX.setValue(x);
		}
		
		public void setTransferToY(int y) {
			transferToY.setValue(y);
			
		}
		
		public void setFaceDirectionOnTransfer( String direction) {
			faceDirectionOnTransfer.setSelectedItem(direction);
		}
		
		public void settransferEventFields(EventTransferMapInfo info) {
			transferToMap.setText(info.getTransferToMap());
			transferToX.setValue(info.getTransferToX());
			transferToY.setValue(info.getTransferToY());
			faceDirectionOnTransfer.setSelectedItem(info.getFaceDirectionOnTransfer());
		}
	}
}