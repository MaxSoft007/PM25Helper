package org.maxsoft.listener;

import java.util.Iterator;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.LocationManager;
import android.widget.Toast;

public class GPSListener implements Listener{
	private GpsStatus gpsstatus;
	private LocationManager locationManager;
	private Context context;
	
	public GPSListener(Context context,LocationManager locationManager) {
		this.context = context;
		this.locationManager = locationManager;
	}
	
	@Override
	public void onGpsStatusChanged(int event) {
		gpsstatus = locationManager.getGpsStatus(null);
		switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX: //第一次定位时的事件
				break;
			case GpsStatus.GPS_EVENT_STARTED: //开始定位事件
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS: //开始发送卫星定位事件
				Toast.makeText(context, "GPS_EVENT_SATELLITE_STATUS", Toast.LENGTH_LONG).show();
				Iterable<GpsSatellite> satellites = gpsstatus.getSatellites();//获取所有的卫星信息
				Iterator<GpsSatellite> iterator = satellites.iterator();
				int count = 0;
				while (iterator.hasNext()) {
					count++;
				}
				Toast.makeText(context, "Satellite Count:" + count, Toast.LENGTH_SHORT).show();
			case GpsStatus.GPS_EVENT_STOPPED: //停止定位事件
				break;
			default:
				break;
		}
	}
}
