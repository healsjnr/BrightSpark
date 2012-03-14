package com.healsjnr.brightspark.map;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.healsjnr.brightspark.jjp.api.Journey;
import com.healsjnr.brightspark.jjp.api.Leg;
import com.healsjnr.brightspark.jjp.api.TransferLeg;
import com.healsjnr.brightspark.jjp.api.TripLeg;
import com.healsjnr.brightspark.jjp.api.WalkLeg;

public class JourneyOverlay extends Overlay {

	private Journey m_journey;

	public void setJourneyPlan(Journey jny) {
		m_journey = jny;
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Vector<Leg> legList = m_journey.getLegList();

		for (Leg l : legList) {
			Path p = plotLeg(mapView, l.getPolyline());

			Paint mPaint = new Paint();
			if (l instanceof WalkLeg) {
				mPaint.setColor(0xAAFFAA33);
			} else if (l instanceof TransferLeg) {
				mPaint.setColor(0xAA000000);
			} else if (l instanceof TripLeg) {
				mPaint.setColor(0xAA0000FF);
			} else {
				mPaint.setColor(0xAAFFFFF);
			}
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3);
			mPaint.setAntiAlias(true);
			canvas.drawPath(p, mPaint);

		}

		super.draw(canvas, mapView, shadow);

	}

	private Path plotLeg(MapView mapView, Vector<GeoPoint> polyline) {
		Projection projection = mapView.getProjection();
		Path p = new Path();

		for (int i = 0; i < polyline.size(); i++) {
			if (i == polyline.size() - 1) {
				break;
			}
			Point from = new Point();
			Point to = new Point();
			projection.toPixels(polyline.get(i), from);
			projection.toPixels(polyline.get(i + 1), to);

			if (i == 0) {
				p.moveTo(from.x, from.y);
			}

			p.lineTo(to.x, to.y);
		}

		return p;

	}

}
