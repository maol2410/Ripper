
public class SkillSwordMaster extends Skill {
	private double BASE_damageMultiplier;
	private double damageMultiplier;
	private double BASE_threatMultiplier;
	private double threatMultiplier;
	private int BASE_additionalDamage;
	private int additionalDamage;

	public SkillSwordMaster() {
		super("Sword-Master", 0, 10, false);
		BASE_damageMultiplier = 1.1;
		BASE_threatMultiplier = 0.9;
		BASE_additionalDamage = 100;
		level = 0;
		levelUp();
	}

	public void levelUp() {
		level++;
		damageMultiplier = Math.pow(BASE_damageMultiplier, level);
		threatMultiplier = Math.pow(BASE_threatMultiplier, level);
		additionalDamage = BASE_additionalDamage * level;
	}

	@Override
	protected void useEffect(Player player, Monster monster) {
		int dmg = (int) (player.damage * damageMultiplier) + additionalDamage;
		monster.hitMe(dmg, threatMultiplier, player);
	}
}
