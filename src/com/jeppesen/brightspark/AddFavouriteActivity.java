package com.jeppesen.brightspark;

import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.jeppesen.brightspark.database.FavouriteDatabaseAdapter;
import com.jeppesen.brightspark.lib.FavouriteLocation;
import com.jeppesen.brightspark.lib.ForwardGeoCodeTask;
import com.jeppesen.brightspark.lib.GeoPosition;
import com.jeppesen.brightspark.lib.GetCurrentGeoLocationTask;
import com.jeppesen.brightspark.lib.IGeoCodedAddressReady;
import com.jeppesen.brightspark.lib.IGeoLocationReadyListener;
import com.jeppesen.brightspark.lib.LocationWrapper;
import com.jeppesen.brightspark.lib.ReverseGeoCodeTask;
import com.jeppesen.brightspark.lib.SimpleAddress;
import com.jeppesen.brightspark.lib.ui.UITools;
import com.jeppesen.brightspark.map.CustomMapView;
import com.jeppesen.brightspark.map.CustomMapView.OnLongpressListener;
import com.jeppesen.brightspark.map.MarkerOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddFavouriteActivity extends MapActivity implements
		IGeoLocationReadyListener, IGeoCodedAddressReady {

	private ImageButton m_currentLocationButton;
	private ImageButton m_searchButton;
	private Button m_saveButton;
	private ProgressDialog m_progressDialog;
	private CustomMapView m_mapView;
	private EditText m_addressInput;

	private MarkerOverlay m_markerOverlay;
	private List<Overlay> m_mapOverlays;
	private SimpleAddress m_favouriteAddress;

	private GeoPosition m_currentPosition;
	
	private Geocoder m_geoCoder;
	
	private FavouriteDatabaseAdapter m_dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_favourite_mainview);

		m_currentLocationButton = (ImageButton) findViewById(R.id.addFavouriteCurrentLocationButton);

		m_currentLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getCurrentLocation();
			}
		});

		m_mapView = (CustomMapView) findViewById(R.id.favouriteMap);

		m_mapView.setOnLongpressListener(new OnLongpressListener() {
			@Override
			public void onLongpress(final MapView view,
					final GeoPoint longpressLocation) {
				setFavouriteLocation(longpressLocation);
			}
		});

		m_mapOverlays = m_mapView.getOverlays();

		Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
		m_markerOverlay = new MarkerOverlay(drawable, this);

		m_addressInput = (EditText) findViewById(R.id.addFavouriteEditText);
		m_addressInput
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_GO) {
							parseAddress();
							return true;
						}
						return false;
					}
				});

		m_addressInput.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					parseAddress();
			}
		});

		m_searchButton = (ImageButton) findViewById(R.id.addFavouriteSearchButton);
		m_searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				parseAddress();
			}
		});

		m_saveButton = (Button) findViewById(R.id.addFavouriteSaveButton);
		m_saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkAddressDetails();
			}
		});

		String country = ApplicationState.COUNTRY_CODE;
		String language = ApplicationState.LANGUAGE;

		m_geoCoder = new Geocoder(getBaseContext(), new Locale(language,
				country));
		
		m_dbAdapter = new FavouriteDatabaseAdapter(this, ApplicationState.DATABASE_NAME);

	}

	private void checkAddressDetails() {
		if (m_favouriteAddress == null
				|| m_favouriteAddress.getGeoPosition() == null) {
			displayDialog("Select a point from map before saving.");
			return;
		}

		m_progressDialog = ProgressDialog.show(this, "", "Saving favourite...",
				true);

		// Note: If the user has selected a point from the map, we don't reverse
		// geo-code. Not really
		// worth it at the moment and the extra level of async task makes things
		// messy.

		showFavouriteNameDialog();
	}

	private void showFavouriteNameDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.favourite_name_dialog, null);
		final EditText favouriteNameEditText = (EditText) textEntryView
				.findViewById(R.id.favouriteNameDialogText);
		AlertDialog dialog = new AlertDialog.Builder(AddFavouriteActivity.this)
				.setTitle("Enter a name for this location.")
				.setView(textEntryView)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String favouriteName = favouriteNameEditText.getText()
								.toString();
						saveFavourite(favouriteName);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								return;
							}
						}).create();
		dialog.show();

	}

	private void saveFavourite(String favouriteName) {
		// Do saving.
		FavouriteLocation fav = new FavouriteLocation(favouriteName,
				m_favouriteAddress);

		m_dbAdapter.open();
		boolean result = m_dbAdapter.createFavouriteLocation(fav);
		m_dbAdapter.close();
		
		if (m_progressDialog.isShowing()) {
			m_progressDialog.dismiss();
		}
		
		if (!result)
		{
			displayDialog("Unable to save favourite...");
			return;
		}
		else
		{
			finish();
		}
	}

	private void getCurrentLocation() {
		m_progressDialog = ProgressDialog.show(this, "",
				"Getting current location...", true);

		LocationManager locMan = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationWrapper locWrapper = new LocationWrapper(locMan);

		locWrapper.startMonitoringBothProviders();

		GetCurrentGeoLocationTask geoLocTask = new GetCurrentGeoLocationTask(
				locWrapper, this);
		geoLocTask.execute(new Integer[] { 2 });
	}

	private void parseAddress() {
		String address = m_addressInput.getText().toString();
		if (address.isEmpty()) {
			return;
		}

		m_progressDialog = ProgressDialog.show(this, "", "Checking address...",
				true);
		ForwardGeoCodeTask geoCodeTask = new ForwardGeoCodeTask(m_geoCoder,
				this);

		geoCodeTask.execute(new String[] { address });
	}

	@Override
	public void geoLocationReady(GeoPosition currentPosition, boolean isGood) {

		m_progressDialog.dismiss();

		if (currentPosition == null) {
			displayDialog("Error getting current location, please try again later.");
			return;
		}

		m_currentPosition = currentPosition;
		m_mapView.navigateToLocation(currentPosition.getGeoPoint());
	}

	private void setCurrentPositionOverlay(GeoPosition p) {
		m_mapOverlays.clear();
		m_markerOverlay.clearOverlays();
		OverlayItem overlay = new OverlayItem(p.getGeoPoint(),
				"Current Location", "Current Location");
		m_markerOverlay.addOverlay(overlay);
		m_mapOverlays.add(m_markerOverlay);
	}

	private void setFavouriteLocation(GeoPoint p) {
		// Set Favourite Address to this position.
		// NOTE: there is no address yet. This isn't geo coded until the user
		// actually saves it.
		m_favouriteAddress = new SimpleAddress("", new GeoPosition(p));
		setCurrentPositionOverlay(m_favouriteAddress.getGeoPosition());
	}

	@Override
	public void geoCodedAddressReady(List<SimpleAddress> addressList) {

		if (m_progressDialog.isShowing()) {
			m_progressDialog.dismiss();
		}

		if (addressList == null || addressList.isEmpty()) {
			displayDialog("Error getting address info, please try again later.");
			return;
		}

		if (addressList.size() == 1) {
			m_favouriteAddress = addressList.get(0);
			setCurrentPositionOverlay(m_favouriteAddress.getGeoPosition());
			m_mapView.navigateToLocation(m_favouriteAddress.getGeoPosition()
					.getGeoPoint());
		} else {
			showAddressOptions(addressList);
		}
	}

	private void showAddressOptions(List<SimpleAddress> addresses) {
		
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int position) {
				AlertDialog alertDialog = (AlertDialog) dialog;
				m_favouriteAddress = (SimpleAddress) alertDialog
						.getListView().getItemAtPosition(
								position);
				setCurrentPositionOverlay(m_favouriteAddress
						.getGeoPosition());
				m_mapView.navigateToLocation(m_favouriteAddress
						.getGeoPosition().getGeoPoint());

				alertDialog.dismiss();
			}
		};
		
		AlertDialog dialog = UITools.createAddressDialog(this, addresses, listener);

		dialog.show();

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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
