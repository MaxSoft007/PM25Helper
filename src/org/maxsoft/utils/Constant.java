package org.maxsoft.utils;

import android.location.Address;
import android.view.View;
/**
 * @function 通用工具类
 * @author MaxSoft
 * @date 2015-03-14
 * @version 1.0.0
 */
public class Constant {
	public static long WELCOME_PAGE_NUM = 4; //欢迎页面的数量
	
	public static int NUM_OF_LIFE_INDEX = 6; //生活指数条数
	public static int NUM_OF_WEATHERINFO = 4; //天气信息条数
	public static int NUM_OF_RESULT = 1; //结果信息条数
	
	public static class OperationStatus{
		public static final int DELETE = 0;
		private static String DELETE_NAME = "删 除";
		
		public static final int SAVE = 1;
		private static String SAVE_NAME = "保 存";
		
		public static final int SUBMIT = 2;
		private static String SUBMIT_NAME = "提 交";
		
		public static final int CHECK = 3;
		private static String CHECK_NAME = "复 核";
		
		public static final int APPROVAL = 4;
		private static String APPROVAL_NAME = "审 核"; 
		
		public static final int RETURN = 5;
		private static String RETURN_NAME = "退 回";
		
		public static final int FAIL = 6;
		private static String FAIL_NAME = "失 败";
		
		public static final int SUCCESS = 7;
		private static String SUCCESS_NAME = "成 功";
		
		private static String EXCEPTION = "异 常...";
		
		/**
		 * @function 提供系统中使用到的操作常量名称
		 * @param int index 操作常量索引值 
		 * @param boolean isPrefix 操作常量中是否添加前缀
		 * @return String result 操作常量名称
		 */
		public static String getOperationStatus(int index,boolean isPrefix){
			String temp = "";
			switch (index) {
				case DELETE:
					temp = DELETE_NAME;
					break;
				case SAVE:
					temp = SAVE_NAME;
					break;
				case SUBMIT:
					temp = SUBMIT_NAME;
					break;
				case CHECK:
					temp = CHECK_NAME;
					break;
				case APPROVAL:
					temp = APPROVAL_NAME;
					break;
				case RETURN:
					temp = RETURN_NAME;
					break;
				case FAIL:
					temp = FAIL_NAME;
					break;
				case SUCCESS:
					temp = SUCCESS_NAME;
					break;
				default:
					temp = EXCEPTION;
					break;
			}
			if(isPrefix){
				temp = "已 " + temp;
			}
			return temp;
		}
		
		/**
		 * @function 提供操作类型名称和操作类型值
		 * @return Object[] index=0 keys; index=1 values; keys[0] -> values[0]; ...
		 */
		public static Object[] getOperationStatusList(){
			String[] values = {DELETE_NAME,SAVE_NAME,SUBMIT_NAME,CHECK_NAME,APPROVAL_NAME,RETURN_NAME,FAIL_NAME,SUCCESS_NAME};
			int[] keys = {DELETE,SAVE,SUBMIT,CHECK,APPROVAL,RETURN,FAIL,SUCCESS};
			Object[] objects = {keys,values};
			return objects;
		}
	}
	
	/**
	 * @function 获取当前位置
	 * @param view
	 */
	public static String locationInfo(View v) {
		String city = "北京";
		final GetLocationInfo getLocationInfo = new GetLocationInfo();
		Address add = null;
		add = getLocationInfo.getLocation(v.getContext());
		if(add != null){
			city = add.getLocality().replace("市", "");
		}
		return city;
	}
	
	/**
	 * @function 根据PM25的值获取相关等级
	 * @param value
	 * @return
	 */
	public static String[] getPMLevel(long value){
		String[] level = new String[2];
		if( value >= 0 && value <= 50){
			level[0] = "1级  优";
			level[1] = "#00FF00"; //绿色
		}else if( value >50 && value <= 100){
			level[0] = "2级  良";
			level[1] = "#FFFF00"; //黄色
		}else if( value > 100 && value <= 150){
			level[0] = "3级  轻 度";
			level[1] = "#FF6100"; //橙色
		}else if( value >150 && value <= 200){
			level[0] = "4级  中 度";
			level[1] = "#FF0000"; //红色
		}else if(value > 200 && value <= 300){
			level[0] = "5级  重 度";
			level[1] = "#FF00FF"; //紫色
		}else {
			level[0] = "6级  严 重";
			level[1] = "#8E236B"; //褐红色
		}
		return level;
	}

}
