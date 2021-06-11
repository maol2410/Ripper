import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class GameOverPanel extends JPanel {

	private JLabel result;
	private JLabel stat1;
	private JLabel stat2;
	private JLabel stat3;
	private JLabel stat4;
	private JLabel stat5;
	private JLabel stat6;
	private JLabel stat7;
	private JLabel stat8;
	private JLabel stat9;
	private JLabel stat10;

	public GameOverPanel(String[] stats) {

		initgameOver(stats);
	}

	private void initgameOver(String[] stats) {

		result = new JLabel();
		stat1 = new JLabel();
		stat2 = new JLabel();
		stat3 = new JLabel();
		stat4 = new JLabel();
		stat5 = new JLabel();
		stat6 = new JLabel();
		stat7 = new JLabel();
		stat8 = new JLabel();
		stat9 = new JLabel();
		stat10 = new JLabel();

		setBackground(Color.BLACK);

		result.setFont(new Font("Times New Roman", 1, 50));
		result.setForeground(Color.GREEN);
		stat1.setFont(new Font("Times New Roman", 1, 15));
		stat1.setForeground(Color.GREEN);
		stat2.setFont(new Font("Times New Roman", 1, 15));
		stat2.setForeground(Color.GREEN);
		stat3.setFont(new Font("Times New Roman", 1, 15));
		stat3.setForeground(Color.GREEN);
		stat4.setFont(new Font("Times New Roman", 1, 15));
		stat4.setForeground(Color.GREEN);
		stat5.setFont(new Font("Times New Roman", 1, 15));
		stat5.setForeground(Color.GREEN);
		stat6.setFont(new Font("Times New Roman", 1, 15));
		stat6.setForeground(Color.GREEN);
		stat7.setFont(new Font("Times New Roman", 1, 15));
		stat7.setForeground(Color.GREEN);
		stat8.setFont(new Font("Times New Roman", 1, 15));
		stat8.setForeground(Color.GREEN);
		stat9.setFont(new Font("Times New Roman", 1, 15));
		stat9.setForeground(Color.GREEN);
		stat10.setFont(new Font("Times New Roman", 1, 15));
		stat10.setForeground(Color.GREEN);

		result.setText(stats[0]);
		stat1.setText(stats[1]);
		stat2.setText(stats[2]);
		stat3.setText(stats[3]);
		stat4.setText(stats[4]);
		stat5.setText(stats[5]);
		stat6.setText(stats[6]);
		stat7.setText(stats[7]);
		stat8.setText(stats[8]);
		stat9.setText(stats[9]);
		stat10.setText(stats[10]);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[] { 100, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 100 };
		setLayout(gridBagLayout);
		GridBagConstraints layout = new GridBagConstraints();
		layout.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		layout.gridx = 0;
		layout.gridy = 0;
		add(result, layout);
		layout.gridx = 0;
		layout.gridy = 1;
		add(stat1, layout);
		layout.gridx = 0;
		layout.gridy = 2;
		add(stat2, layout);
		layout.gridx = 0;
		layout.gridy = 3;
		add(stat3, layout);
		layout.gridx = 0;
		layout.gridy = 4;
		add(stat4, layout);
		layout.gridx = 0;
		layout.gridy = 5;
		add(stat5, layout);
		layout.gridx = 0;
		layout.gridy = 6;
		add(stat6, layout);
		layout.gridx = 0;
		layout.gridy = 7;
		add(stat7, layout);
		layout.gridx = 0;
		layout.gridy = 8;
		add(stat8, layout);
		layout.gridx = 0;
		layout.gridy = 9;
		add(stat9, layout);
		layout.gridx = 0;
		layout.gridy = 10;
		add(stat10, layout);
	}
}
