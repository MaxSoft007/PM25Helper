package org.maxsoft.utils;

import android.location.Address;
import android.view.View;
/**
 * @function ͨ�ù�����
 * @author MaxSoft
 * @date 2015-03-14
 * @version 1.0.0
 */
public class Constant {
	public static long WELCOME_PAGE_NUM = 4; //��ӭҳ�������
	
	public static int NUM_OF_LIFE_INDEX = 6; //����ָ������
	public static int NUM_OF_WEATHERINFO = 4; //������Ϣ����
	public static int NUM_OF_RESULT = 1; //�����Ϣ����
	
	public static class OperationStatus{
		public static final int DELETE = 0;
		private static String DELETE_NAME = "ɾ ��";
		
		public static final int SAVE = 1;
		private static String SAVE_NAME = "�� ��";
		
		public static final int SUBMIT = 2;
		private static String SUBMIT_NAME = "�� ��";
		
		public static final int CHECK = 3;
		private static String CHECK_NAME = "�� ��";
		
		public static final int APPROVAL = 4;
		private static String APPROVAL_NAME = "�� ��"; 
		
		public static final int RETURN = 5;
		private static String RETURN_NAME = "�� ��";
		
		public static final int FAIL = 6;
		private static String FAIL_NAME = "ʧ ��";
		
		public static final int SUCCESS = 7;
		private static String SUCCESS_NAME = "�� ��";
		
		private static String EXCEPTION = "�� ��...";
		
		/**
		 * @function �ṩϵͳ��ʹ�õ��Ĳ�����������
		 * @param int index ������������ֵ 
		 * @param boolean isPrefix �����������Ƿ����ǰ׺
		 * @return String result ������������
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
				temp = "�� " + temp;
			}
			return temp;
		}
		
		/**
		 * @function �ṩ�����������ƺͲ�������ֵ
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
	 * @function ��ȡ��ǰλ��
	 * @param view
	 */
	public static String locationInfo(View v) {
		String city = "����";
		final GetLocationInfo getLocationInfo = new GetLocationInfo();
		Address add = null;
		add = getLocationInfo.getLocation(v.getContext());
		if(add != null){
			city = add.getLocality().replace("��", "");
		}
		return city;
	}
	
	/**
	 * @function ����PM25��ֵ��ȡ��صȼ�
	 * @param value
	 * @return
	 */
	public static String[] getPMLevel(long value){
		String[] level = new String[2];
		if( value >= 0 && value <= 50){
			level[0] = "1��  ��";
			level[1] = "#00FF00"; //��ɫ
		}else if( value >50 && value <= 100){
			level[0] = "2��  ��";
			level[1] = "#FFFF00"; //��ɫ
		}else if( value > 100 && value <= 150){
			level[0] = "3��  �� ��";
			level[1] = "#FF6100"; //��ɫ
		}else if( value >150 && value <= 200){
			level[0] = "4��  �� ��";
			level[1] = "#FF0000"; //��ɫ
		}else if(value > 200 && value <= 300){
			level[0] = "5��  �� ��";
			level[1] = "#FF00FF"; //��ɫ
		}else {
			level[0] = "6��  �� ��";
			level[1] = "#8E236B"; //�ֺ�ɫ
		}
		return level;
	}

}
