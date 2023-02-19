
public class Potion extends Item {
	private int howMany;

	public Potion() {
		super("Potion", CONST.ITEM_POTION);
		howMany = 1;
	}

	public void add() {
		howMany++;
	}

	public boolean subtract() {
		if (howMany > 0) {
			howMany--;
			return true;
		}
		return false;
	}

	public int howMany() {
		return howMany;
	}

	public String getName() {
		return super.getName() + " (" + howMany + ")";
	}
}
