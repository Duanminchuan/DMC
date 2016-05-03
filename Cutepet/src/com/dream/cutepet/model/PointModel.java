package com.dream.cutepet.model;

public class PointModel {
	private float point_x;
	private float point_y;
	private float r;

	public float getPoint_x() {
		return point_x;
	}

	public void setPoint_x(float point_x) {
		this.point_x = point_x;
	}

	public float getPoint_y() {
		return point_y;
	}

	public void setPoint_y(float point_y) {
		this.point_y = point_y;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public PointModel() {
	}

	public PointModel(float point_x, float point_y,float r) {
		this.point_x = point_x;
		this.point_y = point_y;
		this.r = r;
	}
}