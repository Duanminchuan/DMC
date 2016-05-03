package com.dream.cutepet.model;

import java.util.List;
import com.google.gson.Gson;

/**
 * 动态相册的数据model
 * 
 * @author 浅念丶往事如梦
 * 
 */
public class DynamicAlbumModel {
	private int status;
	private List<AlbumData> message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<AlbumData> getMessage() {
		return message;
	}

	public void setMessage(List<AlbumData> message) {
		this.message = message;
	}

	public class AlbumData {
		private String id;
		private String username;
		private String albumname;
		private String albumlogo;
		private String describe;
		private String quantity;
		private String create_time;

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

		public String getAlbumlogo() {
			return albumlogo;
		}

		public void setAlbumlogo(String albumlogo) {
			this.albumlogo = albumlogo;
		}

		public String getDescribe() {
			return describe;
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

	}
	
	/**
	 * 返回json转换后的数据对象
	 * @param result
	 * @return
	 */
	public static DynamicAlbumModel setJson(String result) {
		DynamicAlbumModel data_album = new DynamicAlbumModel();
		Gson gson = new Gson();
		data_album = gson.fromJson(result, DynamicAlbumModel.class);
		return data_album;
	}
}
