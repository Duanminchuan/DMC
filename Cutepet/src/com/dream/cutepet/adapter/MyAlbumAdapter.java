package com.dream.cutepet.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.cutepet.R;
import com.dream.cutepet.model.ImageBeanModel;
import com.dream.cutepet.util.NativeImageLoader;
import com.dream.cutepet.util.NativeImageLoader.NativeImageCallBack;
import com.dream.cutepet.view.MyAlbumImageView;
import com.dream.cutepet.view.MyAlbumImageView.OnMeasureListener;

/**
 * 本地手机图片相册的适配器
 * 
 * @author Administrator
 * 
 */
public class MyAlbumAdapter extends BaseAdapter {
	// 用来封装ImageView的宽和高的对象
	private Point mPoint = new Point(0, 0);

	private List<ImageBeanModel> data;
	private LayoutInflater inflater;
	private GridView mGridView;

	public MyAlbumAdapter() {

	}

	@SuppressLint("UseSparseArrays")
	public MyAlbumAdapter(Context context, List<ImageBeanModel> data,
			GridView mGridView) {
		this.data = data;
		this.mGridView = mGridView;
		this.inflater = LayoutInflater.from(context);
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
		ImageBeanModel model = (ImageBeanModel) getItem(position);
		String path = model.getTopImagePath();

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_photo_album_item,
					null);
			
			holder.iv_album = (MyAlbumImageView) convertView
					.findViewById(R.id.iv_photoAlbum_item_imageview);
			//用来监听ImageView的宽和高
			holder.iv_album.setOnMeasureListener(new OnMeasureListener() {
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_photoAlbum_item_num);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_photoAlbum_item_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(model.getFolderName());
		holder.tv_count.setText(Integer.toString(model.getImageCounts()));
		// 给ImageView设置路径Tag,这是异步加载图片的小技巧
		holder.iv_album.setTag(path);

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
			holder.iv_album.setImageBitmap(bitmap);
		} 
		return convertView;
	}

	public static class ViewHolder {
		MyAlbumImageView iv_album;
		TextView tv_title;
		TextView tv_count;
	}
}
