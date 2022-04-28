public abstract class TileObject {
	protected String name;
	private byte objectType;
	protected Tile location;

	public TileObject(String name, byte objectType) {
		this.name = name;
		this.objectType = objectType;
	}

	public void setPosition(Tile location) {
		this.location = location;
	}

	public byte getType() {
		return objectType;
	}

	public String getName() {
		return name;
	}
}
