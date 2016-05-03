package com.dream.cutepet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dream.cutepet.R;
import com.dream.cutepet.adapter.MyAlbumAdapter;
import com.dream.cutepet.model.ImageBeanModel;
import com.dream.cutepet.util.MyListUtil;
import com.dream.cutepet.util.SDCardUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SetPersonIconActivity extends Activity {
	GridView gridView;
	TextView tv_cancel, tv_camera;
	private ProgressDialog mProgressDialog;

	private MyAlbumAdapter adapter;
	private String username;

	private Map<String, List<String>> data;
	private List<ImageBeanModel> data_album;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		
		username = getIntent().getStringExtra("tel");

		initView();
		
		initData();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 001:
				// 加载好了图片数据
				// 关闭进度条
				if(mProgressDialog != null && mProgressDialog.isShowing()){
					mProgressDialog.dismiss();
				}
				// 对适配器添加数据
				adapter = new MyAlbumAdapter(getApplicationContext(),
						data_album, gridView);
				// 显示gridview
				gridView.setAdapter(adapter);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 显示进度条
		if(mProgressDialog != null){
			mProgressDialog.show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 获取本地相册的Map集合对象
				data = SDCardUtil.getImage(getApplicationContext());
				// 获取相册封面信息集合对象
				data_album = MyListUtil.subGroupOfImage(data);
				mHandler.sendEmptyMessage(001);
			}
		}).start();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		tv_cancel = (TextView) findViewById(R.id.tv_uploadPhoto_cancel);
		tv_camera = (TextView) findViewById(R.id.tv_uploadPhoto_camera);

		tv_cancel.setOnClickListener(listener);
		tv_camera.setOnClickListener(listener);

		gridView = (GridView) findViewById(R.id.gv_upload_photo);
		gridView.setOnItemClickListener(itemClickListener);
		
		mProgressDialog = new ProgressDialog(SetPersonIconActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("友情提示");
		mProgressDialog.setMessage("加载中，请稍后...");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// 照相返回界面后刷新数据
		initData();
	}
	
	/**
	 * 关闭ProgressDialog
	 */
	private void closeProgressDialog(){
		if(mProgressDialog != null && mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_uploadPhoto_cancel:
				closeProgressDialog();
				finish();
				break;
			case R.id.tv_uploadPhoto_camera:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
				break;
			default:
				break;
			}
		}
	};

	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			List<String> childList = data.get(data_album.get(arg2)
					.getFolderName());

			Intent intent = new Intent(SetPersonIconActivity.this,
					UpPersonIconActivity.class);
			intent.putStringArrayListExtra("data",
					(ArrayList<String>) childList);
			intent.putExtra("tel", username);
			startActivityForResult(intent, 0);
			closeProgressDialog();
			finish();
		}
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 照相后，保存照片到指定的路径
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			String fileName = SDCardUtil
					.getAllSDcardFile(getApplicationContext()).get(0).getPath()
					+ "/CameraImage/";
			if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				// 设置照相后的图片保存的名字
				String name = new SimpleDateFormat("yyyyMMdd_hhmmss")
						.format(new Date()) + ".jpg";
				Bundle bundle = data.getExtras();
				// 获取相机返回的数据，并转换为Bitmap图片格式
				Bitmap bitmap = (Bitmap) bundle.get("data");

				FileOutputStream b = null;
				File file = new File(fileName);
				if (!file.exists()) {
					// 创建文件夹
					file.mkdirs();
				}
				String filePath = fileName + name;
				try {
					b = new FileOutputStream(filePath);
					// 把数据写入文件
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
