
public class SkillTaunt extends Skill {
	private int BASE_threat;
	private int threat;

	public SkillTaunt() {
		super("Taunt", 0, 10, false);
		BASE_threat = 1000;
		level = 0;
		levelUp();
	}

	public void levelUp() {
		level++;
		threat = BASE_threat * level;
	}

	@Override
	void useEffect(Player player, Monster monster) {
		monster.addThreat(threat, player);
	}
}
