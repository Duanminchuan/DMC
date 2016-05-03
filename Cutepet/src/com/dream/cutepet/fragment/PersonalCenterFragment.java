package com.dream.cutepet.fragment;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.AboutActivity;
import com.dream.cutepet.AccountActivity;
import com.dream.cutepet.LoginActivity;
import com.dream.cutepet.PersonalInformationActivity;
import com.dream.cutepet.R;
import com.dream.cutepet.RegisterActivity;
import com.dream.cutepet.SetActivity;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.ui.SetPersonIconActivity;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.HttpTools;
import com.dream.cutepet.util.HttpTools.OndemandListener;
import com.dream.cutepet.util.SharedPreferencesUtil;

/**
 * 个人中心的fragment
 * 
 * @author Administrator
 * 
 */
public class PersonalCenterFragment extends Fragment {
	LinearLayout linear_personalInformation;// 资料信息控件
	LinearLayout linear_account;// 账户管理控件
	LinearLayout linear_about;// 关于萌宠控件
	LinearLayout linear_set;// 设置控件

	TextView text_login;
	TextView text_register;
	ImageView back, menu;
	TextView title;

	RelativeLayout relative_weidenglu;
	RelativeLayout relative_yidenglu;

	HttpTools http = new HttpTools();
	String tel;
	String token;

	RadioButton personal_center;

	ImageView image_toxiang_login, image_toxiang_unlogin;

	String logo;
	String attention;
	String fans;
	String collect;
	String nickname;
	String username;

	TextView text_attention;
	TextView text_fans;
	TextView text_enshrine;
	TextView text_nickname;
	TextView text_tel;

