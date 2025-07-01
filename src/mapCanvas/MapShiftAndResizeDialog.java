package mapCanvas;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import interfaces.MapControls;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MapShiftAndResizeDialog extends JDialog {
	private JTextField widthField;
	private JTextField heightField;
	
	public MapShiftAndResizeDialog(MapControls mc, JFrame owner) {
		super(owner, true);
		setResizable(false);
		JLabel widthLabel = new JLabel("width");
		
		widthField = new JTextField();
		widthField.setColumns(10);
		
		JLabel heightLabel = new JLabel("height");
		
		heightField = new JTextField();
		heightField.setColumns(10);
		
		JButton upButton = new JButton("UP");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.shiftUp();
			}
		});
		
		JButton leftButton = new JButton("LEFT");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.shiftLeft();
			}
		});
		
		JButton rightButton = new JButton("RIGHT");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.shiftRight();
			}
		});
		
		JButton downButton = new JButton("DOWN");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.shiftDown();
			}
		});
		
		JButton applyButton = new JButton("APPLY");
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.resize(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(heightLabel, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(heightField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(widthLabel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(widthField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(leftButton)
							.addGap(67)
							.addComponent(rightButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(61)
							.addComponent(downButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(69)
							.addComponent(upButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(31)
							.addComponent(applyButton, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(46, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(widthLabel)
						.addComponent(widthField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(heightLabel)
						.addComponent(heightField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(applyButton)
					.addGap(14)
					.addComponent(upButton)
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(leftButton)
						.addComponent(rightButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(downButton)
					.addGap(17))
		);
		getContentPane().setLayout(groupLayout);
		
		this.pack();
	}
}
