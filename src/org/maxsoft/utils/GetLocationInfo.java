package org.maxsoft.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.maxsoft.listener.GPSListener;
import org.maxsoft.listener.MyLocationListener;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class GetLocationInfo {
	private Address address = null;
	/**
	 * @function 通过手机运营商基站信息定位
	 * @param context
	 * @return 定位信息
	 */
	public String getLocationByTelephony(Context context){
		//获取手机运营商基站服务管理对象
		TelephonyManager telephonyManager =(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation gsm_location = (GsmCellLocation) telephonyManager.getCellLocation();
		int cid = gsm_location.getCid(); //获取GSM基站的ID
		int lac = gsm_location.getLac(); //获取GSM区域代码
		//int psc = gsm_location.getPsc(); //获取UMTS网络中信号最强的干扰码
		String networkOperator = telephonyManager.getNetworkOperator(); //返回当前注册商的数字花名字(MCC + MNC)
		Integer mcc = Integer.valueOf(networkOperator.substring(0, 3)); //获取MCC编号
		Integer mnc = Integer.valueOf(networkOperator.substring(3, 5)); //获取MNC编号
		
		JSONObject holder = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject data = new JSONObject();
		try {
			holder.put("version", "1.1.0");  
            holder.put("host", "maps.google.com");  
            holder.put("address_language", "zh_CN");  
            holder.put("request_address", true);  
            holder.put("radio_type", "gsm");  
            //holder.put("carrier", "HTC");  
            data.put("cell_id", cid);  
            data.put("location_area_code", lac);  
            data.put("mobile_countyr_code", mcc);  
            data.put("mobile_network_code", mnc);  
            array.put(data);  
            holder.put("cell_towers", array);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://www.google.com/loc/json"); //实例化Post传参对象
		StringEntity stringEntity = null;
		try {  
            stringEntity = new StringEntity(holder.toString());  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }
		httpPost.setEntity(stringEntity);  
        HttpResponse httpResponse = null; 
        
        try {
			httpResponse = client.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = null;
        
        try {
			is = httpEntity.getContent();
		} catch (IllegalStateException  e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        
        String result = "";
        try {
			while((result = bufferedReader.readLine()) != null){
				stringBuffer.append(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        //Toast.makeText(context,stringBuffer.toString(), Toast.LENGTH_LONG).show();
        return stringBuffer.toString();
	}
	
	/**
	 * @function 通过GPS获取移动终端当前的位置信息
	 * @param context
	 */
	public Address getLocationByGPS(Context context){
		//获取LocationManager对象
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		//获取GPS服务提供者对象
		LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
		//获取GPS服务提供者名称
		String locationProviderName = locationProvider.getName();
		//根据服务提供者名称使用locationManager获取最后一次位置信息
		Location location = locationManager.getLastKnownLocation(locationProviderName);
		GPSListener gpsListener = new GPSListener(context,locationManager);
		MyLocationListener myLocationListener = new MyLocationListener();
		
		if(location == null){
			locationManager.requestLocationUpdates(locationProviderName, 0, 0, myLocationListener);
		}
		
		locationManager.addGpsStatusListener(gpsListener);
		
		while(true){
			location = locationManager.getLastKnownLocation(locationProviderName);
			if(location != null){
                Log.d("Location", "Latitude: " + location.getLatitude());
                Log.d("Location", "location: " + location.getLongitude());
                break;
            }else{
                Log.d("Location", "Latitude: " + 0);
                Log.d("Location", "location: " + 0);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                 Log.e("Location", e.getMessage());
            }
		}
		
		Geocoder geocoder = new Geocoder(context);
		try {
            int latitude = (int) location.getLatitude();
            int longitude = (int) location.getLongitude();
            List<Address> list = geocoder.getFromLocation(latitude, longitude, 2);
            for(int i=0; i<list.size(); i++){
                address = list.get(i); 
                //Toast.makeText(context, address.getCountryName() + address.getAdminArea() + address.getFeatureName(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
        	e.printStackTrace();
            //Toast.makeText(context,e.getMessage(), Toast.LENGTH_LONG).show();
        }
		return address;
	}
	
	public Address getLocationByInternet(Context context){
		//获取locationManager对象
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		//创建网络提供商对象
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置粗略的精确度
		criteria.setAltitudeRequired(false); //设置是否需要返回海拔信息
		criteria.setBearingRequired(false); //设置是否返回方位信息
		criteria.setCostAllowed(false); //设置是否允许付费服务
		criteria.setPowerRequirement(Criteria.POWER_LOW); //设置电量消耗等级
		criteria.setSpeedRequired(false); //设置返回速度
		
		//根据设置的Criteria对象，获取最符合此标准的provider对象
		String best_provider = locationManager.getBestProvider(criteria, true);
		Log.d("Location", "currentProvider: " + best_provider);
		//根据当前provider对象获取最后一次位置信息
		Location location = locationManager.getLastKnownLocation(best_provider);
		if(location == null){
			locationManager.requestLocationUpdates(best_provider, 0, 0, new MyLocationListener());
		}
		//直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
        //每隔10秒获取一次位置信息
		while (true) {
			location = locationManager.getLastKnownLocation(best_provider);
			if(location != null){
                Log.d("Location", "Latitude: " + location.getLatitude());
                Log.d("Location", "location: " + location.getLongitude());
                break;
            }else{
                Log.d("Location", "Latitude: " + 0);
                Log.d("Location", "location: " + 0);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                 Log.e("Location", e.getMessage());
            }
		}
		Geocoder geocoder = new Geocoder(context);
		try {
			int latitude = (int) location.getLatitude();
			int longitude = (int) location.getLongitude();
			List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);
			Iterator<Address> iterator = list.iterator();
			while(iterator.hasNext()){
				address = iterator.next();
				//Toast.makeText(context, address.toString(), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			//Toast.makeText(context,e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		return address;
	}
	
	public Address getLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
		Criteria criteria = new Criteria();  
		// 获得最好的定位效果  
		criteria.setAccuracy(Criteria.ACCURACY_FINE);  
		criteria.setAltitudeRequired(false);  
		criteria.setBearingRequired(false);  
		criteria.setCostAllowed(false);  
		// 使用省电模式  
		criteria.setPowerRequirement(Criteria.POWER_LOW);  
		// 获得当前的位置提供者  
		String provider = locationManager.getBestProvider(criteria, true);  
		// 获得当前的位置  
		Location location = locationManager.getLastKnownLocation(provider);  
//		String msg = "";  
		Geocoder gc = new Geocoder(context);   
		List<Address> addresses = null;  
		try {  
		    addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);  
		} catch (IOException e) {  
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		    e.printStackTrace();  
		} 
		if (addresses != null && addresses.size() > 0) {   
			address = addresses.get(0);
//			msg += "AddressLine：" + addresses.get(0).getAddressLine(0)+ "\n"; //街道名称   
//			msg += "Locality：" + addresses.get(0).getLocality() + "\n"; //城市位置
//			msg += "AdminArea：" + addresses.get(0).getAdminArea() + "\n"; //城市中心区域
//			msg += "SubLocality：" + addresses.get(0).getSubLocality() + "\n"; //子城市位置  
		}
		
		return address;
	}
}
