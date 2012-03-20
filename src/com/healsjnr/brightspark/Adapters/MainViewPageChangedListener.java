package com.healsjnr.brightspark.Adapters;

import java.util.List;

import com.healsjnr.brightspark.FavouritesPage;
import com.healsjnr.brightspark.JourneyPlanPage;
import com.healsjnr.brightspark.lib.ui.IPageChangedListener;

import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

public class MainViewPageChangedListener extends SimpleOnPageChangeListener {
	
	private IPageChangedListener[] m_pageChangedListeners;
	
	public MainViewPageChangedListener(IPageChangedListener[] pageChangedListeners)
	{
		m_pageChangedListeners = pageChangedListeners;
	}
	
	@Override
	public void onPageSelected(int position) {
		super.onPageSelected(position);
		for(IPageChangedListener pagelistener : m_pageChangedListeners)
		{
			pagelistener.selectedPageUpdated(position);
		}
	}

}
