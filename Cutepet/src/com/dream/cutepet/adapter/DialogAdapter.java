package com.dream.cutepet.adapter;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.model.DialogCharData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DialogAdapter extends BaseAdapter {
	private int COM_MSG = 0;// 收到的消息
	private int TO_MSG = 1;// 发送的消息
	private int ITEMCOUNT = 2;// 消息类型的总数
	private List<DialogCharData> list_char;// 消息对象数组
	private LayoutInflater inflater;

	public DialogAdapter() {
	}

	public DialogAdapter(List<DialogCharData> list_char, Context context) {
		this.list_char = list_char;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list_char.size();
	}

	@Override
	public Object getItem(int position) {
		return list_char.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DialogCharData charData = list_char.get(position);
		boolean isComMsg = charData.isComMsg();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_dialog_left, null);
			viewHolder.fromContent = (TextView) convertView
					.findViewById(R.id.from_chatcontent);
			viewHolder.sendContent = (TextView) convertView
					.findViewById(R.id.send_chatcontent);
			viewHolder.sendLayout = (RelativeLayout) convertView
					.findViewById(R.id.send_layout);
			viewHolder.getLayout = (RelativeLayout) convertView
					.findViewById(R.id.get_layout);
			viewHolder.from_userhead = (ImageView) convertView
					.findViewById(R.id.from_userhead);
			viewHolder.send_userhead = (ImageView) convertView
					.findViewById(R.id.send_userhead);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		/*
		 * DensityUtil densityUtil = new DensityUtil();
		 * densityUtil.setMargins(viewHolder.send_userhead, 0,
		 * DensityUtil.px2dip(context, 30), 0, 0);
		 * densityUtil.setMargins(viewHolder.fromContent,
		 * DensityUtil.px2dip(context, 5), 0, DensityUtil.px2dip(context, 100),
		 * 0); densityUtil.setMargins(viewHolder.sendContent,
		 * DensityUtil.px2dip(context, 100), 0, DensityUtil.px2dip(context, 5),
		 * 0);
		 */
		if (isComMsg) {
			// 收到消息 from显示
			charData.setFrom_userhead(R.drawable.chat_user_icon);
			viewHolder.sendLayout.setVisibility(View.GONE);
			viewHolder.getLayout.setVisibility(View.VISIBLE);
			// 对内容做处理

			viewHolder.from_userhead.setImageResource(charData
					.getFrom_userhead());
			viewHolder.fromContent.setText(charData.getFromMessage());
		} else {
			// 发送消息 send显示
			charData.setSend_userhead(R.drawable.chat_pet_icon);
			viewHolder.sendLayout.setVisibility(View.VISIBLE);
			viewHolder.getLayout.setVisibility(View.GONE);
			// 对内容做处理
			viewHolder.send_userhead.setImageResource(charData
					.getSend_userhead());
			viewHolder.sendContent.setText(charData.getSendMessage());
		}
		return convertView;
	}

	class ViewHolder {
		public TextView fromContent;
		public TextView sendContent;
		ImageView from_userhead;
		ImageView send_userhead;
		RelativeLayout sendLayout;
		RelativeLayout getLayout;
		public boolean isComMsg = true;
	}

	/**
	 * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
	 */
	public int getItemViewType(int position) {
		DialogCharData charData = list_char.get(position);

		if (charData.isComMsg()) {// 收到的消息
			return COM_MSG;
		} else {// 自己发送的消息
			return TO_MSG;
		}
	}

	/**
	 * Item类型的总数
	 */
	public int getViewTypeCount() {
		return ITEMCOUNT;
	}
}
