package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonageModel {
	private String logo;
	private String name;
	private String time;
	private String picture_name;
	private String picture_icon;
	private String content;
	private String address;
	private int like;
	private String username;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPicture_name() {
		return picture_name;
	}

	public void setPicture_name(String picture_name) {
		this.picture_name = picture_name;
	}

	public String getPicture_icon() {
		return picture_icon;
	}

	public void setPicture_icon(String picture_icon) {
		this.picture_icon = picture_icon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static List<PersonageModel> setJson(String result) {
		List<PersonageModel> data = new ArrayList<PersonageModel>();
		PersonageModel model;
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				JSONArray array = jsonObject.getJSONArray("message");
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob = array.getJSONObject(i);
					model = new PersonageModel();
					model.username = ob.getString("username");
					model.logo = ob.getString("icon");
					model.name = ob.getString("nickname");
					model.address = ob.getString("address");
					model.time = ob.getString("time");
					model.picture_icon = ob.getString("image");
					model.picture_name = ob.getString("image_name");
					model.content = ob.getString("content");
					model.address = ob.optString("address", "");
					model.like = ob.getInt("like");
					model.id = ob.getInt("id");
					data.add(model);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
