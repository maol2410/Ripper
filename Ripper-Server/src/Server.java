import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private ServerSocket serverSocket;
	private static int serverPort = 50000;
	private CommunicationConnector connector; // Looks to connect with requested Clients.
	private Thread connectorThread;
	private Room activeRoom;
	private Player player1;
	private Player player2;
	private int ticks;
	private long lastTime; // Latest time for last tick
	private ConsoleThread console; // Listen to commands on the terminal
	private ServerProtocol p1Comm; // The communication for player 1
	private ServerProtocol p2Comm; // The communication for player 2
	private State state; // Enum of states
	private ArrayList<Room> rooms; // ArrayList of all rooms
	private int roomIndex; // Current room

	public Server() {
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			System.out.println("Something went wrong with serversocket: " + e.getMessage());
		}
		connector = new CommunicationConnector(serverSocket);
		connectorThread = new Thread(connector);
		connectorThread.start();
		player1 = new Player("", CONST.PLAYER1); // initializing player 1
		player2 = new Player("", CONST.PLAYER2); // initializing player 2
		activeRoom = null;
		ticks = 0;
		lastTime = System.currentTimeMillis();
		console = new ConsoleThread();
		console.start();
		p1Comm = new ServerProtocol(connector);
		p2Comm = new ServerProtocol(connector);
		state = State.CONNECTING;
		rooms = DataBase.getRooms(); // // initializing all rooms
		roomIndex = 0;
	}

	/*
	 * Depending on the Game state different functions will be executed.
	 */
	public void run() {
		while (console.keepRunning()) {
			switch (state) {
				case CONNECTING:
					waitingForNewRoom();
					break;
				case INGAME:
					inGame();
					break;
				case ROOMCOMPLETE:
					roomComplete();
					break;
				case LEVELUP:
					waitingForNewRoom();
					break;
				default:
					break;
			}
		}
		// When concole.keepRunning() is false.
		p1Comm.close();
		p2Comm.close();
		try {
			serverSocket.close();
		} catch (IOException e) {
		}
		connector.close();
	}

	/*
	 * While CONNECTING or LEVELUP, the server is listing for messages and
	 * processing them.
	 */
	private void waitingForNewRoom() {
		if (!readAndRunMessage())
			return;

		if (p1Comm.getState() == State.WAITING && p2Comm.getState() == State.WAITING) {
			state = State.INGAME;
			runNextRoom();
		}
	}

	/*
	 * While INGAME, the server progress the game and sends information to the
	 * Clients and listen for the Clients messages.
	 */
	private void inGame() {
		if (!readAndRunMessage())
			return;

		tick();
		sendNewIngameInfo();
		sleep();
		if (activeRoom.isDefeat())
			gameOver();
		else if (activeRoom.isVictory()) { // checks if all monster are dead in the current room, which means Victory
											// for the current room.
			state = State.ROOMCOMPLETE;
			if (activeRoom.isFinalRoom()) // Checks if the current room is a final room
				victory(); // if so then the game is completed with victory.
		}
	}

	/*
	 * Sends a Victoy message to the Clients.
	 */
	private void victory() {
		state = State.GAMEOVER;
		String[] lines = new String[11];
		lines[0] = "VICTORY!";
		lines[1] = "Good job!" + player1.getName();
		lines[2] = "You finished the game in " + (ticks / 10.0) + " seconds.";
		lines[4] = "Hope you enjoyed the game!";
		lines[10] = "-Developed by Johan and Mateo.";

		if (p1Comm.getState() != State.GAMEOVER)
			p1Comm.sendGameOver(ticks, lines);
		lines[1] = "Good job!" + player2.getName();
		if (p2Comm.getState() != State.GAMEOVER)
			p2Comm.sendGameOver(ticks, lines);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("Something went wrong when sleeping: " + e.getMessage());
		}
		console.gameOver(); // In victory, we close the Server Console.
							// The Server is now going to close
	}

	/*
	 * While in state of ROOMCOMPLETE, the server progress the game and sends
	 * information to the Clients and listen for the Clients messages. This function
	 * tries to progress the state to LEVELUP.
	 */
	private void roomComplete() {
		if (!readAndRunMessage())
			return;

		if (p1Comm.getState() == State.INGAME || p2Comm.getState() == State.INGAME) {
			tick();
			if (p1Comm.getState() == State.INGAME && player1.usedDoor() != null) // Checks if player 1 has used a door
				p1Comm.sendRequestLevelUp(ticks); // if so send skillRequest to Client
			if (p2Comm.getState() == State.INGAME && player2.usedDoor() != null) // Same as above but player 2
				p2Comm.sendRequestLevelUp(ticks);
			sendNewIngameInfo();
			sleep();
		} else
			state = State.LEVELUP; // If both players has left the current room then change state to LEVELUP
	}

	/*
	 * Sends a Game over message to the Clients.
	 */
	private void gameOver() {
		state = State.GAMEOVER;
		String[] lines = new String[11];
		lines[0] = "GAME OVER!";
		if (!player1.isAlive())
			lines[1] = player1.getName() + " died";
		else if (!player2.isAlive())
			lines[1] = player2.getName() + " died";
		else if (p1Comm.getState() == State.GAMEOVER)
			lines[1] = player1.getName() + " quit";
		else if (p2Comm.getState() == State.GAMEOVER)
			lines[1] = player2.getName() + " quit";
		else
			lines[1] = "We dont know why the game is over..";

		if (p1Comm.getState() != State.GAMEOVER) // If player has quit then we dont need to send informaiton to that
													// client
			p1Comm.sendGameOver(ticks, lines);
		if (p2Comm.getState() != State.GAMEOVER) // Same as above
			p2Comm.sendGameOver(ticks, lines);
		console.gameOver();
	}

	private void tick() {
		ticks++; // Increases the seaquence number.
		activeRoom.tick(); // progresses the monster and player in current room.
	}

	private void runNextRoom() {
		activeRoom = rooms.get(roomIndex++); // gets new room from Room ArrayList
		activeRoom.start(player1, player2); // Adds players to new room
		p1Comm.stateInGame(); // Sets player 1 to INGAME
		p2Comm.stateInGame(); // Sets player 2 to INGAME
		refresh(p1Comm, player1); // Sends all new information to Client 1
		refresh(p2Comm, player2); // Sends all new information to Client 2
	}

	/*
	 * Sends all the current and relevent informaiton to client. Depending on which
	 * state they are.
	 */
	private void refresh(ServerProtocol comm, Player player) {
		switch (comm.getState()) {
			case INGAME:
				comm.sendWalls(ticks, activeRoom.getWallsHorizontal(), activeRoom.getWallsVertical());
				comm.sendFloor(ticks, activeRoom.getFloor());
				sendCombatInfo(comm, player, getAlly(player));
				comm.sendInventory(ticks, player.getInventory());
				break;
			case LEVELUP:
				comm.sendRequestLevelUp(ticks);
				break;
			case WAITING:
				if (getAlly(comm).getState() != State.WAITING)
					comm.sendWaitingForPlayer(ticks);
				break;
			default:
				break;
		}
	}

	private Player getAlly(Player player) {
		if (player == player1)
			return player2;
		return player1;
	}

	private ServerProtocol getAlly(ServerProtocol comm) {
		if (comm == p1Comm)
			return p2Comm;
		return p1Comm;
	}
	/*
	 * Sends All Combat related information to Clients
	 */

	private void sendCombatInfo(ServerProtocol comm, Player player, Player ally) {
		int allyHP = ally.getHP();
		Monster monster = player.getTarget();
		int monsterHP = 0;
		int monsterHPMax = 0;
		String monsterName = null;
		if (monster != null) {
			monsterHP = monster.getHP();
			monsterHPMax = monster.getMaxHP();
			monsterName = monster.getName();
		}
		comm.sendCombatInfo(ticks, player.getHP(), player.getInduction(), player.getInductionMax(), allyHP, monsterHP,
				monsterHPMax, monsterName);
	}
	/*
	 * Sends floor and combatInfo and maybe inventory info. Used while in room. For
	 * both Clients
	 */

	private void sendNewIngameInfo() {
		sendNewIngameInfo(p1Comm, player1);
		sendNewIngameInfo(p2Comm, player2);
	}

	/*
	 * Sends floor and combatInfo and maybe inventory info. Used while in room. For
	 * one client
	 */
	private void sendNewIngameInfo(ServerProtocol comm, Player player) {
		if (comm.getState() != State.INGAME)
			return;

		comm.sendFloor(ticks, activeRoom.getFloor());
		sendCombatInfo(comm, player, getAlly(player));
		if (player.inventoryHasChanged())
			comm.sendInventory(ticks, player.getInventory());
	}

	private boolean readAndRunMessage() {
		if (!readAndRunMessage(p1Comm, player1))
			return false;
		if (!readAndRunMessage(p2Comm, player2))
			return false;
		return true;
	}
	/*
	 * If Message is recieved then the function reads the message and calls the
	 * appropiate funciton.
	 */

	private boolean readAndRunMessage(ServerProtocol comm, Player player) {
		Object[] message = comm.getMessage();
		if (message == null)
			return true;

		switch ((byte) message[0]) {
			case CONST.MESSAGE_DIRECTION:
				player.setDirection((Direction) message[1]);
				return true;
			case CONST.MESSAGE_SKILL:
				player.useSkill((SkillType) message[1]);
				return true;
			case CONST.MESSAGE_USE_ITEM:
				byte useOrDrop = (byte) message[1];
				if (useOrDrop == CONST.USEITEM_USE)
					player.useItem((int) message[2] - 1);
				else if (useOrDrop == CONST.USEITEM_DROP)
					player.dropItem((int) message[2] - 1);
				return true;
			case CONST.MESSAGE_ANSWER_LEVEL_UP:
				player.levelUp((int) message[1]);
				refresh(comm, player);
				return true;
			case CONST.MESSAGE_REFRESH:
				refresh(comm, player);
				return true;
			case CONST.MESSAGE_START:
				player.setName((String) message[1]);
				refresh(comm, player);
				return true;
			case CONST.MESSAGE_QUIT:
				gameOver();
				return false;
			default:
				return true;
		}
	}

	private void sleep() {
		long nextTime = lastTime + 100;
		long currentTime = System.currentTimeMillis();
		long difference = nextTime - currentTime;
		if (difference > 0) {
			lastTime = nextTime;
			try {
				Thread.sleep(difference);
			} catch (InterruptedException e) {
				System.out.println("Something went wrong when sleeping: " + e.getMessage());
			}
		} else
			lastTime = currentTime;
	}
}
