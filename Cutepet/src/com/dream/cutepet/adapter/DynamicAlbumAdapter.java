package com.dream.cutepet.adapter;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.DynamicAlbumModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DynamicAlbumAdapter extends BaseAdapter {
	List<DynamicAlbumModel.AlbumData> data;
	Context context;
	LayoutInflater inflater;
	private AsyncImageLoader imageLoader;
	private String url_Top = "http://211.149.198.8:9805";

	public DynamicAlbumAdapter() {

	}

	public DynamicAlbumAdapter(Context context,
			List<DynamicAlbumModel.AlbumData> data) {
		this.data = data;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
	}

	@Override
	public int getCount() {
		if (data != null) {
			return data.size() + 1;
		} else {
			return 0;
		}
	}

	public void setData(List<DynamicAlbumModel.AlbumData> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ResourceAsColor", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.activity_dynamic_gridview_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_dynamic_gridview_picture);
			holder.textView = (TextView) convertView
					.findViewById(R.id.tv_dynamic_gridview_name);
			holder.tv_new = (TextView) convertView
					.findViewById(R.id.tv_dynamic_gridview_newAlbum);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position != data.size()) {
			holder.tv_new.setVisibility(View.GONE);
			String url_img = url_Top + data.get(position).getAlbumlogo();
			holder.imageView.setTag(url_img);
			Bitmap bm = imageLoader.loadBitmap(holder.imageView, url_img, true);
			if (bm != null) {
				holder.imageView.setImageBitmap(bm);
			}else{
				holder.imageView.setImageResource(R.drawable.album_default);
			}
			
			holder.textView.setText(data.get(position).getAlbumname());
		} else {
			holder.tv_new.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView, tv_new;
	}

}
