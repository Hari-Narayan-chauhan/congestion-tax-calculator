package congestion.tax.calculator.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import congestion.tax.calculator.entity.CityEntity;
import congestion.tax.calculator.entity.HolidayCalendarEntity;
import congestion.tax.calculator.entity.HolidayMonthsEntity;

public class DateUtils {

	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Boolean isHolidayMonth(HolidayMonthsEntity holidayMonthsEntity, int month) {
		if (holidayMonthsEntity == null)
			return false;

		return (holidayMonthsEntity.isJanuary() && month == (Calendar.JANUARY + 1))
				|| (holidayMonthsEntity.isFebruary() && month == (Calendar.FEBRUARY + 1))
				|| (holidayMonthsEntity.isMarch() && month == (Calendar.MARCH + 1))
				|| (holidayMonthsEntity.isApril() && month == (Calendar.APRIL + 1))
				|| (holidayMonthsEntity.isMay() && month == (Calendar.MAY + 1))
				|| (holidayMonthsEntity.isJune() && month == (Calendar.JUNE + 1))
				|| (holidayMonthsEntity.isJuly() && month == (Calendar.JULY + 1))
				|| (holidayMonthsEntity.isAugust() && month == (Calendar.AUGUST + 1))
				|| (holidayMonthsEntity.isSeptember() && month == (Calendar.SEPTEMBER + 1))
				|| (holidayMonthsEntity.isOctober() && month == (Calendar.OCTOBER + 1))
				|| (holidayMonthsEntity.isNovember() && month == (Calendar.NOVEMBER + 1))
				|| (holidayMonthsEntity.isDecember() && month == (Calendar.DECEMBER + 1));

	}

	public static Boolean isBeforeOrAftertOrInPublicHoliday(Date date, CityEntity cityEntity) {
		Set<HolidayCalendarEntity> publicHolidays = cityEntity.getHolidayCalendarEntities();
		if (publicHolidays == null || publicHolidays.isEmpty())
			return false;

		// Same day
		return publicHolidays.stream()
				.anyMatch(holiday -> org.apache.commons.lang3.time.DateUtils.isSameDay(holiday.getDate(), date))
				|| publicHolidays.stream().anyMatch(holiday -> isDateBetweenEndPoints(holiday.getDate(), 	// free days after holidays
						org.apache.commons.lang3.time.DateUtils.addDays(holiday.getDate(),
								cityEntity.getCitySpecificRulesEntity().getNoOfTaxFreeDaysAfterPublicHolidays()),
						date))
				|| publicHolidays.stream().anyMatch(holiday -> isDateBetweenEndPoints(holiday.getDate(),// free days before holidays
						org.apache.commons.lang3.time.DateUtils.addDays(holiday.getDate(),
								-(cityEntity.getCitySpecificRulesEntity().getNoOfTaxFreeDaysAfterPublicHolidays())),
						date));
	

		

	}

	private static boolean isDateBetweenEndPoints(Date startDate, Date endDate, Date date) {

		return !(date.before(startDate) || date.after(endDate));

	}

	public static String formatDate(Date date) {
		return DATEFORMAT.format(date);
	}

}
