package org.maxsoft.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

/**
 * @function 手机相关状态信息检验工具类
 * @author MaxSoft
 * @date 2014-12-21
 * @version 1.0.0
 */
public class PhoneStatusUtils {
	/** 
	 * @function 对网络连接状态进行校验
	 * @param context 
	 * @return  true： 可用； false： 不可用 
	 */  
	public static boolean isOpenNetwork(Context context) {  
	    ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(connManager.getActiveNetworkInfo() != null) {  
	        return connManager.getActiveNetworkInfo().isAvailable();  
	    }  
	    return false;  
	}  
	
	/**
	 * @function 对GPS模块运行状态进行校验
	 * @param context
	 * @return true：可用；false：不可用
	 */
	public static boolean isOpenGPS(Context context){
		LocationManager localManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(localManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			return true;
		}
		return false;
	}
}
