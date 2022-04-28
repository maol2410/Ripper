class CONST {
    // Wall types
	static final byte WALL_EMPTY_OUTSIDE = 1;
	static final byte WALL_EMPTY_INSIDE = 2;
	static final byte WALL = 3;
	static final byte WALL_DOOR = 4;

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

    // Move Direction

    static final byte DIRECTION_RIGHT = 1;
    static final byte DIRECTION_LEFT = 2;
    static final byte DIRECTION_UP = 3;
    static final byte DIRECTION_DOWN = 4;

    // Use Skill
    static final byte SKILL_USE_HEALTH_POTIOM = 1;
    static final byte SKILL_USE_TAUNT = 2;
	static final byte SKILL_USE_PROTECTOR = 3;
	static final byte SKILL_USE_SWORD_MASTER = 4;
	static final byte SKILL_USE_PLAY_NICE = 5;
    static final byte SKILL_USE_AGGRESSIVE_ATTACK = 6;
    static final byte SKILL_USE_NORMAL_ATTACK = 7;
    static final byte SKILL_USE_DEFENSIVE_ATTACK = 8;
}
