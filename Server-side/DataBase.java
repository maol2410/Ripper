import java.util.ArrayList;

public class DataBase {
	private static String[] roomData = {
			"33343331111111112222222111111111222222211111111122222321111111112222232111111111222323211111111122232323331111113333332322111111111111213211111111111123323333331111113332323222111111111232322211111111123222221111111112322222111111111222222211111111122222221111111113333333322222231111111113222222311111111132222223111111111322223331111111113222222311111111132233333111111111322222222231111111111113332311111111111133133111111111111322222222231111111113333322311111111132222223111111111333222231111111113222222311111111132222223111111111322222232222222111111111222222211111111122222221111111112222212111111111222222211111111122212121111111112222222222111111111111212211111111111121121111111111112222222222111111111212122211111111122222221111111112122222111111111222222211111111122222221111111112222222",
			"11111144441111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111113333111111111111322231111111111113222311111111111132223111111111111322231111111111113222311111111111132223111111111111322231111111111113222311111111111132223111111111111322231111111111113222311111111111132223111111111111322231111111111113222311111111111132223111111111111322231111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111111111222211111111111122221111111111112222111111",
			"33333333333333332333333333333332211111111111111221111111111111122113333333333112213233222233231221321132231123122112113223112112211211222211211221121132231121122332113223112332233211222211233221121132231121122112111441112112233233333333233233333333333333331111111111111111322222222222222233311111111111113333111111111111133331111111111111333313222222222313333323132223132333331331132311331333313313222313313333133132223133133331331132311331333222313222313222333133132223133133331331132311331333313311111113313332222222222222223111111111111111112222222222222222211111111111111221111111111111122111111111111112211222222222211221221122221122122112111221112112211211222211211221121122221121122112111221112112222211222211222221121122221121122112111221112112211211111111211222222222222222221111111111111111" };

	public static byte[][] boardWallsHorizontal(int roomNumber) {
		String data = roomData[roomNumber];
		byte[][] result = new byte[16][17];
		int index = 0;
		for (int y = 0; y < result[0].length; y++) {
			for (int x = 0; x < result.length; x++) {
				result[x][y] = (byte) (data.charAt(index++) - 48);
			}
		}
		return result;
	}

	public static byte[][] boardWallsVertical(int roomNumber) {
		String data = roomData[roomNumber];
		byte[][] result = new byte[17][16];
		int index = 272;
		for (int y = 0; y < result[0].length; y++) {
			for (int x = 0; x < result.length; x++) {
				result[x][y] = (byte) (data.charAt(index++) - 48);
			}
		}
		return result;

	}

	public static Tile[][] boardFloor(int roomNumber) {
		String data = roomData[roomNumber];
		Tile[][] result = new Tile[16][16];
		int index = 544;
		for (int y = 0; y < result[0].length; y++) {
			for (int x = 0; x < result.length; x++) {
				byte tileType = (byte) (data.charAt(index++) - 48);
				result[x][y] = new Tile(tileType);
			}
		}
		return result;

	}

	public static Tile doorTile() {
		Tile doorTile = new Tile(CONST.FLOOR_OUTSIDE);
		doorTile.setTileObject(new Door());
		return doorTile;
	}

	private static void connectTiles(byte[][] boardWallsHorizontal, byte[][] boardWallsVertical, Tile[][] boardFloor,
			Tile doorTile) {
		int lastIndex = boardWallsHorizontal[0].length - 1;
		for (int x = 0; x < boardWallsHorizontal.length; x++) {
			if (boardWallsHorizontal[x][0] == CONST.WALL_DOOR)
				boardFloor[x][0].connectToTile(doorTile, Direction.UP);
			for (int y = 1; y < lastIndex; y++) {
				if (boardWallsHorizontal[x][y] == CONST.WALL_EMPTY_INSIDE)
					boardFloor[x][y - 1].connectToTile(boardFloor[x][y], Direction.DOWN);
				else if (boardWallsHorizontal[x][y] == CONST.WALL_DOOR) {
					boardFloor[x][y - 1].connectToTile(doorTile, Direction.DOWN);
					boardFloor[x][y].connectToTile(doorTile, Direction.UP);
				}
			}
			if (boardWallsHorizontal[x][lastIndex] == CONST.WALL_DOOR)
				boardFloor[x][lastIndex - 1].connectToTile(doorTile, Direction.DOWN);
		}

		lastIndex = boardWallsVertical.length - 1;
		for (int y = 0; y < boardWallsVertical[0].length; y++) {
			if (boardWallsVertical[0][y] == CONST.WALL_DOOR)
				boardFloor[0][y].connectToTile(doorTile, Direction.LEFT);
			for (int x = 1; x < lastIndex; x++) {
				if (boardWallsVertical[x][y] == CONST.WALL_EMPTY_INSIDE)
					boardFloor[x - 1][y].connectToTile(boardFloor[x][y], Direction.RIGHT);
				else if (boardWallsVertical[x][y] == CONST.WALL_DOOR) {
					boardFloor[x - 1][y].connectToTile(doorTile, Direction.RIGHT);
					boardFloor[x][y].connectToTile(doorTile, Direction.LEFT);
				}
			}
			if (boardWallsVertical[lastIndex][y] == CONST.WALL_DOOR)
				boardFloor[lastIndex - 1][y].connectToTile(doorTile, Direction.RIGHT);
		}
	}

