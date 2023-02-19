
public class Message {
	private byte type;
	private int sequenceNumber;
	private byte[] data;

	public Message(byte type, int sequenceNumber, byte[] data) {
		this.type = type;
		this.sequenceNumber = sequenceNumber;
		this.data = data;
	}

	public byte type() {
		return type;
	}

	public int sequenceNumber() {
		return sequenceNumber;
	}

	public byte[] data() {
		return data;
	}
}
