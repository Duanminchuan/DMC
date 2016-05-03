package com.dream.cutepet.adapter;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.model.PhotoAlbumModel.PhotoAlbumData;
import com.dream.cutepet.util.MyListUtil;
import com.dream.cutepet.view.MyGridView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 我的相册 的适配器
 * 
 * @author Administrator
 * 
 */
public class MyPhotoAlbumAdapter extends BaseAdapter {
	List<PhotoAlbumData> data;
	Context context;
	LayoutInflater inflater;
	MyPhotoAlbumGridviewAdapter adapter;

	public MyPhotoAlbumAdapter() {

	}

	public MyPhotoAlbumAdapter(Context context, List<PhotoAlbumData> data) {
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(List<PhotoAlbumData> data) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.activity_photo_album_listview_item, null);
			holder = new ViewHolder();
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_photo_album_item_tile);
			holder.gridView = (MyGridView) convertView
					.findViewById(R.id.gv_photoalbum_item_picture);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PhotoAlbumData model = (PhotoAlbumData) getItem(position);
		
		holder.tv_time.setText(model.getTime());

		String picture = model.getPicture();
		List<String> pic = MyListUtil.changeStringToList(picture, ",");
		adapter = new MyPhotoAlbumGridviewAdapter(context, pic);
		holder.gridView.setAdapter(adapter);
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_time;
		MyGridView gridView;
	}
}
