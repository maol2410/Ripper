enum State {
	CONNECTING, WAITING, INGAME, LEVELUP, GAMEOVER
}

public class Protocol {

	private int sequance;
	private Ripper ripper;
	private Communication communication;
	private State state;

	public Protocol() {
		state = State.CONNECTING;
	}

	public void receiveMessage(byte[] byteMessage, byte messageType) {

		if (!validMessageType(messageType))
			return;
		
		System.out.println("State: " + state + " MessageType: " + messageType);

		switch (messageType) {
			// Walls
			case 1:
				state = State.INGAME;
				readWalls(byteMessage);
				break;
			// Floor (Monster & Item & Players)
			case 2:
				readFloors(byteMessage);
				break;
			// Inventory (Backpack)
			case 3:
				readInventory(byteMessage);
				break;
			// Player Information & Ally Player & Monster information
			case 4:
				readCombatInfo(byteMessage);
				break;
			// Request skill choice
			case 5:
				state = State.LEVELUP;
				readRequestskillchoice();
				break;
			// Waiting for other player
			case 6:
				readWaitingForPlayer();
				break;
			// Game over
			case 7:
				state = State.GAMEOVER;
				readGameOver(byteMessage);
				break;
			default:
				break;
		}

	}

	public void addCommunication(Communication communication) {
		this.communication = communication;
	}

	public void addRiper(Ripper ripper) {
		this.ripper = ripper;

	}

	private void readFloors(byte[] bytes) {
		ripper.readFloor(bytes);
	}

	private void readWalls(byte[] bytes) {
		ripper.readWalls(bytes);

	}

	private void readCombatInfo(byte[] bytes) {
		int monsterHp = 0;
		int monsterHpMax = 0;
		String monsterName = null;
		int playerHp = HelperFunctions.translateToInt(bytes, 0);
		int barInduction = bytes[2];
		int allyHp = HelperFunctions.translateToInt(bytes, 3);
		if (bytes[5] == 1) {
			monsterHp = HelperFunctions.translateToInt(bytes, 6);
			monsterHpMax = HelperFunctions.translateToInt(bytes, 8);
			monsterName = HelperFunctions.convertToString(bytes, 10);
		}
		ripper.readCombatInfo(playerHp, barInduction, allyHp, monsterHp, monsterHpMax, monsterName);

	}

	private void readInventory(byte[] bytes) {
		String[] values = new String[12];
		values[0] = HelperFunctions.convertToString(bytes, 0);
		values[1] = HelperFunctions.convertToString(bytes, 35);
		values[2] = HelperFunctions.convertToString(bytes, 70);
		values[3] = HelperFunctions.convertToString(bytes, 105);
		values[4] = HelperFunctions.convertToString(bytes, 140);
		values[5] = HelperFunctions.convertToString(bytes, 175);
		values[6] = HelperFunctions.convertToString(bytes, 210);
		values[7] = HelperFunctions.convertToString(bytes, 245);
		values[8] = HelperFunctions.convertToString(bytes, 280);
		values[9] = HelperFunctions.convertToString(bytes, 315);
		values[10] = HelperFunctions.convertToString(bytes, 350);
		values[11] = HelperFunctions.convertToString(bytes, 385);

		ripper.inventory(values);

	}

	private void readRequestskillchoice() {
		ripper.requestSkillChoice();

	}

	private void readWaitingForPlayer() {
		ripper.waitingForPlayers();

	}

	private void readGameOver(byte[] bytes) {
		String[] values = new String[11];
		values[0] = HelperFunctions.convertToString(bytes, 0, 35);
		values[1] = HelperFunctions.convertToString(bytes, 35, 100);
		values[2] = HelperFunctions.convertToString(bytes, 135, 100);
		values[3] = HelperFunctions.convertToString(bytes, 235, 100);
		values[4] = HelperFunctions.convertToString(bytes, 335, 100);
		values[5] = HelperFunctions.convertToString(bytes, 435, 100);
		values[6] = HelperFunctions.convertToString(bytes, 535, 100);
		values[7] = HelperFunctions.convertToString(bytes, 635, 100);
		values[8] = HelperFunctions.convertToString(bytes, 735, 100);
		values[9] = HelperFunctions.convertToString(bytes, 835, 100);
		values[10] = HelperFunctions.convertToString(bytes, 935, 100);

		ripper.gameOver(values);

	}

	public void sendDirectionChoice(byte direction) {
		byte[] message = new byte[9];
		message[8] = direction;
		insertHeader(message, getSequanceNumber(), CONST.MESSAGE_DIRECTION, 1);
		communication.sending(message);
	}

	public void sendSkillChoice(byte skillkey) {
		byte[] message = new byte[9];
		message[8] = skillkey;
		insertHeader(message, getSequanceNumber(), CONST.MESSAGE_SKILL, 1);
		communication.sending(message);
	}

	public void sendInventoryItem(byte item, byte useOrDrop) {
		byte[] message = new byte[10];
		message[8] = useOrDrop;
		message[9] = item;
		insertHeader(message, getSequanceNumber(), CONST.MESSAGE_USE_ITEM, 2);
		communication.sending(message);
	}

	public void sendSkillAnswer(byte skill) {
		state = State.WAITING;
		byte[] message = new byte[9];
		message[8] = skill;
		insertHeader(message, getSequanceNumber(), CONST.MESSAGE_ANSWER_LEVEL_UP, 1);
		communication.sending(message);
	}

	public void sendStartGame(String playerName) {
		int length = playerName.length();
		state = State.WAITING;
		byte[] message = new byte[8 + length];
		HelperFunctions.insertString(message, playerName, 8, length);
		insertHeader(message, getSequanceNumber(), CONST.MESSAGE_START, length);
		communication.sending(message);
	}

	public void sendQuit() {
		state = State.GAMEOVER;
		byte[] message = new byte[8];
		insertHeader(message, getSequanceNumber(), CONST.MESSAGE_QUIT, 0);
		communication.sending(message);

	}

	private void insertHeader(byte[] message, int sequenceNumber, byte messageType, int dataLength) {
		message[0] = (byte) 0xFF;
		message[1] = (byte) (sequenceNumber >> 24);
		message[2] = (byte) (sequenceNumber >> 16);
		message[3] = (byte) (sequenceNumber >> 8);
		message[4] = (byte) sequenceNumber;
		message[5] = messageType;
		message[6] = (byte) (dataLength >> 8);
		message[7] = (byte) dataLength;
	}

	private int getSequanceNumber() {

		return sequance++;
	}

	private boolean validMessageType(int type) {

		if (state == State.CONNECTING)
			return type == CONST.MESSAGE_GAME_OVER;

		if (state == State.WAITING)
			return type == CONST.MESSAGE_GAME_OVER || type == CONST.MESSAGE_WALLS || type == CONST.MESSAGE_WAITING_FOR_OTHER;

		if (state == State.INGAME)
			return type == CONST.MESSAGE_FLOOR || type == CONST.MESSAGE_WALLS || type == CONST.MESSAGE_COMBAT_INFO
					|| type == CONST.MESSAGE_INVENTORY || type == CONST.MESSAGE_GAME_OVER || type == CONST.MESSAGE_REQUEST_LEVEL_UP;

		if (state == State.LEVELUP)
			return type == CONST.MESSAGE_GAME_OVER;

		if (state == State.GAMEOVER)
			return false;

		return false;
	}
}
