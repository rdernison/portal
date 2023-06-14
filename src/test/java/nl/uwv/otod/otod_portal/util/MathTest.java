package nl.uwv.otod.otod_portal.util;

import org.junit.jupiter.api.Test;

public class MathTest {

	@Test
	public void testPower() {
		double i = 1.059463;
		double result = Math.pow(i, 12.0);
		System.out.println(result);
	}
}
