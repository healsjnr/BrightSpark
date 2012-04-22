package com.healsjnr.brightspark;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.healsjnr.brightspark.Adapters.FavouriteLocationItemAdapter;
import com.healsjnr.brightspark.Adapters.MainViewPageAdapter;
import com.healsjnr.brightspark.database.FavouriteDatabaseAdapter;
import com.healsjnr.brightspark.jjp.api.TimeMode;
import com.healsjnr.brightspark.lib.FavouriteLocation;
import com.healsjnr.brightspark.lib.ForwardGeoCodeTask;
import com.healsjnr.brightspark.lib.IGeoCodedAddressReady;
import com.healsjnr.brightspark.lib.SimpleAddress;
import com.healsjnr.brightspark.lib.SimpleJourneyQuery;
import com.healsjnr.brightspark.lib.TimeUtils;
import com.healsjnr.brightspark.lib.ui.IPageChangedListener;
import com.healsjnr.brightspark.lib.ui.UITools;
import com.healsjnr.brightspark.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class JourneyPlanPage implements IGeoCodedAddressReady, IPageChangedListener {

	private Context m_context;
	private BrightSparkActivity m_parentActivity;
	private Vector<FavouriteLocation> m_favouriteLocations;
	private FavouriteDatabaseAdapter m_databaseHelper;

	private EditText m_fromLocation;
	private EditText m_toLocation;

	private ImageButton m_fromFavouriteButton;
	private ImageButton m_toFavouriteButton;
	private ImageButton m_fromCurrentLocation;
	private ImageButton m_toCurrentLocation;

	private Button m_goButton;

	private Spinner m_timeModeSpinner;

	private TextView m_timeTextView;
	private TextView m_dateTextView;

	private GregorianCalendar m_journeyTime;

	private AlertDialog m_favouriteDialog;
	private ProgressDialog m_progressDialog;

	private SimpleJourneyQuery m_simpleQuery;

	public final static String LEAVE_AFTER_STRING = "Leave After";
	public final static String ARRIVE_BY_STRING = "Arrive By";

	enum LocationEnum {
		From, To
	};

	private LocationEnum m_currentLocationGeoCoding;

	private Geocoder m_geoCoder;

	private FavouriteLocationItemAdapter m_fromLocationAdapter;
	private FavouriteLocationItemAdapter m_toLocationAdapter;

	private OnClickListener m_getFromFavouriteOnClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			FavouriteLocation loc = (FavouriteLocation) view.getTag();

			m_simpleQuery.setOrigin(loc.getLocation(), loc.getName());

			if (m_fromLocation != null) {
				m_fromLocation.setText(loc.getName());
			}
			if (m_favouriteDialog != null && m_favouriteDialog.isShowing()) {
				m_favouriteDialog.dismiss();
			}
		}
	};

	private OnClickListener m_getToFavouriteOnClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			FavouriteLocation loc = (FavouriteLocation) view.getTag();

			m_simpleQuery.setDestination(loc.getLocation(), loc.getName());

			if (m_toLocation != null) {
				m_toLocation.setText(loc.getName());
			}
			if (m_favouriteDialog != null && m_favouriteDialog.isShowing()) {
				m_favouriteDialog.dismiss();
			}
		}
	};

	private TimePickerDialog.OnTimeSetListener m_setTimeListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			updateTime(hour, minute);
		}
	};

	private DatePickerDialog.OnDateSetListener m_setDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			updateDate(year, month, day);
		}
	};

	public JourneyPlanPage(BrightSparkActivity parentActivity, Context context) {
		m_parentActivity = parentActivity;
		m_context = context;

		m_favouriteLocations = new Vector<FavouriteLocation>();
		m_databaseHelper = new FavouriteDatabaseAdapter(m_context,
				ApplicationState.DATABASE_NAME);

		m_journeyTime = (GregorianCalendar) Calendar.getInstance();

		m_simpleQuery = new SimpleJourneyQuery();

		populateLocations();

		m_fromLocationAdapter = new FavouriteLocationItemAdapter(m_context,
				m_parentActivity, R.id.favouriteListView, m_favouriteLocations,
				m_getFromFavouriteOnClick);

		m_toLocationAdapter = new FavouriteLocationItemAdapter(m_context,
				m_parentActivity, R.id.favouriteListView, m_favouriteLocations,
				m_getToFavouriteOnClick);

		String country = ApplicationState.COUNTRY_CODE;
		String language = ApplicationState.LANGUAGE;

		m_geoCoder = new Geocoder(m_context, new Locale(language, country));

	}

	public View initialiseJourneyPlanPage() {
		View page;

		LayoutInflater inflater = m_parentActivity.getLayoutInflater();
		page = inflater.inflate(R.layout.journeyplan_mainview, null, true);

		m_fromLocation = (EditText) page
				.findViewById(R.id.journeyPlannerFromEditText);

		m_fromLocation
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_GO) {
							m_currentLocationGeoCoding = LocationEnum.From;
							String addressText = m_fromLocation.getText()
									.toString();
							doAddressLookup(addressText);
							return true;
						}
						return false;
					}
				});

		m_toLocation = (EditText) page
				.findViewById(R.id.journeyPlannerToEditText);
		m_toLocation
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_GO) {
							m_currentLocationGeoCoding = LocationEnum.To;
							String addressText = m_toLocation.getText()
									.toString();
							doAddressLookup(addressText);
							return true;
						}
						return false;
					}
				});

		m_fromFavouriteButton = (ImageButton) page
				.findViewById(R.id.journeyPlanFromFavouriteButton);
		m_fromFavouriteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showFavouritesPopup(LocationEnum.From);
			}
		});

		m_toFavouriteButton = (ImageButton) page
				.findViewById(R.id.journeyPlanToFavouriteButton);
		m_toFavouriteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showFavouritesPopup(LocationEnum.To);
			}
		});

		m_fromCurrentLocation = (ImageButton) page
				.findViewById(R.id.journeyPlanCurrentLocationFromButton);
		m_fromCurrentLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentLocation(LocationEnum.From);
			}
		});

		m_toCurrentLocation = (ImageButton) page
				.findViewById(R.id.journeyPlanCurrentLocationToButton);
		m_toCurrentLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentLocation(LocationEnum.To);
			}
		});

		m_timeModeSpinner = (Spinner) page
				.findViewById(R.id.journeyPlanTimeModeSpinner);

		String[] timeModes = { LEAVE_AFTER_STRING, ARRIVE_BY_STRING };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_context,
				R.layout.spinner_default, timeModes);
		m_timeModeSpinner.setAdapter(adapter);

		m_timeTextView = (TextView) page
				.findViewById(R.id.journeyPlanTimeTextView);
		m_timeTextView.setText(TimeUtils.getSimpleTimeString(m_journeyTime
				.getTime()));
		m_timeTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog timeDialog = new TimePickerDialog(
						m_parentActivity, m_setTimeListener, m_journeyTime
								.get(Calendar.HOUR_OF_DAY), m_journeyTime
								.get(Calendar.MINUTE), true);
				timeDialog.show();
			}
		});

		m_dateTextView = (TextView) page
				.findViewById(R.id.journeyPlanDateTextView);
		m_dateTextView.setText(TimeUtils.getSimpleDateString(m_journeyTime
				.getTime()));
		m_dateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog dateDialog = new DatePickerDialog(
						m_parentActivity, m_setDateListener, m_journeyTime
								.get(Calendar.YEAR), m_journeyTime
								.get(Calendar.MONTH), m_journeyTime
								.get(Calendar.DATE));
				dateDialog.show();
			}
		});

		m_goButton = (Button) page.findViewById(R.id.journeyPlanGoButton);
		m_goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doJourneyPlan();
			}
		});

		return page;

	}

	private void populateLocations() {
		m_databaseHelper.open();

		m_favouriteLocations.clear();
		m_favouriteLocations.addAll(m_databaseHelper.fetchAllFavourites());

		m_databaseHelper.close();
	}

	private void showFavouritesPopup(LocationEnum location) {
		AlertDialog.Builder builder;

		View page;

		LayoutInflater inflater = m_parentActivity.getLayoutInflater();
		page = inflater.inflate(R.layout.favourite_dialog,
				(ViewGroup) m_parentActivity
						.findViewById(R.id.favouriteDialogRoot));

		ListView lv = (ListView) page
				.findViewById(R.id.favouriteDialogListView);

		switch (location) {
		case From:
			lv.setAdapter(m_fromLocationAdapter);
			break;
		case To:
			lv.setAdapter(m_toLocationAdapter);
			break;
		default:
			return;
		}

		builder = new AlertDialog.Builder(m_parentActivity);
		builder.setView(page);
		m_favouriteDialog = builder.create();
		m_favouriteDialog.show();
	}

	private void setCurrentLocation(LocationEnum location) {
		switch (location) {
		case From:
			m_fromLocation.setText(SimpleJourneyQuery.CURRENT_POSITION_STRING);
			m_simpleQuery.setOrigin(null,
					SimpleJourneyQuery.CURRENT_POSITION_STRING);
			break;
		case To:
			m_toLocation.setText(SimpleJourneyQuery.CURRENT_POSITION_STRING);
			m_simpleQuery.setDestination(null,
					SimpleJourneyQuery.CURRENT_POSITION_STRING);
			break;
		default:
			return;
		}

	}

	private void doAddressLookup(String addressText) {
		m_progressDialog = ProgressDialog.show(m_parentActivity, "",
				"Checking address...", true);

		ForwardGeoCodeTask geoCodeTask = new ForwardGeoCodeTask(m_geoCoder,
				this);

		geoCodeTask.execute(new String[] { addressText });

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

		if (addressList.size() != 1) {
			showAddressOptions(addressList);
		} else {
			SimpleAddress address = addressList.get(0);
			// Handle Address results properly.
			switch (m_currentLocationGeoCoding) {
			case From:
				m_fromLocation.setText(address.toString());
				m_simpleQuery.setOrigin(address.getGeoPosition(), address.toString());
				break;
			case To:
				m_toLocation.setText(address.toString());
				m_simpleQuery.setDestination(address.getGeoPosition(), address.toString());
				break;
			default:
				return;
			}
			
			m_currentLocationGeoCoding = null;
			
		}

	}

	private void doJourneyPlan() {
		String selectedTimeMode = (String) m_timeModeSpinner.getSelectedItem();
		if (selectedTimeMode.equals(LEAVE_AFTER_STRING)) {
			m_simpleQuery.setTimeMode(TimeMode.LeaveAfter);
		} else {
			m_simpleQuery.setTimeMode(TimeMode.ArriveBy);
		}

		m_simpleQuery.setDateTime(m_journeyTime.getTime());
		m_simpleQuery.setNumJourneys(3);

		if (m_simpleQuery.isValid()) {
			m_parentActivity.launchJourneyResultActivtiy(m_simpleQuery);
		}
	}

	private void updateTime(int hour, int minute) {
		m_journeyTime.set(Calendar.HOUR_OF_DAY, hour);
		m_journeyTime.set(Calendar.MINUTE, minute);
		m_timeTextView.setText(TimeUtils.getSimpleTimeString(m_journeyTime
				.getTime()));
	}

	private void updateDate(int year, int month, int day) {
		m_journeyTime.set(Calendar.DATE, day);
		m_journeyTime.set(Calendar.MONTH, month);
		m_journeyTime.set(Calendar.YEAR, year);
		m_dateTextView.setText(TimeUtils.getSimpleDateString(m_journeyTime
				.getTime()));
	}

	private void displayDialog(String messageText) {
		String messageType = "Oooops...";

		Dialog dialog = new AlertDialog.Builder(m_parentActivity).setIcon(0)
				.setTitle(messageType)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).setMessage(messageText).create();
		dialog.show();
	}

	private void showAddressOptions(List<SimpleAddress> addresses) {

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				AlertDialog alertDialog = (AlertDialog) dialog;
				SimpleAddress selectedAddress = (SimpleAddress) alertDialog
						.getListView().getItemAtPosition(position);
				switch (m_currentLocationGeoCoding) {
				case From:
					m_fromLocation.setText(selectedAddress.toString());
					m_simpleQuery.setOrigin(selectedAddress.getGeoPosition(), selectedAddress.toString());
					break;
				case To:
					m_toLocation.setText(selectedAddress.toString());
					m_simpleQuery.setDestination(selectedAddress.getGeoPosition(), selectedAddress.toString());
					break;
				default:
					return;
				}
				
				m_currentLocationGeoCoding = null;

				alertDialog.dismiss();
			}
		};

		AlertDialog dialog = UITools.createAddressDialog(m_parentActivity, addresses, listener);

		dialog.show();

	}

	@Override
	public void selectedPageUpdated(int position) {
		if (position == MainViewPageAdapter.PLAN_JOURNEY_INDEX)
		{
			populateLocations();
		}
	}
}
