package com.meters.nawasco;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.meters.nawasco.helpers.FontManager;
import com.meters.nawasco.helpers.LocationService;
import com.meters.nawasco.helpers.Model;
import com.meters.nawasco.helpers.SessionManager;
import com.u1aryz.android.lib.newpopupmenu.PopupMenu;
import com.u1aryz.android.lib.newpopupmenu.PopupMenu.OnItemSelectedListener;

public class MenuMain extends SherlockListActivity implements OnItemSelectedListener {
	LocationService locator;
	String lat, lng, location;
	Config config;
	String content;
	private JSONObject json;
	JSONArray jArray;
	String meter_owner, meter_no, username, password, _latitude, _longitude;
	SessionManager session;
	private ListAdapter listadapter;
	String meters;
	JSONObject meters_arr;
	ListView lv;
	Model model=new Model();
	ArrayAdapter<String> actionsAdapter;
	static final int CUSTOM_DIALOG_ID1 = 1;
	private final static int VIEWMAP = 0;
	private final static int READ_METER = 1;
	private final static int SEARCH = 2;
	ArrayList<HashMap<String, String>> entry = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		meters = pref.getString("meters", null);
		lv = (ListView) findViewById(android.R.id.list);
		// lv.setSelector(R.drawable.list_selector);
		loadList();
		config = new Config();
		locator = new LocationService(MenuMain.this);
		if (locator.canGetLocation()) {

			double latitude = locator.getLatitude();
			double longitude = locator.getLongitude();
			lat = Double.toString(latitude);
			lng = Double.toString(longitude);
			location = lat + "," + lng;
			setTitle("Current Location:" + location);

		} else {
			locator.showSettingsAlert();
		}

		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// show popup
				PopupMenu menu = new PopupMenu(getApplicationContext());
				menu.setOnItemSelectedListener(MenuMain.this);
				menu.add(VIEWMAP, R.string.menu_map).setIcon(
						getResources().getDrawable(
								android.R.drawable.ic_menu_mapmode));
				menu.add(READ_METER, R.string.menu_read).setIcon(
						getResources().getDrawable(
								android.R.drawable.ic_menu_edit));
				menu.add(SEARCH, R.string.menu_exit).setIcon(
						getResources().getDrawable(
								android.R.drawable.ic_menu_mapmode));
				menu.show(arg1);
				//retrieve a meter lat+lng
				TextView tv_lat,tv_lng;
				tv_lat=(TextView)arg1.findViewById(R.id.tv_lat);
				tv_lng=(TextView)arg1.findViewById(R.id.tv_lng);
				_latitude=tv_lat.getText().toString();
				_longitude=tv_lng.getText().toString();
				model.set_latitude(_latitude);
				model.set_longitude(_longitude);
				Log.d("sark", model.get_latitude());
				
			}
		});
	}

	public void loadList() {
		try {
			jArray = new JSONArray(meters);
			Log.d("yes", "wood" + jArray);
			JSONObject json_data = null;
			for (int i = 0; i < jArray.length(); i++) {

				HashMap<String, String> map = new HashMap<String, String>();
				json_data = jArray.getJSONObject(i);
				meter_owner = json_data.getString("cust_name");
				meter_no = json_data.getString("plot_no");
				_latitude = json_data.getString("lat");
				_longitude = json_data.getString("lon");
				map.put("owner", meter_owner);
				map.put("plot", meter_no);
				map.put("lat", _latitude);
				map.put("lon", _longitude);
				entry.add(map);

			}
			listadapter = new SimpleAdapter(MenuMain.this, entry,
					R.layout.row_menu, new String[] { "owner", "plot", "lat",
							"lon" }, new int[] { R.id.title, R.id.artist,
							R.id.tv_lat, R.id.tv_lng });

			setListAdapter(listadapter);
		} catch (JSONException e) {

		}
	}

	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("Action Item");
		subMenu1.add(0, R.style.Theme_Sherlock, 0, "New Reading");
		subMenu1.add(0, R.style.Theme_Sherlock, 0, "Logout");
		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		// getSupportMenuInflater().inflate(R.menu.activity_cached_items, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle() == "New Reading") {
			startActivity(new Intent(MenuMain.this, ReadMeter.class));
		}
		if (item.getTitle() == "Logout") {
			finish();
		}

		return true;
	}

	@Override
	public void onItemSelected(com.u1aryz.android.lib.newpopupmenu.MenuItem item) {
		switch (item.getItemId()) {
		case VIEWMAP:

			break;
		case READ_METER:
			startActivity(new Intent(getApplicationContext(), ReadMeter.class));
			break;
		}

	}

}
