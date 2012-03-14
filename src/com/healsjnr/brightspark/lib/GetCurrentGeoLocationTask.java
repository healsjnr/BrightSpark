package com.healsjnr.brightspark.lib;

import android.os.AsyncTask;

public class GetCurrentGeoLocationTask extends
		AsyncTask<Integer, Void, GeoPosition> {

	private LocationWrapper m_locationWrapper;
	private IGeoLocationReadyListener m_locationReadyListener;
	private boolean m_isGoodFix;

	public GetCurrentGeoLocationTask(LocationWrapper locwrapper,
			IGeoLocationReadyListener geoReadyListener) {
		m_locationWrapper = locwrapper;
		m_locationReadyListener = geoReadyListener;
	}

	@Override
	protected GeoPosition doInBackground(Integer... seconds) {

		if (seconds.length != 1 || m_locationWrapper == null) {
			return null;
		}

		m_isGoodFix = false;
		int checkTime = seconds[0];
		int checkCount = 0;

		while (!m_isGoodFix && checkCount < checkTime) {
			m_isGoodFix = m_locationWrapper.hasGoodGeoCode();

			if (!m_isGoodFix) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			checkCount++;
		}

		return m_locationWrapper.getLastKnownAsGeoPosition();

	}

	@Override
	protected void onPostExecute(GeoPosition currentLoc) {
		m_locationWrapper.stopMonitoring();
		m_locationReadyListener.geoLocationReady(currentLoc, m_isGoodFix);
	}
}
