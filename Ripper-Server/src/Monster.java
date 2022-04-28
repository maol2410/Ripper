import java.util.ArrayList;
import java.util.Collections;

public class Monster extends Creature {
	private int selfHeal;
	private boolean isAggressive;
	private ArrayList<Threat> enemies;
	private ArrayList<MonsterSkill> skills;
	private Item loot;

	public Monster(String name, byte objectType, int hp, int armor, int blockChance, int damage, int selfHeal,
			boolean isAggressive) {
		super(name, objectType, hp, armor, blockChance, damage);
		this.selfHeal = selfHeal;
		this.isAggressive = isAggressive;
		enemies = new ArrayList<Threat>();
		skills = new ArrayList<MonsterSkill>();
		loot = null;
	}

	public int hitMe(int dmg, double threatMultiplier, Player player) {
		int threat = (int) (dmg * threatMultiplier);
		addThreat(threat, player);
		return super.hitMe(dmg);
	}

	public void addThreat(int threat, Player player) {
		for (Threat enemy : enemies) {
			if (enemy.isEnemy(player)) {
				enemy.addThreat(threat);
				return;
			}
		}
		Threat newThreat = new Threat(player);
		newThreat.addThreat(threat);
		enemies.add(newThreat);
	}

	public void tick() {
		addHP(selfHeal);
		if (enemies.size() > 0) {
			for (Threat enemy : enemies) {
				enemy.tick();
			}
			Collections.sort(enemies, Collections.reverseOrder());
			for (int i = 0; i < enemies.size(); i++) {
				Threat threat = enemies.get(i);
				Player player = threat.getPlayer();
				if (fightPlayer(player)) {
					return;
				} else if (threat.isZero())
					enemies.remove(i--);
			}
		}
	}

	private boolean fightPlayer(Player player) {
		if (!location.isClose(player))
			return false;

		boolean oneSkillAtATime = true;
		for (MonsterSkill skill : skills) {
			if (skill.tick() && oneSkillAtATime) {
				skill.use(player, this);
				oneSkillAtATime = false;
			}
		}
		return true;
	}

	public void poke(Player enemy) {
		if (isAggressive)
			addThreat(0, enemy);
	}

	public void insertLoot(Item loot) {
		this.loot = loot;
	}

	public void addSkill(MonsterSkill skill) {
		skills.add(skill);
	}

	@Override
	void defeated() {
		location.setTileObject(loot);
	}
}
