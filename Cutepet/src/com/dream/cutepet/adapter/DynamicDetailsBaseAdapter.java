package com.dream.cutepet.adapter;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.DynamicDetailsModel;
import com.dream.cutepet.model.DynamicDetailsModel.Message;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.TimeUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DynamicDetailsBaseAdapter extends BaseAdapter {
	List<DynamicDetailsModel.Message> data;
	Context context;
	LayoutInflater inflater;
	AsyncImageLoader imageLoader;
	private String url_top = "http://211.149.198.8:9805";

	public DynamicDetailsBaseAdapter() {

	}

	public DynamicDetailsBaseAdapter(List<DynamicDetailsModel.Message> data,
			Context context) {
		this.data = data;
		this.context = context;
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<DynamicDetailsModel.Message> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (data != null) {
			return data.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.every_dynamic_details, null);
			holder = new ViewHolder();
			holder.dynamic_details_portrait = (ImageView) convertView
					.findViewById(R.id.dynamic_details_comment_portrait);
			holder.dynamic_details_neckname = (TextView) convertView
					.findViewById(R.id.dynamic_details_comment_neckname);
			holder.dynamic_details_comment_content = (TextView) convertView
					.findViewById(R.id.dynamic_details_comment_content);
			holder.dynamic_details_comment_time = (TextView) convertView
					.findViewById(R.id.dynamic_details_comment_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DynamicDetailsModel.Message model = (Message) getItem(position);
		holder.dynamic_details_neckname.setText(model.getNickname());

		String time = model.getCreate_time();
		if (!TextUtils.isEmpty(time) && !time.equals("null")) {
			holder.dynamic_details_comment_time.setText(TimeUtil
					.showTime(TimeUtil.changeTime(time)));
		}
		holder.dynamic_details_comment_content.setText(model.getContent());

		String tx = model.getIcon();
		if (!TextUtils.isEmpty(tx) && !tx.equals("null")) {
			String url_icon = url_top + tx;
			holder.dynamic_details_portrait.setTag(url_icon);
			Bitmap bt = imageLoader.loadBitmap(holder.dynamic_details_portrait,
					url_icon, false);
			if (bt != null) {
				Bitmap cc_tx = BitmapUtil.toRoundBitmap(bt);
				holder.dynamic_details_portrait.setImageBitmap(cc_tx);
			} else {
				holder.dynamic_details_portrait
						.setImageResource(R.drawable.icon_tx);
			}
		} else {
			holder.dynamic_details_portrait
					.setImageResource(R.drawable.icon_tx);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView dynamic_details_portrait;
		TextView dynamic_details_neckname;
		TextView dynamic_details_comment_content;
		TextView dynamic_details_comment_time;
	}
}
