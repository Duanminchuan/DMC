package com.dream.cutepet;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;

/**
 * 注册页面
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends Activity implements OnCheckedChangeListener {

	ImageView back;
	TextView text_yanzhengma, title;
	EditText edit_phone;
	EditText edit_yanzhengma;
	EditText edit_password;
	Button button_add;
	CheckBox checkBox;
	boolean flog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(ocl);

		title = (TextView) findViewById(R.id.title);
		title.setText("注册");

		text_yanzhengma = (TextView) findViewById(R.id.text_yanzhengma);
		text_yanzhengma.setOnClickListener(ocl);

		edit_phone = (EditText) findViewById(R.id.edit_register_phone);
		edit_yanzhengma = (EditText) findViewById(R.id.edit_register_yanzhengma);
		edit_password = (EditText) findViewById(R.id.edit_register_password);

		button_add = (Button) findViewById(R.id.button_add);
		button_add.setOnClickListener(ocl);
		
		checkBox = (CheckBox) findViewById(R.id.register_check);
		checkBox.setOnCheckedChangeListener(this);
	}

	OnClickListener ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.text_yanzhengma:
				String tel = edit_phone.getText().toString().trim();
				if (!TextUtils.isEmpty(tel)) {
					Log.e("xxxxxxxx", "有手机号");
				
					Pattern p = Pattern
							.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
					Matcher m = p.matcher(tel);
					if (m.matches()) {
						yanzhengma(tel);
					} else {
						Log.e("xxxxxxxx", "手机号码格式错误！");
						Toast.makeText(getApplication(), "请输入正确的手机号！",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Log.e("xxxxxxxx", "无手机号");
					Toast.makeText(getApplication(), "请输入手机号",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.button_add:
				String tel1 = edit_phone.getText().toString().trim();
				String verify = edit_yanzhengma.getText().toString().trim();
				String password = edit_password.getText().toString().trim();
				Pattern p = Pattern
						.compile("^\\s*[^\\s\u4e00-\u9fa5]{6,16}\\s*$");
				Matcher m = p.matcher(password);
				if (m.matches()) {
					if(flog){
						register(tel1, verify, password);
					}else {
						Log.e("xxxxxxxx", "未同意协议！");
						Toast.makeText(getApplication(), "未同意协议！",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Log.e("xxxxxxxx", "密码格式错误！");
					Toast.makeText(getApplication(), "请输入正确的密码！",
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 获取验证码
	 * 
	 * @param tel
	 */
	private void yanzhengma(String tel) {
		String httpHost = "http://211.149.198.8:9805/index.php/home/api/verify";
		try {
			HttpPost httpPost = HttpPost.parseUrl(httpHost);
			httpPost.putString("tel", tel);
			httpPost.send();
			Log.e("xxxxxxxxxxxxxxxxxx", tel);
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {

				}

				@Override
				public void end(String result) {
					try {
						Log.e("xxxxxxxxxxxxxxxxxx", result);
						JSONObject jo = new JSONObject(result);
						Toast.makeText(getApplicationContext(),
								jo.getString("verify"), Toast.LENGTH_LONG)
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

	/**
	 * 注册
	 * 
	 * @param tel
	 * @param verify
	 * @param password
	 */
	private void register(String tel, String verify, String password) {
		String httpHost_add = "http://211.149.198.8:9805/index.php/home/api/register";
		try {
			HttpPost httpPost = HttpPost.parseUrl(httpHost_add);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", tel);
			map.put("password", password);
			map.put("verify", verify);
			httpPost.putMap(map);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {

				}

				@Override
				public void end(String result) {
					Log.e("result", result);
					try {
						JSONObject jo = new JSONObject(result);
						int status = jo.getInt("status");
						if (status == 1) {
							Toast.makeText(getApplicationContext(),
									jo.getString("message"), Toast.LENGTH_SHORT)
									.show();
							Intent intent = new Intent(RegisterActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
						Toast.makeText(getApplicationContext(),
								jo.getString("message"), Toast.LENGTH_SHORT)
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		flog = isChecked;
	}
}
