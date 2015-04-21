package org.maxsoft.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener{

	@Override
	public void onLocationChanged(Location location) {//λ�÷����ı�ʱ����
		Log.d("MyLocation", "onLocationChanged");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { //״̬�ı�ʱ����
		//������Ը��ݲ�ͬ��״ִ̬�в�ͬ�Ĺ�����ʾ
		Log.d("MyLocation", "onStatusChanged");
	}

	@Override
	public void onProviderEnabled(String provider) { //Provider����ʱ����
		Log.d("MyLocation", "onProviderEnabled");
	}

	@Override
	public void onProviderDisabled(String provider) { //ProviderʧЧʱ����
		Log.d("MyLocation", "onProviderDisabled");
	}

}
