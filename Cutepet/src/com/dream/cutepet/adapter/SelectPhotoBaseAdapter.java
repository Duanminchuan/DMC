package com.dream.cutepet.adapter;

import java.util.List;
import com.dream.cutepet.R;
import com.dream.cutepet.util.BitmapUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SelectPhotoBaseAdapter extends BaseAdapter {

	List<String> data;
	Context context;
	LayoutInflater inflater;

	public SelectPhotoBaseAdapter() {

	}

	public SelectPhotoBaseAdapter(List<String> data, Context context) {
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.activity_select_photo, null);
			holder = new ViewHolder();
			holder.select_photo = (ImageView) convertView
					.findViewById(R.id.iv_select_photo_item_picture);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.select_photo.setImageBitmap(BitmapUtil.getDiskBitmap(data
				.get(position)));
		return convertView;
	}

	public static class ViewHolder {
		ImageView select_photo;
	}

}
