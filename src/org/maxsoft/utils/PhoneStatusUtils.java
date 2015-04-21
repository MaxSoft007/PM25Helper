package org.maxsoft.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

/**
 * @function �ֻ����״̬��Ϣ���鹤����
 * @author MaxSoft
 * @date 2014-12-21
 * @version 1.0.0
 */
public class PhoneStatusUtils {
	/** 
	 * @function ����������״̬����У��
	 * @param context 
	 * @return  true�� ���ã� false�� ������ 
	 */  
	public static boolean isOpenNetwork(Context context) {  
	    ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(connManager.getActiveNetworkInfo() != null) {  
	        return connManager.getActiveNetworkInfo().isAvailable();  
	    }  
	    return false;  
	}  
	
	/**
	 * @function ��GPSģ������״̬����У��
	 * @param context
	 * @return true�����ã�false��������
	 */
	public static boolean isOpenGPS(Context context){
		LocationManager localManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(localManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			return true;
		}
		return false;
	}
}
