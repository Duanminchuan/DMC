package com.dream.cutepet;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import com.dream.cutepet.adapter.WriteTalkGridAdapter;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.util.TimeUtil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WriteTalkActivity extends Activity {
	String username;
	EditText edit_content;
	EditText edit_address;
	WriteTalkGridAdapter adapter;
	GridView gridView;
	Bundle bundle;
	List<String> getPath;
	ImageView iv_city;
	TencentLocationManager locationManager;
	TencentLocationListener locationListener;
	private ProgressDialog mProgressDialog;

	int s = 0;
	boolean overtime = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writetalk);

		bundle = getIntent().getExtras();

		initData();

		initView();
	}

	private void initData() {
		getPath = new ArrayList<String>();
		if (bundle != null) {
			getPath = bundle.getStringArrayList("checked_path");
		}
	}

	/**
	 * 加载页面
	 */
	private void initView() {
		mProgressDialog = new ProgressDialog(WriteTalkActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("友情提示");
		mProgressDialog.setMessage("正在获取地址中，请稍后...");

		ImageView back = (ImageView) findViewById(R.id.back);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("写说说");
		TextView menu_hide = (TextView) findViewById(R.id.menu_hide);
		menu_hide.setText("发表");
		menu_hide.setVisibility(View.VISIBLE);
		ImageView add_image = (ImageView) findViewById(R.id.add_image);
		edit_content = (EditText) findViewById(R.id.ed_write);
		edit_address = (EditText) findViewById(R.id.address);
		edit_address.setFocusable(false);

		gridView = (GridView) findViewById(R.id.writetalk_gridview);

		adapter = new WriteTalkGridAdapter(getPath, this, gridView);
		gridView.setAdapter(adapter);

		back.setOnClickListener(clickListener);
		menu_hide.setOnClickListener(clickListener);
		add_image.setOnClickListener(clickListener);

		iv_city = (ImageView) findViewById(R.id.cityMore);
		iv_city.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.menu_hide:
				send();
				break;
			case R.id.add_image:
				Intent intent = new Intent();
				intent.setClass(WriteTalkActivity.this,
						WriteTalkUpLoadPhotoActivity.class);
				startActivityForResult(intent, 5551);
				finish();
				break;
			case R.id.cityMore:
				getCityAddress();
				break;
			default:
				break;
			}
		}
	};

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
				Toast.makeText(getApplicationContext(), "获取地址超时",
						Toast.LENGTH_LONG).show();
				locationManager.removeUpdates(locationListener);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 获取城市地址
	 */
	private void getCityAddress() {
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
					String street = location.getStreet();
					if (street.equals("unKnow")) {
						street = "";
					}
					// 定位成功
					String city = location.getCity() + " "
							+ location.getDistrict() + " " + street;
					s = 0;
					overtime = false;

					setCityText(city);
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
						Toast.makeText(getApplicationContext(),
								"建议在wifi环境下使用！", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(getApplicationContext(), "位置信息开关 关闭！",
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
	 * 设置地点
	 * 
	 * @param city
	 */
	private void setCityText(String city) {
		edit_address.setText(city);
		// 关闭进度条
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		Toast.makeText(getApplicationContext(), "地址获取成功！", Toast.LENGTH_SHORT)
				.show();
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * 发表
	 */
	@SuppressLint("SimpleDateFormat")
	private void send() {
		String url = "http://211.149.198.8:9805/index.php/home/api/uploadTalk";
		String tok = SharedPreferencesUtil.getData(this);
		username = tok.split(",")[1];
		String content = edit_content.getText().toString().trim();
		String address = edit_address.getText().toString().trim();
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", username);
			map.put("create_time", TimeUtil.changeTimeToGMT(new Date()));
			map.put("content", content);
			map.put("address", address);
			httpPost.putMap(map);
			for (int i = 0; i < getPath.size(); i++) {
				String file_path = getPath.get(i);
				File file = new File(file_path);
				httpPost.putFile(file.getName(), file, file.getName(), null);
			}
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				public void start() {
				}

				public void end(String result) {
					Log.e("result", "result = " + result);
					try {
						JSONObject object = new JSONObject(result);
						String message = object.getString("message");
						Toast.makeText(getApplicationContext(), message,
								Toast.LENGTH_SHORT).show();
						if (object.getInt("status") == 1) {
							finish();
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
