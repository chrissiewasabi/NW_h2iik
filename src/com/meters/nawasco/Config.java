package com.meters.nawasco;

import android.app.Application;

public class Config extends Application {
	private String link="http://iseetsolutions.com/water/processor.php";
	private String dummy1="({\"status\":1,\"user_det\":{\"phone\":\"254723444567\",\"name\":\"Johana Jabatiso\",\"user_id\":\"10\",\"routes\":[\"8\"],\"metres\":[{\"meter_id\":\"TY340\",\"cust_name\":\"Baba Jimmy\",\"plot_no\":\"NYI567\",\"lat\":\"0.65653736753\",\"lon\":\"0.53456345349\"}],\"failures\":{\"1\":\"Gate Closed\",\"2\":\"No Meter\",\"3\":\"UnPlesant Tenant\",\"4\":\"No Time\",\"5\":\"Meter Spoiled\",\"6\":\"Meter not Found\"}}})";
			

	public String getDummy1() {
		return dummy1;
	}

	public void setDummy1(String dummy1) {
		this.dummy1 = dummy1;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
