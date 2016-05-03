package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetStrategyModel {

	String id;
	String username;
	String money;
	String grade;
	String trait;
	String pet_strategy_image; // image
	String pet_strategy_comment_chinese_name; // petname
	String pet_strategy_comment_english_name;
	String pet_strategy_content_data; // content

	public String getPet_strategy_image() {
		return pet_strategy_image;
	}

	public String getTrait() {
		return trait;
	}

	public void setTrait(String trait) {
		this.trait = trait;
	}

	public void setPet_strategy_image(String pet_strategy_image) {
		this.pet_strategy_image = pet_strategy_image;
	}

	public String getPet_strategy_comment_chinese_name() {
		return pet_strategy_comment_chinese_name;
	}

	public void setPet_strategy_comment_chinese_name(
			String pet_strategy_comment_chinese_name) {
		this.pet_strategy_comment_chinese_name = pet_strategy_comment_chinese_name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPet_strategy_comment_english_name() {
		return pet_strategy_comment_english_name;
	}

	public void setPet_strategy_comment_english_name(
			String pet_strategy_comment_english_name) {
		this.pet_strategy_comment_english_name = pet_strategy_comment_english_name;
	}

	public String getPet_strategy_content_data() {
		return pet_strategy_content_data;
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

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setPet_strategy_content_data(String pet_strategy_content_data) {
		this.pet_strategy_content_data = pet_strategy_content_data;
	}

	/**
	 * 把json解析放到data
	 * @param result
	 * @return
	 */
	public static List<PetStrategyModel> setJson(String result) {
		List<PetStrategyModel> data = new ArrayList<PetStrategyModel>();
		PetStrategyModel model;

		try {
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				JSONArray array = jsonObject.getJSONArray("message");
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob = array.getJSONObject(i);
					model = new PetStrategyModel();
					model.id = ob.getString("id");
					model.username = ob.getString("username");
					model.pet_strategy_comment_chinese_name = ob.getString("petname");
					model.grade = ob.getString("grade");
					model.money = ob.getString("money");
					model.trait = ob.getString("trait");
					model.pet_strategy_content_data = ob.getString("content");
					model.pet_strategy_image = ob.getString("image");
					data.add(model);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;

	}

}
