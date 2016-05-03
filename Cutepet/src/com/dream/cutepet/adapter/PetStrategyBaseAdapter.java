package com.dream.cutepet.adapter;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.PetStrategyModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PetStrategyBaseAdapter extends BaseAdapter {
	List<PetStrategyModel> data;
	Context context;
	LayoutInflater inflater;
	AsyncImageLoader imageLoader;
	String urlTop = "http://211.149.198.8:9805";

	public PetStrategyBaseAdapter() {

	}

	public PetStrategyBaseAdapter(List<PetStrategyModel> data, Context context) {
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
	}

	public void setData(List<PetStrategyModel> data) {
		this.data = data;
	}

	public int getCount() {
		return data == null ? 0 : data.size();
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
			convertView = inflater.inflate(R.layout.every_pet_strategy, null);
			holder = new ViewHolder();
			holder.pet_strategy_image = (ImageView) convertView
					.findViewById(R.id.pet_strategy_image);
			holder.pet_strategy_comment_chinese_name = (TextView) convertView
					.findViewById(R.id.pet_strategy_comment_chinese_name);
			holder.pet_strategy_comment_english_name = (TextView) convertView
					.findViewById(R.id.pet_strategy_comment_english_name);
			holder.pet_strategy_content_data = (TextView) convertView
					.findViewById(R.id.pet_strategy_content_data);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PetStrategyModel model = (PetStrategyModel) getItem(position);

		holder.pet_strategy_comment_chinese_name.setText(model
				.getPet_strategy_comment_chinese_name());
		holder.pet_strategy_comment_english_name.setText(model
				.getPet_strategy_comment_english_name());
		holder.pet_strategy_content_data.setText(model
				.getPet_strategy_content_data());

		String img = model.getPet_strategy_image();
		if (!TextUtils.isEmpty(img) && !img.equals("null")) {
			Log.e("getView	", "img = " + img);
			String imageUrl = urlTop + img;
			// 给imageview设置tag
			holder.pet_strategy_image.setTag(imageUrl);
			// 异步加载图片
			Bitmap bitmap = imageLoader.loadBitmap(holder.pet_strategy_image,
					imageUrl, true);
			if (bitmap != null) {
				holder.pet_strategy_image.setImageBitmap(bitmap);
			} else {
				holder.pet_strategy_image
						.setImageResource(R.drawable.friends_sends_pictures_no);
			}
		} else {
			holder.pet_strategy_image
					.setImageResource(R.drawable.friends_sends_pictures_no);
			holder.pet_strategy_image.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView pet_strategy_image;
		TextView pet_strategy_comment_chinese_name;
		TextView pet_strategy_comment_english_name;
		TextView pet_strategy_content_data;
	}

}
