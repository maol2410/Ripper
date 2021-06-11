import javax.swing.JPanel;
import java.awt.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;

public class InventoryPanel extends JPanel {

	private JLabel inventory; // Label for Inventory
	private NumberedLabel item1;
	private NumberedLabel item2;
	private NumberedLabel item3;
	private NumberedLabel item4;
	private NumberedLabel item5;
	private NumberedLabel item6;
	private NumberedLabel item7;
	private NumberedLabel item8;
	private NumberedLabel item9;
	private NumberedLabel item10;
	private NumberedLabel item11;
	private NumberedLabel item12;

	public InventoryPanel(MouseAdapter mouseAdapter) {

		initComponents(mouseAdapter);

	}

	private void initComponents(MouseAdapter mouseAdapter) {
		inventory = new JLabel();
		item1 = new NumberedLabel(1);
		item2 = new NumberedLabel(2);
		item3 = new NumberedLabel(3);
		item4 = new NumberedLabel(4);
		item5 = new NumberedLabel(5);
		item6 = new NumberedLabel(6);
		item7 = new NumberedLabel(7);
		item8 = new NumberedLabel(8);
		item9 = new NumberedLabel(9);
		item10 = new NumberedLabel(10);
		item11 = new NumberedLabel(11);
		item12 = new NumberedLabel(12);

		// Setting the text and font of the labels
		Dimension dimension = getPreferredSize();
		dimension.width = 160;
		dimension.height = 22;
		inventory.setPreferredSize(dimension);
		inventory.setFont(new Font("Times New Roman", 1, 18));
		inventory.setText("Inventory:");

		item1.setFont(new Font("Times New Roman", 1, 12));
		item1.setText("1.");
		item1.addMouseListener(mouseAdapter);

		item2.setFont(new Font("Times New Roman", 1, 12));
		item2.setText("2.");
		item2.addMouseListener(mouseAdapter);

		item3.setFont(new Font("Times New Roman", 1, 12));
		item3.setText("3.");
		item3.addMouseListener(mouseAdapter);

		item4.setFont(new Font("Times New Roman", 1, 12));
		item4.setText("4.");
		item4.addMouseListener(mouseAdapter);

		item5.setFont(new Font("Times New Roman", 1, 12));
		item5.setText("5.");
		item5.addMouseListener(mouseAdapter);

		item6.setFont(new Font("Times New Roman", 1, 12));
		item6.setText("6.");
		item6.addMouseListener(mouseAdapter);

		item7.setFont(new Font("Times New Roman", 1, 12));
		item7.setText("7.");
		item7.addMouseListener(mouseAdapter);

		item8.setFont(new Font("Times New Roman", 1, 12));
		item8.setText("8.");
		item8.addMouseListener(mouseAdapter);

		item9.setFont(new Font("Times New Roman", 1, 12));
		item9.setText("9.");
		item9.addMouseListener(mouseAdapter);

		item10.setFont(new Font("Times New Roman", 1, 12));
		item10.setText("10.");
		item10.addMouseListener(mouseAdapter);

		item11.setFont(new Font("Times New Roman", 1, 12));
		item11.setText("11.");
		item11.addMouseListener(mouseAdapter);

		item12.setFont(new Font("Times New Roman", 1, 12));
		item12.setText("12.");
		item12.addMouseListener(mouseAdapter);

		// Setting up panel
		setBackground(Color.gray);
		setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		layout.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		layout.gridx = 0;
		layout.gridy = 0;
		add(inventory, layout);
		layout.gridx = 0;
		layout.gridy = 1;
		add(item1, layout);
		layout.gridx = 0;
		layout.gridy = 2;
		add(item2, layout);
		layout.gridx = 0;
		layout.gridy = 3;
		add(item3, layout);
		layout.gridx = 0;
		layout.gridy = 4;
		add(item4, layout);
		layout.gridx = 0;
		layout.gridy = 5;
		add(item5, layout);
		layout.gridx = 0;
		layout.gridy = 6;
		add(item6, layout);
		layout.gridx = 0;
		layout.gridy = 7;
		add(item7, layout);
		layout.gridx = 0;
		layout.gridy = 8;
		add(item8, layout);
		layout.gridx = 0;
		layout.gridy = 9;
		add(item9, layout);
		layout.gridx = 0;
		layout.gridy = 10;
		add(item10, layout);
		layout.gridx = 0;
		layout.gridy = 11;
		add(item10, layout);
		layout.gridx = 0;
		layout.gridy = 12;
		add(item12, layout);

	}

	public void inventory(String[] values) {
		item1.setText("1." + values[0]);
		item2.setText("2." + values[1]);
		item3.setText("3." + values[2]);
		item4.setText("4." + values[3]);
		item5.setText("5." + values[4]);
		item6.setText("6." + values[5]);
		item7.setText("7." + values[6]);
		item8.setText("8." + values[7]);
		item9.setText("9." + values[8]);
		item10.setText("10." + values[9]);
		item11.setText("11." + values[10]);
		item12.setText("12." + values[11]);

	}

}
