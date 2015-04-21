package org.maxsoft.utils;

import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;

/**
 * @function 关于日期信息的工具类
 * @author MaxSoft
 * @date 2014-12-20
 * @version 1.0.0
 */
public class DateUtils {
	private final static long DAYTIME=1000*60*60*24;
	public static final int	FMT_DATE_YYYYMMDD			= 1 ;
	public static final int	FMT_DATE_YYYYMMDD_HHMMSS	= 2 ;
	public static final int	FMT_DATE_HHMMSS				= 3 ;
	public static final int	FMT_DATE_HHMM				= 4 ;
	public static final int	FMT_DATE_SPECIAL    		= 5 ;
	public static final int FMT_DATE_CHINESE			= 6 ;

	public static String formatDate ( Date date , int nFmt ){
		SimpleDateFormat format = new SimpleDateFormat ();
		switch (nFmt){
			case DateUtils.FMT_DATE_YYYYMMDD :
				format.applyPattern ( "yyyy-MM-dd" ) ;
				break ;
			case DateUtils.FMT_DATE_YYYYMMDD_HHMMSS :
				format.applyPattern ( "yyyy-MM-dd HH:mm:ss" ) ;
				break ;
			case DateUtils.FMT_DATE_HHMM :
				format.applyPattern ( "HH:mm" ) ;
				break ;
			case DateUtils.FMT_DATE_HHMMSS :
				format.applyPattern ( "HH:mm:ss" ) ;
				break ;
			case DateUtils.FMT_DATE_SPECIAL :
				format.applyPattern ( "yyyyMMdd" ) ;
				break ;
			case DateUtils.FMT_DATE_CHINESE :
				format.applyPattern ("yyyy年MM月dd日");
				break ;
			default :
				break;
		}
		return format.format ( date ) ;
	}
	
	public static Date parseDate(String strDate, int nFmtDate) throws Exception {
		SimpleDateFormat fmtDate = new SimpleDateFormat();
		switch (nFmtDate) {
		default:
		case FMT_DATE_YYYYMMDD:
			fmtDate.applyPattern("yyyy-MM-dd");
			break;
		case FMT_DATE_YYYYMMDD_HHMMSS:
			fmtDate.applyPattern("yyyy-MM-dd HH:mm:ss");
			break;
		case FMT_DATE_HHMM:
			fmtDate.applyPattern("HH:mm");
			break;
		case FMT_DATE_HHMMSS:
			fmtDate.applyPattern("HH:mm:ss");
			break;
		}
		return fmtDate.parse(strDate);
	}
	
	/**
	 * @function 获取下一天
	 * @param date
	 * @return
	 */
	public static Date getNextDate(Date date,int num){
		Date next_date = new Date(date.getTime() + DAYTIME * num);
		return next_date;
	}
	
	/**
	 * @function 获取前一天
	 * @param date
	 * @return
	 */
	public static Date getYesterdayDate(Date date,int num){
		Date next_date = new Date(date.getTime() - DAYTIME * num);
		return next_date;
	}
	
	/**
	 * @function 获得当前的小时(24小时制)
	 * @return 当前小时
	 */
	public static int getHour(){
		int hour = 0;
		Date now = new Date();
		String formatDate = DateUtils.formatDate(now,DateUtils.FMT_DATE_HHMMSS);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        dateFormat.setLenient(true);
        try{
        	Date timeDate = null;
            timeDate = dateFormat.parse(formatDate);
            String[] split = dateFormat.format(timeDate).split(":");
            hour = Integer.valueOf(split[0]).intValue();
        } catch (ParseException e){
            e.printStackTrace();
        }
		return hour;
	}
	
	public static int getMonth(){
		Date date = new Date();
		String formatDate = DateUtils.formatDate(date, DateUtils.FMT_DATE_YYYYMMDD);
		String month = formatDate.substring(5, 7);
		if(month.indexOf("0")==1){
			month = month.replace("0", "");
		}
		return Integer.parseInt(month);
	}
	
	/**
	 * @function 获取互联网时间
	 * @return Date
	 */
	public static Date getInternetDate(){
	    URL url;
	    Date date = new Date();
		try {
			url = new URL("http://www.beijing-time.org/time.asp");
			URLConnection uc=url.openConnection();//生成连接对象
			uc.connect(); //发出连接
			long ld=uc.getDate(); //取得网站日期时间
			if(ld == 0){
				date = new Date();
			}else{
				date=new Date(ld); //转换为标准时间对象
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(date == null){
				date = new Date();
			}
		}
		return date;
	}
	
	/**
	 * @function 将20150101格式的字符串转为日期字符串型
	 * @param ss 
	 * @return
	 */
	public static String formatStringToDateF(String ss) {
		if(ss == null || ss.trim().length()<8){
			return "";
		}
		String temp = "";
		temp = ss.replaceAll(" ", "");
		temp = ss.substring(0,8);
		char[] charArray = temp.toCharArray();
		StringBuffer sb_temp = new StringBuffer();
		for(int i=0;i<charArray.length&&charArray.length == 8; i++){
			sb_temp.append(charArray[i]);
			if(i==3 || i==5){
				sb_temp.append("-");
			}
		}
		temp = sb_temp.toString() + ss.substring(8, ss.length());
		return temp;
	}
	
	/**
	 * @function 将日期字符串中的年月日换为"-"
	 * @param date
	 * @return 日期字符串
	 */
	public static String formatStringFromChinese(String date){
		if(date != null){
			date = date.replace("年", "-").replace("月", "-").replace("日", "");
		}else{
			date = "";
		}
		return date;
	}

}
