package com.sigtech.panicbutton.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManagerCore {
	protected static final String DATABASE_NAME = "PANIC_BUTTON_DB";
	protected static final String TABLE_USER = " TABLE_USER ";
	protected static final String TABLE_INFO = " TABLE_INFO ";
	protected static Context context = null;
	protected static SQLiteDatabase db;
	protected static boolean AUTO_CLOSE = true;

	// USER DATABASE
	protected static final int COLUMN_USER_COUNT = 13;
	protected static final String TABLE_USER_COLUMNS_DATATYPE = " (id int, username varchar(20),"
			+ "name text,phone varchar(15),"
			+ "password varchar(20),panic_num text,"
			+ "email varchar(20),address varchar(30),"
			+ "role varchar(10),registration_date_time varchar(20),"
			+ "activation_code text,activated boolean,approved boolean)";
	protected static final String TABLE_USER_COLUMNS = " (id , username ,"
			+ "name ,phone ,password ,panic_num ,"
			+ "email ,address ,role ,registration_date_time ,"
			+ "activation_code ,activated ,approved )";
	protected static final String[] col_user = new String[] { "id", "username",
			"name", "phone", "password", "panic_num", "email", "address",
			"role", "registration_date_time", "activation_code", "activated",
			"approved" };

	// INFO DATABASE
	protected static final int COLUMN_INFO_COUNT = 4;
	protected static final String TABLE_INFO_COLUMNS = " (id,title,value,extras)";
	protected static final String TABLE_INFO_COLUMNS_DATATYPE = " (id int,title varchar(20),value varchar(20),"
			+ "extras text)";
	protected static final String[] col_info = new String[] { "id", "title",
			"value", "extras" };

	/***************************************************************************************
	 * DatabaseManager Core Functions
	 * */

	public DatabaseManagerCore(Context c) {
		context = c;
		db = getDatabase();
	}

	public SQLiteDatabase getDatabase() {
		if (context == null)
			return null;
		return context.openOrCreateDatabase(DATABASE_NAME, 0, null);
	}

	public boolean openDB() {
		if (db.isOpen())
			return true;
		else {
			db = getDatabase();
			if (db != null && db.isOpen())
				return true;
		}

		return false;
	}

	public DatabaseManagerCore reopenDB() {
		if (openDB())
			return this;
		return this;
	}

	public boolean initDB() {
		if (openDB()) {
			db.execSQL("create table if not exists " + TABLE_USER
					+ TABLE_USER_COLUMNS_DATATYPE);
			db.execSQL("create table if not exists " + TABLE_INFO
					+ TABLE_INFO_COLUMNS_DATATYPE);
			return true;
		}
		return false;
	}

	// public void dropTableAndReInitWithClose() {
	// if (openDB()) {
	// db.execSQL("drop table if exists " + TABLE_USER);
	// db.execSQL("drop table if exists " + TABLE_INFO);
	// }
	// initDB();
	// if (AUTO_CLOSE)
	// closeAndNullify();
	// }

	public void dropTableAndReInit() {
		if (openDB()) {
			db.execSQL("drop table if exists " + TABLE_USER);
		}
		initDB();
	}

	// public void dropTableAndReInitInfo() {
	// if (openDB()) {
	// db.execSQL("drop table if exists " + TABLE_INFO);
	// }
	// initDB();
	// }

	// public void dropTableAndReInitBoth() {
	// if (openDB()) {
	// db.execSQL("drop table if exists " + TABLE_USER);
	// db.execSQL("drop table if exists " + TABLE_INFO);
	// }
	// initDB();
	// }

	public void closeAndNullify() {
		if (db.isOpen())
			db.close();
		db = null;
	}

	public boolean isClosed() {
		return (db == null || !db.isOpen());
	}

	/***
	 * DatabaseManager Core Functions
	 ***************************************************************************************/

}
