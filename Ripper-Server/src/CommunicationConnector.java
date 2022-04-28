import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CommunicationConnector extends Communication implements Runnable {
	private ServerSocket serverSocket;
	private ArrayList<Object[]> requestedSockets; // CommunicationLinks requested sockets.
	private ArrayList<Object[]> randomConnections; // All new sockets are added here before being processed.
	private boolean keepRunning;

	public CommunicationConnector(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		requestedSockets = new ArrayList<Object[]>();
		randomConnections = new ArrayList<Object[]>();
		keepRunning = true;
	}

	/*
	 * Adds a request to the list or if its already on the list then it looks for
	 * the requested socket.
	 */
	public Object[] requestSocket(String requestName) {
		searchRandomConnections(); // Processing all the socket that has not sent a message.
		for (int i = 0; i < requestedSockets.size(); i++) {
			Object[] socketName = requestedSockets.get(i); // Gets a line of the list (requests).
			// if socket name is on the list. Then we do not add name on the list
			if (((String) socketName[0]).equals(requestName)) {
				if (socketName[1] == null)
					return null;
				requestedSockets.remove(i);
				if (requestedSockets.size() == 0)
					closeAllRandomConnections();
				return socketName; // return all the information of the socket
			}
		}
		// if not on the list then we add the name to the list
		requestedSockets.add(new Object[] { requestName, null, null, null, null });
		return null;
	}

	/*
	 * If we do not have any requested socket, then we clear all random connections.
	 */
	private void closeAllRandomConnections() {
		for (int i = 0; i < randomConnections.size(); i++) {
			Socket socket = (Socket) randomConnections.get(i)[0];
			closeSocket(socket);
		}
		randomConnections.clear();
	}

	/*
	 * If the Server closes then we close all requested connections.
	 */
	private void closeAllRequestedConnections() {
		for (int i = 0; i < requestedSockets.size(); i++) {
			Socket socket = (Socket) requestedSockets.get(i)[1];
			closeSocket(socket);
		}
		requestedSockets.clear();
	}

	/*
	 * Helper method to close a socket.
	 */
	private void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Something went wrong when closing socket: " + e.getMessage());
		}
	}

	/*
	 * Searches for all connections that have a message otherwise they are stuck in
	 * random connections.
	 */
	private void searchRandomConnections() {
		for (int i = 0; i < randomConnections.size(); i++) {
			DataInputStream stream = (DataInputStream) randomConnections.get(i)[1];
			Message message = null;
			try {
				message = readInputStream(stream);
			} catch (IOException e) {
				return;
			}
			if (message != null) {
				if (message.type() == 105)
					checkNameList(new String(message.data()), randomConnections.get(i));
				else
					closeSocket((Socket) randomConnections.get(i)[1]);
				randomConnections.remove(i--);
			}
		}
	}

	/*
	 * If the name is on the list then we accept the socket to the list
	 */
	private void checkNameList(String name, Object[] randomConnection) {
		int index = getIndexOfNameOrEmptyName(name);
		if (index == -1) { // if not on the list then we discard the socket.
			closeSocket((Socket) randomConnection[1]);
			return;
		}
		Object[] request = requestedSockets.get(index);
		request[1] = randomConnection[0];
		request[2] = name;
		request[3] = randomConnection[1];
		request[4] = randomConnection[2];
	}

	/**
	 * Checks if the name matches with any requested line on the list.
	 */
	private int getIndexOfNameOrEmptyName(String name) {
		int result = getIndexOf(name);
		if (result == -1)
			return getIndexOf("");
		return result;
	}

	/*
	 * Checks if the exact name is on the Requested list.
	 */
	private int getIndexOf(String name) {
		for (int i = 0; i < requestedSockets.size(); i++) {
			String requestedName = (String) requestedSockets.get(i)[0];
			if (requestedName.equals(name))
				return i;
		}
		return -1;
	}

	public void close() {
		keepRunning = false;
	}

	/*
	 * When a socket is requested then function starts to listen for new sockets.
	 */

	@Override
	public void run() {
		while (keepRunning) {
			while (requestedSockets.size() > 0) {
				try {
					Socket client = serverSocket.accept();
					InetAddress clientAddress = client.getInetAddress();
					int clientPort = client.getPort();
					System.out.println(
							"A new client is connected\nInet address: " + clientAddress + "\nPort: " + clientPort);
					DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
					DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
					randomConnections.add(new Object[] { client, dataInputStream, dataOutputStream });
				} catch (IOException e) {
					printSomethingWentWrong(e);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Something went wrong when sleeping in connector");
			}
		}
		System.out.println("CommunicationConnector shuting down..");
		closeAllRandomConnections();
		closeAllRequestedConnections();
	}
}
