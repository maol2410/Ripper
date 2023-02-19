
public class Door extends TileObject {
	private boolean isOpen;

	public Door() {
		super("Door", CONST.WALL_DOOR);
		isOpen = false;
	}

	public void openDoor() {
		isOpen = true;
	}

	public boolean isOpen() {
		return isOpen;
	}
}
