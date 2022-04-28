
public class Tile {
	private byte tileType;
	private TileObject tileObject;
	private Tile up;
	private Tile down;
	private Tile right;
	private Tile left;

	public Tile(byte tileType) {
		this.tileType = tileType;
	}

	public boolean hasPath(Direction d) {
		return tileAt(d) != null;
	}

	public TileObject getObject() {
		return tileObject;
	}

	public TileObject getObject(Direction d) {
		Tile tile = tileAt(d);
		if (tile == null)
			return null;
		return tile.tileObject;
	}

	public boolean dropItem(Item item) {
		Tile emptyTile = null;
		if (hasPath(Direction.UP) && getObject(Direction.UP) == null)
			emptyTile = tileAt(Direction.UP);
		else if (hasPath(Direction.LEFT) && getObject(Direction.LEFT) == null)
			emptyTile = tileAt(Direction.LEFT);
		else if (hasPath(Direction.RIGHT) && getObject(Direction.RIGHT) == null)
			emptyTile = tileAt(Direction.RIGHT);
		else if (hasPath(Direction.DOWN) && getObject(Direction.DOWN) == null)
			emptyTile = tileAt(Direction.DOWN);
		else
			return false;
		emptyTile.setTileObject(item);
		return true;
	}

	public void move(Direction d) {
		Tile nextTile = tileAt(d);
		TileObject nextTileObject = nextTile.tileObject;
		nextTile.setTileObject(tileObject);
		tileObject = null;
		setTileObject(nextTileObject);
	}

	private Tile tileAt(Direction d) {
		switch (d) {
		case UP:
			return up;
		case DOWN:
			return down;
		case RIGHT:
			return right;
		case LEFT:
			return left;
		default:
			return null;
		}
	}

	public boolean isClose(TileObject findMe) {
		if (getObject(Direction.UP) == findMe)
			return true;
		if (getObject(Direction.DOWN) == findMe)
			return true;
		if (getObject(Direction.RIGHT) == findMe)
			return true;
		if (getObject(Direction.LEFT) == findMe)
			return true;
		return false;
	}

	public void setTileObject(TileObject tileObject) {
		if (this.tileObject != null)
			this.tileObject.setPosition(null);
		this.tileObject = tileObject;
		if (this.tileObject != null) {
			this.tileObject.setPosition(this);
			if (tileObject instanceof Player)
				pokeMonsters((Player) tileObject);
		}
	}

	private void pokeMonsters(Player player) {
		TileObject something = getObject(Direction.UP);
		pokeMonster(something, player);
		something = getObject(Direction.LEFT);
		pokeMonster(something, player);
		something = getObject(Direction.RIGHT);
		pokeMonster(something, player);
		something = getObject(Direction.DOWN);
		pokeMonster(something, player);
	}

	private void pokeMonster(TileObject something, Player player) {
		if (something instanceof Monster)
			((Monster) something).poke(player);
	}

	public byte getType() {
		if (tileObject != null)
			return tileObject.getType();
		return tileType;
	}

	public void connectToTile(Tile neigbour, Direction d) {
		if (d == Direction.UP) {
			up = neigbour;
			neigbour.down = this;
		} else if (d == Direction.DOWN) {
			down = neigbour;
			neigbour.up = this;
		} else if (d == Direction.RIGHT) {
			right = neigbour;
			neigbour.left = this;
		} else if (d == Direction.LEFT) {
			left = neigbour;
			neigbour.right = this;
		}
	}
}
