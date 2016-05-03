package com.dream.cutepet;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.util.TimeUtil;

/**
 * 攻略点评
 * 
 * @author Administrator
 * 
 */
public class PetStrategyCommentActivity extends Activity {
	String petName, tel, userId;
	float petGrade = 0;
	String petTrait;
	String petContent_data;
	String petImage;
	String username;
	AsyncImageLoader imageLoader;
	String view_address;
	File file;
	ImageView pet_strategy_comment_view;
	EditText et_content;
	Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_strategy_comment);

		ImageCacheManager cacheMgr = new ImageCacheManager(this);
		imageLoader = new AsyncImageLoader(this, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());

		bundle = getIntent().getExtras();
		view_address = getIntent().getStringExtra("path");

		getData();
	}

	/**
	 * 加载页面
	 */
	private void init() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("点评");
		TextView menu_hide = (TextView) findViewById(R.id.menu_hide);
		menu_hide.setVisibility(View.VISIBLE);
		menu_hide.setText("提交");
		ImageView back = (ImageView) findViewById(R.id.back);
		ImageView pet_strategy_comment_addview = (ImageView) findViewById(R.id.pet_strategy_comment_addview);
		pet_strategy_comment_view = (ImageView) findViewById(R.id.pet_strategy_comment_view);

		back.setOnClickListener(clickListener);
		pet_strategy_comment_addview.setOnClickListener(clickListener);
		menu_hide.setOnClickListener(clickListener);

		et_content = (EditText) findViewById(R.id.pet_strategy_comment_edit);

		TextView pet_strategy_comment_chinese_name = (TextView) findViewById(R.id.pet_strategy_comment_chinese_name);
		TextView pet_strategy_content_data = (TextView) findViewById(R.id.pet_strategy_content_data);
		RatingBar pet_strategy_comment_ratingbar = (RatingBar) findViewById(R.id.pet_strategy_comment_ratingbar);
		final TextView pet_strategy_comment_ratingbar_num = (TextView) findViewById(R.id.pet_strategy_comment_ratingbar_num);
		TextView pet_strategy_comment_characteristic = (TextView) findViewById(R.id.pet_strategy_comment_characteristic);
		ImageView pet_strategy_image = (ImageView) findViewById(R.id.pet_strategy_image);

		pet_strategy_comment_chinese_name.setText(petName);
		pet_strategy_content_data.setText(petContent_data);
		pet_strategy_comment_characteristic.setText(petTrait);

		pet_strategy_comment_ratingbar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						petGrade = rating;
						pet_strategy_comment_ratingbar_num.setText(petGrade
								+ "分");
					}
				});

		String imageUrl = "http://211.149.198.8:9805" + petImage;
		pet_strategy_image.setTag(imageUrl);
		Bitmap bitmap = imageLoader.loadBitmap(pet_strategy_image, imageUrl,
				true);
		if (bitmap != null) {
			pet_strategy_image.setImageBitmap(bitmap);
		} else {
			pet_strategy_image
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		if (!TextUtils.isEmpty(view_address)) {
			file = new File(view_address);
			pet_strategy_comment_view.setImageBitmap(BitmapUtil
					.getDiskBitmap(view_address));
		}

	}

	/**
	 * 获得数据
	 */
	private void getData() {
		if (bundle != null) {
			petName = bundle.getString("petName");
			userId = bundle.getString("userId");
			petTrait = bundle.getString("petTrait");
			petContent_data = bundle.getString("petContent_data");
			petImage = bundle.getString("petImage");
			tel = SharedPreferencesUtil.getData(getApplicationContext()).split(
					",")[1];
			init();
		}
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				back();
				break;
			case R.id.menu_hide:
				uploadStrategy(file);
				break;
			case R.id.pet_strategy_comment_addview:
				selectImage();
				break;
			default:
				break;
			}
		}
	};

	private void back() {
		finish();
	}

	/**
	 * 上传攻略
	 * 
	 * @param file
	 */
	private void uploadStrategy(File file) {
		String cont = et_content.getText().toString().trim();
		if (!TextUtils.isEmpty(cont) && !cont.equals("null")) {
			String url_send = "http://211.149.198.8:9805/index.php/home/api/uploadStrategy_comment";
			try {
				HttpPost httpPost = HttpPost.parseUrl(url_send);
				Map<String, String> map = new HashMap<String, String>();
				map.put("tel", tel);
				map.put("issua_id", userId);
				map.put("grade", petGrade + "");
				map.put("content", cont);
				map.put("create_time", TimeUtil.changeTimeToGMT(new Date()));
				httpPost.putMap(map);
				httpPost.putFile(file.getName(), file, file.getName(), null);
				httpPost.send();
				httpPost.setOnSendListener(new OnSendListener() {
					@Override
					public void start() {
					}

					@Override
					public void end(String result) {
						Log.i("uploadStrategy", result);
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
			Toast.makeText(getApplicationContext(), "评论内容不能为空",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 跳转到选择图片页面
	 */
	private void selectImage() {
		Intent intent = new Intent();
		intent.setClass(PetStrategyCommentActivity.this,
				SelectPhotoActivity.class);
		intent.putExtra("flog", 3);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
