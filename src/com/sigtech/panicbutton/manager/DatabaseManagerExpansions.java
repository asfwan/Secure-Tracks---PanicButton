package com.sigtech.panicbutton.manager;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class DatabaseManagerExpansions extends DatabaseManagerCore {
	// SETTERS AND GETTERS
	public DatabaseManagerExpansions(Context c) {
		super(c);
	}

	/*
	 * (id , username ,name ,phone ,password ,panic_num ,email ,address ,role
	 * ,registration_date_time ,activation_code ,activated ,approved )
	 */

	public String getId() {
		return getDetails(null)[0];
	}

	public String getUsername() {
		return getDetails(null)[1];
	}

	public String getName() {
		return getDetails(null)[2];
	}

	public String getPhone() {
		return getDetails(null)[3];
	}

	public String getPassword() {
		return getDetails(null)[4];
	}

	public String getPanicNum() {
		return getDetails(null)[5];
	}

	public String getEmail() {
		return getDetails(null)[6];
	}

	public String getAddress() {
		return getDetails(null)[7];
	}

	public String getRole() {
		return getDetails(null)[8];
	}

	public String getRegDateTime() {
		return getDetails(null)[9];
	}

	public String getActivation() {
		return getDetails(null)[10];
	}

	public String getActivated() {
		return getDetails(null)[11];
	}

	public String getApproved() {
		return getDetails(null)[12];
	}

	public String[] getPanicNums() {
		String lPanicNums = getPanicNum();
		String[] panicNums = null;
		if (lPanicNums.contains(";")) {
			panicNums = lPanicNums.split(";");
			return panicNums;
		}
		return new String[] { lPanicNums };
	}

	public String[] getDetails(String selection) {
		Cursor c = null;
		String retval[] = new String[COLUMN_USER_COUNT];
		initDB();
		if (openDB()) {
			c = db.query(TABLE_USER, col_user, selection, null, null, null,
					null);
			if (c.moveToFirst())
				do {
					for (int i = 0; i < COLUMN_USER_COUNT; i++)
						retval[i] = c.getString(c.getColumnIndex(col_user[i]));
				} while (c.moveToNext());
		}
		c.close();
		if (AUTO_CLOSE)
			closeAndNullify();
		return retval;
	}

	public String[] getInfo(String selection) {
		Cursor c = null;
		String retval[] = new String[COLUMN_INFO_COUNT];
		initDB();
		if (openDB()) {
			c = db.query(TABLE_INFO, col_info, selection, null, null, null,
					null);
			if (c.moveToFirst())
				do {
					for (int i = 0; i < COLUMN_INFO_COUNT; i++)
						retval[i] = c.getString(c.getColumnIndex(col_info[i]));
				} while (c.moveToNext());
		}
		c.close();
		if (AUTO_CLOSE)
			closeAndNullify();
		return retval;
	}

	public void disableAutoClose() {
		AUTO_CLOSE = false;
	}

	public void enableAutoClose() {
		AUTO_CLOSE = true;
	}

	public boolean checkFirstTime() {
		return getDetails(null)[0] == null;
	}

	public boolean infoTableIsEmpty() {
		return getInfo(null)[0] == null;
	}

	public void savePanicNum(String panicNum) {
		if (initDB())
			db.execSQL("update " + TABLE_USER + " set panic_num='" + panicNum
					+ "'");
		if (AUTO_CLOSE)
			closeAndNullify();
	}

	public void saveUser(String col, String value) {
		if (initDB()) {
			if (checkFirstTime())
				db.execSQL("insert into " + TABLE_USER + " (" + col
						+ ") values('" + value + "')");
			else
				db.execSQL("update " + TABLE_USER + " set " + col + "='"
						+ value + "'");
		}
		if (AUTO_CLOSE)
			closeAndNullify();
	}

	public void saveUserApproval(String approval) {
		saveUser("panic_num", approval);
	}

	public void addPanicNum(String panicNum) {
		if (initDB())
			db.execSQL("update " + TABLE_USER + " set panic_num=panic_num||';"
					+ panicNum + "'");
		if (AUTO_CLOSE)
			closeAndNullify();
	}

	public void testSavedDetails() {
		String[] s = getDetails(null);
		String sAll = "";
		for (int i = 0; i < COLUMN_USER_COUNT; i++)
			sAll += s[i] + " ";
		Toast.makeText(context, sAll, Toast.LENGTH_LONG).show();
	}

	public boolean isInfoEmpty(String title) {
		return (getInfo("title='" + title + "'")[0] == null);
	}

	// public void saveUsername(String username) {
	// initDB();
	// if (openDB())
	// db.execSQL("insert into " + TABLE_USER + " (username) "
	// + "values('" + username + "')");
	// closeAndNullify();
	// }
	//
	// public void savePassword(String password) {
	// initDB();
	// if (openDB())
	// db.execSQL("insert into " + TABLE_USER + " (password) "
	// + "values('" + password + "')");
	// closeAndNullify();
	// }
	// public void savePanicEmail(String panicEmail) {
	// initDB();
	// if (openDB())
	// db.execSQL("insert into " + TABLE_USER + " (panic_email) "
	// + "values('" + panicEmail + "')");
	// closeAndNullify();
	// }
	//
	// public void saveUserAddress(String userAddress) {
	// initDB();
	// if (openDB())
	// db.execSQL("insert into " + TABLE_USER + " (user_address) "
	// + "values('" + userAddress + "')");
	// closeAndNullify();
	// }
}
