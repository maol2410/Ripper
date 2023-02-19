import java.util.ArrayList;

public class Room {
	private byte[][] boardWallsHorizontal;
	private byte[][] boardWallsVertical;
	private Tile[][] boardFloor;
	private ArrayList<Monster> monsters;
	private Tile player1StartLocation;
	private Tile player2StartLocation;
	private Player player1;
	private Player player2;
	private Door door;
	private boolean victory;
	private boolean defeat;
	private boolean isFinalRoom;

	public Room() {
		victory = false;
		defeat = false;
		isFinalRoom = false;
	}

	public void insertData(byte[][] boardWallsHorizontal, byte[][] boardWallsVertical, Tile[][] boardFloor,
			ArrayList<Monster> monsters, Door door, Tile player1StartLocation, Tile player2StartLocation) {
		this.boardWallsHorizontal = boardWallsHorizontal;
		this.boardWallsVertical = boardWallsVertical;
		this.boardFloor = boardFloor;
		this.monsters = monsters;
		this.door = door;
		this.player1StartLocation = player1StartLocation;
		this.player2StartLocation = player2StartLocation;
		if (monsters.size() == 0)
			victory();
	}

	public void start(Player player1, Player player2) {
		player1StartLocation.setTileObject(player1);
		player2StartLocation.setTileObject(player2);
		this.player1 = player1;
		player1.newRoom();
		this.player2 = player2;
		player2.newRoom();
	}

	public void setFinalRoom() {
		isFinalRoom = true;
	}

	public boolean isFinalRoom() {
		return isFinalRoom;
	}

	public void tick() {
		for (int i = 0; i < monsters.size(); i++) {
			Monster m = monsters.get(i);
			if (m.isAlive())
				m.tick();
			else {
				monsters.remove(i--);
				if (monsters.size() == 0)
					victory();
			}
		}
		if (!player1.isAlive() || !player2.isAlive())
			defeat = true;
		else {
			player1.tick();
			player2.tick();
		}
	}

	public byte[][] getWallsHorizontal() {
		return boardWallsHorizontal;
	}

	public byte[][] getWallsVertical() {
		return boardWallsVertical;
	}

	public Tile[][] getFloor() {
		return boardFloor;
	}

	private void victory() {
		victory = true;
		door.openDoor();
	}

	public boolean isVictory() {
		return victory;
	}

	public boolean isDefeat() {
		return defeat;
	}
}