	private static ArrayList<Monster> insertObjectsToRoom0(Tile[][] boardFloor) {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		Monster m11 = new Monster("Fierce Zombie", CONST.MONSTER_ZOMBIE_1, 2000, 30, 0, 50, 5, true);
		m11.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		m11.addSkill(new MonsterSkill("Bash", 50, 10.0, 0));
		m11.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		m11.insertLoot(new Potion());
		monsters.add(m11);
		Monster m12 = new Monster("Big Sleepy Zombie", CONST.MONSTER_ZOMBIE_2, 5000, 30, 25, 25, 5, true);
		m12.addSkill(new MonsterSkill("Normal", 10, 1.0, 0));
		m12.addSkill(new MonsterSkill("Bash", 100, 10.0, 0));
		m12.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		m12.insertLoot(new Potion());
		monsters.add(m12);
		boardFloor[11][10].setTileObject(m11);
		boardFloor[10][11].setTileObject(m12);

		Monster m21 = new Monster("Fierce Zombie", CONST.MONSTER_ZOMBIE_1, 2000, 30, 0, 50, 5, true);
		m21.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		m21.addSkill(new MonsterSkill("Bash", 50, 10.0, 0));
		m21.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		m21.insertLoot(new Potion());
		monsters.add(m21);
		Monster m22 = new Monster("Big Sleepy Zombie", CONST.MONSTER_ZOMBIE_2, 5000, 30, 25, 25, 5, true);
		m22.addSkill(new MonsterSkill("Normal", 10, 1.0, 0));
		m22.addSkill(new MonsterSkill("Bash", 100, 10.0, 0));
		m22.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		m22.insertLoot(new Potion());
		monsters.add(m22);
		boardFloor[5][4].setTileObject(m21);
		boardFloor[4][5].setTileObject(m22);

		Monster m31 = new Monster("Boss", CONST.MONSTER_BOSS_1, 10000, 30, 25, 50, 5, true);
		m31.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		m31.addSkill(new MonsterSkill("Bash", 100, 10.0, 0));
		m31.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		m31.insertLoot(new Weapon("Two-Handed Sword lvl 2", CONST.ITEM_WEAPON_TWO_HANDED, 400, false));
		monsters.add(m31);
		Monster m32 = new Monster("Fierce Zombie", CONST.MONSTER_ZOMBIE_1, 2000, 30, 0, 50, 5, true);
		m32.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		m32.addSkill(new MonsterSkill("Bash", 50, 10.0, 0));
		m32.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		m32.insertLoot(new Potion());
		monsters.add(m32);
		Monster m33 = new Monster("Weak Zombie", CONST.MONSTER_ZOMBIE_3, 1500, 30, 25, 25, 5, true);
		m33.addSkill(new MonsterSkill("Normal", 10, 1.0, 0));
		monsters.add(m33);
		Monster m34 = new Monster("Weak Zombie", CONST.MONSTER_ZOMBIE_3, 1500, 30, 25, 25, 5, true);
		m34.addSkill(new MonsterSkill("Normal", 10, 1.0, 0));
		monsters.add(m34);
		boardFloor[9][6].setTileObject(m31);
		boardFloor[8][7].setTileObject(m32);
		boardFloor[8][6].setTileObject(m33);
		boardFloor[9][7].setTileObject(m34);

		boardFloor[13][15].setTileObject(new Armor("Heavy Armor lvl 1", CONST.ITEM_ARMOR_HEAVY, ArmorType.HEAVY, 100));
		boardFloor[12][15].setTileObject(new Weapon("One-Handed Sword lvl 1", CONST.ITEM_WEAPON_ONE_HANDED, 100, true));
		boardFloor[11][15].setTileObject(new Shield("Shield lvl 1", 100, 20));
		boardFloor[15][13]
				.setTileObject(new Weapon("Two-Handed Sword lvl 1", CONST.ITEM_WEAPON_TWO_HANDED, 200, false));
		boardFloor[15][12].setTileObject(new Armor("Light Armor lvl 1", CONST.ITEM_ARMOR_LIGHT, ArmorType.LIGHT, 30));

		return monsters;
	}

