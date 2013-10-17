/****
 * This is the database manager. It hosts static and public functions for the use of the Main.
 * */

package com.sigtech.panicbutton.manager;

import android.content.Context;

public class DatabaseManager extends DatabaseManagerExpansions {

	public DatabaseManager(Context c) {
		super(c);
	}

	public void saveInfoDBVersion(int ver) {
		saveInfo(0, "dbversion", "" + ver, "");
	}

	public void saveInfoStatus(String status) {
		saveInfo(1, "status", status, "");
	}

	public void saveInfoAccessToken(String access_token) {
		saveInfo(2, "access_token", access_token, "");
	}
	
	public void saveInfoPIN(String pin) {
		saveInfo(3, "pin", pin, "");
	}

	public String getInfoDBVersion() {
		return getInfoT("dbversion");
	}

	public String getInfoStatus() {
		return getInfo("title='status'")[2];
	}

	public String getInfoAccessToken() {
		return getInfo("title='access_token'")[2];
	}
	
	public String getInfoPIN() {
		return getInfo("title='pin'")[2];
	}

	protected boolean dbVersionIsEmpty() {
		return getInfo("title='dbversion'")[2] == null;
	}
	
	public boolean infoPINIsEmpty() {
		return getInfoPIN() == null;
	}

	public void saveInfo(int id, String title, String value, String extras) {
		/* (id,title,value,extras) */
		if (openDB()) {
			if (isInfoEmpty(title))
				db.execSQL("insert into " + TABLE_INFO + TABLE_INFO_COLUMNS
						+ " values('" + id + "','" + title + "','" + value
						+ "','" + extras + "')");
			db.execSQL("update " + TABLE_INFO + " set id='" + id + "',title='"
					+ title + "',value='" + value + "',extras='" + extras + "'");
		}
		if (AUTO_CLOSE)
			closeAndNullify();
	}

	public String getInfoT(String title) {
		if (openDB()) {
			if (isInfoEmpty(title))
				return null;
			else
				return getInfo("title='" + title + "'")[2];
		}
		if (AUTO_CLOSE)
			closeAndNullify();
		return null;
	}

	/*
	 * (id, username, name, phone, password,panic_num, email,
	 * address,role,registration_date_time,activation_code,activated,approved)
	 */
	public void saveUserDetails(String id, String username, String name,
			String phone, String password, String panic_num, String email,
			String address, String role, String registration_date_time,
			String activation_code, int activated, int approved) {
		initDB();
		if (openDB()) {
			if (checkFirstTime())
				db.execSQL("insert into " + TABLE_USER + TABLE_USER_COLUMNS
						+ "values('" + id + "', '" + username + "', '" + name
						+ "', '" + phone + "', '" + password + "','"
						+ panic_num + "', '" + email + "','" + address + "','"
						+ role + "','" + registration_date_time + "','"
						+ activation_code + "','" + activated + "','"
						+ approved + "')");
			else
				db.execSQL("update " + TABLE_USER + " set id='" + id
						+ "', username='" + username + "',name='" + name
						+ "',phone='" + phone + "',password='" + password
						+ "',panic_num='" + panic_num + "',email='" + email
						+ "',address='" + address + "',role='" + role
						+ "',registration_date_time='" + registration_date_time
						+ "',activation_code='" + activation_code
						+ "',activated=" + activated + ",approved=" + approved);
		}
		if (AUTO_CLOSE)
			closeAndNullify();
	}
}
