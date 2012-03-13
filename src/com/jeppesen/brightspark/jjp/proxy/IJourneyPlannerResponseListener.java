package com.jeppesen.brightspark.jjp.proxy;

import com.jeppesen.brightspark.jjp.api.AvailableDataSetResponse;
import com.jeppesen.brightspark.jjp.api.JourneyPlan;

public interface IJourneyPlannerResponseListener {
	
	public void journeyPlanningReponseReady(JourneyPlan jp);
	
	public void availableDataSetResponseReady(AvailableDataSetResponse dataSets);

}
