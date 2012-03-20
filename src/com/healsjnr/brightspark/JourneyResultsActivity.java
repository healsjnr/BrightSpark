package com.healsjnr.brightspark;

import java.util.List;

import com.healsjnr.brightspark.Adapters.JourneyResultsAdapter;
import com.healsjnr.brightspark.Adapters.MainViewPageAdapter;
import com.healsjnr.brightspark.jjp.api.AvailableDataSetResponse;
import com.healsjnr.brightspark.jjp.api.JourneyPlan;
import com.healsjnr.brightspark.jjp.proxy.GetJourneyPlanTask;
import com.healsjnr.brightspark.jjp.proxy.IJourneyPlannerResponseListener;
import com.healsjnr.brightspark.lib.GeoPosition;
import com.healsjnr.brightspark.lib.GetCurrentGeoLocationTask;
import com.healsjnr.brightspark.lib.IGeoCodedAddressReady;
import com.healsjnr.brightspark.lib.IGeoLocationReadyListener;
import com.healsjnr.brightspark.lib.LocationWrapper;
import com.healsjnr.brightspark.lib.SimpleAddress;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;
import com.healsjnr.brightspark.R;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.Toast;

public class JourneyResultsActivity extends Activity implements
		IJourneyPlannerResponseListener, IGeoLocationReadyListener {

	public final static String JOURNEY_QUERY = "JourneyQuery";

	private SimpleJourneyQuery m_journeyQuery;
	private JourneyResultsAdapter m_journeyAdapter;
	private JourneyPlan m_journeyPlan;

	private ProgressDialog m_progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (m_journeyPlan != null) {
			journeyPlanningReponseReady(m_journeyPlan);
		} else {

			Bundle extras = getIntent().getExtras();
			if (extras.containsKey(JOURNEY_QUERY)) {
				m_journeyQuery = extras.getParcelable(JOURNEY_QUERY);
			} else {
				// No Journey query - so finish.
				finish();
			}

			m_progressDialog = ProgressDialog.show(this, "Journey Plan",
					"", true);

			if (!prepareForJourneyPlan()) {
				Toast.makeText(this,
						"Origin and Destination are both current location.",
						Toast.LENGTH_LONG);
				m_progressDialog.dismiss();
				finish();
			}
		}
	}

	private boolean prepareForJourneyPlan() {

		// NOTE: Actually this validation method should probably be on
		// SimpleJourneyQuery

		String originDescription = m_journeyQuery.getOriginDescription();
		String destDescription = m_journeyQuery.getDestinationDescription();

		if (m_journeyQuery.getOrigin() == null
				&& m_journeyQuery.getDestination() == null) {
			return false;
		}

		if (originDescription
				.equals(SimpleJourneyQuery.CURRENT_POSITION_STRING)
				&& destDescription
						.equals(SimpleJourneyQuery.CURRENT_POSITION_STRING)) {
			return false;
		} else if (originDescription
				.equals(SimpleJourneyQuery.CURRENT_POSITION_STRING)
				|| destDescription
						.equals(SimpleJourneyQuery.CURRENT_POSITION_STRING)) {
			
			if (m_progressDialog != null && m_progressDialog.isShowing())
			{
				m_progressDialog.setMessage("Getting current location...");
			}
			
			LocationManager locMan = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			LocationWrapper locWrapper = new LocationWrapper(locMan);

			locWrapper.startMonitoringBothProviders();

			GetCurrentGeoLocationTask geoLocTask = new GetCurrentGeoLocationTask(
					locWrapper, this);
			geoLocTask.execute(new Integer[] { 2 });
		} else {
			// Neither origin nor destination are current location, so both
			// should have geo positions.
			doJourneyPlan();
		}

		return true;

	}

	@Override
	public void geoLocationReady(GeoPosition currentPosition, boolean isGood) {

		if (currentPosition == null) {
			displayDialog("Error getting current location, please try again later.");
			finish();
		}

		if (m_journeyQuery.getOrigin() == null) {
			m_journeyQuery.setOriginLocation(currentPosition);
		} else if (m_journeyQuery.getDestination() == null) {
			m_journeyQuery.setDestinationLocation(currentPosition);
		} else {
			Log.e(BrightSparkActivity.LOG_TAG,
					"JourneyResultsActivity - geoLocationReady - Wasn't expecting a geo task to return...");
			finish();
		}

		doJourneyPlan();
	}

	private void doJourneyPlan() {
		if (m_progressDialog != null && m_progressDialog.isShowing())
		{
			m_progressDialog.setMessage("Getting trip details...");
		}
		
		GetJourneyPlanTask journeyTask = new GetJourneyPlanTask(this);
		journeyTask.execute(new SimpleJourneyQuery[] { m_journeyQuery });
	}

	@Override
	public void journeyPlanningReponseReady(JourneyPlan jp) {
		if (m_progressDialog.isShowing()) {
			m_progressDialog.dismiss();
		}
		
		m_journeyPlan = jp;

		if (jp == null || jp.getJourneys() == null || jp.getJourneys().size() == 0) {
			// NOTE: Need to put in Error handling.
			displayDialog("No Journeys Found", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					JourneyResultsActivity.this.finish();
				}
			});
			
			return;
		}
		
		setContentView(R.layout.journeyplan_results_mainview);

		m_journeyAdapter = new JourneyResultsAdapter(this,
				getApplicationContext(), jp);
		ViewPager pager = (ViewPager) findViewById(R.id.mainViewPager);
		pager.setAdapter(m_journeyAdapter);

		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.mainViewIndicator);
		indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
		indicator.setFooterIndicatorPadding(8f);
		indicator.setViewPager(pager);
	}

	@Override
	public void availableDataSetResponseReady(AvailableDataSetResponse dataSets) {
	}

	private void displayDialog(String messageText) {
		String messageType = "Oooops...";

		Dialog dialog = new AlertDialog.Builder(this).setIcon(0)
				.setTitle(messageType)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).setMessage(messageText).create();
		dialog.show();
	}
	
	private void displayDialog(String messageText, DialogInterface.OnClickListener listener) {
		String messageType = "Oooops...";

		Dialog dialog = new AlertDialog.Builder(this).setIcon(0)
				.setTitle(messageType)
				.setPositiveButton("Ok", listener).setMessage(messageText).create();
		dialog.show();
	}

}
