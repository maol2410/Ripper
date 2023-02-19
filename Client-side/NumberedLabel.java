import javax.swing.JLabel;

public class NumberedLabel extends JLabel {

	private int nrLabel;

	public NumberedLabel(int nrLabel) {
		super();
		this.nrLabel = nrLabel;
	}

	public int getNumber() {
		return nrLabel;
	}
}
