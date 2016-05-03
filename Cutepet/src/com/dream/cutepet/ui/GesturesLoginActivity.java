package com.dream.cutepet.ui;

import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.AllPageActivity;
import com.dream.cutepet.LoginActivity;
import com.dream.cutepet.R;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.GesturesUtil;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.view.MyGesturesView;

public class GesturesLoginActivity extends Activity implements OnTouchListener,
		OnClickListener {
	private MyGesturesView gesturesView;
	private ImageView iv_icon;
	private TextView tv_toast, tv_errorToast_1, tv_errorToast_2, tv_byPwd;
	private LinearLayout layout;
	private List<Integer> data;
	private int count = 5;
	private String url = "http://211.149.198.8:9805/index.php/home/api/getUserIcon";
	private String url_top = "http://211.149.198.8:9805";
	private AsyncImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestures_view);

		ImageCacheManager cacheManager = new ImageCacheManager(this);
		imageLoader = new AsyncImageLoader(this, cacheManager.getMemoryCache(),
				cacheManager.getPlacardFileCache());

		initView();

		initData();
	}

	/**
	 * 设置头像
	 * 
	 * @param url_icon
	 */
	private void initIcon(String url_icon) {
		if (!TextUtils.isEmpty(url_icon)) {
			String url_tx = url_top + url_icon;
			iv_icon.setTag(url_tx);
			Bitmap bitmap = imageLoader.loadBitmap(iv_icon, url_tx, false);
			if (bitmap != null) {
				iv_icon.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));
			} else {
				iv_icon.setImageResource(R.drawable.icon_tx);
			}
		} else {
			iv_icon.setImageResource(R.drawable.icon_tx);
		}
	}

	/**
	 * 获取头像数据
	 */
	private void initData() {
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			httpPost.putString("tel",
					SharedPreferencesUtil.getData(getApplicationContext())
							.split(",")[1]);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {

				}

				@Override
				public void end(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						String url_icon = jsonObject.getString("message");
						initIcon(url_icon);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 110:
				gesturesView.setPoint_num();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化控件
	 */
	private void initView() {
		gesturesView = (MyGesturesView) findViewById(R.id.gestures_login);
		iv_icon = (ImageView) findViewById(R.id.gesture_login_icon);
		layout = (LinearLayout) findViewById(R.id.gesture_login_error_llayout);
		tv_toast = (TextView) findViewById(R.id.gesture_login_toast);
		tv_errorToast_1 = (TextView) findViewById(R.id.gesture_login_pwdError_toast);
		tv_errorToast_2 = (TextView) findViewById(R.id.gesture_login_error_toast);
		tv_byPwd = (TextView) findViewById(R.id.gesture_login_byPwd);

		gesturesView.setClickable(true);
		gesturesView.setOnTouchListener(this);

		layout.setVisibility(View.GONE);
		tv_toast.setVisibility(View.VISIBLE);
		tv_byPwd.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.gestures_login:
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				setMessage();
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
		return false;
	}

	/**
	 * 设置
	 */
	private void setMessage() {
		data = gesturesView.getPoint_num();
		String psd = GesturesUtil.getPassword(data);
		Intent intent = new Intent();
		if (GesturesUtil.ExaminePassword(psd, getApplicationContext())) {
			Toast.makeText(getApplication(), "手势密码正确！", Toast.LENGTH_SHORT)
					.show();
			intent.setClass(GesturesLoginActivity.this, AllPageActivity.class);
			startActivity(intent);
			finish();
		} else if (psd != null && !psd.equals("")) {
			count--;
			layout.setVisibility(View.VISIBLE);
			tv_toast.setVisibility(View.GONE);
			tv_errorToast_1.setText("密码错误，还可以再试" + count + "次。");
			tv_errorToast_2.setText("5次失败后，需要重新登录。");
			Toast.makeText(getApplication(), "手势密码错误！", Toast.LENGTH_SHORT)
					.show();
			if (count == 0) {
				intent.setClass(GesturesLoginActivity.this, LoginActivity.class);
				startActivity(intent);
				GesturesUtil.deleteGesture(getApplicationContext());
				finish();
			}
		}
		myhandler.sendEmptyMessage(110);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gesture_login_byPwd:
			forgetPassword();
			break;
		default:
			break;
		}
	}

	/**
	 * 忘记手势密码，登陆后清空手势密码
	 */
	private void forgetPassword() {
		Intent intent = new Intent(GesturesLoginActivity.this,
				LoginActivity.class);
		startActivityForResult(intent, 0);
		// 清空手势密码
		GesturesUtil.deleteGesture(getApplicationContext());
		finish();
	}
}
