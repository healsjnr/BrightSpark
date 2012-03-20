package com.healsjnr.brightspark;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.healsjnr.brightspark.Adapters.FavouriteLocationItemAdapter;
import com.healsjnr.brightspark.Adapters.MainViewPageAdapter;
import com.healsjnr.brightspark.database.FavouriteDatabaseAdapter;
import com.healsjnr.brightspark.jjp.api.TimeMode;
import com.healsjnr.brightspark.lib.FavouriteLocation;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;
import com.healsjnr.brightspark.lib.ui.IPageChangedListener;
import com.healsjnr.brightspark.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FavouritesPage implements IPageChangedListener {

	private Context m_context;
	private BrightSparkActivity m_parentActivity;
	
	private ListView m_listView;
	private FavouriteLocationItemAdapter m_locationAdapter;
	private Button m_addFavouritesButton;
	private FavouriteDatabaseAdapter m_databaseHelper;
	
	private Vector<FavouriteLocation> m_favouriteLocations;
	
	private OnClickListener m_doJourneyOnClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			FavouriteLocation favouriteLoc = (FavouriteLocation) view.getTag();
			
			SimpleJourneyQuery journeyQuery = new SimpleJourneyQuery();
			journeyQuery.setOriginDescription(SimpleJourneyQuery.CURRENT_POSITION_STRING);
			journeyQuery.setDestination(favouriteLoc.getLocation(), favouriteLoc.getName());
			
			Calendar c = Calendar.getInstance();
			journeyQuery.setDateTime(c.getTime());
			
			journeyQuery.setTimeMode(TimeMode.LeaveAfter);
			
			m_parentActivity.launchJourneyResultActivtiy(journeyQuery);
		}
	};
	
	public FavouritesPage(BrightSparkActivity parentActivity, Context parentContext)
	{
		m_parentActivity = parentActivity;
		m_context = parentContext;
		
		m_favouriteLocations = new Vector<FavouriteLocation>();
		m_databaseHelper = new FavouriteDatabaseAdapter(m_context, ApplicationState.DATABASE_NAME);
	
		m_locationAdapter = new FavouriteLocationItemAdapter(m_context, m_parentActivity, R.layout.favourite_listview, m_favouriteLocations, m_doJourneyOnClick);
		populateLocations();
	}
	
	public View initialiseFavouritesPage()
	{
		View page;

		LayoutInflater inflater = m_parentActivity.getLayoutInflater();
		page = inflater.inflate(R.layout.favourites_mainview, null, true);
		
		m_addFavouritesButton = (Button) page.findViewById(R.id.favouriteAddButton);
		m_addFavouritesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				launchAddNewFavouriteActivity();
			}
		});
		
		
		
		m_listView = (ListView) page.findViewById(R.id.favouriteListView);
		m_listView.setAdapter(m_locationAdapter);
		page.setTag(m_locationAdapter);
		populateLocations();
		
		return page;
	}
	
	public void refreshData()
	{
		populateLocations();
	}
	
	private void populateLocations()
	{
		m_databaseHelper.open();
		
		m_favouriteLocations.clear();
		m_favouriteLocations.addAll(m_databaseHelper.fetchAllFavourites());
		
		m_databaseHelper.close();
				
		m_locationAdapter.notifyDataSetChanged();
		
	}
	
	private void launchAddNewFavouriteActivity()
	{
		Intent addFavouriteIntent = new Intent(m_parentActivity.getBaseContext(), AddFavouriteActivity.class);
		m_parentActivity.startActivityForResult(addFavouriteIntent, MainViewPageAdapter.FAVOURITE_INTENT);
		
	}

	@Override
	public void selectedPageUpdated(int position) {
		if (position == MainViewPageAdapter.FAVOURITES_INDEX)
		{
			populateLocations();
		}
	}
}
