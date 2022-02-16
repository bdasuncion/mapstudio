package mapPalette;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PaletteChooser extends JPanel implements ActionListener
{
	private JComboBox paletteIndex;
	private JLabel palettesAvailable;
	private int indexTaken = 0;
	int idx = 0;
	private boolean OKStatus = false;
	public PaletteChooser()
	{
		String index[] = new String[16];
		index[0] = "0";
		index[1] = "1";
		index[2] = "2";
		index[3] = "3";
		index[4] = "4";
		index[5] = "5";
		index[6] = "6";
		index[7] = "7";
		index[8] = "8";
		index[9] = "9";
		index[10] = "10";
		index[11] = "11";
		index[12] = "12";
		index[13] = "13";
		index[14] = "14";
		index[15] = "15";
		
		paletteIndex = new JComboBox(index);
		
		JButton OK = new JButton("OK");
		OK.setActionCommand("OK");
		OK.addActionListener(this);
		
		palettesAvailable  = new JLabel("Palettes Taken " + indexTaken);
		this.add(new JLabel("index"));
		this.add(paletteIndex);
		this.add(OK);
		this.add(palettesAvailable );
		
		this.setVisible(true);
	}
	
	public void setIndexTaken(int taken)
	{
		indexTaken = taken;
		if(indexTaken == 1)
		{
			OKStatus = true;
		}
		palettesAvailable.setText("Palettes Taken " + indexTaken);
	}
	
	public int getIndex()
	{
		return idx;
	}
	
	public boolean getOKStaus()
	{
		if(OKStatus == true)
		{
			OKStatus = false;
			return true;
		}
		
		return false;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		command(e.getActionCommand());	
	}
	
	private void command(String command)
	{
		idx = Integer.parseInt((String)paletteIndex.getSelectedItem());
		if(command == "OK" &&  idx < indexTaken)
		{
			OKStatus = true;
		}
		else
		{
			idx = indexTaken - 1;
			if(idx<0)
				idx = 0;
			paletteIndex.setSelectedIndex(idx);
			OKStatus = true;
		}
	}


}