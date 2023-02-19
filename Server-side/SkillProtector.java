
public class SkillProtector extends Skill {
	private double BASE_blockMultiplier;
	private int BASE_armor;
	private double BASE_threatMultiplier;
	private double threatMultiplier;
	private double damageMultiplier;

	public SkillProtector() {
		super("Protector", 10, 10, true);
		blockChance = 20;
		BASE_blockMultiplier = 0.3;
		BASE_armor = 50;
		BASE_threatMultiplier = 1.0;
		damageMultiplier = 0.5;
		level = 0;
		levelUp();
	}

	public void levelUp() {
		level++;
		blockMultiplier = 1.0 + BASE_blockMultiplier * level;
		armor = BASE_armor * level;
		threatMultiplier = BASE_threatMultiplier * level;
	}

	@Override
	void useEffect(Player player, Monster monster) {
		int dmg = (int) (player.damage * damageMultiplier);
		monster.hitMe(dmg, threatMultiplier, player);
	}
}
