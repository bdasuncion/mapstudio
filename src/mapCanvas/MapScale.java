package mapCanvas;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class MapScale extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 614022073432386410L;
	private int scale = 1;
	private boolean isButtonPressed = false;
	
	public MapScale()
	{
		super();
		
		JRadioButton scale1 = new JRadioButton("1x");
		scale1.setActionCommand("1");
		scale1.setSelected(true);
		JRadioButton scale2 = new JRadioButton("2x");
		scale2.setActionCommand("2");

		
		scale1.addActionListener(this);
		scale2.addActionListener(this);
		
		ButtonGroup group = new ButtonGroup();
		group.add(scale1);		
		group.add(scale2);
		
		this.setLayout(new GridLayout(0, 1));
		
		this.add(new JLabel("Map size view"));
		this.add(scale1);
		this.add(scale2);
	}
	
	public boolean hasNewScale()
	{
		if(isButtonPressed == true)
		{
			isButtonPressed = false;
			return true;
		}
		
		return false;
	}
	public int getScale()
	{
		return scale;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		scale = Integer.parseInt(e.getActionCommand());
		isButtonPressed = true;
	}
}