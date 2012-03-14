package com.healsjnr.brightspark;

import java.util.List;
import java.util.Vector;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.healsjnr.brightspark.jjp.api.JJPLocation;
import com.healsjnr.brightspark.jjp.api.Journey;
import com.healsjnr.brightspark.jjp.api.JourneyPlan;
import com.healsjnr.brightspark.jjp.api.Leg;
import com.healsjnr.brightspark.map.JourneyOverlay;
import com.healsjnr.brightspark.map.MarkerOverlay;
import com.healsjnr.brightspark.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class JourneyMapActivity extends MapActivity {
	
	public final static String JOURNEY_LEG = "JourneyLeg";

	private Journey m_journey;
	private JourneyOverlay m_journeyOverlay;
	private List<Overlay> m_mapOverlays;
	private MapView m_mapView;
	private MarkerOverlay m_markerOverlay;
	private int m_numLegs;
	private int m_legPosition;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.journey_map_view);

		m_mapView = (MapView) findViewById(R.id.theMap); // Get map from XML
		m_mapOverlays = m_mapView.getOverlays();

		m_journey = ApplicationState.getInstance().getMapJourney();
		if (m_journey == null) {
			return;
		}
		
		m_numLegs = m_journey.getLegList().size();

		m_journeyOverlay = new JourneyOverlay();
		Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
		m_markerOverlay = new MarkerOverlay(drawable,this);
		
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey(JOURNEY_LEG)) {
			m_legPosition = extras.getInt(JOURNEY_LEG);
			if (m_legPosition < 0 || m_legPosition >= m_numLegs)
			{
				m_legPosition = 0;
			}
		} else {
			m_legPosition = 0;
		}
	
		displayJourney();
	}

	private void displayJourney() {

		Vector<Leg> legs = m_journey.getLegList();
		if (legs.size() == 0) {
			return;
		}

		GeoPoint selectedLegPosition = legs.get(m_legPosition).getOrigin().getPosition()
				.getGeoPoint(); 
		
		for(int i = 0; i < legs.size(); i++)
		{
			
			Leg leg = legs.get(i);
			JJPLocation orig = leg.getOrigin();
			JJPLocation dest = leg.getDestination();
			String stopTitle = leg.getOrigin().getDescription();
			String stopSnippet = leg.toString();
			if (i == legs.size() - 1)
			{
				OverlayItem destinationOverlay = new OverlayItem(dest.getPosition().getGeoPoint(), dest.getDescription(), "Arrive Destination");
				m_markerOverlay.addOverlay(destinationOverlay);
				
			}
			
			OverlayItem overlay = new OverlayItem(orig.getPosition().getGeoPoint(), stopTitle, stopSnippet);
			m_markerOverlay.addOverlay(overlay);
			
		}

				
		m_journeyOverlay.setJourneyPlan(m_journey);
		m_mapOverlays.add(m_journeyOverlay);
		m_mapOverlays.add(m_markerOverlay);
		
		navigateToLocation(selectedLegPosition);

	}

	private void navigateToLocation(GeoPoint p) {

		// GeoPoint
		m_mapView.displayZoomControls(true); // display Zoom (seems that it doesn't
		// work yet)
		MapController mc = m_mapView.getController();
		mc.animateTo(p); // move map to the given point
		int zoomlevel = m_mapView.getMaxZoomLevel(); // detect maximum zoom level
		mc.setZoom(zoomlevel - 1); // zoom
		m_mapView.setSatellite(false); // display only "normal" mapview
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
