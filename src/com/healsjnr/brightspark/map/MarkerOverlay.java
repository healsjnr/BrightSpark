package com.healsjnr.brightspark.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MarkerOverlay extends ItemizedOverlay<OverlayItem> {

	Context m_parentContext;
	List<OverlayItem> m_overlayItems = new ArrayList<OverlayItem>();
	Drawable m_defaultMarker;
				
	public MarkerOverlay(Drawable defaultMarker, Context parent)
	{
		super(defaultMarker);
		
		m_defaultMarker = defaultMarker;
		boundCenterBottom(m_defaultMarker);
		m_parentContext = parent;
						
	}
	
	public void addOverlay(OverlayItem item)
	{
		m_overlayItems.add(item);
		populate();
	}
	
	public void addOverlay(List<OverlayItem> items)
	{
		m_overlayItems.addAll(items);
		populate();
	}
	
	public void clearOverlays()
	{
		m_overlayItems.clear();
		populate();		
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return m_overlayItems.get(i);
	}

	@Override
	public int size() {
		return m_overlayItems.size();
	}
	
	@Override
	protected boolean onTap(int i)
	{
		Toast.makeText(m_parentContext, m_overlayItems.get(i).getSnippet(), Toast.LENGTH_SHORT).show();
		return true;
	}
	

}
