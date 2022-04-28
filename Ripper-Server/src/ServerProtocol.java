
public class ServerProtocol {
	private State state;
	private CommunicationLink comm;

	public ServerProtocol(CommunicationConnector connector) {
		state = State.CONNECTING;
		comm = new CommunicationLink(connector);
	}

	/*
	 * Recieves messages from CommunicationLink and validates the message otherwise
	 * discarded.
	 */
	public Object[] getMessage() {
		Message latestMessage = comm.getMessage();
		if (latestMessage == null)
			return null;
		Message secondMessage = comm.getMessage();
		while (secondMessage != null) {
			if (secondMessage.sequenceNumber() >= latestMessage.sequenceNumber())
				latestMessage = secondMessage;
			secondMessage = comm.getMessage();
		}

		byte type = latestMessage.type();
		if (!validMessageType(type)) // If message is not valid then return null
			return null;

		return translateMessage(type, latestMessage.data()); // Otherwise return the message translated.
	}

	/*
	 * Checks if the message type is valid in the current state
	 */
	private boolean validMessageType(int type) {
		if (state == State.CONNECTING)
			return type == CONST.MESSAGE_START || type == CONST.MESSAGE_QUIT;

		if (state == State.INGAME)
			return type == CONST.MESSAGE_DIRECTION || type == CONST.MESSAGE_SKILL || type == CONST.MESSAGE_USE_ITEM
					|| type == CONST.MESSAGE_REFRESH || type == CONST.MESSAGE_QUIT;

		if (state == State.LEVELUP)
			return type == CONST.MESSAGE_REFRESH || type == CONST.MESSAGE_ANSWER_LEVEL_UP || type == CONST.MESSAGE_QUIT;

		if (state == State.WAITING)
			return type == CONST.MESSAGE_QUIT;

		if (state == State.GAMEOVER)
			return false;

		return false;
	}

	/*
	 * Translates the data of a message to an object array that the server can
	 * interpret. It also check that the length of the message is correct.
	 */
	private Object[] translateMessage(byte type, byte[] data) {
		switch (type) {
			case CONST.MESSAGE_DIRECTION:
				if (data.length != 1)
					return null;
				Direction direction = Direction.typeOf(data[0]);
				return new Object[] { type, direction };
			case CONST.MESSAGE_SKILL:
				if (data.length != 1)
					return null;
				SkillType skillType = SkillType.typeOf(data[0]);
				return new Object[] { type, skillType };
			case CONST.MESSAGE_USE_ITEM:
				if (data.length != 2)
					return null;
				return new Object[] { type, (byte) data[0], (int) data[1] };
			case CONST.MESSAGE_REFRESH:
				return new Object[] { type };
			case CONST.MESSAGE_ANSWER_LEVEL_UP:
				if (data.length != 1)
					return null;
				state = State.WAITING;
				return new Object[] { type, (int) data[0] };
			case CONST.MESSAGE_START:
				state = State.WAITING;
				String name = HelpFunctions.convertToString(data, 0, data.length);
				return new Object[] { type, name };
			case CONST.MESSAGE_QUIT:
				state = State.GAMEOVER;
				return new Object[] { type };
			default:
				return null;
		}
	}

	/*
	 * Send walls to the client.
	 */
	public void sendWalls(int ticks, byte[][] boardWallsHorizontal, byte[][] boardWallsVertical) {
		int dataLength = boardWallsHorizontal.length * boardWallsHorizontal[0].length
				+ boardWallsVertical.length * boardWallsVertical[0].length;
		byte[] message = new byte[dataLength + 8];
		insertHeader(message, ticks, CONST.MESSAGE_WALLS, dataLength);
		int index = 8;
		index = insertWalls(message, boardWallsHorizontal, index); // Insters horizontal walls first
		index = insertWalls(message, boardWallsVertical, index); // Instert vertical walls second
		comm.sendMessage(message); // Sends the message to client
	}

	/*
	 * Translate Floor tiles to bytes and sends to Client
	 */
	public void sendFloor(int ticks, Tile[][] boardFloor) {
		int dataLength = boardFloor.length * boardFloor[0].length;
		byte[] message = new byte[dataLength + 8];
		insertHeader(message, ticks, CONST.MESSAGE_FLOOR, dataLength);
		int index = 8;
		for (int y = 0; y < boardFloor[0].length; y++) {
			for (int x = 0; x < boardFloor.length; x++) {
				message[index++] = boardFloor[x][y].getType(); // If an object (item, player or monster) is located in
															  // current tile then the object value is inserted instead.
			}
		}
		comm.sendMessage(message);
	}

