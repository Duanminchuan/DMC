package com.dream.cutepet.util;

import java.util.List;

import com.dream.cutepet.model.PointModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 手势密码的 工具类
 * 
 * @author 浅念丶往事如梦
 * 
 */
public class GesturesUtil {

	private static String Name = "GesturesData";
	private static String key_password = "GesturesPwd";
	private static String key_flog = "GesturesFlog";

	/**
	 * 判断该位置是否存在
	 * 
	 * @param i
	 * @param point_num
	 * @return
	 */
	public static boolean hasNumber(int i, List<Integer> point_num) {
		for (int temp : point_num) {
			if (temp == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 保存密码
	 * 
	 * @param psd
	 * @param context
	 */
	public static void savePassword(String psd, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key_password, psd);
		editor.commit();
	}

	/**
	 * 获取密码
	 * 
	 * @param data
	 * @return
	 */
	public static String getPassword(List<Integer> data) {
		String temp = "";
		for (Integer demo : data) {
			temp += demo;
		}
		return temp;
	}

	/**
	 * 检查密码是否正确
	 * 
	 * @param psd
	 * @param context
	 * @return
	 */
	public static boolean ExaminePassword(String psd, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		String data = preferences.getString(key_password, "没有数据");
		if (data.equals(psd)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否设置了密码
	 * 
	 * @param context
	 * @return 设置了，返回true
	 */
	public static boolean hasPassword(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		String data = preferences.getString(key_password, "");
		if (!TextUtils.isEmpty(data)) {
			return true;
		}
		return false;
	}

	/**
	 * 设置是否使用手势密码
	 * 
	 * @param flog
	 * @param context
	 */
	public static void setGesturesUser(boolean flog, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key_flog, flog);
		editor.commit();
	}

	/**
	 * 判断是否使用手势密码
	 * 
	 * @param context
	 * @return 使用手势密码，则返回true
	 */
	public static boolean IsGestures(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		boolean flog = preferences.getBoolean(key_flog, false);
		if (flog)
			return true;
		return false;
	}

	/**
	 * 删除手势密码
	 * 
	 * @param context
	 */
	public static void deleteGesture(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		// 删除token
		preferences.edit().clear().commit();
	}

	/**
	 * 判断手势当前的位置是否在圆的范围内
	 * 
	 * @param x
	 *            手势当前的x坐标
	 * @param y
	 *            手势当前的y坐标
	 * @param point
	 *            9个圆的集合对象
	 * @return 存在，则返回的值 大于或者等于0
	 */
	public static int PassCircle(float x, float y, List<PointModel> point) {
		for (int i = 0; i < point.size(); i++) {
			float point_x = point.get(i).getPoint_x();
			float point_y = point.get(i).getPoint_y();
			float r = point.get(i).getR();
			if (x >= point_x - r && x <= point_x + r && y >= point_y - r
					&& y <= point_y + r) {
				return i;
			}
		}
		return -1;
	}

}
