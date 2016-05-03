package com.dream.cutepet;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.NativeImageLoader;
import com.dream.cutepet.util.NativeImageLoader.NativeImageCallBack;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.util.TimeUtil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * 主人寄语发布界面
 * 
 * @author Administrator
 * 
 */
public class PersonalReleaseActivity extends Activity {
	TextView title, menu_hide, tv_address;
	ImageView back, iv_pet_logo, iv_pet_select;
	EditText et_type, et_content;
	String view_address;
	File file;
	private String username;
	private final String url = "http://211.149.198.8:9805/index.php/home/api/uploadPersonal";
	TencentLocationManager locationManager;
	TencentLocationListener locationListener;

	private ProgressDialog mProgressDialog;
	
	int s = 0;
	boolean overtime = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_release);

		initData();

		initview();
	}
	
	private void time() {
		overtime = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (overtime) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					s++;
					if (s == 35) {
						Log.e("time", "登录超时");
						s = 0;
						overtime = false;
						handler.sendEmptyMessage(007);
					}
				}
			}
		}).start();
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 007:
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), "获取地址超时", Toast.LENGTH_LONG).show();
				// 关闭获取地址的监听
				locationManager.removeUpdates(locationListener);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 获取地址信息
	 */
	private void setAddress() {
		// 显示进度条
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
		
		time();
		
		TencentLocationRequest request = TencentLocationRequest.create();
		request.setAllowCache(true);
		request.setInterval(100);
		request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);

		locationManager = TencentLocationManager
				.getInstance(getApplicationContext());

		locationListener = new TencentLocationListener() {
			/**
			 * 位置回调接口
			 */
			@Override
			public void onLocationChanged(TencentLocation location, int error,
					String reason) {
				// location：新的位置；error：错误码；reason：错误描述
				if (TencentLocation.ERROR_OK == error) {
					
					s = 0;
					overtime = false;
					
					// 定位成功
					String address = location.getCity() + " "
							+ location.getDistrict() + " "
							+ location.getStreet();
					setAddressText(address);
				}
			}

			/**
			 * 状态回调接口
			 */
			@Override
			public void onStatusUpdate(String name, int status, String desc) {
				// name：GPS，Wi-Fi等；status：新的状态, 启用或禁用；desc：状态描述
				if (name.equals("wifi")) {
					switch (status) {
					case 0:
						Toast.makeText(getApplicationContext(), "建议在wifi环境下使用！",
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				}
			}
		};
		int error = locationManager.requestLocationUpdates(request,
				locationListener);
		switch (error) {
		case 0:
			Log.i("getCityAddress", "注册位置监听器成功");
			break;
		case 1:
			Log.i("getCityAddress", "设备缺少使用腾讯定位SDK需要的基本条件");
			break;
		case 2:
			Log.i("getCityAddress", "配置的 key 不正确");
			break;
		case 3:
			Log.i("getCityAddress", "自动加载libtencentloc.so失败");
			break;
		default:
			break;
		}

	}

	/**
	 * 设置地址的信息
	 * 
	 * @param address
	 */
	private void setAddressText(String address) {
		tv_address.setText(address);
		// 关闭进度条
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		Toast.makeText(getApplicationContext(), "地址获取成功！",
				Toast.LENGTH_SHORT).show();
		// 关闭获取地址的监听
		locationManager.removeUpdates(locationListener);
	}

	private void initData() {
		username = SharedPreferencesUtil.getData(getApplicationContext())
				.split(",")[1];
		view_address = getIntent().getStringExtra("path");
		if (!TextUtils.isEmpty(view_address)) {
			file = new File(view_address);
		}
	}

	/**
	 * 加载页面
	 */
	private void initview() {
		mProgressDialog = new ProgressDialog(PersonalReleaseActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("友情提示");
		mProgressDialog.setMessage("正在获取地址中，请稍后...");
		
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		menu_hide = (TextView) findViewById(R.id.menu_hide);
		menu_hide.setVisibility(View.VISIBLE);
		title.setText("发布");
		menu_hide.setText("完成");

		tv_address = (TextView) findViewById(R.id.ed_pet_address);
		tv_address.setOnClickListener(clickListener);

		iv_pet_logo = (ImageView) findViewById(R.id.iv_pet_logo);
		iv_pet_select = (ImageView) findViewById(R.id.iv_pet_select);

		back.setOnClickListener(clickListener);
		menu_hide.setOnClickListener(clickListener);
		iv_pet_select.setOnClickListener(clickListener);

		et_type = (EditText) findViewById(R.id.ed_pet_breed);
		et_content = (EditText) findViewById(R.id.ed_pet_jiyu);

		if (!TextUtils.isEmpty(view_address)) {
			Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
					view_address, new NativeImageCallBack() {
						@Override
						public void onImageLoader(Bitmap bitmap, String path) {
							if (bitmap != null) {
								iv_pet_logo.setImageBitmap(BitmapUtil
										.compressImage(bitmap));
							}
						}
					});
			if (bitmap != null) {
				iv_pet_logo.setImageBitmap(bitmap);
			}
		}
	}

	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.menu_hide:
				String type = et_type.getText().toString().trim();
				String content = et_content.getText().toString().trim();
				String address = tv_address.getText().toString().trim();
				if (file != null && !TextUtils.isEmpty(type)
						&& !TextUtils.isEmpty(content)) {
					upload(type, content, address);
				} else {
					Toast.makeText(getApplicationContext(), "发布内容不能为空！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.iv_pet_select:
				Intent intent = new Intent();
				intent.setClass(PersonalReleaseActivity.this,
						SelectPhotoActivity.class);
				intent.putExtra("flog", 1);
				startActivityForResult(intent, 0);
				finish();
				break;
			case R.id.ed_pet_address:
				setAddress();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 上传主人寄语
	 * 
	 * @param name
	 * @param type
	 * @param content
	 * @param address
	 */
	private void upload(String type, String content, String address) {
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", username);
			map.put("time", TimeUtil.changeTimeToGMT(new Date()));
			map.put("content", content);
			map.put("image_name", type);
			map.put("address", address);
			httpPost.putMap(map);
			httpPost.putFile(file.getPath(), file, file.getName(), null);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						int status = jsonObject.getInt("status");
						if (status == 1) {
							Toast.makeText(getApplicationContext(), "上传成功！",
									Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("message"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
