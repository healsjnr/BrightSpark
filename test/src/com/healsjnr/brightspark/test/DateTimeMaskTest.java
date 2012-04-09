package com.healsjnr.brightspark.test;

import java.util.Calendar;
import java.util.Date;

import com.healsjnr.brightspark.lib.DateTimeMask;

import junit.framework.TestCase;

public class DateTimeMaskTest extends TestCase {
	
	public void testSetMask()
	{
		int day = Calendar.THURSDAY;
		
		DateTimeMask dtMask = new DateTimeMask();
		dtMask.setDay(day);
		
		assertTrue(dtMask.isSetForDay(day));

		assertFalse(dtMask.isSetForDay(Calendar.SUNDAY));
		assertFalse(dtMask.isSetForDay(Calendar.MONDAY));
		assertFalse(dtMask.isSetForDay(Calendar.TUESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.WEDNESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.FRIDAY));
		assertFalse(dtMask.isSetForDay(Calendar.SATURDAY));
		
	}
	
	public void testSetForToday()
	{
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		
		DateTimeMask dtMask = new DateTimeMask();
		dtMask.setDay(day);
		
		assertTrue(dtMask.isSetForToday());
	}
	
	public void testSetMaskForWeekday()
	{
	
		DateTimeMask dtMask = new DateTimeMask();
		dtMask.setAllWeekdays();

		assertTrue(dtMask.isSetForDay(Calendar.MONDAY));
		assertTrue(dtMask.isSetForDay(Calendar.TUESDAY));
		assertTrue(dtMask.isSetForDay(Calendar.WEDNESDAY));
		assertTrue(dtMask.isSetForDay(Calendar.THURSDAY));
		assertTrue(dtMask.isSetForDay(Calendar.FRIDAY));
		assertFalse(dtMask.isSetForDay(Calendar.SATURDAY));
		assertFalse(dtMask.isSetForDay(Calendar.SUNDAY));
		
	}
	
	public void testSetMaskForWeekend()
	{
	
		DateTimeMask dtMask = new DateTimeMask();
		dtMask.setAllWeekend();

		assertFalse(dtMask.isSetForDay(Calendar.MONDAY));
		assertFalse(dtMask.isSetForDay(Calendar.TUESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.WEDNESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.THURSDAY));
		assertFalse(dtMask.isSetForDay(Calendar.FRIDAY));
		assertTrue(dtMask.isSetForDay(Calendar.SATURDAY));
		assertTrue(dtMask.isSetForDay(Calendar.SUNDAY));
		
	}
	
	public void testSetMaskUsingString()
	{
	
		DateTimeMask dtMask = new DateTimeMask();
		
		String dateTimeString = "-MTW-Fs";
		
		dtMask.setDayMaskFromString(dateTimeString);

		assertTrue(dtMask.isSetForDay(Calendar.MONDAY));
		assertTrue(dtMask.isSetForDay(Calendar.TUESDAY));
		assertTrue(dtMask.isSetForDay(Calendar.WEDNESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.THURSDAY));
		assertTrue(dtMask.isSetForDay(Calendar.FRIDAY));
		assertTrue(dtMask.isSetForDay(Calendar.SATURDAY));
		assertFalse(dtMask.isSetForDay(Calendar.SUNDAY));
		
		dateTimeString = "-------";
		
		dtMask.setDayMaskFromString(dateTimeString);

		assertFalse(dtMask.isSetForDay(Calendar.MONDAY));
		assertFalse(dtMask.isSetForDay(Calendar.TUESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.WEDNESDAY));
		assertFalse(dtMask.isSetForDay(Calendar.THURSDAY));
		assertFalse(dtMask.isSetForDay(Calendar.FRIDAY));
		assertFalse(dtMask.isSetForDay(Calendar.SATURDAY));
		assertFalse(dtMask.isSetForDay(Calendar.SUNDAY));
		
		dateTimeString = "SMTWtFs";
		
		dtMask.setDayMaskFromString(dateTimeString);

		assertTrue(dtMask.isSetForDay(Calendar.MONDAY));
		assertTrue(dtMask.isSetForDay(Calendar.TUESDAY));
		assertTrue(dtMask.isSetForDay(Calendar.WEDNESDAY));
		assertTrue(dtMask.isSetForDay(Calendar.THURSDAY));
		assertTrue(dtMask.isSetForDay(Calendar.FRIDAY));
		assertTrue(dtMask.isSetForDay(Calendar.SATURDAY));
		assertTrue(dtMask.isSetForDay(Calendar.SUNDAY));

		
	}
	
	public void testSetTimes()
	{
		Calendar c = Calendar.getInstance();
		
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.HOUR, -1);
		
		Calendar endTime = Calendar.getInstance();
		endTime.add(Calendar.HOUR, 1);
	
		DateTimeMask dtMask = new DateTimeMask();
		dtMask.setDay(c.get(Calendar.DAY_OF_WEEK));
		dtMask.setStartTime(startTime.getTime());
		dtMask.setEndTime(endTime.getTime());
		
		assertTrue(dtMask.isRunningNow());
	}

}
