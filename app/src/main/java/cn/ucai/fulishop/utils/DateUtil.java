package cn.ucai.fulishop.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtil {

	private static SimpleDateFormat sdf = null;

	/** 获取系统时间 */
	public static String getCurrentDate() {
		Date d = new Date();
		sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return sdf.format(d);
	}

	/** 时间戳转换成字符窜 */
	public static String getDateToString(long time) {
		Date d = new Date(time * 1000);
		sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return sdf.format(d);
	}

	public static String getDateToString2(long time) {
		Date d = new Date(time * 1000);
		sdf = new SimpleDateFormat("yyyy-MM-dd  HH:MM:SS", Locale.CHINA);
		return sdf.format(d);
	}

	public static String getDateToString3(long time) {
		Date d = new Date(time * 1000);
		sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
		return sdf.format(d);
	}

	// 获取17位时间字符串
	public static String get17Date(Date date) {
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		return time.format(date);
	}

	// 获取星期几
	public static String getWeek(long time) {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		Date d = new Date(time * 1000);
		c.setTime(d);
		String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(week)) {
			week = "天";
		} else if ("2".equals(week)) {
			week = "一";
		} else if ("3".equals(week)) {
			week = "二";
		} else if ("4".equals(week)) {
			week = "三";
		} else if ("5".equals(week)) {
			week = "四";
		} else if ("6".equals(week)) {
			week = "五";
		} else if ("7".equals(week)) {
			week = "六";
		}
		return "星期" + week;
	}
}
