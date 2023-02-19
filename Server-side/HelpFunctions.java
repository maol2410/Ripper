import java.util.Random;

public class HelpFunctions {
	private static Random rand = new Random();

	public static double nextDouble() {
		return rand.nextDouble();
	}

	public static int nextInt() {
		return rand.nextInt(101);
	}

	public static String convertToString(byte[] bytes, int index, int length) {
		int i = 0;
		for (; i < length; i++) {

			if (bytes[index + i] == 0) {
				break;
			}
		}

		return new String(bytes, index, i);
	}

	public static void insertString(byte[] message, String str, int index, int length) {
		if (str == null)
			str = "";
		for (int i = 0; i < length; i++) {
			if (i < str.length())
				message[i + index] = (byte) str.charAt(i);
			else
				message[i + index] = 0;
		}
	}
}
