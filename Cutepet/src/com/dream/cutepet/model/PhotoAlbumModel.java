package com.dream.cutepet.model;

import java.util.List;

import com.google.gson.Gson;

/**
 * 相册的数据对象
 * 
 * @author Administrator
 * 
 */
public class PhotoAlbumModel {

	private int status;
	private List<PhotoAlbumData> message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<PhotoAlbumData> getMessage() {
		return message;
	}

	public void setMessage(List<PhotoAlbumData> message) {
		this.message = message;
	}

	public class PhotoAlbumData {
		private String id;
		private String username;
		private String albumname;
		private String picture;
		private String time;
		private String quantity;

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

		public String getAlbumname() {
			return albumname;
		}

		public void setAlbumname(String albumname) {
			this.albumname = albumname;
		}

		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

	}

	public static PhotoAlbumModel setJson(String json) {
		PhotoAlbumModel data = new PhotoAlbumModel();
		Gson gson = new Gson();
		data = gson.fromJson(json, PhotoAlbumModel.class);
		return data;
	}
}
