package com.dream.cutepet.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dream.cutepet.R;
import com.dream.cutepet.util.BitmapUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class WriteTalkUpLoadAdapter extends BaseAdapter {
	private List<String> data;
	private LayoutInflater inflater;
	private static Map<Integer, Boolean> isSelected;

	public WriteTalkUpLoadAdapter() {

	}

	@SuppressLint("UseSparseArrays") 
	public WriteTalkUpLoadAdapter(Context context, List<String> data) {
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}

	private void initData() {
		for (int i = 0; i < data.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	/**
	 * 设置选中后的集合
	 * 
	 * @param isSelected
	 */
	public static void setIsSelected(Map<Integer, Boolean> isSelected) {
		WriteTalkUpLoadAdapter.isSelected = isSelected;
	}

	/**
	 * 获取 adapter的 选中集合
	 * 
	 * @return
	 */
	public static Map<Integer, Boolean> getIsSelected() {
		return isSelected;
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
			convertView = inflater.inflate(R.layout.activity_up_photo_item,
					null);
			holder = new ViewHolder();
			holder.cb_check = (CheckBox) convertView
					.findViewById(R.id.cb_up_photo_item_checked);
			holder.iv_picture = (ImageView) convertView
					.findViewById(R.id.iv_up_photo_item_picture);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv_picture.setImageBitmap(BitmapUtil.getDiskBitmap(data
				.get(position)));

		return convertView;
	}

	public static class ViewHolder {
		ImageView iv_picture;
		public CheckBox cb_check;
	}

}
