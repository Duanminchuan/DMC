package com.dream.cutepet.ui;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.R;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.TimeUtil;

/**
 * 新建相册
 * 
 * @author Administrator
 * 
 */
public class NewPhotoAlbumActivity extends Activity {
	TextView tv_cancel, tv_accomplish;
	EditText et_name, et_describe;
	private String username;
	private String httpHost = "http://211.149.198.8:9805/index.php/home/api/newAlbum";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_photo_album);

		username = getIntent().getStringExtra("tel");

		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		et_name = (EditText) findViewById(R.id.et_newPhotoAlbum_albumName);
		et_describe = (EditText) findViewById(R.id.et_newPhotoAlbum_albumDescribe);

		tv_cancel = (TextView) findViewById(R.id.tv_newPhotoAlbum_cancel);
		tv_accomplish = (TextView) findViewById(R.id.tv_newPhotoAlbum_accomplish);
		tv_cancel.setOnClickListener(listener);
		tv_accomplish.setOnClickListener(listener);
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_newPhotoAlbum_accomplish:
				accomplish();
				break;
			case R.id.tv_newPhotoAlbum_cancel:
				cancel();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 取消
	 */
	private void cancel() {
		finish();
	}

	/**
	 * 完成
	 */
	private void accomplish() {
		String name = et_name.getText().toString().trim();
		String describe = et_describe.getText().toString().trim();
		if (name == null || name.equals("")) {
			Toast.makeText(getApplicationContext(), "相册的名字不能为空！",
					Toast.LENGTH_SHORT).show();
		} else if (describe == null || describe.equals("")) {
			Toast.makeText(getApplicationContext(), "相册的描述不能为空！",
					Toast.LENGTH_SHORT).show();
		} else {
			createPhotoAlbum(name, describe);
			finish();
		}
	}

	/**
	 * 创建相册
	 * 
	 * @param name
	 * @param describe
	 */
	private void createPhotoAlbum(final String name, final String describe) {
		try {
			final HttpPost httpPost = HttpPost.parseUrl(httpHost);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", username);
			map.put("albumname", name);
			map.put("describe", describe);
			map.put("time", TimeUtil.changeTimeToGMT(new Date()));
			httpPost.putMap(map);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					Log.i("result", "result =" + result);
					try {
						JSONObject jb = new JSONObject(result);
						Toast.makeText(getApplicationContext(),
								jb.getString("message"), Toast.LENGTH_SHORT)
								.show();
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
