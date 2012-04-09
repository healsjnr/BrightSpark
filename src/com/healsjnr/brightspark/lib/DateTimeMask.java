package com.healsjnr.brightspark.lib;

import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;
import android.util.Log;

import com.healsjnr.brightspark.BrightSparkActivity;

public class DateTimeMask {
	
	private static char NULL_CHAR = '-';
	
	private boolean[] m_dayMask = new boolean[7];
	private Date m_startTime;
	private Date m_endTime;
	
	public DateTimeMask()
	{
		clearMask();
	}
	
	public DateTimeMask(boolean[] dayMask, Date startTime, Date endTime)
	{
		if (dayMask.length != 7)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DateTimeMask(): invalid day mask - wrong number of elements: " + dayMask.length);
			return;
		}
		
		m_dayMask = dayMask;
		m_startTime = startTime;
		m_endTime = endTime;
	}
	
	public Date getStartTime()
	{
		return m_startTime;
	}
	
	public void setStartTime(Date time)
	{
		m_startTime = time;
	}
	
	public Date getEndTime()
	{
		return m_endTime;
	}
	
	public void setEndTime(Date time)
	{
		m_endTime = time;
	}
	
	public void clearMask()
	{
		for (int i = 0; i < m_dayMask.length; i++)
		{
			m_dayMask[i] = false;
		}
	}

	public boolean[] getDayMask()
	{
		return m_dayMask;
	}
	
	public String getDayMaskString()
	{
		char[] dayChars = new char[7];
		for (int i = 0; i < dayChars.length; i++)
		{
			dayChars[i] = NULL_CHAR;
		}
		
		if (m_dayMask[0]) dayChars[0] = 'S';
		if (m_dayMask[1]) dayChars[1] = 'M';
		if (m_dayMask[2]) dayChars[2] = 'T';
		if (m_dayMask[3]) dayChars[3] = 'W';
		if (m_dayMask[4]) dayChars[4] = 't';
		if (m_dayMask[5]) dayChars[5] = 'F';
		if (m_dayMask[6]) dayChars[6] = 's';
		
		return new String(dayChars);
	}
	
	public void setDayMask(boolean[] dayMask)
	{
		if (dayMask.length != 7)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DateTimeMask(): invalid day mask - wrong number of elements: " + dayMask.length);
			return;
		}
		
		m_dayMask = dayMask;
	}
	
	public void setDayMaskFromString(String dayMask)
	{
		if (dayMask.length() != 7)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DateTimeMask(): invalid day mask string - wrong number of elements: " + dayMask.length());
			return;
		}
		
		for(int i = 0; i < dayMask.length(); i++)
		{
			m_dayMask[i] = (dayMask.charAt(i) != NULL_CHAR); 
		}
		
	}
	
	public void setAllWeekdays()
	{
		m_dayMask[Calendar.MONDAY-1] = true;
		m_dayMask[Calendar.TUESDAY-1] = true;
		m_dayMask[Calendar.WEDNESDAY-1] = true;
		m_dayMask[Calendar.THURSDAY-1] = true;
		m_dayMask[Calendar.FRIDAY-1] = true;
	}
	
	public void setAllWeekend()
	{
		m_dayMask[Calendar.SATURDAY-1] = true;
		m_dayMask[Calendar.SUNDAY-1] = true;
	}
	
	public void setDay(int day)
	{
		if (day < 1 || day > 7)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DateTimeMask - setDay: Invalid day integer: " + day);
			return;
		}
		
		m_dayMask[day-1] = true;
	}
	
	public boolean isSetForDay(int day)
	{
		if (day < 1 || day > 7)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "DateTimeMask - setDay: Invalid day integer: " + day);
			return false;
		}
		
		return m_dayMask[day-1];
		
	}
	
	public boolean isSetForToday()
	{
		Calendar c = Calendar.getInstance();
		int currentDay = c.get(Calendar.DAY_OF_WEEK);
		return m_dayMask[currentDay-1];
	}
	
	public boolean isRunningNow()
	{
		if (!isSetForToday())
		{
			return false;
		}
		
		int minutesPastMidnight = TimeUtils.convertTimeToMinutesPastMidnight(Calendar.getInstance());
		int startMinutes = TimeUtils.convertTimeToMinutesPastMidnight(m_startTime);
		int endMinutes = TimeUtils.convertTimeToMinutesPastMidnight(m_endTime);
		
		return (minutesPastMidnight >= startMinutes) && (minutesPastMidnight <= endMinutes);		
		
	}

}
