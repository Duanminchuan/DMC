package com.dream.cutepet;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.model.PetMessageModel;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;

public class PerfectInformationActivity extends Activity {
	ImageView back;
	TextView title, menu_hide;
	EditText et_name, et_age, et_type, et_content;
	RadioGroup radioGroup;
	RadioButton rbtn_man,rbtn_woman;
	String sex;
	private String usernmae;
	PetMessageModel data;
	String url = "http://211.149.198.8:9805/index.php/home/api/uploadPetMessage";
	private Bundle bundle;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfect_information);
		usernmae = getIntent().getStringExtra("tel");
		
		bundle = getIntent().getExtras();
		
		initview();
	}

	/**
	 * 上传资料
	 */
	private void update() {
		String nickname = et_name.getText().toString().trim();
		String age = et_age.getText().toString().trim();
		String type = et_type.getText().toString().trim();
		String content = et_content.getText().toString().trim();
		try {
			Integer.parseInt(age);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "年龄必须为整数！",
					Toast.LENGTH_SHORT).show();
		}

		if (!TextUtils.isEmpty(nickname) && !TextUtils.isEmpty(age)
				&& !TextUtils.isEmpty(type) && !TextUtils.isEmpty(content)
				&& !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(usernmae)) {
			try {
				HttpPost httpPost = HttpPost.parseUrl(url);
				Map<String, String> map = new HashMap<String, String>();
				map.put("tel", usernmae);
				map.put("nickname", nickname);
				map.put("petnumber", usernmae);
				map.put("sex", sex);
				map.put("type", type);
				map.put("age", age);
				map.put("content", content);
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
							Toast.makeText(getApplicationContext(),
									jb.getString("message"), Toast.LENGTH_SHORT)
									.show();
							if (jb.getInt("status") == 1) {
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
		} else {
			Toast.makeText(getApplicationContext(), "请填写完资料！",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 加载界面
	 */
	private void initview() {
		title = (TextView) findViewById(R.id.title);
		title.setText("宠物信息");

		menu_hide = (TextView) findViewById(R.id.menu_hide);
		menu_hide.setText("保存");
		menu_hide.setVisibility(View.VISIBLE);
		back = (ImageView) findViewById(R.id.back);
		menu_hide.setOnClickListener(clickListener);
		back.setOnClickListener(clickListener);

		radioGroup = (RadioGroup) findViewById(R.id.rgroup_setPet);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
		et_name = (EditText) findViewById(R.id.input_nickname);
		et_age = (EditText) findViewById(R.id.input_age);
		et_content = (EditText) findViewById(R.id.input_yourstory);
		et_type = (EditText) findViewById(R.id.input_breed);
		rbtn_man = (RadioButton) findViewById(R.id.man);
		rbtn_woman = (RadioButton) findViewById(R.id.woman);
		
		if(bundle != null){
			et_name.setText(bundle.getString("nickname"));
			et_age.setText(bundle.getInt("age")+"");
			et_content.setText(bundle.getString("content"));
			et_type.setText(bundle.getString("type"));
			String sex = bundle.getString("sex");
			if(!TextUtils.isEmpty(sex)){
				if(sex.equals("男")){
					rbtn_man.setChecked(true);
					rbtn_woman.setChecked(false);
				}else{
					rbtn_man.setChecked(false);
					rbtn_woman.setChecked(true);
				}
			}else{
				rbtn_man.setChecked(false);
				rbtn_woman.setChecked(false);
			}
		}
	}

	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.woman:
				sex = "女";
				break;
			case R.id.man:
				sex = "男";
				break;
			default:
				break;
			}
		}
	};

	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.menu_hide:
				update();
				break;
			default:
				break;
			}
		}
	};
}
