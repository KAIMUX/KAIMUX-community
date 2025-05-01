package lt.itsvaidas.ZCAPI.tools;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TOOLS {

	private static final Random r = new Random();

    public static @NotNull String formatTime(long l) {
		long s = l;
		
		if (s > 31536000)
			s = (s - System.currentTimeMillis()) / 1000L;
		
		long m = 0;
		long h = 0;
		long d = 0;
		while (s >= 60) {
			s -= 60;
			m++;
			if (m >= 60) {
				m -= 60;
				h++;
				if (h >= 24) {
					h -= 24;
					d++;
				}
			}
		}
        if (d == 0) {
        	if (h == 0) {
        		if (m == 0) {
        			return s+"s";
        		}
        		return m+"m "+s+"s";
        	}
        	return h+"h "+m+"m "+s+"s";
        }
        return d+"d "+h+"h "+m+"m "+s+"s";
    }

	public static @NotNull String formatDate(long l) {
		if (l == -1)
			return "....-..-.. ..:..:..";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(new Date(l));
	}
	
	public static String formatDoubleAsMoney(double i) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,##0.00");
		return decimalFormat.format(i);
	}

	public static @NotNull List<String> cutText(@NotNull String text, int kas, String color) {
		List<String> returnas = new ArrayList<>();
		StringBuilder zodziai = new StringBuilder();
		int sk = 0;
		for (String zodis : text.split(" ")) {
			if (zodziai.isEmpty()) {
				zodziai = new StringBuilder(color + zodis);
			} else {
				zodziai.append(" ").append(zodis);
			}
			sk = sk + 1;
			if (sk == kas) {
				sk = 0;
				returnas.add(zodziai.toString());
				zodziai = new StringBuilder();
			}
		}
		returnas.add(zodziai.toString());
		return returnas;
	}

	/**
	 * Returns a random integer between min and max
	 * @param min - minimum value (inclusive)
	 * @param max - maximum value (inclusive)
	 * @return random integer
	 */
	public static int randomInt(int min, int max) {
	    return r.nextInt((max - min) + 1) + min;
	}

	public static @NotNull String randomString() {
		return randomString(10);
	}

	public static @NotNull String randomString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder returnas = new StringBuilder();
		for (int i = 0; i < length; i++) {
			returnas.append(chars.charAt(randomInt(0, chars.length() - 1)));
		}
		return returnas.toString();
	}
}
