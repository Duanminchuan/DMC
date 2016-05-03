package com.dream.cutepet.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户资料的数据对象
 * 
 * @author Administrator
 *
 */
public class UserModel {

	private String username;
	private String password;
	private int regtime;
	private String logo;
	private String nickname;
	private String sex;
	private String birth;
	private String constellation;
	private String occupation;
	private String corporation;
	private String site;
	private String hometown;
	private String mail;
	private String personality;
	private String attention;
	private String fans;
	private String enshrine;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRagtime() {
		return regtime;
	}

	public void setRagtime(int ragtime) {
		this.regtime = ragtime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getCorporation() {
		return corporation;
	}

	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getPersonality() {
		return personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getEnshrine() {
		return enshrine;
	}

	public void setEnshrine(String enshrine) {
		this.enshrine = enshrine;
	}

	public static void getDate() {
		// 测试数据
	}

	/**
	 * 从后台获取的json对象，将其转换成user的集合对象
	 * 
	 * @param json
	 * @return
	 */
	public static UserModel changeJson(String json) {
		UserModel model = new UserModel();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray arr = jsonObject.getJSONArray("message");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsObject = arr.getJSONObject(i);
				model.username = jsObject.getString("username");
				model.password = jsObject.getString("password");
				model.regtime = jsObject.getInt("regtime");
				model.logo = jsObject.getString("logo");
				model.nickname = jsObject.getString("nickname");
				model.sex = jsObject.getString("sex");
				model.birth = jsObject.getString("birth");
				model.constellation = jsObject.getString("constellation");
				model.occupation = jsObject.getString("occupation");
				model.corporation = jsObject.getString("corporation");
				model.site = jsObject.getString("site");
				model.hometown = jsObject.getString("hometown");
				model.mail = jsObject.getString("mail");
				model.personality = jsObject.getString("personality");
				model.attention = jsObject.getString("attention");
				model.fans = jsObject.getString("fans");
				model.enshrine = jsObject.getString("enshrine");

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return model;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}
