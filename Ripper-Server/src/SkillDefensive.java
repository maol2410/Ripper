
public class SkillDefensive extends Skill {
	private double damageMultiplier;

	public SkillDefensive() {
		super("Defensive", 10, 10, true);
		blockChance = 50;
		armor = 100;
		damageMultiplier = 0.5;
		level = 1;
	}

	@Override
	void useEffect(Player player, Monster monster) {
		int dmg = (int) (player.damage * damageMultiplier);
		monster.hitMe(dmg, 1.0, player);
	}
}