	private static ArrayList<Monster> insertObjectsToRoom1(Tile[][] boardFloor) {
		boardFloor[6][9].setTileObject(new Armor("Heavy Armor lvl 2", CONST.ITEM_ARMOR_HEAVY, ArmorType.HEAVY, 150));
		boardFloor[6][8].setTileObject(new Weapon("One-Handed Sword lvl 2", CONST.ITEM_WEAPON_ONE_HANDED, 200, true));
		boardFloor[6][7].setTileObject(new Shield("Shield lvl 2", 150, 30));
		boardFloor[9][9].setTileObject(new Weapon("Two-Handed Sword lvl 2", CONST.ITEM_WEAPON_TWO_HANDED, 400, false));
		boardFloor[9][8].setTileObject(new Armor("Light Armor lvl 2", CONST.ITEM_ARMOR_LIGHT, ArmorType.LIGHT, 45));
		boardFloor[9][7].setTileObject(new Armor("Medium Armor lvl 2", CONST.ITEM_ARMOR_MEDIUM, ArmorType.MEDIUM, 105));
		boardFloor[7][3].setTileObject(new Potion());
		boardFloor[7][2].setTileObject(new Potion());
		boardFloor[8][3].setTileObject(new Potion());
		boardFloor[8][2].setTileObject(new Potion());

		return new ArrayList<Monster>();
	}

	private static ArrayList<Monster> insertObjectsToRoom2(Tile[][] boardFloor) {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		Monster monster1 = new Monster("Normal-Zombie", CONST.MONSTER_ZOMBIE_1, 5000, 30, 25, 50, 5, true);
		monster1.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		monster1.addSkill(new MonsterSkill("Bash", 100, 10.0, 0));
		monster1.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		monster1.insertLoot(new Shield("Shield lvl 3", 200, 35));
		monsters.add(monster1);
		Monster monster2 = new Monster("Bashing-Zombie", CONST.MONSTER_ZOMBIE_2, 5000, 30, 25, 50, 5, true);
		monster2.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		monster2.addSkill(new MonsterSkill("Bash", 50, 10.0, 0));
		monster2.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		monster2.insertLoot(new Armor("Heavy Armor lvl 3", CONST.ITEM_ARMOR_HEAVY, ArmorType.HEAVY, 200));
		monsters.add(monster2);
		Monster monster3 = new Monster("Healing-Zombie", CONST.MONSTER_ZOMBIE_3, 5000, 30, 25, 50, 5, true);
		monster3.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		monster3.addSkill(new MonsterSkill("Heal", 100, 0.0, 1000));
		monster3.addSkill(new MonsterSkill("Bash", 300, 10.0, 0));
		monster3.insertLoot(new Armor("Light Armor lvl 3", CONST.ITEM_ARMOR_LIGHT, ArmorType.LIGHT, 60));
		monsters.add(monster3);
		Monster monsterBoss1 = new Monster("The Bashing BOSS", CONST.MONSTER_BOSS_1, 10000, 100, 50, 100, 5, true);
		monsterBoss1.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		monsterBoss1.addSkill(new MonsterSkill("Bash", 50, 10.0, 0));
		monsterBoss1.addSkill(new MonsterSkill("Heal", 300, 0.0, 1000));
		monsterBoss1.insertLoot(new Weapon("Two-Handed Sword lvl 4", CONST.ITEM_WEAPON_TWO_HANDED, 800, false));
		monsters.add(monsterBoss1);

		Monster monsterBoss2 = new Monster("The Healing BOSS", CONST.MONSTER_BOSS_2, 15000, 100, 50, 100, 10, true);
		monsterBoss2.addSkill(new MonsterSkill("Normal", 5, 1.0, 0));
		monsterBoss2.addSkill(new MonsterSkill("Heal", 100, 5.0, 2000));
		monsterBoss2.addSkill(new MonsterSkill("Bash", 300, 10.0, 0));
		monsterBoss2.insertLoot(new Weapon("One-Handed Sword lvl 4", CONST.ITEM_WEAPON_ONE_HANDED, 400, true));
		monsters.add(monsterBoss2);

		// Adding Monster to boards
		boardFloor[3][10].setTileObject(monster1);
		boardFloor[12][10].setTileObject(monster2);
		boardFloor[8][6].setTileObject(monster3);
		boardFloor[7][9].setTileObject(monsterBoss1);
		boardFloor[8][12].setTileObject(monsterBoss2);

		// Adding Items to board
		boardFloor[2][5].setTileObject(new Potion());
		boardFloor[13][5].setTileObject(new Potion());
		return monsters;
	}

