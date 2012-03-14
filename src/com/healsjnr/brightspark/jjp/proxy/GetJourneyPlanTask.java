package com.healsjnr.brightspark.jjp.proxy;

import com.healsjnr.brightspark.ApplicationState;
import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.jjp.api.JourneyPlan;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;
import com.healsjnr.brightspark.lib.TimeUtils;

import android.os.AsyncTask;
import android.util.Log;

public class GetJourneyPlanTask extends AsyncTask<SimpleJourneyQuery, Void, JourneyPlan> {

	IJourneyPlannerResponseListener m_responseListener;

	public GetJourneyPlanTask(IJourneyPlannerResponseListener responseListener)
	{
		m_responseListener = responseListener;
	}
	
	@Override
	protected JourneyPlan doInBackground(SimpleJourneyQuery... queries) {
		if (queries.length != 1)
		{
			Log.e(BrightSparkActivity.LOG_TAG, "GetJourneyPlanTask - doInBackground - invalid number of journeys plans.");
			return null;
		}
		SimpleJourneyQuery query = queries[0];
		return JourneyPlannerProxy.doJourneyPlan(ApplicationState.DATA_SET, query.getOrigin().toString(),
				query.getDestination().toString(), TimeUtils.getDateString(query.getDateTime()), 
				query.getTimeMode().toString(), query.getNumJourneys());
		
	}
	
	@Override
	protected void onPostExecute(JourneyPlan result) {
		m_responseListener.journeyPlanningReponseReady(result);
	}

}
