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
	 * @function ͨ���ֻ���Ӫ�̻�վ��Ϣ��λ
	 * @param context
	 * @return ��λ��Ϣ
	 */
	public String getLocationByTelephony(Context context){
		//��ȡ�ֻ���Ӫ�̻�վ����������
		TelephonyManager telephonyManager =(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation gsm_location = (GsmCellLocation) telephonyManager.getCellLocation();
		int cid = gsm_location.getCid(); //��ȡGSM��վ��ID
		int lac = gsm_location.getLac(); //��ȡGSM�������
		//int psc = gsm_location.getPsc(); //��ȡUMTS�������ź���ǿ�ĸ�����
		String networkOperator = telephonyManager.getNetworkOperator(); //���ص�ǰע���̵����ֻ�����(MCC + MNC)
		Integer mcc = Integer.valueOf(networkOperator.substring(0, 3)); //��ȡMCC���
		Integer mnc = Integer.valueOf(networkOperator.substring(3, 5)); //��ȡMNC���
		
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
		HttpPost httpPost = new HttpPost("http://www.google.com/loc/json"); //ʵ����Post���ζ���
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
	 * @function ͨ��GPS��ȡ�ƶ��ն˵�ǰ��λ����Ϣ
	 * @param context
	 */
	public Address getLocationByGPS(Context context){
		//��ȡLocationManager����
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		//��ȡGPS�����ṩ�߶���
		LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
		//��ȡGPS�����ṩ������
		String locationProviderName = locationProvider.getName();
		//���ݷ����ṩ������ʹ��locationManager��ȡ���һ��λ����Ϣ
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
		//��ȡlocationManager����
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		//���������ṩ�̶���
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//���ô��Եľ�ȷ��
		criteria.setAltitudeRequired(false); //�����Ƿ���Ҫ���غ�����Ϣ
		criteria.setBearingRequired(false); //�����Ƿ񷵻ط�λ��Ϣ
		criteria.setCostAllowed(false); //�����Ƿ������ѷ���
		criteria.setPowerRequirement(Criteria.POWER_LOW); //���õ������ĵȼ�
		criteria.setSpeedRequired(false); //���÷����ٶ�
		
		//�������õ�Criteria���󣬻�ȡ����ϴ˱�׼��provider����
		String best_provider = locationManager.getBestProvider(criteria, true);
		Log.d("Location", "currentProvider: " + best_provider);
		//���ݵ�ǰprovider�����ȡ���һ��λ����Ϣ
		Location location = locationManager.getLastKnownLocation(best_provider);
		if(location == null){
			locationManager.requestLocationUpdates(best_provider, 0, 0, new MyLocationListener());
		}
		//ֱ��������һ��λ����ϢΪֹ�����δ������һ��λ����Ϣ������ʾĬ�Ͼ�γ��
        //ÿ��10���ȡһ��λ����Ϣ
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
		// �����õĶ�λЧ��  
		criteria.setAccuracy(Criteria.ACCURACY_FINE);  
		criteria.setAltitudeRequired(false);  
		criteria.setBearingRequired(false);  
		criteria.setCostAllowed(false);  
		// ʹ��ʡ��ģʽ  
		criteria.setPowerRequirement(Criteria.POWER_LOW);  
		// ��õ�ǰ��λ���ṩ��  
		String provider = locationManager.getBestProvider(criteria, true);  
		// ��õ�ǰ��λ��  
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
//			msg += "AddressLine��" + addresses.get(0).getAddressLine(0)+ "\n"; //�ֵ�����   
//			msg += "Locality��" + addresses.get(0).getLocality() + "\n"; //����λ��
//			msg += "AdminArea��" + addresses.get(0).getAdminArea() + "\n"; //������������
//			msg += "SubLocality��" + addresses.get(0).getSubLocality() + "\n"; //�ӳ���λ��  
		}
		
		return address;
	}
}
