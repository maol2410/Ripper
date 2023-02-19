
public class Inventory {
	private Item[] inventory;
	private Weapon weapon;
	private Shield shield;
	private Armor armor;
	private Potion potions;
	private boolean hasChanged;

	public Inventory() {
		inventory = new Item[12];
		potions = new Potion();
		inventory[0] = potions;
		hasChanged = true;
	}

	private void pickItem2NewSlot(Item item) {
		for (int i = 1; i < inventory.length; i++) {
			if (inventory[i] == null) {
				inventory[i] = item;
				item.location.setTileObject(null);
				hasChanged = true;
				return;
			}
		}
	}

	public void pickItem(Item item) {
		if (item instanceof Potion) {
			potions.add();
			hasChanged = true;
			item.location.setTileObject(null);
		} else {
			pickItem2NewSlot(item);
		}
	}

	private boolean validIndex(int index) {
		return 0 <= index && index < inventory.length;
	}

	private void remove(Item item) {
		if (item instanceof Weapon) {
			if (item == weapon)
				weapon = null;
		} else if (item instanceof Shield) {
			if (item == shield)
				shield = null;
		} else if (item instanceof Armor) {
			if (item == armor)
				armor = null;
		}
		for (int i = 1; i < inventory.length; i++) {
			if (item == inventory[i]) {
				inventory[i] = null;
				hasChanged = true;
				return;
			}
		}
	}

	public void dropItem(int index, Tile fromLocation) {
		if (index == 0) {
			if (potions.howMany() > 0 && fromLocation.dropItem(new Potion())) {
				potions.subtract();
				hasChanged = true;
			}
		} else if (validIndex(index) && inventory[index] != null) {
			Item item = inventory[index];
			if (fromLocation.dropItem(item))
				remove(item);
		}
	}

	public void toggleItem(int index) {
		if (validIndex(index) && inventory[index] != null) {
			hasChanged = true;
			Item item = inventory[index];
			if (item instanceof Weapon) {
				if (item == weapon)
					weapon = null;
				else if (shield == null || ((Weapon) item).isOneHanded())
					weapon = (Weapon) item;
			} else if (item instanceof Shield) {
				if (item == shield)
					shield = null;
				else if (weapon == null || weapon.isOneHanded())
					shield = (Shield) item;
			} else if (item instanceof Armor) {
				if (item == armor)
					armor = null;
				else
					armor = (Armor) item;
			}
		}
	}

	public boolean usePotion() {
		hasChanged = true;
		return potions.subtract();
	}

	public int getDamage() {
		if (weapon == null)
			return 0;
		if (armor == null)
			return weapon.getDamage();
		return (int) (weapon.getDamage() * armor.getDamageMultiplier());
	}

	public int getArmor() {
		int result = 0;
		if (shield != null)
			result += shield.getArmor();
		if (armor != null)
			result += armor.getArmor();
		return result;
	}

	public int getBlockChance() {
		if (shield == null)
			return 0;
		return shield.getBlockChance();
	}

	public boolean hasChanged() {
		boolean result = hasChanged;
		hasChanged = false;
		return result;
	}

	public String[] getInventory() {
		String[] result = new String[inventory.length];
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null)
				result[i] = "";
			else if (inventory[i] == weapon || inventory[i] == shield || inventory[i] == armor)
				result[i] = "(active) " + inventory[i].getName();
			else
				result[i] = inventory[i].getName();
		}
		return result;
	}
}
