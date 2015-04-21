package org.maxsoft.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.maxsoft.pm.pojo.Result;
import com.google.gson.Gson;

/**
 * @function ��ȡPM2.5�����Ϣ�ķ���
 * @author MaxSoft
 * @date 2015-03-16
 */
public class ReadPM25Info {
	
	private String temp_url = "";
	
	@SuppressWarnings("unused")
	private ReadPM25Info() {
		//TODO
	}
	
	public ReadPM25Info(String city) throws Exception{
		try {
			this.getUrl(city);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @function ��ȡJSON��Ϣ��תΪResult����
	 * @return org.maxsoft.pm.pojo.Result
	 * @throws Exception
	 */
	public Result ReadFromInternet()throws Exception {
		StringBuffer sb = new StringBuffer();
		String temp = "";
		Result result = null;
		try {
			URL url = new URL(temp_url);
			InputStream is = url.openStream(); // ��ȡinternet������
			InputStreamReader isr = new InputStreamReader(is); //����������
			BufferedReader br = new BufferedReader(isr);
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(sb.toString()); //ʵ����JSON����
			int status = jsonObject.getInt("error"); //��ȡ��Ϣ״̬
			System.out.println(sb.toString());
			if(status == 0){ //�ɹ�
				result = gson.fromJson(sb.toString(),Result.class);
			}else{ //ʧ��
				result = new Result();
				result.setError(status);
				result.setDate(jsonObject.getString("date"));
				result.setStatus(jsonObject.getString("status"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * @function ��ȡ������url��ַ
	 * @param city String ��������
	 * @return url String
	 */
	private void getUrl(String city)throws Exception{
		try {
			city = URLEncoder.encode(city,"UTF-8");
			this.temp_url = "http://api.map.baidu.com/telematics/v3/weather?location="+ city +"&output=json&ak=13dae0706a840d453e6532e0518c989d&qq-pf-to=pcqq.c2c";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new Exception("��������ת��ʧ��");
		}
	}

}
