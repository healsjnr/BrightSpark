package com.healsjnr.brightspark;

import com.healsjnr.brightspark.Adapters.MainViewPageAdapter;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;
import com.healsjnr.brightspark.R;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class BrightSparkActivity extends Activity {
    /** Called when the activity is first created. */
	
	public static final String COUNTRY_CODE = "AU";
	public static final String LANGUAGE_CODE = "EN";
	public static String LOG_TAG = "BrightSpark";
	
	private MainViewPageAdapter m_mainViewAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        m_mainViewAdapter = new MainViewPageAdapter(this, getApplicationContext());
        ViewPager pager = (ViewPager) findViewById(R.id.mainViewPager);
	    pager.setAdapter(m_mainViewAdapter);
	    
        
	    TitlePageIndicator indicator = (TitlePageIndicator)findViewById( R.id.mainViewIndicator );
	    indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
	    indicator.setFooterIndicatorPadding(8f);
	    indicator.setViewPager(pager);
	    indicator.setOnPageChangeListener(m_mainViewAdapter.getOnPageChangedListener());
	    
	    
	    pager.setCurrentItem(MainViewPageAdapter.COUNTDOWN_INDEX);
        
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }
    public void launchJourneyResultActivtiy(SimpleJourneyQuery query)
    {
    	Intent doJourneyIntent = new Intent(this, JourneyResultsActivity.class);
		doJourneyIntent.putExtra(JourneyResultsActivity.JOURNEY_QUERY, query);
		startActivity(doJourneyIntent);
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	m_mainViewAdapter.updateView(requestCode);
    }
    
}