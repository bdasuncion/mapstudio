package mapCanvas;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import infoObjects.ActorInfo;

public class ActorSettingDialog extends JDialog implements ActionListener, KeyListener {
	JTextField nameField;
	JLabel nameLabel;
	
	static private final String OK = "OK";
	static private final String CANCEL = "CANCEL";
	
	private Vector<ActorInfo> actors;
	int x, y;
	ActorInfo info = null;
	JButton okButton;
	JButton cancelButton;
	
    public ActorSettingDialog(JFrame owner) {
    	super(owner, true);
    	
    	nameField = new JTextField();
    	nameField.setPreferredSize(new Dimension(100, 20));
    	nameField.addKeyListener(this);
    	nameLabel = new JLabel("Name");
    	JPanel namePanel = new JPanel();
    	namePanel.add(nameLabel);
    	namePanel.add(nameField);
    	
    	okButton = new JButton("OK");
    	okButton.setActionCommand(OK);
    	okButton.addActionListener(this);
    	
    	cancelButton = new JButton("Cancel");
    	cancelButton.setActionCommand(CANCEL);
    	cancelButton.addActionListener(this);
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	JPanel actorPanel = new JPanel();
    	actorPanel.setLayout(new GridLayout(2,1));
    	actorPanel.add(namePanel);
    	actorPanel.add(buttonPanel);
    	    	
    	add(actorPanel);
    	this.setSize(300,200);
		this.setResizable(false);
		this.pack();
    }
    
    public void setActorList(int x, int y, Vector<ActorInfo> a) {
    	info = null;
    	actors = a;
    	this.x = x;
    	this.y = y;
    	for (ActorInfo actorInfo: actors) {
			if (x == actorInfo.getX() && y == actorInfo.getY()) {
				info = actorInfo;
				nameField.setText(info.getType());
			}
		}
    	nameField.requestFocus();
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals(OK)) {
			
			if (info != null) {
				if (!nameField.getText().contentEquals("")) {
				    info.setType(nameField.getText());
				} else {
					actors.remove(info);
				}
			} else if (!nameField.getText().contentEquals("")){
				info = new ActorInfo();
				info.setType(nameField.getText());
				info.setX(x);
				info.setY(y);
				actors.add(info);
			}
		}
		
		nameField.setText("");
		this.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
             okButton.doClick();
         } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	 cancelButton.doClick();
         }
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
