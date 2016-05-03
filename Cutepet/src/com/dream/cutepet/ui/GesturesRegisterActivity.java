package com.dream.cutepet.ui;

import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.util.GesturesUtil;
import com.dream.cutepet.view.MyGesturesView;
import com.dream.cutepet.view.RegistureGesturesView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GesturesRegisterActivity extends Activity implements
		OnTouchListener, OnClickListener {
	private MyGesturesView gv_big;
	private RegistureGesturesView gv_small;
	private TextView tv_toast;
	private List<Integer> data;
	private String password;

	private ImageView back;
	private TextView title;

	@SuppressLint("HandlerLeak")
	private Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 110:
				gv_big.setPoint_num();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestures_register);

		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText("密保设置");
		back.setOnClickListener(this);

		gv_big = (MyGesturesView) findViewById(R.id.gesture_registure_big);
		gv_small = (RegistureGesturesView) findViewById(R.id.gesture_registure_small);
		tv_toast = (TextView) findViewById(R.id.gesture_registure_toast);

		tv_toast.setText("绘制解锁图案");

		gv_big.setClickable(true);
		gv_big.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.gesture_registure_big:
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
		data = gv_big.getPoint_num();
		String psd = GesturesUtil.getPassword(data);
		if (TextUtils.isEmpty(password)) {
			password = psd;
			gv_small.setPoint_num(data);
			tv_toast.setText("确认解锁图案");
		} else {
			if (password.equals(psd)) {
				GesturesUtil.savePassword(psd, getApplicationContext());
				Toast.makeText(getApplication(), "手势密码设置成功！", Toast.LENGTH_SHORT)
						.show();
				finish();
			} else {
				Toast.makeText(getApplication(), "与上一次绘制图案不一致！",
						Toast.LENGTH_SHORT).show();
			}
		}
		myhandler.sendEmptyMessage(110);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
