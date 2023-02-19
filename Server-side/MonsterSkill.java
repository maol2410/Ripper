
public class MonsterSkill {
	private String name;
	private final int COOL_DOWN_MAX;
	private int coolDown;
	private double damageMultiplier;
	private int healUp;

	public MonsterSkill(String name, int coolDown, double damageMultiplier, int healUp) {
		this.name = name;
		this.COOL_DOWN_MAX = coolDown;
		this.coolDown = coolDown;
		this.damageMultiplier = damageMultiplier;
		this.healUp = healUp;
	}

	public boolean tick() {
		if (coolDown > 0)
			coolDown--;
		return coolDown == 0;
	}

	public void use(Player player, Monster monster) {
		attack(player, monster);
		heal(monster);
		coolDown = COOL_DOWN_MAX;
	}

	private void attack(Player player, Monster monster) {
		if (damageMultiplier != 0.0) {
			int dmg = (int) (monster.damage * damageMultiplier);
			player.hitMe(dmg, monster);
		}
	}

	private void heal(Monster monster) {
		if (healUp != 0)
			monster.addHP(healUp);
	}
}
