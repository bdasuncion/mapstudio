package mapCanvas;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import interfaces.MapViewSettings;


public class VisibleLayer extends JPanel implements ItemListener, ActionListener, ChangeListener
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
	private ButtonGroup collisionLayerViewFilter;
	private JRadioButton selectAll;
	private JRadioButton setMinMaxHeight;
	private JSpinner minHeight;
	private JSpinner maxHeight;
	private SpinnerNumberModel minHeightModel;
	private SpinnerNumberModel maxHeightModel;
	private boolean visibleCollision = false;
	private boolean visible0 = false;
	private boolean visible1 = false;
	private boolean visible2 = true;
	private boolean visible3 = true;
	private boolean isChecked = false;
	private boolean collisionFilterSelectAll = true;
	private static final String ALL = "ALL";
	private static final String BYHEIGHT = "BYHEIGHT";
	private static final int MINHEIGHT = 0;
	private static final int MAXHEIGHT = 1024;
	MapViewSettings mapViewSettings;
	
	public VisibleLayer(MapViewSettings mvs) {
		super();
		
		mapViewSettings = mvs;
		
		collisonLayer = new JCheckBox("Collision Layer");
		collisonLayer.setSelected(false);
		
		collisionLayerViewFilter = new ButtonGroup();
		selectAll = new JRadioButton("View All");
		selectAll.setActionCommand(ALL);
		selectAll.addActionListener(this);
		setMinMaxHeight = new JRadioButton("Filter by height");
		minHeightModel = new SpinnerNumberModel(0, MINHEIGHT, MAXHEIGHT, 8);
		maxHeightModel = new SpinnerNumberModel(0, MINHEIGHT, MAXHEIGHT, 8);
		setMinMaxHeight.setActionCommand(BYHEIGHT);
		setMinMaxHeight.addActionListener(this);
		collisionLayerViewFilter.add(selectAll);
		collisionLayerViewFilter.add(setMinMaxHeight);
		selectAll.setEnabled(collisonLayer.isSelected());
		setMinMaxHeight.setEnabled(collisonLayer.isSelected());
		
		minHeight = new JSpinner(minHeightModel);
		maxHeight = new JSpinner(maxHeightModel);
		
		minHeight.setEnabled(setMinMaxHeight.isSelected());
		minHeight.addChangeListener(this);
		maxHeight.setEnabled(setMinMaxHeight.isSelected());
		maxHeight.addChangeListener(this);
		
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
		this.add(selectAll);
		this.add(setMinMaxHeight);
		this.add(minHeight);
		this.add(maxHeight);
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
		 
		 selectAll.setEnabled(collisonLayer.isSelected());
		 setMinMaxHeight.setEnabled(collisonLayer.isSelected());
		 if (collisonLayer.isSelected()) {
			 if (collisionFilterSelectAll) {
				 selectAll.doClick();
			 } else {
				 setMinMaxHeight.doClick();
			 }
		 }
		 
		 
		 isChecked = true;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().contentEquals(ALL)) {
			mapViewSettings.setCollisionFilter(MINHEIGHT, MAXHEIGHT);
			collisionFilterSelectAll = true;
		} else {
			mapViewSettings.setCollisionFilter(minHeightModel.getNumber().intValue(), 
					maxHeightModel.getNumber().intValue());
			collisionFilterSelectAll = false;
		}
		
		minHeight.setEnabled(setMinMaxHeight.isSelected());
		maxHeight.setEnabled(setMinMaxHeight.isSelected());
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		mapViewSettings.setCollisionFilter(minHeightModel.getNumber().intValue(), 
				maxHeightModel.getNumber().intValue());
	}
} 