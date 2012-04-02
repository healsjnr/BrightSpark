package com.healsjnr.brightspark.jjp.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.jjp.proxy.JourneyPlannerProxy;
import com.healsjnr.brightspark.lib.TimeUtils;

import android.util.Log;

public class JourneyPlan {

	private Vector<Journey> m_journeys;
	private HashMap<String, ServiceProvider> m_serviceProviderMap;
	private HashMap<String, JJPLocation> m_allLocationMap;
	private HashMap<String, TransitLocation> m_transitLocations;
	private HashMap<String, GeoLocation> m_geoLocations;
	
	private String m_journeyOriginString;
	private String m_journeyDestinationString;
	
	private boolean m_journeyPlanSuccess;
	private String m_journeyPlanErrorMessage;
	
	public JourneyPlan()
	{
		m_serviceProviderMap = new HashMap<String, ServiceProvider>();
		m_allLocationMap = new HashMap<String, JJPLocation>();
		m_transitLocations = new HashMap<String, TransitLocation>();
		m_geoLocations = new HashMap<String, GeoLocation>();
		m_journeys = new Vector<Journey>();
		m_journeyPlanSuccess = false;
		m_journeyPlanErrorMessage = "Unitialised.";
		
	}
	
	public boolean isJourneyPlanSuccessful()
	{
		return m_journeyPlanSuccess;
	}
	
	public String getErrorMessage()
	{
		return m_journeyPlanErrorMessage;
	}
	
	public String toString()
	{
		String returnString = "Journey Count: " + this.m_journeys.size();
		return returnString;	
	}
	
	public String getJourneyOrigin()
	{
		return m_journeyOriginString;
	}
	
	public void setJourneyOrigin(String origin)
	{
		m_journeyOriginString = origin;
	}
	
	public String getJourneyDestination()
	{
		return m_journeyDestinationString;
	}
	
	public void setJourneyDestination(String destination)
	{
		m_journeyDestinationString = destination;
	}
	
	public Boolean hasLocation(JJPLocation loc)
	{
		return m_allLocationMap.containsKey(loc.getId());		
	}
	
	public JJPLocation getLocationById(String id)
	{
		if (m_allLocationMap.containsKey(id))
		{
			return (JJPLocation)m_allLocationMap.get(id);
		}
		else
		{
			return null;
		}
	}
	
	public TransitLocation getTransitLocation(String locUid)
	{
		if (m_transitLocations.containsKey(locUid))
		{
			return (TransitLocation)m_transitLocations.get(locUid);
		}
		else
		{
			return null;
		}
	}
	
	public GeoLocation getGeoLocationById(String id)
	{
		if (m_geoLocations.containsKey(id))
		{
			return (GeoLocation)m_geoLocations.get(id);
		}
		else
		{
			return null;
		}
	}
	
	public Boolean hasServiceProvider(String id)
	{
		return m_serviceProviderMap.containsKey(id);
	}
	
	public ServiceProvider getServiceProvider(String id)
	{
		if(hasServiceProvider(id))
		{
			return m_serviceProviderMap.get(id);
		}
		else
		{
			return null;
		}
	}
	
	public void addServiceProvider(ServiceProvider sp)
	{
		if (!hasServiceProvider(sp.getId()))
		{
			m_serviceProviderMap.put(sp.getId(), sp);
		}
	}
	
	public void addLocation(JJPLocation loc)
	{
		if (!m_allLocationMap.containsKey(loc.getId()))
		{
			m_allLocationMap.put(loc.getId(), loc);
		}

		if (loc instanceof TransitLocation)
		{
			if (!m_transitLocations.containsKey(loc.getId()))
			{
				m_transitLocations.put(loc.getId(), (TransitLocation)loc);
			
			}
		} else if(loc instanceof GeoLocation)
		{
			if (!m_geoLocations.containsKey(loc.getId()))
			{
				m_geoLocations.put(loc.getId(), (GeoLocation)loc);
			}
		}
		
		// #TODO should probably do something if type is not one of the two above? Although this should never happen..

	}
	
	public void setJourneys(Vector<Journey> journeys)
	{
		m_journeys = journeys;
	}
	
	public void addJourney(Journey journey)
	{
		m_journeys.add(journey);
	}
	
	public Vector<Journey> getJourneys()
	{
		return m_journeys;
	}
	
