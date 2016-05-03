package com.dream.cutepet.adapter;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.PersonageModel;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.TimeUtil;

public class HomePageAdapter extends BaseAdapter implements OnClickListener {

	List<PersonageModel> data;
	Context context;
	LayoutInflater inflater;
	LayoutInflater inflater_m;
	private AsyncImageLoader imageLoader;
	String url_Top = "http://211.149.198.8:9805";
	private CallParise mCallParise;
	private SetMessage mSetMessage;

	public HomePageAdapter() {

	}

	public HomePageAdapter(Context context, List<PersonageModel> data,
			CallParise mCallParise, SetMessage mSetMessage) {
		this.data = data;
		this.context = context;
		this.mCallParise = mCallParise;
		this.mSetMessage = mSetMessage;
		inflater = LayoutInflater.from(context);
		inflater_m = LayoutInflater.from(context);
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
	}

	public interface CallParise {
		public void click(View v);
	}

	public interface SetMessage {
		public void click(View v);
	}

	public void setData(List<PersonageModel> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.activity_homepage_listview_item, null);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.iv_homepage_personage_icon);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_name);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_time);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_content);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_address);
			holder.tv_message = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_getMessage);
			holder.tv_baoyang = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_getBaoyang);
			holder.tv_like = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_like);
			holder.rlayout_pic = (RelativeLayout) convertView
					.findViewById(R.id.rl_homepage_personage_picture);
			holder.iv_picture = (ImageView) convertView
					.findViewById(R.id.iv_homepage_personage_picture);
			holder.tv_word = (TextView) convertView
					.findViewById(R.id.tv_homepage_personage_word);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final PersonageModel model = (PersonageModel) getItem(position);

		// 将格林威治时间转换为date型
		Date time = TimeUtil.changeTime(model.getTime());
		holder.tv_time.setText(TimeUtil.showTime(time));

		String name = model.getName();
		if (!TextUtils.isEmpty(name) && !name.equals("null")) {
			holder.tv_name.setText(name + "");
		} else {
			holder.tv_name.setText(model.getUsername() + "");
		}

		holder.tv_content.setText(model.getContent());
		holder.tv_like.setText(model.getLike() + "");
		holder.tv_like.setOnClickListener(this);
		holder.tv_like.setTag(position);
		holder.tv_message.setOnClickListener(this);
		holder.tv_message.setTag(position);

		if (!TextUtils.isEmpty(model.getAddress())) {
			holder.tv_address.setVisibility(View.VISIBLE);
			holder.tv_address.setText(model.getAddress());
		} else {
			holder.tv_address.setVisibility(View.INVISIBLE);
		}

		holder.tv_word.setText(model.getPicture_name());

		String tx = model.getLogo();
		if (!TextUtils.isEmpty(tx) && !tx.equals("null")) {
			String iconUrl = url_Top + tx;
			// 给 ImageView 设置一个 tag
			holder.icon.setTag(iconUrl);
			// 异步加载头像
			Bitmap bitmap = imageLoader.loadBitmap(holder.icon, iconUrl, false);
			if (bitmap != null) {
				holder.icon.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));
			} else {
				holder.icon.setImageResource(R.drawable.icon_tx);
			}
		} else {
			holder.icon.setImageResource(R.drawable.icon_tx);
		}

		// 获取图片的url地址
		String imgUrl = url_Top + model.getPicture_icon();
		// 给 ImageView 设置一个 tag
		holder.iv_picture.setTag(imgUrl);
		// 异步加载图片
		Bitmap bt = imageLoader.loadBitmap(holder.iv_picture, imgUrl, true);
		if (bt != null) {
			holder.iv_picture.setImageBitmap(bt);
		} else {
			holder.iv_picture
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView icon, iv_picture;
		TextView tv_name, tv_time, tv_content, tv_address, tv_message,
				tv_baoyang, tv_word;
		RelativeLayout rlayout_pic, rlayout_comment;
		TextView tv_like;
		TextView tv_send, tv_cancel;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_homepage_personage_like:
			mCallParise.click(v);
			break;
		case R.id.tv_homepage_personage_getMessage:
			mSetMessage.click(v);
			break;
		default:
			break;
		}
	}
}
