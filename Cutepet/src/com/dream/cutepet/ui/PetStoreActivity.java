package com.dream.cutepet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.dream.cutepet.R;
import com.dream.cutepet.ReleaseActivity;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.PetStoreModel;
import com.dream.cutepet.server.LoginSampleHelper;
import com.dream.cutepet.util.SharedPreferencesUtil;

/**
 * 宠物商店
 * 
 * @author 余飞
 * 
 */
public class PetStoreActivity extends Activity implements OnClickListener {
	ImageView back, iv_logo;
	TextView tv_send;
	TextView menu_hide, tv_name, tv_address, tv_type, title;
	int index;
	PetStoreModel data;
	String userId;
	String password;
	String username;
	String address;
	String type;
	String icon;
	String name;
	RelativeLayout layout_petStore, layout_issue;
	AsyncImageLoader asyncImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_store);

		ImageCacheManager imageCacheManager = new ImageCacheManager(
				getApplicationContext());
		asyncImageLoader = new AsyncImageLoader(getApplicationContext(),
				imageCacheManager.getMemoryCache(),
				imageCacheManager.getPlacardFileCache());

		index = getIntent().getIntExtra("index", -1);
		username = getIntent().getStringExtra("username");
		address = getIntent().getStringExtra("address");
		type = getIntent().getStringExtra("type");
		icon = getIntent().getStringExtra("icon");
		name = getIntent().getStringExtra("name");
		initData();

		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (index != -1) {
			data = PetStoreModel.getData().get(index);
		}
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		title = (TextView) findViewById(R.id.title);
		title.setText("店铺详情");
		back = (ImageView) findViewById(R.id.back);
		menu_hide = (TextView) findViewById(R.id.menu_hide);
		menu_hide.setText("发布");
		menu_hide.setVisibility(View.VISIBLE);
		tv_send = (TextView) findViewById(R.id.tv_petStore_send);
		back.setOnClickListener(this);
		menu_hide.setOnClickListener(this);
		tv_send.setOnClickListener(this);

		iv_logo = (ImageView) findViewById(R.id.iv_petStore_logo);
		tv_name = (TextView) findViewById(R.id.tv_petStore_name);
		tv_address = (TextView) findViewById(R.id.tv_petStore_address);
		tv_type = (TextView) findViewById(R.id.tv_petStore_type);

		String url_img = icon;
		// 异步加载图片
		iv_logo.setTag(url_img);
		Bitmap bitmap = asyncImageLoader.loadBitmap(iv_logo, url_img, true);
		if (bitmap != null) {
			iv_logo.setImageBitmap(bitmap);
		} else {
			iv_logo.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		tv_name.setText(name);
		tv_address.setText(address);
		tv_type.setText(type);
	}

	/**
	 * 发布跳转
	 */
	private void issue() {
		Intent intent = new Intent(PetStoreActivity.this, ReleaseActivity.class);
		startActivity(intent);
	}

	/**
	 * 返回
	 */
	private void back() {
		finish();
	}

	/**
	 * 跳转到聊天界面
	 */
	@SuppressWarnings("deprecation")
	private void returnToChat() {
		String token = SharedPreferencesUtil.getData(this
				.getApplicationContext());
		if (token != null && !token.equals("")) {// 判断获取的token值是否为空
			YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
			String target = username;// 消息接收者ID
			Intent intent = imKit.getChattingActivityIntent(target);
			startActivityForResult(intent, 0);
		} else {
			Toast.makeText(this, "请先登陆", Toast.LENGTH_SHORT).show();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.menu_hide:
			issue();
			break;
		case R.id.tv_petStore_send:
			returnToChat();
			break;
		default:
			break;
		}
	}
}
