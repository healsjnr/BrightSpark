package com.jeppesen.brightspark.Adapters;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jeppesen.brightspark.ApplicationState;
import com.jeppesen.brightspark.JourneyMapActivity;
import com.jeppesen.brightspark.JourneyResultsActivity;
import com.jeppesen.brightspark.R;
import com.jeppesen.brightspark.jjp.api.Journey;
import com.jeppesen.brightspark.jjp.api.JourneyPlan;
import com.jeppesen.brightspark.jjp.api.Leg;
import com.jeppesen.brightspark.lib.TimeUtils;
import com.viewpagerindicator.TitleProvider;

public class JourneyResultsAdapter extends PagerAdapter implements
		TitleProvider {

	private int m_numJourneys;
	private JourneyPlan m_journeyPlan;
	private List<Journey> m_journeys;

	private Activity m_parentActivity;
	private Context m_context;
	private JourneyLegAdapter m_journeyLegAdapter;

	public JourneyResultsAdapter(Activity parentActivity,
			Context parentContext, JourneyPlan journeyPlan) {
		m_parentActivity = parentActivity;
		m_context = parentContext;
		m_journeyPlan = journeyPlan;
		m_journeys = m_journeyPlan == null ? null : m_journeyPlan.getJourneys();
		m_numJourneys = m_journeys == null ? 0 : m_journeys.size();

	}

	@Override
	public String getTitle(int position) {
		if (m_journeys == null || position > m_journeys.size()) {
			return "";
		}
		return TimeUtils
				.getTimeString(m_journeys.get(position).getDepartTime());
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		View page;
		LayoutInflater inflater = m_parentActivity.getLayoutInflater();
		page = inflater.inflate(R.layout.singleresult_mainview, null, true);

		if (m_journeys == null || position > m_journeys.size()) {
			return page;
		}

		Journey currentJourney = m_journeys.get(position);
		
		JourneyLegAdapter journeyLegAdapter = new JourneyLegAdapter(m_context, m_parentActivity, R.layout.leg_transfer_listview, currentJourney.getLegList());

		ListView journeyList = (ListView) page.findViewById(R.id.singleJourneyListView);
		journeyList.setTag(currentJourney);
		
		journeyList.setAdapter(journeyLegAdapter);
		
		journeyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Journey jny = (Journey)parent.getTag();
				ApplicationState.getInstance().setMapJourney(jny);
				Intent showJourneyIntent = new Intent(m_parentActivity, JourneyMapActivity.class);
				showJourneyIntent.putExtra(JourneyMapActivity.JOURNEY_LEG , position);
				m_parentActivity.startActivity(showJourneyIntent);
			}
		});

		((ViewPager) collection).addView(page);

		return page;
	}

	@Override
	public int getCount() {
		return m_numJourneys;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
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
