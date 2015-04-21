package org.maxsoft.pm.pojo;

import java.io.Serializable;

import org.maxsoft.utils.Constant;

/**
 * @function 接收PM25信息的实体类
 * @author MaxSoft
 * @date 2015-03-16
 */
public class PM25 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String currentCity = ""; //城市名称
	private String pm25 = ""; //PM2.5值
	private LifeIndex[] index = null; // new LifeIndex[Constant.NUM_OF_LIFE_INDEX]; //生活指数
	private WeatherInfo[] weather_data = null; // new WeatherInfo[Constant.NUM_OF_WEATHERINFO]; //天气信息
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public LifeIndex[] getIndex() {
		return index;
	}
	public void setIndex(LifeIndex[] index) {
		this.index = index;
	}
	public WeatherInfo[] getWeather_data() {
		return weather_data;
	}
	public void setWeather_data(WeatherInfo[] weather_data) {
		this.weather_data = weather_data;
	}
}
