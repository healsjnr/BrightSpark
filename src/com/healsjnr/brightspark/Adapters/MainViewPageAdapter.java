package com.healsjnr.brightspark.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;


import com.healsjnr.brightspark.BrightSparkActivity;
import com.healsjnr.brightspark.FavouritesPage;
import com.healsjnr.brightspark.JourneyPlanPage;
import com.healsjnr.brightspark.R;
import com.viewpagerindicator.TitleProvider;

public class MainViewPageAdapter extends PagerAdapter implements TitleProvider {
	
	public static final int FAVOURITE_INTENT = 0;
	public static final int COUNTDOWN_INTENT = 1;
	public static final int JOURNEY_PLAN_INTNET = 2;
	
	public static final int PLAN_JOURNEY_INDEX = 0;
	public static final int COUNTDOWN_INDEX = 1;
	public static final int FAVOURITES_INDEX = 2;
	public static final int PAGE_COUNT = 3;
	
	private FavouritesPage m_favouritesPage;
	private JourneyPlanPage m_journeyPage;
	
	
	// Parent activity and context members.  
	private BrightSparkActivity m_parentActivity;
	private Context m_context;
	
	private final String[] m_titles = new String [] {
			"Plan",
			"Countdown",
			"Favourites"
		};
	
	public MainViewPageAdapter(BrightSparkActivity parentActivity, Context parentCotnext)
	{
		m_parentActivity = parentActivity;
		m_context = parentCotnext;
		m_favouritesPage = new FavouritesPage(m_parentActivity, m_context);
		m_journeyPage = new JourneyPlanPage(m_parentActivity, m_context);
	}
	
	// Helper method to create the correct view based on the position
	private View generateViewPage(int position)
	{
		View page;
		LayoutInflater inflater = m_parentActivity.getLayoutInflater();
		
		switch (position) {
		case PLAN_JOURNEY_INDEX:
			page = m_journeyPage.initialiseJourneyPlanPage();
			break;
		case COUNTDOWN_INDEX:
			page = inflater.inflate(R.layout.countdown_mainview, null, true);
			break;
		case FAVOURITES_INDEX:
			page = m_favouritesPage.initialiseFavouritesPage();
			break;
		default:
			page = null;
			break;
		}
		
		return page;
		
	}
	
	public void updateView(int intentResult)
	{
		switch (intentResult) {
		case JOURNEY_PLAN_INTNET:
			break;
		case COUNTDOWN_INTENT:
			break;
		case FAVOURITE_INTENT:
			if (m_favouritesPage != null)
			{
				m_favouritesPage.refreshData();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		
		// The page that will become the view for this item in the view pager
		View page = generateViewPage(position);
		
		((ViewPager) collection).addView(page);
				
		return page;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	} 
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}
	
	@Override
	public String getTitle(int position) {
		return m_titles[position];
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

}
