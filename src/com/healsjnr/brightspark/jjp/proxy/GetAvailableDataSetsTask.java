package com.healsjnr.brightspark.jjp.proxy;

import com.healsjnr.brightspark.jjp.api.AvailableDataSetResponse;

import android.os.AsyncTask;

public class GetAvailableDataSetsTask extends AsyncTask<Void, Void, AvailableDataSetResponse> {

	IJourneyPlannerResponseListener m_responseListener;
	JourneyPlannerProxy m_jpProxy;
	
	public GetAvailableDataSetsTask(IJourneyPlannerResponseListener responseListener)
	{
		m_responseListener = responseListener;
		m_jpProxy = new JourneyPlannerProxy();
	}
	
	@Override
	protected AvailableDataSetResponse doInBackground(Void... arg0) {
		return m_jpProxy.getAvailableDataSets();
	}
	
	@Override
	protected void onPostExecute(AvailableDataSetResponse result) {
		m_responseListener.availableDataSetResponseReady(result);
	}

}
