package com.healsjnr.brightspark.lib;

import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;
import android.util.Log;

import com.healsjnr.brightspark.BrightSparkActivity;

public class DateTimeMask {
	
	private boolean[] m_dayMask;
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
		m_dayMask = new boolean[] {false, false, false, false, false, false, false};
	}

	public boolean[] getDayMask()
	{
		return m_dayMask;
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
