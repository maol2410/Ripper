
public abstract class Creature extends TileObject {
	private final int hp_MAX;
	private int hp;
	private int armor;
	private double armorFactor;
	private int blockChance;
	private final int damage_MIN;
	protected int damage;
	private boolean isAlive;

	public Creature(String name, byte objectType, int hp, int armor, int blockChance, int damage) {
		super(name, objectType);
		this.hp_MAX = hp;
		this.hp = hp;
		setArmor(armor);
		this.blockChance = blockChance;
		this.damage_MIN = damage;
		this.damage = damage;
		isAlive = true;
	}

	abstract void defeated();

	protected void setArmor(int armor) {
		this.armor = armor;
		armorFactor = Math.pow(2.0, (-armor / 100.0));
	}

	protected void addHP(int change) {
		hp += change;
		if (hp <= 0) {
			hp = 0;
			isAlive = false;
			defeated();
		} else if (hp > hp_MAX)
			hp = hp_MAX;
	}

	protected void setDamage(int dmg) {
		if (dmg < damage_MIN)
			damage = damage_MIN;
		else
			damage = dmg;
	}

	protected void setBlockChance(int blockChance) {
		if (blockChance > 99)
			this.blockChance = 99;
		else
			this.blockChance = blockChance;
	}

	private int damageMe(int dmg) {
		int finalDmg = (int) (dmg * armorFactor + 0.5);
		addHP(-finalDmg);
		return finalDmg;
	}

	protected int hitMe(int dmg) {
		if (HelpFunctions.nextInt() > blockChance)
			return damageMe(dmg);
		return 0;
	}

	public int getHP() {
		return hp;
	}

	public int getMaxHP() {
		return hp_MAX;
	}

	public boolean isAlive() {
		return isAlive;
	}
}
