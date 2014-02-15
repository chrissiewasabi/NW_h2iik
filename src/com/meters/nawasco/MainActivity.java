package com.meters.nawasco;

import loopj.android.http.AsyncHttpClient;
import loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.meters.nawasco.helpers.AlertDialogManager;
import com.meters.nawasco.helpers.CheckConnectivity;
import com.meters.nawasco.helpers.FontManager;
import com.meters.nawasco.helpers.SessionManager;
import com.meters.nawasco.responses.LoginResponse;

public class MainActivity extends Activity {
	EditText et_user, et_pass;
	Button btn_login;
	CheckBox cb_sess;
	CheckConnectivity cd;
	AlertDialogManager alerts = new AlertDialogManager();
	String result, feedback, link, username, password, fails, meters;
	ProgressDialog progress;
	Config config;

	Boolean isInternet = false;
	private AsyncHttpClient asyncHttpClient;
	SessionManager session;
	TextView tv_logo;
	FontManager fontmanager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		et_user = (EditText) findViewById(R.id.et_uname);
		et_pass = (EditText) findViewById(R.id.et_pass);
		btn_login = (Button) findViewById(R.id.btn_login);
		this.asyncHttpClient = new AsyncHttpClient();
		cd = new CheckConnectivity(getApplicationContext());
		isInternet = cd.isConnectingToInternet();
		config = new Config();
		link = config.getLink();
		session = new SessionManager(getApplicationContext());
		fontmanager=new FontManager(getApplicationContext());
		tv_logo=(TextView)findViewById(R.id.txt_logo);
		tv_logo.setTypeface(fontmanager.LogoFont());;
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				username = et_user.getText().toString();
				password = et_pass.getText().toString();
				if (username.length() > 0 && password.length() > 0) {
					if (!isInternet) {
						ShowDialog();
					} else {
						LoginService();
					}
				} else {
					alerts.showAlertDialog(MainActivity.this, "",
							"Please fill all fields", false);
				}
			}
		});
	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager

		.getDefaultSharedPreferences(this);

		Editor editor = sharedPreferences.edit();

		editor.putString(key, value);

		editor.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void LoginService() {

		JSONObject send_jo = new JSONObject();
		JSONObject req = new JSONObject();
		JSONArray payload = new JSONArray();

		try {
			req.put("user_name", username.trim());
			req.put("password", password.trim());
			payload.put(req);
			send_jo.put("params", payload);
			send_jo.put("service_id", "1");
			Log.d("sented", send_jo.toString());
		} catch (JSONException e) {
		}

		RequestParams params = new RequestParams();

		params.put("data", send_jo.toString());

		asyncHttpClient.post(link, params, new LoginResponse(this));

	}

	@SuppressLint("InlinedApi")
	public void serverResponse(String content) {

		Log.d("Response", "json: " + content);
		try {
			JSONObject response = new JSONObject(content);

			result = response.getString("status");
			if (result.equals("1")) {
				JSONObject user_list = response.getJSONObject("user_det");
				JSONObject fail_list = user_list.getJSONObject("failures");
				JSONArray meter_list = user_list.getJSONArray("metres");
				meters = meter_list.toString();
				session.createErrorList(fail_list.toString());
				savePreferences("meters", meters);
				session.createLoginSession(username, password);
				Log.d("Response", "fails: " + meter_list.toString());
				session.CheckStuff();
				Intent i = new Intent(getApplicationContext(), MenuMain.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);

			} else

			{
				Log.d("wer", result);

				alerts.showAlertDialog(MainActivity.this, "",
						"Credentials are  wrong", false);

			}
		} catch (JSONException je) {
			Log.d("Response", "fails: ");
			je.printStackTrace();
		}
	}

	public void showProgress(String string) {
		// TODO Auto-generated method stub
		progress = ProgressDialog.show(MainActivity.this, "",
				"Verifying Credentials...", true, false);
	}

	public void dismissProgressDialog() {
		// TODO Auto-generated method stub
		progress.dismiss();
	}

	public void showDialogMessage(String msq) {

		alerts.showAlertDialog(this, "Error", msq, false);
	}

	public void ShowDialog() {

		AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
		alert.setTitle("Internet");
		alert.setMessage("Login Requires Data Connection");
		alert.setButton(Dialog.BUTTON_POSITIVE,
				getResources().getString(R.string.settings),
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int which) {
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						startActivity(intent);
					}
				});
		alert.setButton(Dialog.BUTTON_NEGATIVE,
				getResources().getString(R.string.exit),
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int which) {
						finish();
					}
				});

		alert.show();
	}

}