	public static ArrayList<Monster> processRoom0(byte[][] boardWallsHorizontal, byte[][] boardWallsVertical,
			Tile[][] boardFloor, Tile doorTile) {
		connectTiles(boardWallsHorizontal, boardWallsVertical, boardFloor, doorTile);
		return insertObjectsToRoom0(boardFloor);
	}

	public static ArrayList<Monster> processRoom1(byte[][] boardWallsHorizontal, byte[][] boardWallsVertical,
			Tile[][] boardFloor, Tile doorTile) {
		connectTiles(boardWallsHorizontal, boardWallsVertical, boardFloor, doorTile);
		return insertObjectsToRoom1(boardFloor);
	}

	public static ArrayList<Monster> processRoom2(byte[][] boardWallsHorizontal, byte[][] boardWallsVertical,
			Tile[][] boardFloor, Tile doorTile) {
		connectTiles(boardWallsHorizontal, boardWallsVertical, boardFloor, doorTile);
		return insertObjectsToRoom2(boardFloor);
	}

	public static ArrayList<Room> getRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		byte[][] boardWallsHorizontal = boardWallsHorizontal(0);
		byte[][] boardWallsVertical = boardWallsVertical(0);
		Tile[][] boardFloor = boardFloor(0);
		Tile doorTile = doorTile();
		Door door = (Door) doorTile.getObject();
		ArrayList<Monster> monsters = processRoom0(boardWallsHorizontal, boardWallsVertical, boardFloor, doorTile);
		Room room = new Room();
		Tile player1StartLocation = boardFloor[14][15];
		Tile player2StartLocation = boardFloor[15][14];
		room.insertData(boardWallsHorizontal, boardWallsVertical, boardFloor, monsters, door, player1StartLocation,
				player2StartLocation);
		rooms.add(room);

		boardWallsHorizontal = boardWallsHorizontal(1);
		boardWallsVertical = boardWallsVertical(1);
		boardFloor = boardFloor(1);
		doorTile = doorTile();
		door = (Door) doorTile.getObject();
		monsters = processRoom1(boardWallsHorizontal, boardWallsVertical, boardFloor, doorTile);
		room = new Room();
		player1StartLocation = boardFloor[7][15];
		player2StartLocation = boardFloor[8][15];
		room.insertData(boardWallsHorizontal, boardWallsVertical, boardFloor, monsters, door, player1StartLocation,
				player2StartLocation);
		rooms.add(room);

		boardWallsHorizontal = boardWallsHorizontal(2);
		boardWallsVertical = boardWallsVertical(2);
		boardFloor = boardFloor(2);
		doorTile = doorTile();
		door = (Door) doorTile.getObject();
		monsters = processRoom2(boardWallsHorizontal, boardWallsVertical, boardFloor, doorTile);
		room = new Room();
		player1StartLocation = boardFloor[7][14];
		player2StartLocation = boardFloor[8][14];
		room.insertData(boardWallsHorizontal, boardWallsVertical, boardFloor, monsters, door, player1StartLocation,
				player2StartLocation);
		room.setFinalRoom();
		rooms.add(room);

		return rooms;
	}
}
