package com.dream.cutepet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.dream.cutepet.R;
import com.dream.cutepet.util.NativeImageLoader;
import com.dream.cutepet.util.NativeImageLoader.NativeImageCallBack;
import com.dream.cutepet.view.MyAlbumImageView;
import com.dream.cutepet.view.MyAlbumImageView.OnMeasureListener;

public class WriteTalkGridAdapter extends BaseAdapter {
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
	List<String> data;
	Context context;
	LayoutInflater inflater;
	private GridView mGridView;

	public WriteTalkGridAdapter() {

	}

	public WriteTalkGridAdapter(List<String> data, Context context,
			GridView mGridView) {
		this.data = data;
		this.context = context;
		this.mGridView = mGridView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<String> data) {
		this.data = data;
		this.notifyDataSetChanged();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = data.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_photo_item, null);
			viewHolder = new ViewHolder();
			
			viewHolder.mImageView = (MyAlbumImageView) convertView
					.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView
					.findViewById(R.id.child_checkbox);

			// 用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mCheckBox.setVisibility(View.GONE);
		
		viewHolder.mImageView.setTag(path);
		// 利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,
				mPoint, new NativeImageCallBack() {
					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						ImageView mImageView = (ImageView) mGridView
								.findViewWithTag(path);
						if (bitmap != null && mImageView != null) {
							mImageView.setImageBitmap(bitmap);
						}
					}
				});
		if (bitmap != null) {
			viewHolder.mImageView.setImageBitmap(bitmap);
		} else {
			viewHolder.mImageView.setVisibility(View.GONE);
		}
		return convertView;
	}

	public class ViewHolder {
		public MyAlbumImageView mImageView;
		public CheckBox mCheckBox;
	}
}
