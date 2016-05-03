package com.dream.cutepet.adapter;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.view.PhotoImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyPhotoAlbumGridviewAdapter extends BaseAdapter {
	List<String> data;
	private String url_Top = "http://211.149.198.8:9805";
	private AsyncImageLoader imageLoader;
	private LayoutInflater inflater;

	public MyPhotoAlbumGridviewAdapter() {

	}

	public MyPhotoAlbumGridviewAdapter(Context context, List<String> data) {
		this.data = data;
		inflater = LayoutInflater.from(context);
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		String img = (String) getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.set_peticon_item, null);
			holder.mImageView = (PhotoImageView) convertView
					.findViewById(R.id.iv_setPetIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(img) && !img.equals("null")) {
			String pic_url = url_Top + img;
			holder.mImageView.setTag(pic_url);
			Bitmap bitmap = imageLoader.loadBitmap(holder.mImageView, pic_url,
					true);
			if (bitmap != null) {
				holder.mImageView.setImageBitmap(bitmap);
			} else {
				holder.mImageView
						.setImageResource(R.drawable.friends_sends_pictures_no);
			}
		} else {
			holder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
			holder.mImageView.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class ViewHolder {
		PhotoImageView mImageView;
	}
}
