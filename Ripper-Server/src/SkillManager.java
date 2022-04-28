import java.util.EnumMap;
import java.util.Set;

public class SkillManager {
	private Player player;
	private Skill activeSkill;
	private Skill stance;
	EnumMap<SkillType, Skill> skillMap;
	Set<SkillType> keys;

	public SkillManager(Player player) {
		this.player = player;
		skillMap = new EnumMap<SkillType, Skill>(SkillType.class);
		skillMap.put(SkillType.AGGRESSIVE, new SkillAggressive());
		skillMap.put(SkillType.DEFENSIVE, new SkillDefensive());
		skillMap.put(SkillType.NORMAL, new SkillNormal());
		skillMap.put(SkillType.PLAY_NICE, new SkillPlayNice());
		skillMap.put(SkillType.POTION, new SkillPotion());
		skillMap.put(SkillType.PROTECTOR, new SkillProtector());
		skillMap.put(SkillType.SWORD_MASTER, new SkillSwordMaster());
		skillMap.put(SkillType.TAUNT, new SkillTaunt());
		keys = skillMap.keySet();
		activeSkill = null;
		stance = skillMap.get(SkillType.NORMAL);
	}

	public void useSkill(SkillType skillType) {
		Skill skill = skillMap.get(skillType);
		if (skill.isStance())
			stance = skill;
		activeSkill = skill;
	}

	private void activeSkillTick(Monster target) {
		if (activeSkill != null && activeSkill.activeTick()) {
			activeSkill.use(player, target);
			if (!activeSkill.isStance())
				activeSkill = stance;
		}
	}

	private void checkActiveSkill(Monster target) {
		if (target == null) {
			if (!(activeSkill instanceof SkillPotion))
				activeSkill = null;
		} else if (activeSkill == null)
			activeSkill = stance;
	}

	public void tick(Monster target) {
		checkActiveSkill(target);
		for (SkillType key : keys) {
			Skill skill = skillMap.get(key);
			if (skill != activeSkill)
				skill.idleTick();
		}
		activeSkillTick(target);
	}

	public int getArmor() {
		if (activeSkill != null)
			return activeSkill.getArmor();
		return 0;
	}

	public int getBlockChance() {
		if (activeSkill != null)
			return activeSkill.getBlockChance();
		return 0;
	}

	public double getBlockMultiplier() {
		if (activeSkill != null)
			return activeSkill.getBlockMultiplier();
		return 1.0;
	}

	public boolean levelUp(int skillChoice) {
		if (skillChoice == CONST.ANSWER_TAUNT)
			((SkillTaunt) skillMap.get(SkillType.TAUNT)).levelUp();
		else if (skillChoice == CONST.ANSWER_PROTECTOR)
			((SkillProtector) skillMap.get(SkillType.PROTECTOR)).levelUp();
		else if (skillChoice == CONST.ANSWER_SWORD_MASTER)
			((SkillSwordMaster) skillMap.get(SkillType.SWORD_MASTER)).levelUp();
		else if (skillChoice == CONST.ANSWER_PLAY_NICE)
			((SkillPlayNice) skillMap.get(SkillType.PLAY_NICE)).levelUp();
		else
			return false;
		return true;
	}

	public int getInduction() {
		if (activeSkill == null)
			return 0;
		return activeSkill.induction;
	}

	public int getInductionMax() {
		if (activeSkill == null)
			return 0;
		return activeSkill.INDUCTION_MAX;
	}
}
