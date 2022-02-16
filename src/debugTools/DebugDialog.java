package debugTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;

public class DebugDialog  extends JDialog{

	private boolean OKStatus;
	private JButton okButton;
	PanelDisplay display;
	
	public DebugDialog(JFrame owner)
	{
		super(owner, true);
		
		display = new PanelDisplay();
		this.add(display);
		display.setVisible(true);
		
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new buttonPress());
		this.add(okButton);
		okButton.setVisible(true);
		this.setSize(300,200);
	//	this.setResizable(false);
		this.pack();
	}
	
	public void setPanelDisplay(BufferedImage img[],int numDisplay,
			int wit, int hit)
	{
		display.setDisplay(img, numDisplay, wit, hit);
		display.setVisible(true);
		//display = new PanelDisplay(img, numDisplay, wit, hit);

	}
	
	private class buttonPress implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			command(e.getActionCommand());	
		}	
	}
	
	private void command(String command)
	{
		if(command == "OK")
		{
			this.setVisible(false);
			OKStatus = true;
		}
	}
}
