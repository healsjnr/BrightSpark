package com.jeppesen.brightspark.lib;

import java.util.Calendar;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationWrapper implements LocationListener {

	private final static int MIN_DISTANCE_NOTIFICATION = 0;
	private final static int MIN_TIME_NOTIFICATION = 0;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	// The max age of the geo code for it to be consider "good"
	private static final int GOOD_GEOCODE_MAX_AGE = TWO_MINUTES;
	// The max accuracy allowed for the geo code to deemed "good"
	private static final int GOOD_GEOCODE_MAX_ACCURACY= 100;
	
	private LocationManager m_locationManager;
	private Location m_lastKnownLocation;
	
	private boolean m_monitoringNetwork = false;
	private boolean m_monitoringGPS = false;
	
	
	public LocationWrapper(LocationManager locationManager)
	{
		m_locationManager = locationManager;
		setBestLastKnownLocatoin();
	}
	
	public boolean hasGoodGeoCode()
	{
		if (m_lastKnownLocation == null || m_locationManager == null)
		{
			return false;
		}
		
		boolean timeOk = false;
		Calendar c = Calendar.getInstance();
		long timeDelta =  c.getTimeInMillis() - m_lastKnownLocation.getTime();
		timeOk = timeDelta <= GOOD_GEOCODE_MAX_AGE;
		
		boolean accuracyOk = false;
		float accuracy = m_lastKnownLocation.getAccuracy();
		accuracyOk = (accuracy > 0) && (accuracy < GOOD_GEOCODE_MAX_ACCURACY);
		
		return timeOk && accuracyOk;
	}
	
	public Location getLastKnownAsLocation()
	{
		return m_lastKnownLocation;
	}
	
	public GeoPosition getLastKnownAsGeoPosition()
	{
		if (m_lastKnownLocation != null){				
			GeoPosition geoPos = new GeoPosition(m_lastKnownLocation.getLatitude(), m_lastKnownLocation.getLongitude());
			return geoPos;
		}
		
		return null;
	}
	
	/**
	 * Get's the last known Network and GPS locations, compares them using isBetterLocation (from http://developer.android.com/guide/topics/location/obtaining-user-location.html)
	 * and assigns the best to bestLocation
	 * 
	 * This is compared against the current m_lastKnownLocation, if it is better, updated last known. 
	 *   
	 */
	protected void setBestLastKnownLocatoin()
	{
		Location networkLocation = m_locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Location gpsLocation = m_locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location bestLocation;
		if(isBetterLocation(networkLocation, gpsLocation))
		{
			bestLocation = networkLocation;
		}
		else
		{
			bestLocation = gpsLocation;
		}
		
		if (m_lastKnownLocation == null)
		{
			m_lastKnownLocation = bestLocation;
		}
		else if (isBetterLocation(bestLocation, m_lastKnownLocation))
		{
			m_lastKnownLocation = bestLocation;
		}
	}
	
	public void startMonitoringBothProviders()
	{
		m_monitoringGPS = true;
		m_monitoringNetwork = true;
		
		m_locationManager.removeUpdates(this);
		setBestLastKnownLocatoin();
		m_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_NOTIFICATION , MIN_DISTANCE_NOTIFICATION, this);
		m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_NOTIFICATION , MIN_DISTANCE_NOTIFICATION, this);
	}
	
	public void startMonitoringNetwork()
	{
		m_monitoringNetwork = true;
		
		m_locationManager.removeUpdates(this);
		setBestLastKnownLocatoin();
		m_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_NOTIFICATION, MIN_DISTANCE_NOTIFICATION, this);
	}
	
	public void startMonitoringGPS()
	{
		m_monitoringGPS = true;
		
		m_locationManager.removeUpdates(this);
		setBestLastKnownLocatoin();
		m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_NOTIFICATION, MIN_DISTANCE_NOTIFICATION, this);
	}
	
	public void stopMonitoring()
	{
		m_monitoringGPS = false;
		m_monitoringNetwork = false;
		m_locationManager.removeUpdates(this);
	}
	
	public boolean isMonitoring()
	{
		return m_monitoringGPS || m_monitoringNetwork;		
	}
	
	public boolean isMonitoringNetwork()
	{
		return m_monitoringNetwork;
	}
	
	public boolean isMonitoringGPS()
	{
		return m_monitoringGPS;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (isBetterLocation(location, m_lastKnownLocation))
		{
			m_lastKnownLocation = location;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }
	    
	    if (location == null){
	    	// null is never better than the old location
	    	return false;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}


}