	private AsyncImageLoader imageLoader;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_personal_center, null);

		ImageCacheManager cacheManager = new ImageCacheManager(getActivity());
		imageLoader = new AsyncImageLoader(getActivity(),
				cacheManager.getMemoryCache(),
				cacheManager.getPlacardFileCache());

		initView(view);

		if (checkisLogin()) {
			// 登录状态
			judgeLongin();
		} else {// 未登录
			judgeUnLongin();
		}
		return view;
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
					Log.i("PersonalCenterFragment", "initData = " + result);
					try {
						JSONObject jsonObject = new JSONObject(result);
						JSONObject jo = jsonObject.getJSONObject("message");
						logo = jo.optString("logo", "");
						attention = jo.optString("attention", "");
						if (attention.equals("null")) {
							attention = "0";
						}
						fans = jo.optString("fans", "");
						if (fans.equals("null")) {
							fans = "0";
						}
						collect = jo.optString("collect", "");
						if (collect.equals("null")) {
							collect = "0";
						}
						nickname = jo.optString("nickname", "");
						if (nickname.equals("null")) {
							nickname = "";
						}
						username = jo.optString("username", "");

					} catch (JSONException e) {
						e.printStackTrace();
					}
					updateView();
				}
			});

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	// UI控件的更新
	private void updateView() {
		if (!TextUtils.isEmpty(logo) && !logo.equals("null")) {
			String img_url = "http://211.149.198.8:9805" + logo;
			image_toxiang_login.setTag(img_url);
			Bitmap bitmap = imageLoader.loadBitmap(image_toxiang_login,
					img_url, false);
			if (bitmap != null) {
				Bitmap cc_tx = BitmapUtil.toRoundBitmap(bitmap);
				image_toxiang_login.setImageBitmap(cc_tx);
			} else {
				image_toxiang_login.setImageResource(R.drawable.icon_tx);
			}
		} else {
			image_toxiang_login.setImageResource(R.drawable.icon_tx);
		}

		text_attention.setText(attention);
		text_fans.setText(fans);
		text_enshrine.setText(collect);
		text_nickname.setText(nickname);
		text_tel.setText(username);
	}

	/**
	 * 设置头像
	 */
	private void setIcon() {
		Intent intent = new Intent(getActivity(), SetPersonIconActivity.class);
		intent.putExtra("tel", username);
		startActivityForResult(intent, 0);
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		// // 找到每个控件
		title = (TextView) view.findViewById(R.id.title);
		title.setText("个人中心");
		linear_personalInformation = (LinearLayout) view
				.findViewById(R.id.linear_personalInformation);

		linear_personalInformation = (LinearLayout) view
				.findViewById(R.id.linear_personalInformation);
		linear_account = (LinearLayout) view.findViewById(R.id.linear_account);
		linear_about = (LinearLayout) view.findViewById(R.id.linear_about);
		linear_set = (LinearLayout) view.findViewById(R.id.linear_set);
		text_login = (TextView) view.findViewById(R.id.text_login);
		text_register = (TextView) view.findViewById(R.id.text_register);

		relative_weidenglu = (RelativeLayout) view
				.findViewById(R.id.relative_weidenglu);
		relative_yidenglu = (RelativeLayout) view
				.findViewById(R.id.relative_yidenglu);
		personal_center = (RadioButton) view.findViewById(R.id.personal_center);

		image_toxiang_unlogin = (ImageView) view
				.findViewById(R.id.image_toxiang);
		image_toxiang_login = (ImageView) view
				.findViewById(R.id.image_toxiang_hide);
		image_toxiang_login.setOnClickListener(ocl);
		image_toxiang_unlogin.setOnClickListener(ocl);

		text_attention = (TextView) view.findViewById(R.id.text_attention);
		text_fans = (TextView) view.findViewById(R.id.text_fans);
		text_enshrine = (TextView) view.findViewById(R.id.text_enshrine);
		text_nickname = (TextView) view.findViewById(R.id.text_nickname);
		text_tel = (TextView) view.findViewById(R.id.text_tel);

		// 设置每个控件的点击事件
		linear_personalInformation.setOnClickListener(ocl);
		linear_account.setOnClickListener(ocl);
		linear_about.setOnClickListener(ocl);
		linear_set.setOnClickListener(ocl);

		// 设置每个控件的点击事件
		linear_personalInformation.setOnClickListener(ocl);
		linear_account.setOnClickListener(ocl);
		linear_about.setOnClickListener(ocl);
		linear_set.setOnClickListener(ocl);

		// 设置每个控件的点击事件
		linear_personalInformation.setOnClickListener(ocl);
		linear_account.setOnClickListener(ocl);
		linear_about.setOnClickListener(ocl);
		linear_set.setOnClickListener(ocl);

		text_login.setOnClickListener(ocl);
		text_register.setOnClickListener(ocl);
	}

	OnClickListener ocl = new OnClickListener() {
		@SuppressWarnings("unused")
		private OndemandListener demandListener = new OndemandListener() {
			@Override
			public void start() {

			}

			@Override
			public void end(String result) {
				try {
					JSONObject jo = new JSONObject(result);
					Log.i("demand=====>", jo.getString("message"));
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							PersonalInformationActivity.class);
					startActivityForResult(intent, 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.linear_personalInformation:
				// 点击后当前个人中心页面跳转到个人信息页面
				if (checkisLogin()) {
					intent.setClass(getActivity(),
							PersonalInformationActivity.class);
					startActivityForResult(intent, 0);
				} else {
					Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG)
							.show();
				}

				break;
			case R.id.linear_account:
				// 点击后当前页面个人中心页面跳转到账户管理页面
				if (checkisLogin()) {
					intent.setClass(getActivity(), AccountActivity.class);
					startActivityForResult(intent, 0);
				} else {
					Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG)
							.show();
				}

				break;
			case R.id.linear_about:
				// 点击后当前页面个人中心页面跳转到关于萌宠页面
				intent.setClass(getActivity(), AboutActivity.class);
				startActivityForResult(intent, 0);
				break;
			case R.id.linear_set:
				// 点击后当前页面个人中心页面跳转到设置页面
				intent.setClass(getActivity(), SetActivity.class);
				startActivityForResult(intent, 0);
				break;
			case R.id.text_login:
				intent.setClass(getActivity(), LoginActivity.class);
				// 点击登录后跳转到登录页面
				startActivityForResult(intent, 0);
				getActivity().finish();
				break;
			case R.id.text_register:
				intent.setClass(getActivity(), RegisterActivity.class);
				// 点击注册后跳转到注册页面
				startActivityForResult(intent, 0);
				getActivity().finish();
				break;
			case R.id.personal_center:
				// http.setOnHttpListener(mListener);
				break;
			case R.id.image_toxiang:
				Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT)
						.show();
				break;
			case R.id.image_toxiang_hide:
				setIcon();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		CheckIsLogin();
	}

	/**
	 * 判断是否处于登录状态
	 */
	private void CheckIsLogin() {
		Log.i("CheckIsLogin", "CheckIsLogin");
		// 获取本地的sharepreference存储的token值
		String result = SharedPreferencesUtil.getData(getActivity()
				.getApplicationContext());
		Log.i("CheckIsLogin", "result=" + result);
		if (result == null || result.equals("")) {// 判断获取的token值是否为空
			Log.i("CheckIsLogin", "当前没有处于登录状态");
			judgeUnLongin();
		} else {
			// 不为空，则显示个人信息
			String[] temp = result.split(",");
			tel = temp[1];
			token = temp[0];
			judgeLongin();
		}
	}

	/**
	 * 判断 是否 处于登录 状态
	 * 
	 * @return 登录，返回true
	 */
	private boolean checkisLogin() {
		String result = SharedPreferencesUtil.getData(getActivity()
				.getApplicationContext());
		if (result == null || result.equals("")) {// 判断获取的token值是否为空
			Log.i("CheckIsLogin", "当前没有处于登录状态");
			return false;
		} else {
			// 不为空，则显示个人信息
			String[] temp = result.split(",");
			tel = temp[1];
			token = temp[0];
			return true;
		}
	}

	/**
	 * 处于登录状态的 个人信息界面
	 */
	public void judgeLongin() {
		relative_weidenglu.setVisibility(View.GONE);
		relative_yidenglu.setVisibility(View.VISIBLE);
		initData();
	}

	/**
	 * 处于未登录状态的个人信息界面
	 * 
	 */
	public void judgeUnLongin() {
		relative_weidenglu.setVisibility(View.VISIBLE);
		relative_yidenglu.setVisibility(View.GONE);
	}

}
