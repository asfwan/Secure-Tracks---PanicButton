/****
 * This is the default location listener for this app.
 * */

package com.sigtech.panicbutton.manager;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.sigtech.panicbutton.frags.MainPanicFragment;
import com.sigtech.panicbutton.manager.internet.LocationUpdater;

public class MyLocationListener implements LocationListener {
	Context c;

	public MyLocationListener(Context c) {
		this.c = c;
	}

	@Override
	public void onLocationChanged(Location location) {
		DataManager.longitude = location.getLongitude();
		DataManager.latitude = location.getLatitude();
		if (DataManager.ON_MAIN_PANIC_SCREEN) {
			MainPanicFragment.tv_lat.setText("Lat: "
					+ String.valueOf(DataManager.latitude));
			MainPanicFragment.tv_long.setText("Long: "
					+ String.valueOf(DataManager.longitude));
		}
		Toast.makeText(
				c,
				"Lat: " + DataManager.latitude + " Long: "
						+ DataManager.longitude, Toast.LENGTH_LONG).show();

		DataManager.LATLNG_SET = true;
		DataManager.LATLNG_SET_EXACT = true;
		if (!DataManager.PANICKED) {
			new LocationUpdater(c).updateLocation();
			MainPanicFragment.notifyLocationUpdated();
		} else {
			new LocationUpdater(c).updateLocationOnEmergency(true);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// DataManager
		// .showToast(c, "Warning! " + provider + " has been disabled!");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// DataManager.showToast(c, "The " + provider + " has been enabled!");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}