import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;


public class Communication implements Runnable {

	private Socket socket;
	private Protocol protocol;
	private final int serverPort = 50000;
	private final String serverIP = "192.168.1.75";
	private final int clientPort;
	private final String clientIP = "192.168.1.75";
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private final boolean DEBUG = false;

	public Communication(Protocol protocol) {
		this.protocol = protocol;
		Random random = new Random();
		clientPort = random.nextInt(50000) + 10000;

		try {
			socket = new Socket(InetAddress.getByName(serverIP), serverPort, InetAddress.getByName(clientIP),
					clientPort);
			System.out.println("Connected to server: " + socket);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("This is an auto-generated message " + e.getMessage());
		}
	}

	public void run() {
		try {
			reciving(dataInputStream);
		} catch (IOException e) {
			System.out.println("Something happened while reciving message: " + e.getMessage());
		}

	}

	public void sending(byte[] byteMessage) {
		try {
			printMessage(byteMessage);
			dataOutputStream.write(byteMessage);
			dataOutputStream.flush();
		} catch (IOException e) {
			System.out.println("Something happened while sending message: " + e.getMessage());
		}
	}

	private void reciving(DataInputStream dataInputStream) throws IOException {
		int howManyBytes = 0;
		byte[] messageHeader = new byte[7];
		readStartByte();
		while (dataInputStream.available() < 7) {
		}
		howManyBytes = dataInputStream.read(messageHeader);

		while (howManyBytes != -1) {
			int length = HelperFunctions.translateToInt(messageHeader, 5);
			byte[] message = new byte[length];
			while (dataInputStream.available() < length) {
			}
			howManyBytes = dataInputStream.read(message);
			printMessage(messageHeader);
			printRecieveMessage(message);

			protocol.receiveMessage(message, messageHeader[4]);
			// Creating a new byte array for new messageHeader
			readStartByte();
			messageHeader = new byte[7];
			while (dataInputStream.available() < 7) {
			}
			howManyBytes = dataInputStream.read(messageHeader);
		}
	}

	private void readStartByte() throws IOException {
		byte[] startByte = new byte[1];
		dataInputStream.read(startByte);
		while (startByte[0] != (byte) 0xff) {
			dataInputStream.read(startByte);
		}
	}

	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Somethig went wrong when closing Socket " + e.getMessage());
		}
	}

	private void printMessage(byte[] bytes) {
		if (!DEBUG) {
			return;
		}
		int index = 0;
		for (; index < 7; index++) {
			 System.out.print(bytes[index] + ".");
		}

		// System.out.print("--");

		for (; index < bytes.length; index++) {
			// System.out.print(bytes[index] + ".");
		}
	}

	private void printRecieveMessage(byte[] bytes) {
		if (!DEBUG) {
			return;
		}
		int index = 0;
		for (; index < bytes.length; index++) {
			 System.out.print(bytes[index] + ".");
		}
	}
}
