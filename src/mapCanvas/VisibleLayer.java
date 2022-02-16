package mapCanvas;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interfaces.MapViewSettings;


public class VisibleLayer extends JPanel implements ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -23559982387120572L;
	private JCheckBox collisonLayer;
	private JCheckBox layer0;
	private JCheckBox layer1;
	private JCheckBox layer2;
	private JCheckBox layer3;
	private JCheckBox grid;
	private JCheckBox actorsLayer;
	private JCheckBox eventsLayer;
	private boolean visibleCollision = false;
	private boolean visible0 = false;
	private boolean visible1 = false;
	private boolean visible2 = true;
	private boolean visible3 = true;
	private boolean isChecked = false;
	MapViewSettings mapViewSettings;
	
	public VisibleLayer(MapViewSettings mvs) {
		super();
		
		mapViewSettings = mvs;
		
		collisonLayer = new JCheckBox("Collision Layer");
		collisonLayer.setSelected(false);
		
		layer0 = new JCheckBox("Layer 0");
		layer0.setSelected(false);
		
		layer1 = new JCheckBox("Layer 1");
		layer1.setSelected(false);
		
		layer2 = new JCheckBox("Layer 2");
		layer2.setSelected(true);
		
		layer3 = new JCheckBox("Layer 3");
		layer3.setSelected(true);
		
		grid = new JCheckBox("Grid");
		grid.setSelected(true);
		
		actorsLayer = new JCheckBox("Characters");
		actorsLayer.setSelected(false);
		
		eventsLayer = new JCheckBox("Events");
		eventsLayer.setSelected(false);
		
		collisonLayer.addItemListener(this);
		layer0.addItemListener(this);
		layer1.addItemListener(this);
		layer2.addItemListener(this);
		layer3.addItemListener(this);
		grid.addItemListener(this);
		actorsLayer.addItemListener(this);
		eventsLayer.addItemListener(this);
		
		this.setLayout(new GridLayout(0, 1));
		
		this.add(new JLabel("Select Visible Layers"));
		this.add(collisonLayer);
		this.add(layer0);
		this.add(layer1);
		this.add(layer2);
		this.add(layer3);
		this.add(grid);
		this.add(actorsLayer);
		this.add(eventsLayer);
		
	}

	public void itemStateChanged(ItemEvent e) {
		 if (e.getStateChange() == ItemEvent.DESELECTED) {
			 if(e.getItemSelectable() == collisonLayer) {
				 visibleCollision = false;
				 mapViewSettings.setViewLayerCollision(visibleCollision);
			 } else if(e.getItemSelectable() == layer0)  {
				 mapViewSettings.setViewLayer0(false);
			 } else if(e.getItemSelectable() == layer1) {
				 mapViewSettings.setViewLayer1(false);
			 } else if(e.getItemSelectable() == layer2) {
				 mapViewSettings.setViewLayer2(false);
			 } else if(e.getItemSelectable() == layer3) {
				 mapViewSettings.setViewLayer3(false);
			 } else if (e.getItemSelectable() == grid) {
				 mapViewSettings.setViewGrid(false);
			 } else if (e.getItemSelectable() == actorsLayer) {
				 mapViewSettings.setViewLayerActors(false);
			 } else if (e.getItemSelectable() == eventsLayer) {
				 mapViewSettings.setViewLayerEvents(false);
			 }
		 } else {
			 if(e.getItemSelectable() == collisonLayer)  {
				 mapViewSettings.setViewLayerCollision(true);
			 } else if(e.getItemSelectable() == layer0) {
				 mapViewSettings.setViewLayer0(true);
			 } else if(e.getItemSelectable() == layer1) {
				 mapViewSettings.setViewLayer2(true);
			 } else if(e.getItemSelectable() == layer2) {
				 mapViewSettings.setViewLayer2(true);
			 } else if(e.getItemSelectable() == layer3) {
				 mapViewSettings.setViewLayer3(true);
			 } else if (e.getItemSelectable() == grid) {
				 mapViewSettings.setViewGrid(true);
			 }	else if (e.getItemSelectable() == actorsLayer) {
				 mapViewSettings.setViewLayerActors(true);
			 } else if (e.getItemSelectable() == eventsLayer) {
				 mapViewSettings.setViewLayerEvents(true);
			 }		 
		 }
		 
		 isChecked = true;
	}
} 