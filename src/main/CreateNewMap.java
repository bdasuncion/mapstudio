package main;

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

public class CreateNewMap extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1681395367584655149L;
	private JSpinner widthOptions;
	private JSpinner heightOptions;
	private JSpinner tileWidthOptions;
	private JSpinner tileHeightOptions;
	private JButton okButton,cancelButton;
	private int width,height,tileWidth = 8,tileHeight = 8;
	private boolean OKStatus;
	
	public CreateNewMap(JFrame owner)
	{
		super(owner, true);
		
		SpinnerNumberModel numberModelWidth = new SpinnerNumberModel(256,256,2048,8); 
		SpinnerNumberModel numberModelHeight = new SpinnerNumberModel(256,256,2048,8);
		
		SpinnerNumberModel numberModelTileWidth = new SpinnerNumberModel(8,8,32,8); 
		SpinnerNumberModel numberModelTileHeight = new SpinnerNumberModel(8,8,32,8);
		
		widthOptions = new JSpinner(numberModelWidth);
		widthOptions.setValue(256);
		widthOptions.setSize(15,10);
		heightOptions = new JSpinner(numberModelHeight);
		heightOptions.setValue(256);
		widthOptions.setSize(15,10);
		
		tileWidthOptions = new JSpinner(numberModelTileWidth);
		tileWidthOptions.setSize(15, 10);
		tileHeightOptions = new JSpinner(numberModelTileHeight);
		tileHeightOptions.setSize(15, 10);
		
		widthOptions.setVisible(true);
		heightOptions.setVisible(true);
		tileWidthOptions.setVisible(true);
		tileHeightOptions.setVisible(true);
		
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new buttonPress());
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new buttonPress());
		
		JPanel panelOptions1;
		panelOptions1 = new JPanel();
		panelOptions1.add(new JLabel("Map Width"));
		panelOptions1.add(widthOptions);

		
		JPanel panelOptions2;
		panelOptions2 = new JPanel();
		panelOptions2.add(new JLabel("Map Height"));
		panelOptions2.add(heightOptions);
		
		JPanel panelOptions3;
		panelOptions3 = new JPanel();
		panelOptions3.add(new JLabel("Tile Width"));
		panelOptions3.add(tileWidthOptions);
		
		JPanel panelOptions4;
		panelOptions4 = new JPanel();
		panelOptions4.add(new JLabel("Tile Height"));
		panelOptions4.add(tileHeightOptions);

		
		JPanel panelButtons;
		panelButtons = new JPanel();
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		
		panelOptions1.setVisible(true);
		panelOptions2.setVisible(true);
		panelButtons.setVisible(true);
		
		JPanel panelNewFile = new JPanel();
		panelNewFile.setLayout(new GridLayout(5,1));
		panelNewFile.add(panelOptions1);
		panelNewFile.add(panelOptions2);
		panelNewFile.add(panelOptions3);
		panelNewFile.add(panelOptions4);
		panelNewFile.add(panelButtons);
		
		
		this.add(panelNewFile);
		this.setSize(300,200);
		this.setResizable(false);
		this.pack();
	}
	
	public int getMapHeight() 
	{
		return height;
	}

	public int getMapWidth() 
	{
		return width;
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
			width = ((SpinnerNumberModel) widthOptions.getModel()).getNumber().intValue();
			height = ((SpinnerNumberModel) heightOptions.getModel()).getNumber().intValue();
			tileWidth = ((SpinnerNumberModel) tileWidthOptions.getModel()).getNumber().intValue();
			tileHeight = ((SpinnerNumberModel) tileHeightOptions.getModel()).getNumber().intValue();
			//adjust map dimensions to multiple of tile dimensions
			if(width%tileWidth > 0)
				width += tileWidth - width%tileWidth;
			if(height%tileHeight > 0)
				height += tileHeight - height%tileHeight;
			
			OKStatus = true;
		}
		else
		{
			this.setVisible(false);
			OKStatus = false;
		}
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}
}