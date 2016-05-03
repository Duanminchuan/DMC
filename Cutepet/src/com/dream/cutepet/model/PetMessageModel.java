package com.dream.cutepet.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PetMessageModel {
	private int id;
	private String username;
	private String nickname;
	private int petnumber;
	private String sex;
	private String type;
	private int age;
	private String content;
	private String image;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getPetnumber() {
		return petnumber;
	}

	public void setPetnumber(int petnumber) {
		this.petnumber = petnumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public static PetMessageModel setJson(String result) {
		PetMessageModel model = new PetMessageModel();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject object = jsonObject.getJSONObject("message");
			model.id = object.getInt("id");
			model.username = object.getString("username");
			model.nickname = object.getString("nickname");
			model.petnumber = object.getInt("petnumber");
			model.sex = object.getString("sex");
			model.type = object.getString("type");
			model.age = object.getInt("age");
			model.content = object.getString("content");
			model.image = object.getString("image");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return model;
	}

}
