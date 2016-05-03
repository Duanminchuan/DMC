package com.dream.cutepet.ui;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dream.cutepet.R;
import com.dream.cutepet.adapter.MyPhotoAlbumAdapter;
import com.dream.cutepet.model.PhotoAlbumModel;
import com.dream.cutepet.model.PhotoAlbumModel.PhotoAlbumData;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;

/**
 * 我的相册
 * 
 * @author Administrator
 * 
 */
public class MyPhotoAlbumActivity extends Activity {
	TextView tv_upload, tv_title;
	MyPhotoAlbumAdapter adapter;
	AlertDialog dialog;
	Builder builder;
	ListView listView;
	ImageView iv_icon, back;
	List<PhotoAlbumData> data = new ArrayList<PhotoAlbumModel.PhotoAlbumData>();
	private String username, title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_album);

		username = getIntent().getStringExtra("tel");
		title = getIntent().getStringExtra("title");

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		String url = "http://211.149.198.8:9805/index.php/home/api/getPhoto";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", username);
			map.put("albumname", title);
			httpPost.putMap(map);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					Log.i("result", "result = " + result);
					try {
						JSONObject jb = new JSONObject(result);
						if (jb.getInt("status") == 1) {
							data = PhotoAlbumModel.setJson(result).getMessage();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					adapter.setData(data);
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("InflateParams")
	private void initView() {
		tv_upload = (TextView) findViewById(R.id.tv_PhotoAlbum_Upload);
		tv_upload.setOnClickListener(listener);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(listener);

		listView = (ListView) findViewById(R.id.lv_photo_album_listview);

		View headview = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.activity_photo_album_header, null);
		tv_title = (TextView) headview
				.findViewById(R.id.tv_photo_album_header_title);
		iv_icon = (ImageView) headview
				.findViewById(R.id.iv_photo_album_header_icon);
		tv_title.setText(title + "");
		// 第三个参数是确定是否可以被选中
		listView.addHeaderView(headview, null, false);
		adapter = new MyPhotoAlbumAdapter(this, data);
		listView.setAdapter(adapter);
		builder = new AlertDialog.Builder(this);
	}

	/**
	 * 上传相片
	 */
	private void PhotoAlbum_Upload() {
		Intent intent = new Intent(MyPhotoAlbumActivity.this,
				UploadPhotoActivity.class);
		intent.putExtra("tel", username);
		intent.putExtra("title", title);
		startActivityForResult(intent, 1008611);
	}

	// /**
	// * 放大照片(全屏显示)
	// *
	// * @param position
	// */
	// private void EnlargeImage(int position) {
	// // 创建AlertDialog
	// dialog = builder.create();
	// ImageView imageView = new ImageView(this);
	// // 通过点击的position获取该图片的地址，并转换为bitmap型
	// Bitmap bitmap = BitmapFactory.decodeFile(data.getPhoto().get(position));
	// imageView.setImageBitmap(bitmap);
	// imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// imageView.setScaleType(ScaleType.FIT_CENTER);
	// // 设置不留白
	// imageView.setAdjustViewBounds(true);
	// imageView.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (dialog != null) {
	// // 关闭AlertDialog，并置为空
	// dialog.dismiss();
	// dialog = null;
	// }
	// }
	// });
	// // 设置dialog的上下文为该图片
	// dialog.setView(imageView);
	// dialog.show();
	// }

	// OnItemClickListener itemClickListener = new OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // 放大照片
	// EnlargeImage(position);
	// }
	// };

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_PhotoAlbum_Upload:
				PhotoAlbum_Upload();
				break;
			case R.id.back:
				back();
				break;
			default:
				break;
			}
		}
	};

	private void back() {
		finish();
	}
}
