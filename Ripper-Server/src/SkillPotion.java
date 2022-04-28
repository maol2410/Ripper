
public class SkillPotion extends Skill {

	public SkillPotion() {
		super("Potion", 10, 0, false);
	}

	@Override
	void useEffect(Player player, Monster monster) {
		if (player.usePotion())
			player.addHP(500);
	}
}
