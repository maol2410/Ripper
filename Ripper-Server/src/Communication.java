import java.io.DataInputStream;
import java.io.IOException;

public class Communication {
	private static final boolean DEBUG = false;

	/*
	 * Reads the message and checks that the message has an valid header and data
	 * length.
	 */
	protected static Message readInputStream(DataInputStream dataInputStream) throws IOException {
		if (!readStartByte(dataInputStream))
			return null;
		int available = dataInputStream.available();
		if (available >= 7) {
			byte[] header = new byte[7];
			dataInputStream.read(header);
			int length = ((header[5] & 0xFF) << 8) + (header[6] & 0xFF);
			available = dataInputStream.available();
			if (available >= length) {
				byte[] data = new byte[length];
				dataInputStream.read(data);
				printMessage(header);
				printRecieveMessage(data);
				int sequenceNumber = ((header[1] & 0xFF) << 24) + ((header[2] & 0xFF) << 16) + ((header[3] & 0xFF) << 8)
						+ (header[4] & 0xFF);
				byte messageType = header[4];
				return new Message(messageType, sequenceNumber, data);
			}
		}
		return null;
	}

	/*
	 * Checks that the message has an valid start byte, if not then it clears the
	 * stream until a start byte.
	 */
	private static boolean readStartByte(DataInputStream dataInputStream) throws IOException {
		byte[] startByte = new byte[1];
		while (dataInputStream.available() > 0) {
			dataInputStream.read(startByte);
			if (startByte[0] == (byte) 0xFF) // if the byte is 0XFF then we have a valid start byte.
				return true;
			else
				System.out.println("Wrong start byte: " + (startByte[0] & 0xFF));
		}
		return false;
	}

	protected static void printSomethingWentWrong(IOException e) {
		System.out.println("Something went wrong: " + e.getMessage());
	}

	protected static void printMessage(byte[] bytes) {
		if (!DEBUG)
			return;

		int index = 0;
		for (; index < 7; index++) {
			System.out.print(bytes[index] + ".");
		}

		System.out.print("--");

		for (; index < bytes.length; index++) {
			System.out.print(bytes[index] + ".");
		}
	}

	private static void printRecieveMessage(byte[] bytes) {
		if (!DEBUG)
			return;

		int index = 0;
		for (; index < bytes.length; index++) {
			System.out.print(bytes[index] + ".");
		}
	}
}
