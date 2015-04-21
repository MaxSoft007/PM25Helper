package org.maxsoft.pm.pojo;

import java.io.Serializable;

/**
 * @function ����������Ϣ��ʵ����
 * @author MaxSoft
 * @date 2015-03-16
 */
public class WeatherInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String date = ""; //":"��һ 03��16�� (ʵʱ��19��)",
	private String dayPictureUrl = ""; //":"http://api.map.baidu.com/images/weather/day/duoyun.png",
	private String nightPictureUrl=""; //":"http://api.map.baidu.com/images/weather/night/yin.png",
	private String weather = ""; //":"����ת��",
	private String wind = ""; //":"����΢��",
	private String temperature = ""; //":"20 ~ 8��"
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDayPictureUrl() {
		return dayPictureUrl;
	}
	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}
	public String getNightPictureUrl() {
		return nightPictureUrl;
	}
	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
}
