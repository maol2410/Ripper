
public class Weapon extends Item {
	private int damage;
	private boolean oneHanded;

	public Weapon(String name, byte objectType, int damage, boolean oneHanded) {
		super(name, objectType);
		this.damage = damage;
		this.oneHanded = oneHanded;
	}

	public int getDamage() {
		return damage;
	}

	public boolean isOneHanded() {
		return oneHanded;
	}
}
