package org.maxsoft.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener{

	@Override
	public void onLocationChanged(Location location) {//位置发生改变时调用
		Log.d("MyLocation", "onLocationChanged");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { //状态改变时调用
		//这里可以根据不同的状态执行不同的过程提示
		Log.d("MyLocation", "onStatusChanged");
	}

	@Override
	public void onProviderEnabled(String provider) { //Provider开启时调用
		Log.d("MyLocation", "onProviderEnabled");
	}

	@Override
	public void onProviderDisabled(String provider) { //Provider失效时调用
		Log.d("MyLocation", "onProviderDisabled");
	}

}
