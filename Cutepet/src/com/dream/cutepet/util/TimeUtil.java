package com.dream.cutepet.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间操作类，用于将时间转换成相应的格式
 * 
 * @author 余飞
 * 
 */
public class TimeUtil {

	/**
	 * 获取该时间的 指定时长格式 yyyy年MM月dd日
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeBy2(long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(date);
	}

	/**
	 * 获取该时间的 指定时长格式 yyyy.MM.dd
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeBy1(long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		return format.format(date);
	}

	/**
	 * 将 格林尼治标准时间 转换成 Date型
	 * 
	 * @param time
	 *            微博上的格林尼治标准时间
	 * @return Date型的时间
	 */
	public static Date changeTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		try {
			Date date = format.parse(time);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将时间转换成GMT
	 * @param date
	 * @return
	 */
	public static String changeTimeToGMT(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		String time = format.format(date);
		return time;
	}

	/**
	 * 获取昨天的起始时间
	 * 
	 * @param lastDay
	 * @return
	 */
	public static long getYesterdayMinTimeMillis(Calendar lastDay) {
		int year = lastDay.get(Calendar.YEAR);
		int month = lastDay.get(Calendar.MONTH);
		int day = lastDay.get(Calendar.DAY_OF_MONTH) - 1;

		lastDay.set(year, month, day, 0, 0, 0);
		long minLastday = lastDay.getTimeInMillis();
		return minLastday;
	}

	/**
	 * 获取昨天的结束时间
	 * 
	 * @param lastDay
	 * @return
	 */
	public static long getYesterdayMaxTimeMillis(Calendar lastDay) {
		int year = lastDay.get(Calendar.YEAR);
		int month = lastDay.get(Calendar.MONTH);
		int day = lastDay.get(Calendar.DAY_OF_MONTH) - 1;

		lastDay.set(year, month, day, 23, 59, 59);
		long maxLastday = lastDay.getTimeInMillis();
		return maxLastday;
	}

	/**
	 * 判断是不是昨天的时间
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isYesterday(long time) {
		Calendar lastDay = Calendar.getInstance();
		if (time >= getYesterdayMinTimeMillis(lastDay)
				&& time <= getYesterdayMaxTimeMillis(lastDay)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据时间的显示条件 来 显示时间
	 * 
	 * @param date
	 * @return
	 */
	public static String showTime(Date date) {
		Calendar nowtime = Calendar.getInstance();
		// 计算时间差
		long temp = nowtime.getTimeInMillis() - date.getTime();

		long days = temp / (1000 * 60 * 60 * 24);
		long hours = (temp - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (temp - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);

		String time = "";
		if (days > 0) {
			time = days + "天前";
		} else if (isYesterday(date.getTime())) {
			time = "昨天";
		} else if (days == 0 && hours > 0) {
			time = hours + "小时前";
		} else if (days == 0 && hours == 0 && minutes > 0) {
			time = minutes + "分钟前";
		} else if (days == 0 && hours == 0 && minutes == 0) {
			time = "刚刚";
		}
		return time;
	}

}
