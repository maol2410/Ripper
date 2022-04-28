
public class Player extends Creature {
	private final int WALK_CD = 3;
	private int walkCD;
	private Direction goTo;
	protected Monster target;
	private SkillManager skills;
	private Inventory inventory;
	private Door usedDoor;

	public Player(String name, byte objectType) {
		super(name, objectType, 1000, 0, 0, 10);
		walkCD = 0;
		goTo = Direction.NONE;
		target = null;
		skills = new SkillManager(this);
		inventory = new Inventory();
		usedDoor = null;
	}

	private void move() {
		location.move(goTo);
		target = null;
		walkCD = WALK_CD;
	}

	public void hitMe(int dmg, Monster monster) {
		if (target == null)
			target = monster;
		super.hitMe(dmg);
	}

	public void tick() {
		if (location == null)
			return;
		if (target != null && !location.isClose(target))
			target = null;
		tickWalk();
		skills.tick(target);
		updateStats();
	}

	private void tickWalk() {
		if (walkCD > 0)
			walkCD--;
		if (goTo != Direction.NONE && walkCD == 0) {
			if (location.hasPath(goTo)) {
				TileObject something = location.getObject(goTo);
				if (something == null)
					move();
				else if (something instanceof Item) {
					inventory.pickItem((Item) something);
					move();
				} else if (something instanceof Monster)
					target = (Monster) something;
				else if (something instanceof Door) {
					if (((Door) something).isOpen()) {
						usedDoor = (Door) something;
						location.setTileObject(null);
					}
				}
			}
			goTo = Direction.NONE;
		}
	}

	public void setDirection(Direction goTo) {
		this.goTo = goTo;
	}

	public void useSkill(SkillType skillType) {
		skills.useSkill(skillType);
	}

	public void useItem(int index) {
		inventory.toggleItem(index);
	}

	public void dropItem(int index) {
		inventory.dropItem(index, location);
	}

	public boolean levelUp(int skillChoice) {
		return skills.levelUp(skillChoice);
	}

	protected boolean usePotion() {
		return inventory.usePotion();
	}

	private void updateStats() {
		int armor = inventory.getArmor() + skills.getArmor();
		setArmor(armor);

		int dmg = inventory.getDamage();
		setDamage(dmg);

		double hitChanceInventory = (100 - inventory.getBlockChance()) / 100.0;
		double hitChanceSkills = (100 - skills.getBlockChance()) / 100.0;
		double hitChance = hitChanceInventory * hitChanceSkills;
		double blockMultiplier = skills.getBlockMultiplier();
		hitChance = Math.pow(hitChance, blockMultiplier);
		int blockChance = 100 - (int) (hitChance * 100);
		setBlockChance(blockChance);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInduction() {
		return skills.getInduction();
	}

	public int getInductionMax() {
		return skills.getInductionMax();
	}

	public Monster getTarget() {
		return target;
	}

	public boolean inventoryHasChanged() {
		return inventory.hasChanged();
	}

	public String[] getInventory() {
		return inventory.getInventory();
	}

	public Door usedDoor() {
		return usedDoor;
	}

	public void newRoom() {
		usedDoor = null;
		addHP(1000);
	}

	@Override
	void defeated() {
		location.setTileObject(null);
	}
}
