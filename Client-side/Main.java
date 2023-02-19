public class Main {

	public static void main(String[] args) {
		Protocol protocol = new Protocol();
		Ripper ripper = new Ripper(protocol);
		Communication communication = new Communication(protocol);
		Thread thread = new Thread(communication);
		thread.start();
		protocol.addRiper(ripper);
		protocol.addCommunication(communication);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				protocol.sendQuit();
				communication.closeSocket();
			}
		}));

		ripper.run();
	}
}
