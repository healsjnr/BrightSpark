package com.healsjnr.brightspark.test;

import com.healsjnr.brightspark.jjp.api.TimeMode;
import com.healsjnr.brightspark.lib.CountdownJourney;
import com.healsjnr.brightspark.lib.FavouriteLocation;
import com.healsjnr.brightspark.lib.SimpleAddress;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;

import junit.framework.TestCase;

public class CountdownTest extends TestCase {
	
	public void testGetSimpleJourney()
	{
		SimpleAddress address = new SimpleAddress("133 test st, New Farm", 1.1, 2.2);
		FavouriteLocation fav = new FavouriteLocation("Home", address, 1);
		
		CountdownJourney journey = new CountdownJourney(fav, "Test 1");
		
		SimpleJourneyQuery query = journey.getNextJourneyQuery();
		
		assertNotNull(query);
		assertEquals(SimpleJourneyQuery.CURRENT_POSITION_STRING, query.getOriginDescription());
		assertEquals(fav.getName(), query.getDestinationDescription());
		assertEquals(TimeMode.LeaveAfter, query.getTimeMode());
		
		SimpleAddress fromAddress = new SimpleAddress("8 another st, New Farm", 5.1, 12.2);
		FavouriteLocation fromFav = new FavouriteLocation("Some Other Place", fromAddress, 2);
		
		CountdownJourney journeyWithOrigin = new CountdownJourney(fromFav, fav, "Test 2");
		
		SimpleJourneyQuery queryWithOrigin = journeyWithOrigin.getNextJourneyQuery();
		
		assertNotNull(queryWithOrigin);
		assertEquals(fromFav.getName(), queryWithOrigin.getOriginDescription());
		assertEquals(fav.getName(), queryWithOrigin.getDestinationDescription());
		assertEquals(TimeMode.LeaveAfter, queryWithOrigin.getTimeMode());
	
	}
	

}
