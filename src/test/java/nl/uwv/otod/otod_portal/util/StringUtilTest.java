package nl.uwv.otod.otod_portal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;



public class StringUtilTest {

	@Test
	void testConvertStringContainingSpaceToInteger() {
		String thousand = "1 000";
		int result = StringUtil.convertStringContainingSpaceToInteger(thousand);
		assertEquals(1000, result);
	}
}
