package com.dream.cutepet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.adapter.FaceImageAdapter;
import com.dream.cutepet.util.DensityUtil;
import com.dream.cutepet.util.SharedPreferencesUtil;

/**
 * 引导页
 */
public class GuidepageActivity extends Activity {
	ViewPager viewPager;
	FaceImageAdapter faceImageAdapter;
	private List<View> views;
	RelativeLayout relativeLayout;
	// 底部小点图片
	private ImageView[] image_dots;
	// 引导页图片资源
	private int[] guide_pic = { R.drawable.home_yindao_one,
			R.drawable.home_yindao_two, R.drawable.home_yindao_three };
	// 当前选中位置
	private int currentIndex;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        SharedPreferences setting = getSharedPreferences("first_time", 0);  
        Boolean user_first = setting.getBoolean("FIRST",true);  
        if(user_first){//第一次  
             setting.edit().putBoolean("FIRST", false).commit();  
         }else{  
        	 Intent intent = new Intent(GuidepageActivity.this,
 					AllPageActivity.class); 
        	 startActivity(intent);
        	 finish();
        } 
		setContentView(R.layout.activity_guide_page);
		viewPager = (ViewPager) findViewById(R.id.guide_page_viewpager);
		views = new ArrayList<View>();

		// 初始化按钮
		initButton();
		// 初始化图片
		initView();
		// 初始化底部小点
		initDots();
	}

	/**
	 * 引导页按钮
	 */
	private void initButton() {
		relativeLayout = (RelativeLayout) findViewById(R.id.guide_re);
		RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		button = new Button(this);
		btnParams.setMargins(0, 0, 0, DensityUtil.px2dip(this, 185));
		btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		button.setLayoutParams(btnParams);
		button.setGravity(Gravity.CENTER);
		button.setText("开启我们的萌宠之旅");
		button.setTextColor(Color.rgb(51, 51, 51));// 字体颜色
		button.setBackgroundResource(R.drawable.guide_page_button);
		button.setVisibility(View.GONE);
		relativeLayout.addView(button);
		button.setOnClickListener(onClickListener);
	}

	/**
	 * 初始化图片
	 */
	private void initView() {
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// 初始化引导图片列表
		for (int i = 0; i < guide_pic.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(mParams);
			imageView.setImageResource(guide_pic[i]);
			views.add(imageView);
		}
		// 初始化Adapter
		faceImageAdapter = new FaceImageAdapter(views);
		viewPager.setAdapter(faceImageAdapter);
		viewPager.setOnPageChangeListener(onPageChangeListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	/**
	 * 初始化底部小点
	 */
	private void initDots() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.guide_page_linear);
		linearLayout.setPadding(0, 0, 0, DensityUtil.px2dip(this, 65));

		LinearLayout.LayoutParams image_dotsParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		image_dotsParams.setMargins(DensityUtil.px2dip(this, 45), 0, 0, 0);
		image_dots = new ImageView[guide_pic.length];
		// 循环取得小点图片
		for (int i = 0; i < guide_pic.length; i++) {
			if (i > 0) {
				linearLayout.getChildAt(i).setLayoutParams(image_dotsParams);
			}
			image_dots[i] = (ImageView) linearLayout.getChildAt(i);
			image_dots[i].setEnabled(true);// 都设为灰色
			image_dots[0].setEnabled(false);
			image_dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}
	}

	/**
	 * 设置当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > guide_pic.length - 1
				|| currentIndex == positon) {
			return;
		}
		image_dots[positon].setEnabled(false);
		image_dots[currentIndex].setEnabled(true);
		currentIndex = positon;
		button.setVisibility(View.GONE);
		if (positon == guide_pic.length - 1) {
			button.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 点击跳转到登录页
	 */
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(GuidepageActivity.this,
					AllPageActivity.class);
			startActivity(intent);
			finish();
		}
	};

	/**
	 * viewpager滑动响应事件
	 */
	ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			setCurDot(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	/**
	 * 判断是否处于登录状态
	 * 
	 * @return
	 */
	private boolean checkLogin() {
		String result = SharedPreferencesUtil.getData(getApplicationContext());
		if (!TextUtils.isEmpty(result)) {
			// 已经登录了
			return true;
		}
		return false;
	}

	/**
	 * 设置当前的引导页
	 */
	/*
	 * private void setCurView(int position) { if (position < 0 || position >=
	 * guide_pic.length) { return; } viewPager.setCurrentItem(position); }
	 */
}
