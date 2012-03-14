package com.healsjnr.brightspark.jjp.proxy;

import com.healsjnr.brightspark.jjp.api.AvailableDataSetResponse;

import android.os.AsyncTask;

public class GetAvailableDataSetsTask extends AsyncTask<Void, Void, AvailableDataSetResponse> {

	IJourneyPlannerResponseListener m_responseListener;
	
	public GetAvailableDataSetsTask(IJourneyPlannerResponseListener responseListener)
	{
		m_responseListener = responseListener;
	}
	
	@Override
	protected AvailableDataSetResponse doInBackground(Void... arg0) {
		return JourneyPlannerProxy.getAvailableDataSets();
	}
	
	@Override
	protected void onPostExecute(AvailableDataSetResponse result) {
		m_responseListener.availableDataSetResponseReady(result);
	}

}
