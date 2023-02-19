import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CommunicationLink {
	private String name;
	CommunicationConnector connector;
	private Socket client;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private boolean isConnected;

	public CommunicationLink(CommunicationConnector connector) {
		this.connector = connector;
		isConnected = false;
		name = ""; // initialize the Clients name temporarly.
	}

	/*
	 * Asking the connector for a socket with a client that has the specified name.
	 * If the name is empty ("") then we are looking for any new socket.
	 */
	private Message tryConnect() {
		Message message = null;
		Object[] socketStuff = connector.requestSocket(name); // loads in a new requested socket
		if (socketStuff != null) {
			String newName = (String) socketStuff[2]; // Extracting the name of the client
			if (name.equals("")) { // if the old name is empty, then we have recieved a new client.
				int length = newName.length();
				byte[] nameInBytes = new byte[length];
				HelpFunctions.insertString(nameInBytes, newName, 0, length);
				message = new Message(CONST.MESSAGE_START, 0, nameInBytes); // new client then we will return start message.
			} else // otherwise this is a reconnect
				message = new Message(CONST.MESSAGE_REFRESH, 0, new byte[0]); // old client then we will return refresh message.
			client = (Socket) socketStuff[1];
			name = newName;
			dataInputStream = (DataInputStream) socketStuff[3];
			dataOutputStream = (DataOutputStream) socketStuff[4];
			isConnected = true;
		}
		return message;
	}

	/*
	 * Try to retrive a message from client.
	 */
	public Message getMessage() {
		if (!isConnected) // if there is no connection, then we try to connect.
			return tryConnect();
		Message message = null;
		try {
			message = Communication.readInputStream(dataInputStream);
		} catch (IOException e) {
			if (client.isClosed())
				isConnected = false;
			Communication.printSomethingWentWrong(e);
		}
		return message;
	}

	/*
	 * Sends message to Client
	 */
	public void sendMessage(byte[] message) {
		if (!isConnected) // if there is no connection, then return nothing.
			return;
		try {
			Communication.printMessage(message);
			dataOutputStream.write(message);
			dataOutputStream.flush();
		} catch (IOException e) {
			if (client.isClosed())
				isConnected = false;
			Communication.printSomethingWentWrong(e);
		}
	}

	public void close() {
		if (!isConnected)
			return;
		try {
			client.close(); // If there is a socket then close.
		} catch (IOException e) {
			Communication.printSomethingWentWrong(e);
		}
	}
}
