package com.dream.cutepet.adapter;

import java.util.Date;
import java.util.List;

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

import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.SquareModel;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.MyListUtil;
import com.dream.cutepet.util.TimeUtil;
import com.dream.cutepet.view.MyGridView;

public class SquareBaseAdapter extends BaseAdapter {
	List<SquareModel> data;
	Context context;
	LayoutInflater inflater;
	AsyncImageLoader imageLoader;
	SquareModel model;
	String urlTop = "http://211.149.198.8:9805";
	
	public SquareBaseAdapter() {

	}

	@SuppressLint("UseSparseArrays") 
	public SquareBaseAdapter(List<SquareModel> data, Context context) {
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
	}

	public void setData(List<SquareModel> data) {
		this.data = data;
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
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.every_square, null);
			holder.square_portrait = (ImageView) convertView
					.findViewById(R.id.square_portrait);
			holder.square_neckname = (TextView) convertView
					.findViewById(R.id.square_nickname);
			holder.square_comment_content = (TextView) convertView
					.findViewById(R.id.square_content);
			holder.square_comment_time = (TextView) convertView
					.findViewById(R.id.square_time);
			holder.square_image = (MyGridView) convertView
					.findViewById(R.id.square_image);
			holder.square_address = (TextView) convertView
					.findViewById(R.id.square_address);
			holder.square_comment_num = (TextView) convertView
					.findViewById(R.id.square_comment_num);
			holder.square_praise_num = (TextView) convertView
					.findViewById(R.id.square_praise_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		model = (SquareModel) getItem(position);

		String name = model.getSquare_neckname();
		if (!TextUtils.isEmpty(name) && !name.equals("null")) {
			holder.square_neckname.setText(name + "");
		} else {
			holder.square_neckname.setText(model.getSquare_username() + "");
		}

		Date date = TimeUtil.changeTime(model.getSquare_comment_time());
		holder.square_comment_time.setText(TimeUtil.showTime(date));

		holder.square_comment_content
				.setText(model.getSquare_comment_content());
		holder.square_praise_num.setText(model.getSquare_praise_num());
		holder.square_comment_num.setText(model.getSquare_comment_num());
		
		if(!TextUtils.isEmpty(model.getSquare_address())){
			holder.square_address.setVisibility(View.VISIBLE);
			holder.square_address.setText(model.getSquare_address());
		}else{
			holder.square_address.setVisibility(View.INVISIBLE);
		}
		
		String img = model.getSquare_image();
		List<String> pic = MyListUtil.changeStringToList(img, ",");
		SquareGridviewAdapter adapter = new SquareGridviewAdapter(context, pic);
		if (pic.size() == 1) {
			holder.square_image.setNumColumns(1);
		} else if (pic.size() == 2) {
			holder.square_image.setNumColumns(2);
		} else if (pic.size() > 2) {
			holder.square_image.setNumColumns(3);
		}
		holder.square_image.setAdapter(adapter);

		String tx = model.getSquare_portrait();
		if (!TextUtils.isEmpty(tx) && !tx.equals("null")) {
			String portraitUrl = urlTop + tx;
			// 给imageview设置tag
			holder.square_portrait.setTag(portraitUrl);
			// 异步加载图片
			Bitmap bt_icon = imageLoader.loadBitmap(holder.square_portrait,
					portraitUrl, false);
			if (bt_icon != null) {
				holder.square_portrait.setImageBitmap(BitmapUtil
						.toRoundBitmap(bt_icon));
			} else {
				holder.square_portrait.setImageResource(R.drawable.icon_tx);
			}
		} else {
			holder.square_portrait.setImageResource(R.drawable.icon_tx);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView square_portrait;
		TextView square_neckname;
		TextView square_comment_content;
		TextView square_comment_time;
		MyGridView square_image;
		TextView square_address;
		TextView square_comment_num;
		TextView square_praise_num;
	}
}
