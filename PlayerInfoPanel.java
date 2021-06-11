import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {

	private JLabel player; // Label for players name.
	private JProgressBar playerHp; // Progress bar for players Hp
	private JLabel monster; // Label for Monsters name
	private JProgressBar monsterHp; // Progress bar for Monsters name.
	private JLabel ally; // Label for ally
	private JProgressBar allyHp; // Progressbar for allies Hp
	private JLabel induction;
	private JProgressBar inductionBar;

	public PlayerInfoPanel() {

		initComponents();

	}

	private void initComponents() {

		// initializing components
		UIManager.put("ProgressBar.selectionForeground", Color.black);
		player = new JLabel();
		playerHp = new JProgressBar();

		monster = new JLabel();
		monsterHp = new JProgressBar();

		ally = new JLabel();
		allyHp = new JProgressBar();

		induction = new JLabel();
		inductionBar = new JProgressBar();

		// Setting font of the labels
		player.setFont(new Font("Times New Roman", 1, 13));

		monster.setFont(new Font("Times New Roman", 1, 13));

		ally.setFont(new Font("Times New Roman", 1, 13));
		ally.setText("Ally: ");

		induction.setFont(new Font("Times New Roman", 1, 13));
		induction.setText("Induction Bar: ");

		// Setting predSize of the JprogressBars
		Dimension prefSize = getPreferredSize();
		prefSize.width = 120;
		prefSize.height = 20;
		// Painting the progressbar and adding the life
		playerHp.setPreferredSize(new Dimension(prefSize));
		playerHp.setStringPainted(true);
		playerHp.setVisible(true);

		monsterHp.setStringPainted(true);
		monsterHp.setPreferredSize(new Dimension(prefSize));
		monsterHp.setVisible(false);

		allyHp.setStringPainted(true);
		allyHp.setPreferredSize(new Dimension(prefSize));

		inductionBar.setStringPainted(true);
		inductionBar.setPreferredSize(prefSize);

		// Adding the components to the left panel
		setBackground(Color.gray);
		setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		layout.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		layout.gridx = 0;
		layout.gridy = 0;
		add(player, layout);
		layout.gridx = 0;
		layout.gridy = 1;
		add(playerHp, layout);
		layout.gridx = 0;
		layout.gridy = 2;
		add(monster, layout);
		layout.gridx = 0;
		layout.gridy = 3;
		add(monsterHp, layout);
		layout.gridx = 0;
		layout.gridy = 4;
		add(ally, layout);
		layout.gridx = 0;
		layout.gridy = 5;
		add(allyHp, layout);
		layout.gridx = 0;
		layout.gridy = 6;
		add(induction, layout);
		layout.gridx = 0;
		layout.gridy = 7;
		add(inductionBar, layout);

	}

	public void monster(String monsterName, int monsterValue, int monsterMax) {
		if (monsterName != null) {
			monster.setText(monsterName + ":");
			monsterHp.setValue((monsterValue * 100) / monsterMax);
			monsterHp.setString(monsterValue + "/" + monsterMax);
			monsterHp.setVisible(true);
		} else {
			monster.setText("");
			monsterHp.setVisible(false);
		}
	}

	public void playerName(String playerName) {
		player.setText(playerName + ":");
	}

	public void playerHp(int playerValue) {
		playerHp.setValue(playerValue / 10);
		playerHp.setString(playerValue + "/1000");
	}

	public void allyHp(int allyValue) {

		allyHp.setValue(allyValue / 10);
		allyHp.setString(allyValue + "/1000");

	}

	public void inductionBar(int inductionValue) {
		inductionBar.setValue(inductionValue);
	}
}
