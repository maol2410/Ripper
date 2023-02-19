import java.util.HashMap;

enum ArmorType {
	LIGHT, MEDIUM, HEAVY
}

enum Direction {
	NONE(0), UP(3), DOWN(4), RIGHT(1), LEFT(2);

	private int value;
	private static HashMap<Integer, Direction> map = new HashMap<>();

	private Direction(int value) {
		this.value = value;
	}

	static {
		for (Direction type : Direction.values()) {
			map.put(type.value, type);
		}
	}

	public static Direction typeOf(int value) {
		return (Direction) map.get(value);
	}

	public int getValue() {
		return value;
	}
}

enum SkillType {
	DEFENSIVE(8), NORMAL(7), AGGRESSIVE(6), TAUNT(2), PROTECTOR(3), SWORD_MASTER(4), PLAY_NICE(5), POTION(1);

	private int value;
	private static HashMap<Integer, SkillType> map = new HashMap<>();

	private SkillType(int value) {
		this.value = value;
	}

	static {
		for (SkillType type : SkillType.values()) {
			map.put(type.value, type);
		}
	}

	public static SkillType typeOf(int value) {
		return (SkillType) map.get(value);
	}

	public int getValue() {
		return value;
	}
}

enum State {
	CONNECTING, WAITING, INGAME, ROOMCOMPLETE, LEVELUP, GAMEOVER
}

class CONST {
	// Wall types
	static final byte WALL_EMPTY_OUTSIDE = 1;
	static final byte WALL_EMPTY_INSIDE = 2;
	static final byte WALL = 3;
	static final byte WALL_DOOR = 4;

	// Floor types
	static final byte FLOOR_OUTSIDE = 1;
	static final byte FLOOR_INSIDE = 2;

	// Player types
	static final byte PLAYER1 = 4;
	static final byte PLAYER2 = 5;

	// Item types
	static final byte ITEM_POTION = 15;
	static final byte ITEM_SHIELD = 21;
	static final byte ITEM_WEAPON_ONE_HANDED = 16;
	static final byte ITEM_WEAPON_TWO_HANDED = 17;
	static final byte ITEM_ARMOR_LIGHT = 18;
	static final byte ITEM_ARMOR_MEDIUM = 19;
	static final byte ITEM_ARMOR_HEAVY = 20;

	// Monster types
	static final byte MONSTER_ZOMBIE_1 = 6;
	static final byte MONSTER_ZOMBIE_2 = 7;
	static final byte MONSTER_ZOMBIE_3 = 8;
	static final byte MONSTER_ZOMBIE_4 = 9;
	static final byte MONSTER_ZOMBIE_5 = 10;
	static final byte MONSTER_ZOMBIE_6 = 11;
	static final byte MONSTER_BOSS_1 = 12;
	static final byte MONSTER_BOSS_2 = 13;
	static final byte MONSTER_BOSS_3 = 14;

	// Message types from server
	static final byte MESSAGE_WALLS = 1;
	static final byte MESSAGE_FLOOR = 2;
	static final byte MESSAGE_INVENTORY = 3;
	static final byte MESSAGE_COMBAT_INFO = 4;
	static final byte MESSAGE_REQUEST_LEVEL_UP = 5;
	static final byte MESSAGE_WAITING_FOR_OTHER = 6;
	static final byte MESSAGE_GAME_OVER = 7;

	// Message types from client
	static final byte MESSAGE_DIRECTION = 100;
	static final byte MESSAGE_SKILL = 101;
	static final byte MESSAGE_USE_ITEM = 102;
	static final byte MESSAGE_REFRESH = 103;
	static final byte MESSAGE_ANSWER_LEVEL_UP = 104;
	static final byte MESSAGE_START = 105;
	static final byte MESSAGE_QUIT = 106;

	// Use or Drop item
	static final byte USEITEM_USE = 1;
	static final byte USEITEM_DROP = 2;

	// Answer Skill Choice
	static final byte ANSWER_TAUNT = 1;
	static final byte ANSWER_PROTECTOR = 2;
	static final byte ANSWER_SWORD_MASTER = 3;
	static final byte ANSWER_PLAY_NICE = 4;
}
