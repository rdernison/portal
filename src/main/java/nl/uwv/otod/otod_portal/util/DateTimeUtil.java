package nl.uwv.otod.otod_portal.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import nl.uwv.otod.otod_portal.model.Month;

public class DateTimeUtil {

	public static LocalDateTime parseDateTime(String input) {
		return LocalDateTime.parse(input);
	}

	public static LocalDate parseDate(String input) {
		return LocalDate.parse(input);
	}
	
	public static LocalDate parseDutchDate(String dutchDate) {
		// input: dinsdag, 4. augustus 2020 - 11:53
		var dayDate = dutchDate.split(",");
		return getLocalDate(dayDate[1]);
	}
	
	private static LocalDate getLocalDate(String input) {
		var dateAndTime = input.split("\\w\\-\\w");
		return parseLocalDate(dateAndTime[0].trim());
	}
	
	private static LocalDate parseLocalDate(String input) {
		var date = input.trim();
		var ddMMyyyy = date.split(" ");
		var day = Integer.parseInt(ddMMyyyy[0].substring(0, ddMMyyyy[0].length() - 1));
		var monthIndex = Month.valueOf(ddMMyyyy[1].toUpperCase()).getIndex();
		var year = Integer.parseInt(ddMMyyyy[2]);
		return LocalDate.of(year, monthIndex, day);
	}

	public static LocalDateTime parseDutchDateTime(String dutchDateTime) {
		// input: dinsdag, 4. augustus 2020 - 11:53
		var dayDate = dutchDateTime.split(",");
		return getLocalDateTime(dayDate[1]);
	}

	private static LocalDateTime getLocalDateTime(String input) {
		var dateAndTime = input.split("\\s\\-\\s");
		var localDate = parseLocalDate(dateAndTime[0]);
		var time = dateAndTime[1];
		var hh = Integer.parseInt(time.substring(0, time.indexOf(':')));
		var mm = Integer.parseInt(time.substring(time.indexOf(':') + 1));
		return LocalDateTime.of(localDate, LocalTime.of(hh, mm));
	}

}
