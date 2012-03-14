package com.healsjnr.brightspark.jjp.proxy;

import com.healsjnr.brightspark.jjp.api.AvailableDataSetResponse;
import com.healsjnr.brightspark.jjp.api.JourneyPlan;

public interface IJourneyPlannerResponseListener {
	
	public void journeyPlanningReponseReady(JourneyPlan jp);
	
	public void availableDataSetResponseReady(AvailableDataSetResponse dataSets);

}
