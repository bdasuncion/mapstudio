package mapPalette;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.IndexColorModel;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public class MapPalette extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7741741647069956770L;
	private IndexColorModel Palette; 
	private int rgbVal = 0;
	int buttIdxSel = 0;
	private short R[];
	private short G[];
	private short B[];
	private JButton[] colourButton;
	private JSlider sliderR;
	private JSlider sliderG;
	private JSlider sliderB;
	private JLabel RVal;
	private JLabel GVal;
	private JLabel BVal;
	private int adjust = 1;
	private JFileChooser fileChooser;
	private boolean newColour = false;
	private boolean chooseNew = false;
	private boolean imported = false;
	private int palDepth;
	
	public MapPalette(IndexColorModel palette, int paletteDepth)	
	{
		JPanel palettePanel;
		this.Palette = palette;
		
		palDepth = paletteDepth;
		if(paletteDepth == 16)
			adjust = 8;
		else
			adjust = 1;
	
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);	
		this.setLayout(new BorderLayout());
		palettePanel = new JPanel();
		colourButton = new JButton[Palette.getMapSize()];
		R = new short[Palette.getMapSize()];
		G = new short[Palette.getMapSize()];
		B = new short[Palette.getMapSize()];
		
		palettePanel.setLayout(new BoxLayout(palettePanel,BoxLayout.Y_AXIS));
		for(int k = 0; k<Palette.getMapSize()/16; k++)
		{
			JPanel buttonPanel16 = new JPanel();
			for(int i = 0; i<16; i++)
			{
				rgbVal = Palette.getRGB(i + k*16);
			
				R[i + k*16] = (short)(Palette.getRed(i + k*16));
				G[i + k*16] = (short)(Palette.getGreen(i + k*16));
				B[i + k*16] = (short)(Palette.getBlue(i + k*16));
				colourButton[i + k*16] = new JButton();
				colourButton[i + k*16].setBackground(new Color(rgbVal));
				colourButton[i + k*16].setPreferredSize(new Dimension(15,15));
				colourButton[i + k*16].setActionCommand(String.valueOf(rgbVal) + "_" + String.valueOf(i + k*16));
				colourButton[i + k*16].addActionListener(new selectColor());
				buttonPanel16.add(colourButton[i + k*16]);
			}
			palettePanel.add(buttonPanel16);
		}
		
		this.add(palettePanel, BorderLayout.CENTER);
		this.createMixerPanel();
		this.setVisible(true);
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new PaletteFileFilter());
		newColour = false;
	}
	
	private void createMixerPanel()
	{
		sliderR = new JSlider(0,255/adjust,Palette.getRed(0)/adjust);
		sliderR.addChangeListener(new modifySlider());
		sliderR.setBackground(Color.RED);
		sliderG = new JSlider(0,255/adjust, Palette.getGreen(0)/adjust);
		sliderG.addChangeListener(new modifySlider());
		sliderG.setBackground(Color.GREEN);
		sliderB = new JSlider(0,255/adjust, Palette.getBlue(0)/adjust);
		sliderB.addChangeListener(new modifySlider());
		sliderB.setBackground(Color.BLUE);
		
		RVal = new JLabel("R: " + Integer.toString(sliderR.getValue()));
		GVal = new JLabel("G: " + Integer.toString(sliderG.getValue()));
		BVal = new JLabel("B: " + Integer.toString(sliderB.getValue()));
		
		JPanel coulourMixerPanel = new JPanel();
		coulourMixerPanel.setLayout(new GridLayout(3,0));
		
		coulourMixerPanel.add(sliderR);
		coulourMixerPanel.add(sliderG);
		coulourMixerPanel.add(sliderB);
		
		JPanel coulourValuePanel = new JPanel();
		coulourValuePanel.setLayout(new GridLayout(3,0));
		coulourValuePanel.add(RVal);
		coulourValuePanel.add(GVal);
		coulourValuePanel.add(BVal);
		
		JPanel MixerValuePanel = new JPanel();
		
		MixerValuePanel.add(this.createMenu());
		MixerValuePanel.add(coulourMixerPanel);
		MixerValuePanel.add(coulourValuePanel);
		
		this.add(MixerValuePanel, BorderLayout.SOUTH);		
	}
	
	private JPanel createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu mainMenu;
		JMenuItem importPalette;
		JMenuItem exportPalette;
		JPanel panelForMenu;

		mainMenu = new JMenu("Options");
		importPalette = new JMenuItem("Import");
		importPalette.addActionListener(new importPalette());
		exportPalette = new JMenuItem("Export");
		exportPalette.addActionListener(new exportPalette());

		mainMenu.add(importPalette);
		mainMenu.add(exportPalette);
		menuBar.add(mainMenu);
		panelForMenu = new JPanel();
		panelForMenu.add(menuBar);		
		return panelForMenu;		
	}
	
	public int getRval()
	{
		return (int)R[buttIdxSel];
	}
	
	public int getGval()
	{
		return (int)G[buttIdxSel];
	}
	
	public int getBval()
	{
		return (int)B[buttIdxSel];
	}
	
	public int getIdxSel()
	{
		return buttIdxSel;	
	}
	
	public IndexColorModel getPalette()
	{
		byte r[] = new byte[R.length];
		byte g[] = new byte[G.length];
		byte b[] = new byte[B.length];
		
		for(int i = 0; i< R.length; i++)
		{
			r[i] = (byte)R[i];
			g[i] = (byte)G[i];
			b[i] = (byte)B[i];
		}
		
		int ImageDepth = Palette.getPixelSize();
		IndexColorModel newModel = new IndexColorModel(ImageDepth,R.length,r,g,b);
		Palette = newModel;
		return Palette;
	}
	
	public int getPaletteDepth()
	{
		return palDepth;
	}
	
	public void newPalette(IndexColorModel pal)
	{
		Palette = pal;
		this.updatePalette();
	}
	
	public boolean hasNewColour()
	{
		if(newColour == true)
		{
			newColour = false;
			return true;
		}
		return false;
	}
	
	public boolean isColourChosen()
	{
		if(chooseNew == true)
		{
			chooseNew = false;
			return true;
		}
		return false;
	}
	
	public boolean isImportedPal()
	{
		if(imported == true)
		{
			imported = false;
			return true;
		}
		return false;
	}
	
	class selectColor implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			Color colorVal = new Color(Integer.parseInt(command.substring(0, command.indexOf("_"))));
			String buttonSel = e.getActionCommand();
			buttIdxSel = Integer.parseInt(buttonSel.substring(buttonSel.indexOf("_")+1, buttonSel.length()));
			rgbVal = colorVal.getRGB();
			sliderR.setValue(colorVal.getRed()/adjust);
			sliderG.setValue(colorVal.getGreen()/adjust);
			sliderB.setValue(colorVal.getBlue()/adjust);
			RVal.setText("R: " + Integer.toString(sliderR.getValue()));
			GVal.setText("G: " + Integer.toString(sliderG.getValue()));
			BVal.setText("B: " + Integer.toString(sliderB.getValue()));
			colourButton[buttIdxSel].setSelected(true);
			chooseNew = true;
		}
	}
	
	class modifySlider implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			R[buttIdxSel] = (short)(sliderR.getValue()*adjust);
			G[buttIdxSel] = (short)(sliderG.getValue()*adjust);
			B[buttIdxSel] = (short)(sliderB.getValue()*adjust);
			Color colorVal = new Color(sliderR.getValue()*adjust,sliderG.getValue()*adjust,sliderB.getValue()*adjust);
			colourButton[buttIdxSel].setBackground(colorVal);
			rgbVal = colorVal.getRGB();
			colourButton[buttIdxSel].setActionCommand(String.valueOf(rgbVal) + "_" + String.valueOf(buttIdxSel));
			RVal.setText("R: " + Integer.toString(sliderR.getValue()));
			GVal.setText("G: " + Integer.toString(sliderG.getValue()));
			BVal.setText("B: " + Integer.toString(sliderB.getValue()));
			newColour = true;
		}
	}
	
	class importPalette implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			importPal();
		}
	}
	
	private void importPal()
	{
/*		fileChooser.showOpenDialog(this);
		PaletteFileReader fileIn = new PaletteFileReader(fileChooser.getSelectedFile());
		Palette = fileIn.getPalette();
		this.updatePalette();
		for(int i = 0; i<Palette.getMapSize(); i++)
		{
			rgbVal = Palette.getRGB(i);
		
			R[i] = (short)(Palette.getRed(i));
			G[i] = (short)(Palette.getGreen(i));
			B[i] = (short)(Palette.getBlue(i));
			colourButton[i].setBackground(new Color(rgbVal));
			colourButton[i].setActionCommand(String.valueOf(rgbVal) + "_" + String.valueOf(i));
		}
		imported = true;
		buttIdxSel = 0;*/
	}
	
	private void updatePalette()
	{
		for(int i = 0; i<Palette.getMapSize(); i++)
		{
			rgbVal = Palette.getRGB(i);
			
			R[i] = (short)(Palette.getRed(i));
			G[i] = (short)(Palette.getGreen(i));
			B[i] = (short)(Palette.getBlue(i));
			colourButton[i].setBackground(new Color(rgbVal));
			colourButton[i].setActionCommand(String.valueOf(rgbVal) + "_" + String.valueOf(i));
		}	
		buttIdxSel = 0;
	}
	
	class exportPalette implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			exportPal();
		}
	}
	
	private void exportPal()
	{
/*		byte r[] = new byte[R.length];
		byte g[] = new byte[G.length];
		byte b[] = new byte[B.length];
		
		for(int i = 0; i< R.length; i++)
		{
			r[i] = (byte)R[i];
			g[i] = (byte)G[i];
			b[i] = (byte)B[i];
		}
		
		int ImageDepth = (int)(java.lang.Math.log((double)Palette.getMapSize())/java.lang.Math.log((double)2));
		Palette = new IndexColorModel(ImageDepth,R.length,r,g,b);
		fileChooser.showSaveDialog(this);
		PaletteFileWriter fileOut = new PaletteFileWriter(fileChooser.getSelectedFile(), Palette, palDepth);
		*/	
	}
	
	private class PaletteFileFilter extends FileFilter
	{

		public boolean accept(File f) {
			if (f.isDirectory()) 
		     {
				return true;
		     }
		     
		     String fileName = f.getName();
		     int i = fileName.lastIndexOf('.');
		     
		    String ext = null; 
	        if (i > 0 &&  i < fileName.length() - 1)
	        {
	            ext = fileName.substring(i+1).toLowerCase();
	        }
	        else 
	        	return false;
	        if(ext.matches("pal") == true)
	        	return true;

	        return false;
		}

		public String getDescription() 
		{
			return "Palette File(*.pal)";
		}
	}
}