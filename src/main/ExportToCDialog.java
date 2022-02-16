package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExportToCDialog extends JDialog implements ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2032065256312438595L;
	boolean export[];  
	private JButton okButton;
	private JCheckBox collisonLayer;
	private JCheckBox layer0;
	private JCheckBox layer1;
	private JCheckBox layer2;
	private JCheckBox layer3;
	
	public ExportToCDialog(JFrame owner)
	{
		super(owner, true);
		export = new boolean[5];
		for(int i = 0; i<export.length; i++)
		{
			export[i] = false;
		}
		
		collisonLayer = new JCheckBox("Collision Layer");
		collisonLayer.setSelected(false);
		
		layer0 = new JCheckBox("Layer 0");
		layer0.setSelected(false);
		
		layer1 = new JCheckBox("Layer 1");
		layer1.setSelected(false);
		
		layer2 = new JCheckBox("Layer 2");
		layer2.setSelected(false);
		
		layer3 = new JCheckBox("Layer 3");
		layer3.setSelected(false);
		
		collisonLayer.addItemListener(this);
		layer0.addItemListener(this);
		layer1.addItemListener(this);
		layer2.addItemListener(this);
		layer3.addItemListener(this);
		
		JPanel panelChecker = new JPanel();
		panelChecker.setLayout(new GridLayout(0, 1));
		panelChecker.add(new JLabel("Select layers to export"));
		panelChecker.add(collisonLayer);
		panelChecker.add(layer0);
		panelChecker.add(layer1);
		panelChecker.add(layer2);
		panelChecker.add(layer3);
		panelChecker.setVisible(true);
	
		okButton = new JButton("OK");
		okButton.addActionListener(new buttonPress());
		panelChecker.add(okButton);
		
		this.add(panelChecker);
		
	
		
	//	this.add(okButton);
		this.setSize(300,100);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}
	
	private class buttonPress implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			pressButton();	
		}	
	}
	
	private void pressButton()
	{
		this.setVisible(false);
	}
	
	public void itemStateChanged(ItemEvent e) 
	{
		 if (e.getStateChange() == ItemEvent.DESELECTED)
		 {
			 if(e.getItemSelectable() == collisonLayer)
			 {
				 export[0] = false;
			 }
			 else if(e.getItemSelectable() == layer0)
			 {
				 export[1] = false;
			 }
			 else if(e.getItemSelectable() == layer1)
			 {
				 export[2] = false;
			 }
			 else if(e.getItemSelectable() == layer2)
			 {
				 export[3] = false;
			 }
			 else if(e.getItemSelectable() == layer3)
			 {
				 export[4] = false;
			 }
		 }
		 else
		 {
			 if(e.getItemSelectable() == collisonLayer)
			 {
				 export[0] = true;
			 }
			 else if(e.getItemSelectable() == layer0)
			 {
				 export[1] = true;
			 }
			 else if(e.getItemSelectable() == layer1)
			 {
				 export[2] = true;
			 }
			 else if(e.getItemSelectable() == layer2)
			 {
				 export[3] = true;
			 }
			 else if(e.getItemSelectable() == layer3)
			 {
				 export[4] = true;
			 }			 
		 }
	}
	
	public boolean[] getExportLayers()
	{
		return export;
	}
	
	
}