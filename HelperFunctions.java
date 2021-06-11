public class HelperFunctions {
	public static String convertToString(byte[] bytes) {
		return convertToString(bytes, 0);
	}

	public static String convertToString(byte[] bytes, int index) {

		return convertToString(bytes, index, 35);
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

	public static int translateToInt(byte[] bytes, int value) {
		int result = ((bytes[value] & 0xff) << 8) + (bytes[value + 1] & 0xff);
		return result;

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
