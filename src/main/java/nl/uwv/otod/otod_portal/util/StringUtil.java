package nl.uwv.otod.otod_portal.util;

public class StringUtil {

	public static Integer convertStringContainingSpaceToInteger(String input) {
		int value = 0;
		int spaceIndex = input.indexOf(" ");
		if (spaceIndex == -1) {
			value = Integer.parseInt(input);
		} else {
			String left = input.substring(0, spaceIndex);
			String right = input.substring(spaceIndex + 1);
			value = Integer.parseInt(left + right);
		}
		return value;
	}
}