	public void parseJourneyPlan(String jsonJourneyPlan) throws Exception
	{
		JSONObject j = new JSONObject(jsonJourneyPlan);
		JSONObject request = j.getJSONObject("Request");
		JSONObject status = j.getJSONObject("Status");
		
		if (!parseStatus(status))
		{
			return;
		}
		
		JSONArray journeys;
		
		try {
			journeys = j.getJSONArray("Journeys");
		} catch (Exception e) {
			m_journeyPlanErrorMessage = "No Journeys.";
			Log.e(BrightSparkActivity.LOG_TAG,"parseJourneyPlan - No Journeys Found.");
			return;
		}
		
		JSONArray serviceProviders = j.getJSONArray("ServiceProviderReferenceData");
		JSONArray locations = j.getJSONArray("Locations");
		
		addLocationsFromJSONArray(locations);
		addServiceProvidersFromJSONArray(serviceProviders);
		addJourneysFromJSONArray(journeys);
		
		m_journeyPlanSuccess = true;
		m_journeyPlanErrorMessage = "OK";
		
	}

	private boolean parseStatus(JSONObject status) throws JSONException{
		
		if (status == null)
		{
			return false;
		}
		
		int severity = status.getInt("Severity");
		if (severity != 0)
		{
			m_journeyPlanErrorMessage = "";
			JSONArray details = status.getJSONArray("Details");
			for (int i = 0; i < details.length(); i++)
			{
				String messageDetails = ((JSONObject)details.get(i)).getString("Message");
				m_journeyPlanErrorMessage += messageDetails;
			}
			return false;
		}
				
		return true;
		
		
	}

	// Parses a JSON array of journeys into journey objects. 
	public void addJourneysFromJSONArray(JSONArray journeys) throws Exception
	{
		try
		{
			for(int i = 0; i < journeys.length(); i++)
			{
				//Get details from JSON object.
				JSONObject journey = journeys.getJSONObject(i);
				int id = journey.getInt("Id");
				
				String departString = journey.getString("DepartTime");
				String arriveString = journey.getString("ArriveTime");
				
				Date departDateTime = TimeUtils.getDateFromString(departString);
				Date arriveDateTime = TimeUtils.getDateFromString(arriveString);
								
				String origin = journey.getString("OriginLocationId");
				JJPLocation originLoc = this.getLocationById(origin);
				if (originLoc instanceof GeoLocation)
				{
					originLoc.setDescription(m_journeyOriginString);
				}				
								
				String dest = journey.getString("DestinationLocationId");
				JJPLocation destLoc = this.getLocationById(dest);
				if (destLoc instanceof GeoLocation)
				{
					destLoc.setDescription(m_journeyDestinationString);
				}
				
				int duration = journey.getInt("DurationMinutes");
				
				int changes = journey.getInt("ChangeCount");
				
				// Create new journey
				Journey jny = new Journey(id, originLoc, destLoc, departDateTime, arriveDateTime, duration, changes);
				
				// Add legs to journey
				JSONArray legs = journey.getJSONArray("Legs");
				addLegsToJourneyFromJSONArray(legs, jny);
				
				this.addJourney(jny);
				
			} 
		}
		catch (Exception e)
		{
			Log.e(BrightSparkActivity.LOG_TAG,"addJourneysFromJSONArray - Unexpected Exception: " + e.toString());
			throw e;
		}
		
	}
	
	public void addLegsToJourneyFromJSONArray(JSONArray legs, Journey journey) throws Exception
	{
		try
		{
			for(int i = 0; i < legs.length(); i++)
			{
				//Get base details from JSON object.
				JSONObject leg = legs.getJSONObject(i);
				String legType = leg.getString("__type");
				int id = leg.getInt("Id");
				JJPLocation origin = this.getLocationById(leg.getString("OriginLocationId"));
				JJPLocation dest = this.getLocationById(leg.getString("DestinationLocationId"));
				int duration = leg.getInt("DurationMinutes");
				String polyline = leg.optString("Polyline");
				
				if (legType.compareToIgnoreCase(JourneyPlannerProxy.TYPE_STRING_WALK_LEG) == 0)
				{
					int walkDistance = leg.getInt("WalkDistanceMetres");
					WalkLeg walkLeg = new WalkLeg(id, origin, dest, polyline, duration, walkDistance);
					journey.addLegToList(walkLeg);
				} 
				else if (legType.compareToIgnoreCase(JourneyPlannerProxy.TYPE_STRING_TRANSFER_LEG) == 0)
				{
					String mode = leg.getString("Mode");
					TransferLeg transferLeg = new TransferLeg(id, origin, dest, polyline, duration, mode);
					journey.addLegToList(transferLeg);
				}
				else if (legType.compareToIgnoreCase(JourneyPlannerProxy.TYPE_STRING_TRIP_LEG) == 0)
				{
					Date departTime = TimeUtils.getDateFromString(leg.getString("DepartTime")); 
					Date arriveTime = TimeUtils.getDateFromString(leg.getString("ArriveTime"));
					String mode = leg.getString("Mode");
					String tripUid = leg.optString("TripUid");
					String tripCode = leg.optString("TripCode");
					String routeName = leg.optString("RouteName");
					String routeCode = leg.optString("RouteCode");
					ServiceProvider sp = this.getServiceProvider(leg.getString("ServiceProviderUid"));
					String headsign = leg.optString("Headsign");
					
					TripLeg tripLeg = new TripLeg(id, origin, dest, polyline, duration, 
							departTime, arriveTime, mode, tripUid, tripCode, routeName, routeCode,
							sp, headsign);
					journey.addLegToList(tripLeg);										
				}
				else
				{
					Log.e(BrightSparkActivity.LOG_TAG,"Unexpected Leg Type String: " + legType);
					throw new Exception("Unexpected Leg Type String: " + legType);
				}
				
						
			} 
		}
		catch (Exception e)
		{
			Log.e(BrightSparkActivity.LOG_TAG,"addLegsToJourneyFromJSONArray - Unexpected Exception: " + e.toString());
			throw e;
		}
		
		
	}
	
