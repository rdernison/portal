package nl.uwv.otod.otod_portal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.ChronoField;

import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.model.Month;

public class DateTimeUtilTest {

	@Test
	public void testParseDutchDate() {
		var input = "dinsdag, 4. augustus 2020 - 11:53";
		var date = DateTimeUtil.parseDutchDate(input);
		var dd = date.get(ChronoField.DAY_OF_MONTH);
		var mm = date.get(ChronoField.MONTH_OF_YEAR);
		var yyyy = date.get(ChronoField.YEAR);
		
		assertEquals(4, dd);
		assertEquals(8, mm);
		assertEquals(2020, yyyy);
	}
	
	@Test
	public void testParseDutchdateTime() {
		var input = "dinsdag, 4. augustus 2020 - 11:53";
		var dateTime = DateTimeUtil.parseDutchDateTime(input);
		var dd = dateTime.get(ChronoField.DAY_OF_MONTH);
		var mm = dateTime.get(ChronoField.MONTH_OF_YEAR);
		var yyyy = dateTime.get(ChronoField.YEAR);
		var hh = dateTime.get(ChronoField.HOUR_OF_DAY);
		var min = dateTime.get(ChronoField.MINUTE_OF_HOUR);
		assertEquals(4, dd);
		assertEquals(8, mm);
		assertEquals(2020, yyyy);
		assertEquals(11, hh);
		assertEquals(53, min);
		
	}
	
	@Test
	public void testMonth() {
		var jan = Month.valueOf("JANUARI").getIndex();
		var feb = Month.valueOf("FEBRUARI").getIndex();
		var maa = Month.valueOf("MAART").getIndex();
		var apr = Month.valueOf("APRIL").getIndex();
		var mei = Month.valueOf("MEI").getIndex();
		var jun = Month.valueOf("JUNI").getIndex();
		var jul = Month.valueOf("JULI").getIndex();
		var aug = Month.valueOf("AUGUSTUS").getIndex();
		var sep = Month.valueOf("SEPTEMBER").getIndex();
		var okt = Month.valueOf("OKTOBER").getIndex();
		var nov = Month.valueOf("NOVEMBER").getIndex();
		var dec = Month.valueOf("DECEMBER").getIndex();
		
		assertEquals(1, jan);
		assertEquals(2, feb);
		assertEquals(3, maa);
		assertEquals(4, apr);
		assertEquals(5, mei);
		assertEquals(6, jun);
		assertEquals(7, jul);
		assertEquals(8, aug);
		assertEquals(9, sep);
		assertEquals(10, okt);
		assertEquals(11, nov);
		assertEquals(12, dec);
	}
}
