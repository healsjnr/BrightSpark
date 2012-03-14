package com.healsjnr.brightspark.jjp.api;

public class StatusResponse
{

    public int Severity;

    public StatusDetail[] Details;
    
    @Override
    public String toString()
    {
    	String responseString = "Severity: " + Severity + "\n";
    	
    	if (Details != null)
    	{
    		for(StatusDetail detail : Details)
    		{
    			responseString += "Code: " + detail.Code + " Message: " + detail.Message + "\n";
    		}
    	}
    	
    	return responseString;
    }

}

