
public class SkillAggressive extends Skill {
	private double damageMultiplier;

	public SkillAggressive() {
		super("Aggressive", 10, 10, true);
		damageMultiplier = 2.0;
		level = 1;
	}

	@Override
	void useEffect(Player player, Monster monster) {
		int dmg = (int) (player.damage * damageMultiplier);
		monster.hitMe(dmg, 1.0, player);
	}
}
