package com.healsjnr.brightspark.Adapters;

import java.util.Vector;

import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.jjp.api.Leg;
import com.healsjnr.brightspark.jjp.api.TransferLeg;
import com.healsjnr.brightspark.jjp.api.TripLeg;
import com.healsjnr.brightspark.jjp.api.WalkLeg;
import com.healsjnr.brightspark.lib.TimeUtils;
import com.healsjnr.brightspark.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JourneyLegAdapter extends ArrayAdapter<Leg> {

	private Context m_context;
	private Activity m_parentActivity;
	private Vector<Leg> m_legs;

	public JourneyLegAdapter(Context context, Activity parentActivity,
			int textViewResourceId, Vector<Leg> legs) {
		super(context, textViewResourceId, legs);
		m_context = context;
		m_legs = legs;
		m_parentActivity = parentActivity;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (m_legs == null || position >= m_legs.size()) {
			return v;
		}

		Leg currentLeg = m_legs.get(position);

		if (currentLeg == null) {
			return v;
		}

		if (currentLeg instanceof WalkLeg) {
			v = buildWalkLegView((WalkLeg) currentLeg, v);
		} else if (currentLeg instanceof TripLeg) {
			v = buildTripLegView((TripLeg) currentLeg, v);
		} else if (currentLeg instanceof TransferLeg) {
			v = buildTransferLegView((TransferLeg) currentLeg, v);
		} else {
			Log.i(BrightSparkActivity.LOG_TAG,
					"JourneyLegAdapter - getView - unexepcted Leg class: "
							+ currentLeg.getClass().toString());
			return v;
		}

		v.setTag(currentLeg);
		return v;
	}

	private View buildWalkLegView(WalkLeg currentLeg, View v) {
		LayoutInflater vi = (LayoutInflater) m_parentActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.leg_walk_listview, null);
		

		TextView walkTime = (TextView) v.findViewById(R.id.walkLegTimeTextView);
		TextView walkDistance = (TextView) v
				.findViewById(R.id.walkLegDistanceTextView);

		walkTime.setText("Walk " + currentLeg.getDuration() + " minutes.");
		walkDistance.setText("(" + currentLeg.getWalkDistance() + "m)");

		return v;
	}

	private View buildTripLegView(TripLeg currentLeg, View v) {
		LayoutInflater vi = (LayoutInflater) m_parentActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.leg_trip_listview, null);
		

		TextView departTime = (TextView) v.findViewById(R.id.tripLegDepartTime);
		TextView departLocation = (TextView) v
				.findViewById(R.id.tripLegDepartLocation);
		TextView arriveTime = (TextView) v
				.findViewById(R.id.tripLegArrivalTime);
		TextView arriveLocation = (TextView) v
				.findViewById(R.id.tripLegArriveLocation);
		TextView routeNumber = (TextView) v.findViewById(R.id.tripLegRouteCode);
		TextView routeDescription = (TextView) v
				.findViewById(R.id.tripLegRouteDescription);

		departTime.setText(TimeUtils.getSimpleTimeString(currentLeg
				.getDepartTime()));
		departLocation.setText(currentLeg.getOrigin().getDescription());

		arriveTime.setText(TimeUtils.getSimpleTimeString(currentLeg
				.getArriveTime()));
		arriveLocation.setText(currentLeg.getDestination().getDescription());

		routeNumber.setText(currentLeg.getRouteCode());
		routeDescription.setText(currentLeg.getMode() + " - "
				+ currentLeg.getRouteName());

		return v;
	}

	private View buildTransferLegView(TransferLeg currentLeg, View v) {
		LayoutInflater vi = (LayoutInflater) m_parentActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.leg_transfer_listview, null);
		

		TextView transferLeg = (TextView) v
				.findViewById(R.id.transferLegTextView);
		transferLeg.setText(currentLeg.getDuration() + " minute "
				+ currentLeg.getMode() + " transfer.");

		return v;
	}

}
