package com.dream.cutepet.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class DynamicDetailsModel {
	private int status;
	private List<Message> message;

	public DynamicDetailsModel() {
		message = new ArrayList<DynamicDetailsModel.Message>();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Message> getMessage() {
		return message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}

	public class Message {
		private String id;
		private String issue_id;
		private String uid;
		private String nickname;
		private String content;
		private String create_time;
		private String icon;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIssue_id() {
			return issue_id;
		}

		public void setIssue_id(String issue_id) {
			this.issue_id = issue_id;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		@Override
		public String toString() {
			return "DynamicDetailsModel [id=" + id + ", issue_id=" + issue_id
					+ ", uid=" + uid + ", nickname=" + nickname + ", content="
					+ content + ", create_time=" + create_time + ", icon="
					+ icon + "]";
		}
	}

	@Override
	public String toString() {
		return "DynamicDetailsModel [status=" + status + ", message="
				+ message.toString() + "]";
	}

	public static DynamicDetailsModel setJson(String result) {
		DynamicDetailsModel data = new DynamicDetailsModel();
		try {
			JSONObject jb = new JSONObject(result);
			if(jb.getInt("status") == 1){
				Gson gson = new Gson();
				data = gson.fromJson(result, DynamicDetailsModel.class);
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
