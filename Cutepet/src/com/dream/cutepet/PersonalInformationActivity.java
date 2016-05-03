package com.dream.cutepet;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.HttpTools;
import com.dream.cutepet.util.HttpTools.GetIssueListener;
import com.dream.cutepet.util.SharedPreferencesUtil;

/**
 * 资料信息
 * 
 * @author Administrator
 * 
 */
public class PersonalInformationActivity extends Activity {
	ImageView image_personalInformation, back;
	TextView title, menu_hide;
	DatePickerDialog datePickerDialog;
	EditText edit_nickname;
	RadioGroup radio_sex;
	TextView text_birth;
	EditText edit_constellation;
	EditText edit_occupation;
	EditText edit_corporation;
	EditText edit_site;
	EditText edit_hometown;
	EditText edit_mail;
	EditText edit_personality;

	RadioButton radio_man;
	RadioButton radio_woman;

	String nickname;
	String sex = "";
	String birth;
	String constellation;
	String occupation;
	String corporation;
	String site;
	String hometown;
	String mail;
	String personality;

	HttpTools http = new HttpTools();
	String tel;
	String token;

	// UserModel data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_information);

		initView();

		getData();
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		menu_hide = (TextView) findViewById(R.id.menu_hide);

		edit_nickname = (EditText) findViewById(R.id.edit_nickname);

		radio_sex = (RadioGroup) findViewById(R.id.radio_sex);
		radio_sex.setOnCheckedChangeListener(changeListener);

		text_birth = (TextView) findViewById(R.id.text_birth);
		edit_constellation = (EditText) findViewById(R.id.edit_constellation);
		edit_occupation = (EditText) findViewById(R.id.edit_occupation);
		edit_corporation = (EditText) findViewById(R.id.edit_corporation);
		edit_site = (EditText) findViewById(R.id.edit_site);
		edit_hometown = (EditText) findViewById(R.id.edit_hometown);
		edit_mail = (EditText) findViewById(R.id.edit_mail);
		edit_personality = (EditText) findViewById(R.id.edit_personality);

		radio_man = (RadioButton) findViewById(R.id.radio_man);
		radio_woman = (RadioButton) findViewById(R.id.radio_woman);

		text_birth = (TextView) findViewById(R.id.text_birth);
		title = (TextView) findViewById(R.id.title);
		menu_hide = (TextView) findViewById(R.id.menu_hide);
		title.setText("资料信息");
		menu_hide.setText("保存");
		menu_hide.setVisibility(View.VISIBLE);
		menu_hide.setId(998);
		menu_hide.setOnClickListener(ocl);

