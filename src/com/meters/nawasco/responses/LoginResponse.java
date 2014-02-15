package com.meters.nawasco.responses;

import com.meters.nawasco.MainActivity;

import android.app.Activity;
import android.util.Log;

import loopj.android.http.AsyncHttpResponseHandler;

public class LoginResponse extends AsyncHttpResponseHandler {
	MainActivity login;
	Activity activity;
	String[] names;
	int[] ids;

	public LoginResponse(MainActivity lipukaApplication) {
		super();
		this.login = lipukaApplication;
	}

	public void onStart() {
		login.showProgress("Sending request");
	}

	public void onFinish() {

		// Log.d(Main.TAG, "called onfinish");

	}

	public void onSuccess(String content) {
		login.dismissProgressDialog();
		Log.d("Resp", "json string: " + content);

		login.serverResponse(content);
	}

	public void onFailure(Throwable error) {
		login.dismissProgressDialog();
		Log.d("Resp", "json string: " + error);
//		login.showDialogMessage("There is a problem with your internet connection, check and try again");

	}
}
