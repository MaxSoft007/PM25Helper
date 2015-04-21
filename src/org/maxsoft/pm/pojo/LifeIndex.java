package org.maxsoft.pm.pojo;

import java.io.Serializable;

/**
 * @function 接收天气指数的实体类
 * @author MaxSoft
 * @date 2015-03-16
 */
public class LifeIndex implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String title = ""; //标题
	private String zs = ""; //指数级
	private String tipt = ""; //子标题
	private String des = ""; //描述
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getZs() {
		return zs;
	}
	public void setZs(String zs) {
		this.zs = zs;
	}
	public String getTipt() {
		return tipt;
	}
	public void setTipt(String tipt) {
		this.tipt = tipt;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
}
