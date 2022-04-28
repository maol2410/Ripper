
public class Armor extends Item {
	private ArmorType type;
	private int armor;
	private double damageMultiplier;

	public Armor(String name, byte objectType, ArmorType type, int armor) {
		super(name, objectType);
		this.type = type;
		this.armor = armor;
		if (type == ArmorType.HEAVY)
			this.damageMultiplier = 1.0;
		else if (type == ArmorType.MEDIUM)
			this.damageMultiplier = 1.5;
		else if (type == ArmorType.LIGHT)
			this.damageMultiplier = 2.0;
	}

	public ArmorType type() {
		return type;
	}

	public int getArmor() {
		return armor;
	}

	public double getDamageMultiplier() {
		return damageMultiplier;
	}
}
