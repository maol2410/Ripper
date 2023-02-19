
public class Shield extends Item {
	private int armor;
	private int blockChance;

	public Shield(String name, int armor, int blockChance) {
		super(name, CONST.ITEM_SHIELD);
		this.armor = armor;
		this.blockChance = blockChance;
	}

	public int getArmor() {
		return armor;
	}

	public int getBlockChance() {
		return blockChance;
	}
}