	/*
	 * Send inventory, containing 12 strings and each string is 35 bytes, to client.
	 */
	public void sendInventory(int ticks, String[] inventory) {
		int dataLength = inventory.length * 35;
		byte[] message = new byte[dataLength + 8];
		insertHeader(message, ticks, CONST.MESSAGE_INVENTORY, dataLength);
		int index = 8;
		for (int i = 0; i < inventory.length; i++) {
			HelpFunctions.insertString(message, inventory[i], index, 35);
			index += 35;
		}
		comm.sendMessage(message);
	}

	/*
	 * Sends Combat related information to client.
	 */
	public void sendCombatInfo(int ticks, int playerHP, int playerInduction, int playerInductionMax, int allyHP,
			int monsterHP, int monsterHPMax, String monsterName) {
		int dataLength = 6;
		if (monsterName != null)
			dataLength += 39; // add length for monster information
		byte[] message = new byte[dataLength + 8];
		insertHeader(message, ticks, CONST.MESSAGE_COMBAT_INFO, dataLength);
		message[8] = (byte) (playerHP >> 8); // rigth shift 8 bytes to include the playerHP on two bytes.
		message[9] = (byte) playerHP;
		if (playerInductionMax == 0)
			message[10] = 0;
		else // calculates the induction time in percent
			message[10] = (byte) ((playerInductionMax - playerInduction) * 100 / playerInductionMax);

		message[11] = (byte) (allyHP >> 8);
		message[12] = (byte) allyHP;
		if (monsterName == null) // if NO monster then send byte for off.
			message[13] = 0;
		else { // else input information about monster
			message[13] = 1;
			message[14] = (byte) (monsterHP >> 8);
			message[15] = (byte) monsterHP;
			message[16] = (byte) (monsterHPMax >> 8);
			message[17] = (byte) monsterHPMax;
			HelpFunctions.insertString(message, monsterName, 18, 35);
		}
		comm.sendMessage(message);
	}

	/*
	 * Requests a skill to be level up to Client
	 */
	public void sendRequestLevelUp(int ticks) {
		state = State.LEVELUP;
		byte[] message = new byte[8];
		insertHeader(message, ticks, CONST.MESSAGE_REQUEST_LEVEL_UP, 0);
		comm.sendMessage(message);
	}

	/*
	 * Send a message to client that the other player is not ready
	 */
	public void sendWaitingForPlayer(int ticks) {
		byte[] message = new byte[8];
		insertHeader(message, ticks, CONST.MESSAGE_WAITING_FOR_OTHER, 0);
		comm.sendMessage(message);
	}

	/*
	 * Sends Game over information to Client(for example Gameover or Victory). The
	 * information contains 11 strings where the first string (35 bytes) is reserved
	 * to title. The other 10 strings (100 bytes each) represents lines under the
	 * title.
	 */
	public void sendGameOver(int ticks, String[] lines) {
		state = State.GAMEOVER;
		int dataLength = 1035;
		byte[] message = new byte[dataLength + 8];
		insertHeader(message, ticks, CONST.MESSAGE_GAME_OVER, dataLength);
		int index = 8;
		HelpFunctions.insertString(message, lines[0], index, 35);
		index += 35;
		for (int i = 1; i < lines.length; i++) {
			HelpFunctions.insertString(message, lines[i], index, 100);
			index += 100;
		}
		comm.sendMessage(message);
	}

	/*
	 * Creates an header for the message to be sent to Client
	 */
	private void insertHeader(byte[] message, int ticks, byte messageType, int dataLength) {
		message[0] = (byte) 0xFF;
		message[1] = (byte) (ticks >> 24);
		message[2] = (byte) (ticks >> 16);
		message[3] = (byte) (ticks >> 8);
		message[4] = (byte) ticks;
		message[5] = messageType;
		message[6] = (byte) (dataLength >> 8);
		message[7] = (byte) dataLength;
	}

	/*
	 * Helper method for insertWalls, so it can insert the horizontal walls first
	 * and vertical walls later.
	 */
	private int insertWalls(byte[] message, byte[][] walls, int index) {
		for (int y = 0; y < walls[0].length; y++) {
			for (int x = 0; x < walls.length; x++) {
				message[index++] = walls[x][y];
			}
		}
		return index;
	}

	public State getState() {
		return state; // returns current state
	}

	public void close() {
		comm.close(); // closes the client socket
	}

	public void stateInGame() {
		state = State.INGAME; // sets state to InGame when the server starts a new room
	}
}
