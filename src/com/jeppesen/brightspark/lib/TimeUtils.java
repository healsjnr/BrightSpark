package com.jeppesen.brightspark.lib;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.jeppesen.brightspark.BrightSparkActivity;

import android.text.format.Time;
import android.util.Log;

public class TimeUtils {

	/**
	 * Creates a date based on a string in the format yyyy-MM-dd'T'HH:mm
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static Date getDateFromString(String dateString) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

		return df.parse(dateString);

	}
	
	/**
	 * Creates a date based on a string in the format yyyy-MM-dd'T'HH:mm
	 * Assumes date is UTC
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static Date getDateFromStringUTC(String dateString) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		return df.parse(dateString);

	}

	/**
	 * Creates a date based on a string in the format supplied
	 * @param dateString
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date getDateFromString(String dateString, String format)
			throws Exception {
		DateFormat df = new SimpleDateFormat(format);

		return df.parse(dateString);

	}
	
	/**
	 * Creates a date based on a string in the format supplied. Assume data is UTC
	 * @param dateString
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date getDateFromStringUTC(String dateString, String format)
			throws Exception {
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.parse(dateString);

	}

	/**
	 * Returns a date string in the format yyyy-MM-dd'T'HH:mm
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		String dateString = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			dateString = df.format(date);
		} catch (Exception e) {
			Log.e(BrightSparkActivity.LOG_TAG,
					"getDateString - Exception creating string from date. "
							+ e.toString());
		}

		return dateString;
	}
	
	/**
	 * Returns a UTC date string in the format yyyy-MM-dd'T'HH:mm
	 * @param date
	 * @return
	 */
	public static String getDateStringUTC(Date date) {
		String dateString = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			dateString = df.format(date);
		} catch (Exception e) {
			Log.e(BrightSparkActivity.LOG_TAG,
					"getDateString - Exception creating string from date. "
							+ e.toString());
		}

		return dateString;
	}

	/**
	 * Returns a date string in the format specified
	 * @param d
	 * @param format
	 * @return
	 */
	public static String getDateString(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);

	}
	
	/**
	 * Returns a UTC date string in the format specified
	 * @param d
	 * @param format
	 * @return
	 */
	public static String getDateStringUTC(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(d);

	}
	
	/**
	 * Returns the time portion of a date in the form HH:mm
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getSimpleTimeString(Date date) 
	{
		DateFormat df = new SimpleDateFormat("HH:mm");
		
		return df.format(date);
	}
	
	/**
	 * Returns the time portion of a date in the form HH:mm
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getSimpleTimeStringNow(Date date) 
	{
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("HH:mm");
		
		return df.format(c.getTime());
	}
	
	/**
	 * Returns the UTC time portion of a date in the form HH:mm
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getSimpleTimeStringUTC(Date date) 
	{
		DateFormat df = new SimpleDateFormat("HH:mm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(date);
	}
	
	/**
	 * Returns the Date portion of a date in the form dd/MM/yyyy
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getSimpleDateString(Date date) 
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		return df.format(date);
	}
	
	/**
	 * Returns the UTC Date portion of a date in the form dd/MM/yyyy 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getSimpleDateStringUTC(Date date) 
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		return df.format(date);
	}

	/**
	 * Returns a time string in the format HH:mm
	 * @param time
	 * @return
	 */
	public static String getTimeString(Time time) {

		int hour = time.hour;
		int minute = time.minute;

		String timeString = new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)).toString();

		return timeString;
	}
	
	/**
	 * Returns a time string in the format HH:mm
	 * @param time
	 * @return
	 */
	public static String getTimeString(Date dateTime) {

		Time time = new Time();
		time.set(dateTime.getTime());
		int hour = time.hour;
		int minute = time.minute;

		String timeString = new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)).toString();

		return timeString;
	}

	/**
	 * Returns a time string in the format HH:mm
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static String getTimeString(int hour, int minute) {
		String timeString = new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)).toString();

		return timeString;

	}

	/**
	 *  This returns time zone offset from GMT in the form HHmm (eg., +1000)
	 * @return
	 */
	public static String getCurrentTimeZoneInHours() {
		// HACK we can probably refactor this to use SimpleDateFormat with "zZ" which should convert to "+|-HHMM".
		Calendar c = GregorianCalendar.getInstance();
		int zoneOffsetMilli = c.get(Calendar.ZONE_OFFSET);
		int zoneOffsetMinutes = zoneOffsetMilli / 60000;
		int minutes = zoneOffsetMinutes % 60;
		int hours = zoneOffsetMinutes / 60;

		return pad(hours) + pad(minutes);
	}


	/**
	 * Convert a day of week int (ie., 0 - 6) to a 
	 * three letter day string (ie., Mon)
	 * HACK - got to be a better way to do this. 
	 * @param date
	 * @return
	 */
 	public static String getDayOfWeekString(Date date)
	{
		int dayOfWeek = date.getDay();
		
		switch(dayOfWeek)
		{
		case 0: 
			return "Sun";
		case 1:
			return "Mon";
		case 2:
			return "Tue";
		case 3:
			return "Wed";
		case 4:
			return "Thur";
		case 5:
			return "Fri";
		case 6:
			return "Sat";
		}
		
		return "";
	}
	
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

}
