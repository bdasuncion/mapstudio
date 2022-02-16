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


public class TileSetResizeDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1681395367584655149L;
	private JSpinner textFieldWidth;
	private JSpinner textFieldHeight;
	private JButton okButton,cancelButton;
	private Number width;
	private Number height;
	private boolean OKStatus;
	
	public TileSetResizeDialog(JFrame owner)
	{
		super(owner, true);
		
		SpinnerNumberModel numberModelWidth = new SpinnerNumberModel(1,1,16,1); 
		SpinnerNumberModel numberModelHeight = new SpinnerNumberModel(1,1,16,1);
		textFieldWidth = new JSpinner(numberModelWidth);
		textFieldHeight = new JSpinner(numberModelHeight);
	
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new buttonPress());
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new buttonPress());
		
		JPanel panelOptionsWidth;
		panelOptionsWidth = new JPanel();
		panelOptionsWidth.add(new JLabel("Width in Tiles"));
		panelOptionsWidth.add(textFieldWidth);
		
		JPanel panelOptionsHeight;
		panelOptionsHeight = new JPanel();
		panelOptionsHeight.add(new JLabel("Height in Tiles"));
		panelOptionsHeight.add(textFieldHeight);
		
		JPanel panelButtons;
		panelButtons = new JPanel();
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		
		panelOptionsWidth.setVisible(true);
		panelOptionsHeight.setVisible(true);
		panelButtons.setVisible(true);
		
		JPanel panelNewFile = new JPanel();
		panelNewFile.setLayout(new GridLayout(3,1));
		panelNewFile.add(panelOptionsWidth);
		panelNewFile.add(panelOptionsHeight);
		panelNewFile.add(panelButtons);
		
		
		this.add(panelNewFile);
		this.setSize(300,100);
		this.setResizable(false);
		this.pack();
	}
	
	public int getTileHeight() 
	{
		return height.intValue();
	}

	public int getTileWidth() 
	{
		return width.intValue();
	}
	
	public boolean getOKStaus()
	{
		return OKStatus;
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
			width = ((SpinnerNumberModel) textFieldWidth.getModel()).getNumber();
			height = ((SpinnerNumberModel) textFieldHeight.getModel()).getNumber();
			OKStatus = true;
		}
		else
		{
			this.setVisible(false);
			OKStatus = false;
		}
	}
}