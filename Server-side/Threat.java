
public class Threat implements Comparable<Threat> {
	private Player enemy;
	private int threat;

	public Threat(Player enemy) {
		this.enemy = enemy;
		threat = 0;
	}

	public void addThreat(int threat) {
		this.threat += threat;
		if (this.threat < 0)
			this.threat = 0;
	}

	public boolean isEnemy(Player enemy) {
		return this.enemy == enemy;
	}

	public void tick() {
		int calmDown = threat / 1000 + 10;
		addThreat(-calmDown);
	}

	public Player getPlayer() {
		return enemy;
	}

	public boolean isZero() {
		return threat == 0;
	}

	@Override
	public int compareTo(Threat o) {
		return this.threat - o.threat;
	}
}
