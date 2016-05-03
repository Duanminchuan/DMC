package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dream.cutepet.R;

public class PetStoreModel {
	private String logo;// 图片
	private String name;// 店名
	private String address;// 地址
	private String type;// 宠物种类
	private String username;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取测试数据
	 * 
	 * @return
	 */
	public static List<PetStoreModel> getData() {
		List<PetStoreModel> data = new ArrayList<PetStoreModel>();
		PetStoreModel model;
		for (int i = 0; i < 12; i++) {
			model = new PetStoreModel();
			model.setLogo(R.drawable.test_homepage + "");
			model.setName("宠物店 " + i + " 号");
			model.setAddress("地址 " + i + " 号");
			model.setType("品种 " + i + " 号");
			data.add(model);
		}
		return data;
	}

	/**
	 * 将json转换成对象
	 * 
	 * @param result
	 * @return
	 */
	public static List<PetStoreModel> setJson(String result) {
		List<PetStoreModel> data = new ArrayList<PetStoreModel>();
		PetStoreModel model;
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				JSONArray array = jsonObject.getJSONArray("message");
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob = array.getJSONObject(i);
					model = new PetStoreModel();
					model.username = ob.getString("username");
					model.logo = ob.getString("logo");
					model.name = ob.getString("storename");
					model.address = ob.getString("address");
					model.type = ob.getString("type");
					model.id = ob.getString("id");
					data.add(model);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
