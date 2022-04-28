
public class SkillPlayNice extends Skill {
	private int BASE_negativeThreat;
	private int negativeThreat;

	public SkillPlayNice() {
		super("Play-Nice", 0, 10, false);
		BASE_negativeThreat = 250;
		level = 0;
		levelUp();
	}

	public void levelUp() {
		level++;
		negativeThreat = BASE_negativeThreat * level;
	}

	@Override
	void useEffect(Player player, Monster monster) {
		monster.addThreat(-negativeThreat, player);
	}
}
