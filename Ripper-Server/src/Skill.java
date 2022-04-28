
public abstract class Skill {
	private String name;
	protected int level;
	protected final int COOL_DOWN_MAX;
	protected int coolDown;
	protected final int INDUCTION_MAX;
	protected int induction;
	private boolean isStance;
	protected int blockChance;
	protected double blockMultiplier;
	protected int armor;

	public Skill(String name, int coolDown, int induction, boolean isStance) {
		this.name = name;
		level = 1;
		COOL_DOWN_MAX = coolDown;
		this.coolDown = 0;
		INDUCTION_MAX = induction;
		this.induction = induction;
		this.isStance = isStance;
		blockChance = 0;
		blockMultiplier = 1.0;
		armor = 0;
	}

	public void idleTick() {
		if (coolDown > 0)
			coolDown--;
		induction = INDUCTION_MAX;
	}

	public boolean activeTick() {
		if (coolDown > 0) {
			coolDown--;
			return false;
		}
		if (induction > 0) {
			induction--;
			return false;
		}
		return true;
	}

	abstract void useEffect(Player player, Monster monster);

	public void use(Player player, Monster monster) {
		useEffect(player, monster);
		coolDown = COOL_DOWN_MAX;
		if (!isStance)
			induction = INDUCTION_MAX;
	}

	public int getBlockChance() {
		if (isStance && induction == 0)
			return blockChance;
		return 0;
	}

	public double getBlockMultiplier() {
		if (isStance && induction == 0)
			return blockMultiplier;
		return 1.0;
	}

	public int getArmor() {
		if (isStance && induction == 0)
			return armor;
		return 0;
	}

	public boolean isStance() {
		return isStance;
	}
}
