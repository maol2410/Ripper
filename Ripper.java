import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ripper extends JFrame implements KeyListener {

	private JPanel framePanel; // The main panel for the whole frame
	private JPanel left;
	private JPanel centerPanel;
	private JPanel rightPanel;
	private LoginPanel loginPanel;
	private GamePanel gamePanel; // Panel for gamePanel.
	private PlayerInfoPanel playerInfoPanel;
	private InventoryPanel inventoryPanel;
	private GameOverPanel gameOverPanel;
	private Protocol protocol;
	private String playerName;

	public Ripper(Protocol protocol) {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.protocol = protocol;
		initLoginComponents();

	}

	public void run() {
		initGameComponents();
		protocol.sendStartGame(playerName);
		System.out.println(playerName);
	}

	private void initLoginComponents() {
		loginPanel = new LoginPanel();
		add(loginPanel);
		while (!(loginPanel.button())) {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(300, 300);
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
		}
		if (loginPanel.playerName().equals("")) {
			JOptionPane.showMessageDialog(this, "You have not choosen a playerName", "Error",
					JOptionPane.WARNING_MESSAGE);
			getContentPane().removeAll();
			initLoginComponents();
		} else 
			getContentPane().removeAll();
	}

	private void initGameComponents() {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				NumberedLabel label = (NumberedLabel) e.getSource();
				inventoryEvent(label.getNumber(), e.getButton());
			}
		};
		framePanel = new JPanel();
		left = new JPanel();
		centerPanel = new JPanel();
		rightPanel = new JPanel();
		gamePanel = new GamePanel();
		playerInfoPanel = new PlayerInfoPanel();
		playerName = loginPanel.playerName();
		playerInfoPanel.playerName(playerName);
		inventoryPanel = new InventoryPanel(mouseAdapter);

		// Left Panel
		left.setPreferredSize(new Dimension(180, 100));
		left.setBackground(Color.gray);
		left.add(playerInfoPanel);
		left.setBorder(BorderFactory.createTitledBorder("LEFT"));

		// Center panel
		gamePanel.setPreferredSize(new Dimension(500, 510));
		centerPanel.setBackground(Color.gray);
		centerPanel.add(gamePanel);
		centerPanel.setBorder(BorderFactory.createTitledBorder("CENTER"));

		// right panel
		rightPanel.setPreferredSize(new Dimension(180, 100));
		rightPanel.setBackground(Color.gray);
		rightPanel.add(inventoryPanel);
		rightPanel.setBorder(BorderFactory.createTitledBorder("RIGHT"));

		// adding to main Panel
		framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.X_AXIS));
		framePanel.add(left);
		framePanel.add(centerPanel);
		framePanel.add(rightPanel);

		// Creating frame
		add(framePanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880, 570);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

	private void inventoryEvent(int number, int mousebutton) {

		if (mousebutton == 1) { // Use Item

			protocol.sendInventoryItem((byte) number, CONST.USEITEM_USE);
		} else if (mousebutton == 3) { // Drop Item

			protocol.sendInventoryItem((byte) number, CONST.USEITEM_DROP);
		}
	}

	public void readFloor(byte[] bytes) {

		gamePanel.readFloor(bytes);

	}

	public void readWalls(byte[] bytes) {

		gamePanel.readWalls(bytes);
	}

	public void waitingForPlayers() {
		gamePanel.waitingForPlayers();
	}

	public void readCombatInfo(int playerHp, int inductionBar, int allyHp, int monsterHp, int monsterHpMax,
			String monsterName) {
		playerInfoPanel.playerHp(playerHp);
		playerInfoPanel.inductionBar(inductionBar);
		playerInfoPanel.allyHp(allyHp);
		playerInfoPanel.monster(monsterName, monsterHp, monsterHpMax);
	}

	public void inventory(String[] values) {
		inventoryPanel.inventory(values);

	}

	public void gameOver(String[] values) {
		gameOverPanel = new GameOverPanel(values);
		getContentPane().removeAll();
		setPreferredSize(new Dimension(500, 510));
		add(gameOverPanel);
		setVisible(true);

	}

	public void requestSkillChoice() {
		Object[] skill = { "Taunt", "Protector", "Sword-Master", "Play-Nice" };
		String s = (String) JOptionPane.showInputDialog(this, "Please choose the skill to level up", "Skill Choice",
				JOptionPane.PLAIN_MESSAGE, null, skill, "Taunt");

		byte skillByte = CONST.ANSWER_TAUNT;

		switch (s) {
			case "Taunt":
				skillByte = CONST.ANSWER_TAUNT;
				break;
			case "Protector":
				skillByte = CONST.ANSWER_PROTECTOR;
				break;
			case "Sword-Master":
				skillByte = CONST.ANSWER_SWORD_MASTER;
				break;
			case "Play-Nice":
				skillByte = CONST.ANSWER_PLAY_NICE;
				break;
			default:
				break;
		}
		protocol.sendSkillAnswer(skillByte);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
			case KeyEvent.VK_RIGHT:
				protocol.sendDirectionChoice(CONST.DIRECTION_RIGHT);
				break;
			case KeyEvent.VK_LEFT:
				protocol.sendDirectionChoice(CONST.DIRECTION_LEFT);
				break;
			case KeyEvent.VK_UP:
				protocol.sendDirectionChoice(CONST.DIRECTION_UP);
				break;
			case KeyEvent.VK_DOWN:
				protocol.sendDirectionChoice(CONST.DIRECTION_DOWN);
				break;
			case KeyEvent.VK_R:
				protocol.sendSkillChoice(CONST.SKILL_USE_HEALTH_POTIOM); // Health potion
				break;
			case KeyEvent.VK_1:
				protocol.sendSkillChoice(CONST.SKILL_USE_TAUNT); // Taunt
				break;
			case KeyEvent.VK_2:
				protocol.sendSkillChoice(CONST.SKILL_USE_PROTECTOR); // Protector
				break;
			case KeyEvent.VK_A:
				protocol.sendSkillChoice(CONST.SKILL_USE_SWORD_MASTER);// Sword-Master
				break;
			case KeyEvent.VK_S:
				protocol.sendSkillChoice(CONST.SKILL_USE_PLAY_NICE);// Play-Nice
				break;
			case KeyEvent.VK_Q:
				protocol.sendSkillChoice(CONST.SKILL_USE_AGGRESSIVE_ATTACK);// Aggressive Attack
				break;
			case KeyEvent.VK_W:
				protocol.sendSkillChoice(CONST.SKILL_USE_NORMAL_ATTACK);// Normal Attack
				break;
			case KeyEvent.VK_E:
				protocol.sendSkillChoice(CONST.SKILL_USE_DEFENSIVE_ATTACK);// Defensive Attack
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
