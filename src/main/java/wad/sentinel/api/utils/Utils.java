package wad.sentinel.api.utils;

import com.google.common.io.Files;

public class Utils {

	public Utils() {
	}

	public static String replaceNull(String str) {
		if (str == null) {
			str = "";
		}
		str = str.trim();
		return str;
	}

	public static String ajustarLongitud(String str, int len, boolean izquierda, String caracter) {
		if (str == null) {
			str = "";
		}
		if (izquierda) {
			str = ajustarLongitudIzquierda(str, len, caracter);
		} else {
			str = ajustarLongitudDerecha(str, len, caracter);
		}

		return str;
	}

	private static String ajustarLongitudDerecha(String str, int len, String caracter) {
		if (str == null) {
			str = "";
		}
		while (str.length() < len) {
			str = str + caracter;
		}

		return str;
	}

	private static String ajustarLongitudIzquierda(String str, int len, String caracter) {
		if (str == null) {
			str = "";
		}
		while (str.length() < len) {
			str = caracter + str;
		}
		return str;
	}

	/**
	 * Returns the file extension for the given file name, or the empty string if
	 * the file has no extension.
	 * The result does not include the '.'.
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileExtension(String filename) {
		return Files.getFileExtension(filename);
	}

	public static String replaceString(String str, String oldStr, String newStr) {
		return str.replace(oldStr, newStr);
	}
}
