package com.dream.cutepet.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class SharedPreferencesUtil {

	/**
	 * 保存token值到本地
	 * 
	 * @param token
	 */
	public static void saveToken(Context context, String token) {
		Log.i("saveToken", "token！" + token);
		SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("token", token);
		editor.commit();
		Log.i("saveToken", "token保存成功！");
	}

	/**
	 * 获取数据
	 * 
	 * @return str[0]=tel;str[1]=token;
	 */
	public static String getData(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
		String data = preferences.getString("token", null);
		Log.i("getData", "token获取成功！");
		return data;
	}

	/**
	 * 删除token
	 * 
	 * @param context
	 */
	public static void deleteData(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
		// 删除token
		preferences.edit().clear().commit();
		Log.i("deleteData", "token删除成功！");
	}
	
	/**
	 * 判断账号是否登录
	 * 
	 * @return
	 */
	public static boolean checkLogin(Context context) {
		String tok = SharedPreferencesUtil.getData(context);
		if (tok != null && !tok.equals("")) {
			return true;
		}
		Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
		return false;
	}
	
	/**
	 * 获取存储在本地的username
	 * @param context
	 * @return
	 */
	public static String getUsername(Context context){
		if(checkLogin(context)){
			String tok = SharedPreferencesUtil.getData(context);
			return tok.split(",")[1];
		}else{
			return "未登录";
		}
	}
	
	/**
	 * 获取存储在本地的token
	 * @param context
	 * @return
	 */
	public static String getToken(Context context){
		if(checkLogin(context)){
			String tok = SharedPreferencesUtil.getData(context);
			return tok.split(",")[0];
		}else{
			return "未登录";
		}
	}
	
	


}
