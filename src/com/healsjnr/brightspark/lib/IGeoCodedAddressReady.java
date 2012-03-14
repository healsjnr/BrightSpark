package com.healsjnr.brightspark.lib;

import java.util.List;

// This interface can be used to handle responses from both Forward and Reverse Geo Coding request.
// Forward Geo Coding: In this case an address is encoded into a Geo Point. 
//   - To confirm the address though, we return a list of address suggested by google along with the geo points
//     for each. 
// Reverse Geo Coding: In this case we supply a geo point and receive back an address
//   - Again, a list of possible addresses are returned with theo point for each. 
// So in each case the below interface can be used as they both return a List of addresses. 

public interface IGeoCodedAddressReady {
	
	public void geoCodedAddressReady(List<SimpleAddress> addressList);

}
