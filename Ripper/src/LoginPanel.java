import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Color;

public class LoginPanel extends JPanel {

	private JTextArea textArea;
	private JButton logiButton;

	public LoginPanel() {

		initComponents();
	}

	private void initComponents() {

		textArea = new JTextArea();
		logiButton = new JButton("Please Enter Name to Login");

		textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		textArea.setAlignmentY(Component.CENTER_ALIGNMENT);

		logiButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		logiButton.setAlignmentY(Component.TOP_ALIGNMENT);
		setBackground(Color.gray);

		setLayout(new GridLayout(2, 1));
		add(textArea);
		add(logiButton);
	}

	public String playerName() {
		if (textArea.getText() != null) {

			return textArea.getText();
		}
		return "";
	}

	public boolean button() {

		return logiButton.getModel().isPressed();

	}

}
