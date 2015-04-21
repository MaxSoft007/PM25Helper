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
 * @function 获取PM2.5相关信息的方法
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
	 * @function 读取JSON信息并转为Result对象
	 * @return org.maxsoft.pm.pojo.Result
	 * @throws Exception
	 */
	public Result ReadFromInternet()throws Exception {
		StringBuffer sb = new StringBuffer();
		String temp = "";
		Result result = null;
		try {
			URL url = new URL(temp_url);
			InputStream is = url.openStream(); // 获取internet输入流
			InputStreamReader isr = new InputStreamReader(is); //缓冲输入流
			BufferedReader br = new BufferedReader(isr);
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(sb.toString()); //实例化JSON对象
			int status = jsonObject.getInt("error"); //获取信息状态
			System.out.println(sb.toString());
			if(status == 0){ //成功
				result = gson.fromJson(sb.toString(),Result.class);
			}else{ //失败
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
	 * @function 获取编码后的url地址
	 * @param city String 城市名称
	 * @return url String
	 */
	private void getUrl(String city)throws Exception{
		try {
			city = URLEncoder.encode(city,"UTF-8");
			this.temp_url = "http://api.map.baidu.com/telematics/v3/weather?location="+ city +"&output=json&ak=13dae0706a840d453e6532e0518c989d&qq-pf-to=pcqq.c2c";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new Exception("城市名称转码失败");
		}
	}

}
