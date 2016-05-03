package com.dream.cutepet;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.dream.cutepet.adapter.AllPageFragmentPagerAdapter;
import com.dream.cutepet.fragment.HomePageAndPetStrategyFragment;
import com.dream.cutepet.fragment.PersonalCenterFragment;
import com.dream.cutepet.fragment.SquareAndDynamicFragment;
import com.dream.cutepet.server.LoginSampleHelper;
import com.dream.cutepet.ui.GesturesLoginActivity;
import com.dream.cutepet.util.GesturesUtil;
import com.dream.cutepet.util.SharedPreferencesUtil;

/**
 * 所有页面串起来的界面
 * 
 * @author Administrator
 * 
 */
public class AllPageActivity extends FragmentActivity {
	LoginSampleHelper loginSampleHelper;
	List<Fragment> list;
	HomePageAndPetStrategyFragment homePageAndPetStrategyFragment;
	SquareAndDynamicFragment squareAndDynamicFragment;
	PersonalCenterFragment personalCenterFragmentChen;
	RadioGroup radioGroup;
	RadioButton rbtn_home, rbtn_petring, rbtn_mine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allpage);

		registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		initview();
	}

	@Override
	protected void onStart() {
		super.onStart();
		String token = SharedPreferencesUtil.getData(this
				.getApplicationContext());
		if (token != null && !token.equals("")) {// 判断获取的token值是否为空
			checkLogin();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mHomeKeyEventReceiver);
	}

	/**
	 * 加载控件
	 */
	private void initview() {
		rbtn_home = (RadioButton) findViewById(R.id.service);
		rbtn_petring = (RadioButton) findViewById(R.id.pet_ring);
		rbtn_mine = (RadioButton) findViewById(R.id.personal_center);
		rbtn_home.setChecked(true);

		homePageAndPetStrategyFragment = new HomePageAndPetStrategyFragment();
		squareAndDynamicFragment = new SquareAndDynamicFragment();
		personalCenterFragmentChen = new PersonalCenterFragment();
		radioGroup = (RadioGroup) findViewById(R.id.bottom_radiogroup);

		list = new ArrayList<Fragment>();
		list.add(homePageAndPetStrategyFragment);
		list.add(squareAndDynamicFragment);
		list.add(personalCenterFragmentChen);

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		AllPageFragmentPagerAdapter allPageFragmentPagerAdapter = new AllPageFragmentPagerAdapter(
				getSupportFragmentManager(), list);
		viewPager.setAdapter(allPageFragmentPagerAdapter);
		viewPager.setOnPageChangeListener(pageChangeListener);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
	}

	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				rbtn_home.setChecked(true);
				rbtn_petring.setChecked(false);
				rbtn_mine.setChecked(false);
				break;
			case 1:
				rbtn_home.setChecked(false);
				rbtn_petring.setChecked(true);
				rbtn_mine.setChecked(false);
				break;
			case 2:
				rbtn_home.setChecked(false);
				rbtn_petring.setChecked(false);
				rbtn_mine.setChecked(true);
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	/**
	 * 监听是否点击了home键将客户端推到后台
	 */
	private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
		String SYSTEM_REASON = "reason";
		String SYSTEM_HOME_KEY = "homekey";
		String SYSTEM_HOME_KEY_LONG = "recentapps";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {// 按下home键，触发
					// 判断是否使用手势密码
					if (isLogin()
							&& GesturesUtil.IsGestures(getApplicationContext())) {
						Intent it = new Intent(AllPageActivity.this,
								GesturesLoginActivity.class);
						startActivityForResult(it, 0);
						finish();
					}
				} else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
					// 表示长按home键,显示最近使用的程序列表
				}
			}
		}
	};

	/**
	 * 底部菜单点击事件
	 */
	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
			switch (checkedId) {
			case R.id.service:
				viewPager.setCurrentItem(0);
				break;
			case R.id.pet_ring:
				viewPager.setCurrentItem(1);
				break;
			case R.id.personal_center:
				viewPager.setCurrentItem(2);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	private boolean isLogin() {
		String data = SharedPreferencesUtil.getData(getApplicationContext());
		if (!TextUtils.isEmpty(data)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否登陆
	 */
	@SuppressWarnings("deprecation")
	private void checkLogin() {
		if (YWChannel.getInstance().getNetWorkState().isNetWorkNull()) {
			return;
		}
		loginSampleHelper = LoginSampleHelper.getInstance();
		String localId = IMPrefsTools.getStringPrefs(AllPageActivity.this,
				"userId", "");
		String localPassword = IMPrefsTools.getStringPrefs(
				AllPageActivity.this, "password", "");
		LoginSampleHelper.getInstance().initIMKit(localId, "23331616");
		loginSampleHelper.login_Sample(localId, localPassword,
				new IWxCallback() {
					@Override
					public void onSuccess(Object... arg0) {
					}

					@Override
					public void onProgress(int arg0) {

					}

					@Override
					public void onError(int arg0, String arg1) {

					}
				});
	}
}