package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SquareModel {
	String square_portrait;
	String square_neckname;
	String square_comment_content;
	String square_comment_time;
	String square_image;
	String square_address;
	String square_comment_num;
	String square_praise_num;
	String square_id;
	String square_username;
	String square_comment;
	public String getSquare_comment() {
		return square_comment;
	}

	public void setSquare_comment(String square_comment) {
		this.square_comment = square_comment;
	}

	public String getSquare_id() {
		return square_id;
	}

	public String getSquare_username() {
		return square_username;
	}

	public void setSquare_username(String square_username) {
		this.square_username = square_username;
	}

	public void setSquare_id(String square_id) {
		this.square_id = square_id;
	}

	public String getSquare_image() {
		return square_image;
	}

	public void setSquare_image(String square_image) {
		this.square_image = square_image;
	}

	public String getSquare_address() {
		return square_address;
	}

	public void setSquare_address(String square_address) {
		this.square_address = square_address;
	}

	public String getSquare_comment_num() {
		return square_comment_num;
	}

	public void setSquare_comment_num(String square_comment_num) {
		this.square_comment_num = square_comment_num;
	}

	public String getSquare_praise_num() {
		return square_praise_num;
	}

	public void setSquare_praise_num(String square_praise_num) {
		this.square_praise_num = square_praise_num;
	}

	public String getSquare_portrait() {
		return square_portrait;
	}

	public void setSquare_portrait(String square_portrait) {
		this.square_portrait = square_portrait;
	}

	public String getSquare_neckname() {
		return square_neckname;
	}

	public void setSquare_neckname(String square_neckname) {
		this.square_neckname = square_neckname;
	}

	public String getSquare_comment_content() {
		return square_comment_content;
	}

	public void setSquare_comment_content(String square_comment_content) {
		this.square_comment_content = square_comment_content;
	}

	public String getSquare_comment_time() {
		return square_comment_time;
	}

	public void setSquare_comment_time(String square_comment_time) {
		this.square_comment_time = square_comment_time;
	}
	
	/**
	 * 把json解析放到data
	 * @param result
	 * @return
	 */
	public static List<SquareModel> setJson(String result) {
		List<SquareModel> data = new ArrayList<SquareModel>();
		SquareModel model;
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				JSONArray array = jsonObject.getJSONArray("message");
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob = array.getJSONObject(i);
					model = new SquareModel();
					model.square_id = ob.getString("id");
					model.square_username = ob.getString("username");
					model.square_neckname = ob.getString("nickname");
					model.square_comment_time = ob.getString("create_time");
					model.square_comment_content = ob.getString("content");
					model.square_address = ob.optString("address", "");
					model.square_praise_num = ob.getString("like");
					model.square_comment_num = ob.getString("comment");
					model.square_image=ob.getString("picture");
					model.square_portrait=ob.getString("icon");
					data.add(model);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}
