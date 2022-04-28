import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleThread extends Thread {
	private boolean keepRunning;

	public ConsoleThread() {
		keepRunning = true;
	}

	public void run() {
		System.out.println("Type \"stop\" to stop the server and close all connections.");
		if (!runConsole())
			runBufferedReader();
	}

	private boolean runConsole() {
		Console cnsl = System.console();
		if (cnsl == null)
			return false;
		String str = "";
		while (keepRunning) {
			str = cnsl.readLine("Server> ");
			str = str.toLowerCase();
			validStr(str);
		}
		return true;
	}

	private void runBufferedReader() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
		while (keepRunning) {
			try {
				str = reader.readLine();
			} catch (IOException e) {
				System.out.println("Something went wrong with runBufferedReader IO: " + e.getMessage());
			}
			str = str.toLowerCase();
			validStr(str);
		}
	}

	private void validStr(String str) {
		if (str.equals("stop") || str.equals("exit") || str.equals("close") || str.equals("quit"))
			keepRunning = false;
	}

	public boolean keepRunning() {
		return keepRunning;
	}

	public void gameOver() {
		keepRunning = false;
	}
}
