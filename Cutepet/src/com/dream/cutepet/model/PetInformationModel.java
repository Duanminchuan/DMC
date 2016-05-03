package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;

import com.dream.cutepet.R;

public class PetInformationModel {
	private String logo;
	private String name;
	private String type;
	private String habit;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHabit() {
		return habit;
	}

	public void setHabit(String habit) {
		this.habit = habit;
	}

	/**
	 * 获取测试数据
	 * @return
	 */
	public static List<PetInformationModel> getData() {
		List<PetInformationModel> data = new ArrayList<PetInformationModel>();
		PetInformationModel model;
		for (int i = 0; i < 10; i++) {
			model = new PetInformationModel();
			model.setLogo(R.drawable.homepage_personage_pic1 + "");
			model.setName("测试名字-->" + i + "号数据");
			model.setType("测试品种-->" + i + "号数据");
			model.setHabit("测试习性-->" + i + "号数据");
			data.add(model);
		}
		return data;
	}
}
