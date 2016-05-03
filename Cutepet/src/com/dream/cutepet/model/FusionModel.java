package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FusionModel {
	private String logo;
	private String time;
	private String content;
	private String picture;
	private String id;
	private String username;
	private String nickname;
	private String address;
	private String like;
	private String comment;
	

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTime() {
		return time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public static List<FusionModel> getJson(String result) {
		List<FusionModel> data = new ArrayList<FusionModel>();
		FusionModel model;

		try {
			JSONObject jsonObject = new JSONObject(result);
			int status=jsonObject.getInt("status");
			if(status==1){
				JSONArray array=jsonObject.getJSONArray("message");
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob=array.getJSONObject(i);
					model=new FusionModel();
					model.id=ob.getString("id");
					model.username=ob.getString("username");
					model.nickname=ob.getString("nickname");
					model.time=ob.getString("create_time");
					model.content=ob.getString("content");
					model.address=ob.getString("address");
					model.like=ob.getString("like");
					model.comment=ob.getString("comment");
					model.picture=ob.getString("picture");
					model.logo=ob.getString("icon");
					data.add(model);
				}
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 获取测试数据
	 * 
	 * @return
	 *//*
	public static List<FusionModel> getData() {
		List<FusionModel> data = new ArrayList<FusionModel>();
		FusionModel model;

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_1 + "");
		model.setTime("2012.06.10");
		model.setContent("我是测试数据-------------------->1号数据");
		model.setPicture(new String[] { R.drawable.fusion_01_01 + "" });
		data.add(model);

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_2 + "");
		model.setTime("2012.08.10");
		model.setContent("我是测试数据-------------------->2号数据");
		model.setPicture(new String[] { R.drawable.fusion_02_01 + "", R.drawable.fusion_02_02 + "" });
		data.add(model);

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_3 + "");
		model.setTime("2013.05.10");
		model.setContent("我是测试数据-------------------->3号数据");
		model.setPicture(new String[] { R.drawable.fusion_03_01 + "", R.drawable.fusion_03_02 + "",
				R.drawable.fusion_03_03 + "" });
		data.add(model);

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_1 + "");
		model.setTime("2013.05.10");
		model.setContent("我是测试数据-------------------->4号数据");
		model.setPicture(new String[] { R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "",
				R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "" });
		data.add(model);

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_2 + "");
		model.setTime("2013.05.10");
		model.setContent("我是测试数据-------------------->5号数据");
		model.setPicture(new String[] { R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "",
				R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "" });
		data.add(model);

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_2 + "");
		model.setTime("2013.05.10");
		model.setContent("我是测试数据-------------------->6号数据");
		model.setPicture(
				new String[] { R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "",
						R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "" });
		data.add(model);

		model = new FusionModel();
		model.setLogo(R.drawable.fusion_logo_3 + "");
		model.setTime("2013.05.10");
		model.setContent("我是测试数据-------------------->7号数据");
		model.setPicture(new String[] { R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "",
				R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "",
				R.drawable.fusion_03_01 + "", R.drawable.fusion_03_01 + "" });
		data.add(model);

		return data;
		}*/
	
}