	public void addServiceProvidersFromJSONArray(JSONArray serviceProviders) throws Exception
	{
		try
		{
			for(int i = 0; i < serviceProviders.length(); i++)
			{
				
				JSONObject sp = serviceProviders.getJSONObject(i); 
				String id = sp.getString("ServiceProviderUid");
				String name = sp.optString("Name");
				String timezone = sp.optString("TimeZone");
				String phoneNumber = sp.optString("PhoneNumber");
				String url = sp.optString("Url");
				
				ServiceProvider serviceProvider = new ServiceProvider(id, name, timezone, phoneNumber, url);
				
				this.addServiceProvider(serviceProvider);
								
			}
		} catch (Exception e)
		{
			Log.e(BrightSparkActivity.LOG_TAG,"addServiceProvidersFromJSONArray - Unexpected Exception: " + e.toString());
			throw e;
		}
	}
	
	public void addLocationsFromJSONArray(JSONArray locations) throws Exception
	{
		try
		{
			for(int i = 0; i < locations.length(); i++)
			{
				// Get Base Location properties. 
				String id = locations.getJSONObject(i).getString("Id");
				JSONObject loc = locations.getJSONObject(i).getJSONObject("Location");
				String locType = loc.getString("__type");
				String dataSet = loc.getString("DataSet");
				
				if (locType.compareTo(JourneyPlannerProxy.TYPE_STRING_TRANSIT_LOCATION) == 0)
				{
					// Get other properties and add to journey plan. 
					String description = loc.optString("Description");
					String stopUid = loc.optString("StopUid");
					String stopCode = loc.optString("Code");
					
					String positionString = loc.getString("Position");
					
					// Try parsing position string to GeoPosition
					GeoPosition pos;
					try 
					{
						pos = new GeoPosition(positionString);
					}
					catch (Exception e)
					{
						pos = null;
						Log.e(BrightSparkActivity.LOG_TAG,"addLocationsFromJSONArray - Unexpected Position String: " + positionString);
						throw e;
					}
				
					this.addLocation(new TransitLocation(id, dataSet, description, pos, stopCode, stopUid));	
				}
				else if (locType.compareTo(JourneyPlannerProxy.TYPE_STRING_GEO_LOCATION) == 0) 
				{
					// Get other properties and add to journey plan.
					String description = loc.optString("Description");
					String positionString = loc.getString("Position");
					
					// Try parsing position string to GeoPosition
					GeoPosition pos;
					try 
					{
						pos = new GeoPosition(positionString);
					}
					catch (Exception e)
					{
						pos = null;
						Log.e(BrightSparkActivity.LOG_TAG,"addLocationsFromJSONArray - Unexpected Position String: " + positionString);
						throw e;
					}
				
					this.addLocation(new GeoLocation(id, dataSet, pos, description));	
				}
				else
				{
					Log.e(BrightSparkActivity.LOG_TAG," addLocationsFromJSONArray - Unexpected Location Type String: " + locType);
					throw new Exception("Unexpected Location Type String: " + locType);
				}
			} 
		}
		catch (Exception e)
		{
			Log.e(BrightSparkActivity.LOG_TAG," addLocationsFromJSONArray - Unexpected Exception: " + e.toString());
			throw e;
		}
	}
}

