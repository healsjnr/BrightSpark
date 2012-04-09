package com.healsjnr.brightspark.Adapters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.JourneyResultsActivity;
import com.healsjnr.brightspark.jjp.api.TimeMode;
import com.healsjnr.brightspark.lib.FavouriteLocation;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;
import com.healsjnr.brightspark.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FavouriteLocationItemAdapter extends
		ArrayAdapter<FavouriteLocation> {

	private Context m_context;
	private BrightSparkActivity m_parentActivity;
	private List<FavouriteLocation> m_locations;
	private OnClickListener m_onClickListener;
	
	public FavouriteLocationItemAdapter(Context context, BrightSparkActivity parentActivity, int textViewResourceId, List<FavouriteLocation> locations, OnClickListener clickEvent) {
		super(context, textViewResourceId, locations);
		m_context = context;
		m_locations = locations;
		m_parentActivity = parentActivity;
		m_onClickListener = clickEvent;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) m_parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.favourite_listview, null);
		}
		
		FavouriteLocation fav = m_locations.get(position);
		
		if (fav != null)
		{
			TextView name = (TextView) v.findViewById(R.id.favouriteListViewName);
			name.setText(fav.getName());
			TextView address = (TextView) v.findViewById(R.id.favouriteListViewAddress);
			address.setText(fav.getAddress().toString());
			
			v.setTag(fav);
			v.setOnClickListener(m_onClickListener);

		}
		
		return v;
		
	}
	

}