		back.setOnClickListener(ocl);
		text_birth.setOnClickListener(ocl);

	}

	/*
	 * 判断是否登录
	 */
	public void getData() {

		String result = SharedPreferencesUtil.getData(this);
		if (result != null && !result.equals("")) {
			String[] temp = result.split(",");
			tel = temp[1];
			token = temp[0];

			initData();
		}

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		String url = "http://211.149.198.8:9805/index.php/home/api/demand";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", tel);
			map.put("token", token);
			httpPost.putMap(map);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					// 获取后台传递过来的json值
					// data = UserModel.changeJson(result);

					try {
						JSONObject jb = new JSONObject(result);
						JSONObject jsonObject = jb.getJSONObject("message");
						nickname = jsonObject.optString("nickname", "");
						if (nickname.equals("null")) {
							nickname = "";
						}
						sex = jsonObject.optString("sex", "");
						if (sex.equals("null")) {
							sex = "";
						}
						birth = jsonObject.optString("birth", "");
						if (birth.equals("null")) {
							birth = "";
						}
						constellation = jsonObject.optString("constellation", "");
						if (constellation.equals("null")) {
							constellation = "";
						}
						occupation = jsonObject.optString("occupation", "");
						if (occupation.equals("null")) {
							occupation = "";
						}
						corporation = jsonObject.optString("corporation", "");
						if (corporation.equals("null")) {
							corporation = "";
						}
						site = jsonObject.optString("site", "");
						if (site.equals("null")) {
							site = "";
						}
						hometown = jsonObject.optString("hometown", "");
						if (hometown.equals("null")) {
							hometown = "";
						}
						mail = jsonObject.optString("mail", "");
						if (mail.equals("null")) {
							mail = "";
						}
						personality = jsonObject.optString("personality", "");
						if (personality.equals("null")) {
							personality = "";
						}
						updateView();

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});

		} catch (

		MalformedURLException e) {
			e.printStackTrace();
		}
	}

	// UI控件的更新
	private void updateView() {

		edit_nickname.setText(nickname);
		if (sex.equals("男")) {
			radio_man.setChecked(true);
			radio_woman.setChecked(false);
		} else {
			radio_man.setChecked(false);
			radio_woman.setChecked(true);
		}
		text_birth.setText(birth);
		edit_constellation.setText(constellation);
		edit_occupation.setText(occupation);
		edit_corporation.setText(corporation);
		edit_site.setText(site);
		edit_hometown.setText(hometown);
		edit_mail.setText(mail);
		edit_personality.setText(personality);
	}

	OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			switch (arg1) {
			case R.id.radio_man:
				sex = "男";
				break;
			case R.id.radio_woman:
				sex = "女";
				break;
			default:
				break;
			}
		}
	};

	OnClickListener ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.text_birth:
				creatDatePickDialog();
				break;
			case 998:
				nickname = edit_nickname.getText().toString();
				// if (radio_man.isChecked()) {
				// sex = "男";
				// } else {
				// sex = "女";
				// }
				birth = text_birth.getText().toString();
				constellation = edit_constellation.getText().toString();
				occupation = edit_occupation.getText().toString();
				corporation = edit_corporation.getText().toString();
				site = edit_site.getText().toString();
				hometown = edit_hometown.getText().toString();
				mail = edit_mail.getText().toString();
				personality = edit_personality.getText().toString();

				Map<String, String> map = new HashMap<String, String>();
				map.put("nickname", nickname);
				map.put("sex", sex);
				map.put("birth", birth);
				map.put("constellation", constellation);
				map.put("occupation", occupation);
				map.put("corporation", corporation);
				map.put("site", site);
				map.put("hometown", hometown);
				map.put("mail", mail);
				map.put("personality", personality);
				map.put("tel", tel);
				map.put("token", token);
				String httpHost = "http://211.149.198.8:9805/index.php/home/api/userdata";
				HttpTools http = new HttpTools();
				http.getIssue(httpHost, map, inssueListener);
				/*
				 * http.setOnuserdataListener(userdataListener);
				 * http.userdataAccount(tel, token);
				 */
				break;
			default:
				break;
			}
		}
	};

	/*
	 * 异步
	 */
	class UserdataTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... arg0) {
			return arg0[0];
		}

		protected void onPostExecute(String result) {
			try {
				JSONObject jo = new JSONObject(result);
				if (jo.getInt("status") == 1) {
					Toast.makeText(getApplication(), jo.optString("message"), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplication(), jo.optString("message"), Toast.LENGTH_LONG).show();
				}
				/*
				 * JSONObject jsonObject = new JSONObject(result); nickname =
				 * jsonObject.optString("nickname"); sex =
				 * jsonObject.optString("sex"); birth =
				 * jsonObject.optString("birth"); constellation =
				 * jsonObject.optString("constellation"); occupation =
				 * jsonObject.optString("occupation"); corporation =
				 * jsonObject.optString("corporation"); site =
				 * jsonObject.optString("site"); hometown =
				 * jsonObject.optString("hometown"); mail =
				 * jsonObject.optString("mail"); personality =
				 * jsonObject.optString("personality"); updateView();
				 */
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	GetIssueListener inssueListener = new GetIssueListener() {

		@Override
		public void getIssue(String result) {
			UserdataTask userdataTask = new UserdataTask();
			userdataTask.execute(result);

		}
	};

	public void creatDatePickDialog() {
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// // 为Calendar对象设置时间为当前日期

		int year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		int month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		int day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天

		datePickerDialog = new DatePickerDialog(this, null, year, month, day);

		datePickerDialog.show();
		datePickerDialog.setOnDismissListener(new OnDismissListener() {
			// 设置弹出框的消失监听事件
			@Override
			public void onDismiss(DialogInterface arg0) {
				// 创建弹出框的对象
				DatePicker daterPicker = datePickerDialog.getDatePicker();
				// 获取弹出框上的时间
				int year = daterPicker.getYear();
				int month = daterPicker.getMonth() + 1;
				int deyofmonth = daterPicker.getDayOfMonth();
				text_birth.setText(year + "-" + month + "-" + deyofmonth);
			}
		});
	}
}
