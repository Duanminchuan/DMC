package com.dream.cutepet.model;

public class DialogCharData {
	private String name;// 消息来自
	private String date;// 消息日期
	private String message;// 消息内容
	private boolean isComMsg;// 是否收到消息
	private String fromName;// 收到消息
	private String fromMessage;// 收到消息内容
	private String sendName;// 发送消息
	private String sendMessage;// 发送消息内容
	private int from_userhead;//聊天对方的头像
	private int send_userhead;//我的头象
	public int getFrom_userhead() {
		return from_userhead;
	}

	public void setFrom_userhead(int from_userhead) {
		this.from_userhead = from_userhead;
	}

	public int getSend_userhead() {
		return send_userhead;
	}

	public void setSend_userhead(int send_userhead) {
		this.send_userhead = send_userhead;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromMessage() {
		return fromMessage;
	}

	public void setFromMessage(String fromMessage) {
		this.fromMessage = fromMessage;
	}

	public String getSendName() {
		return sendName;
	}	

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}

	public boolean isComMsg() {
		return isComMsg;
	}

	public void setComMsg(boolean isComMsg) {
		this.isComMsg = isComMsg;
	}
}