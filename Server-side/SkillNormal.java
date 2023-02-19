
public class SkillNormal extends Skill {

	public SkillNormal() {
		super("Normal", 10, 10, true);
		blockChance = 25;
		level = 1;
	}

	@Override
	void useEffect(Player player, Monster monster) {
		int dmg = player.damage;
		monster.hitMe(dmg, 1.0, player);
	}
}
