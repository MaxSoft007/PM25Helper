package org.maxsoft.pm.pojo;

import java.io.Serializable;

import org.maxsoft.utils.Constant;
/**
 * @function 请求结果信息的实体类
 * @url http://api.map.baidu.com/telematics/v3/weather?location=%E5%87%89%E5%9F%8E&output=json&ak=13dae0706a840d453e6532e0518c989d&qq-pf-to=pcqq.c2c
 * @author MaxSoft
 * @date 2015-03-16
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long error = -1; //状态码
	private String status = ""; //状态名称
	private String date = ""; //日期
	private PM25[] results = null; //new PM25[Constant.NUM_OF_RESULT]; //PM2.5信息体
	public long getError() {
		return error;
	}
	public void setError(long error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public PM25[] getResults() {
		return results;
	}
	public void setResults(PM25[] results) {
		this.results = results;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
