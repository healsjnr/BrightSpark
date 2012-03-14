package com.healsjnr.brightspark.lib;

import java.util.List;
import java.util.Vector;

import com.healsjnr.brightspark.BrightSparkActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

public class ReverseGeoCodeTask extends
		AsyncTask<GeoPosition, Void, List<SimpleAddress>> {
	
	private IGeoCodedAddressReady m_addressReadyListener;
	private Geocoder m_geoCoder;
	
	public ReverseGeoCodeTask(Geocoder geoCoder, IGeoCodedAddressReady addressReadyListener)
	{
		m_addressReadyListener = addressReadyListener;
		m_geoCoder = geoCoder;
	}
	
	// Note for the reverse Geo Coder we only return the first result for the Address field. 
	@Override
	protected List<SimpleAddress> doInBackground(
			GeoPosition... currentLocation) {
		if (currentLocation.length != 1) {
			return null;
		}

		List<SimpleAddress> myAddressList = new Vector<SimpleAddress>();

		try {
			GeoPosition currentPos = currentLocation[0];
			List<Address> addressList = m_geoCoder.getFromLocation(
					currentPos.getLatitude(), currentPos.getLongitude(), 1);

			for (Address a : addressList) {
				myAddressList.add(new SimpleAddress(a));
			}

		} catch (Exception e) {				
			Log.i(BrightSparkActivity.LOG_TAG,
					"getGeoCodeFromAddress - Exception getting geoCode result."
							+ e.toString());
			return null;
		}

		return myAddressList;

	}
	
	@Override
	protected void onPostExecute(List<SimpleAddress> result) {
		m_addressReadyListener.geoCodedAddressReady(result);
	}

}
