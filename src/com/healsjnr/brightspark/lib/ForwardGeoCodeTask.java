package com.jeppesen.brightspark.lib;

import java.util.List;
import java.util.Vector;

import com.jeppesen.brightspark.BrightSparkActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

public class ForwardGeoCodeTask extends
		AsyncTask<String, Void, List<SimpleAddress>> {
	
	private IGeoCodedAddressReady m_addressReadyListener;
	private Geocoder m_geoCoder;
	
	public ForwardGeoCodeTask(Geocoder geoCoder, IGeoCodedAddressReady addressReadyListener)
	{
		m_addressReadyListener = addressReadyListener;
		m_geoCoder = geoCoder;
	}
	
	@Override
	protected List<SimpleAddress> doInBackground(String... addresses) {
		
		if (addresses.length != 1) {
			return null;
		}

		List<SimpleAddress> myAddressList = new Vector<SimpleAddress>();

		try {

			List<Address> addressList = m_geoCoder.getFromLocationName(
					addresses[0], 1); // From addresses

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
