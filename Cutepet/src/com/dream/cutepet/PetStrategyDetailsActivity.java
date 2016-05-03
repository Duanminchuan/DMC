package com.dream.cutepet;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.mobileim.utility.IMPrefsTools;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.PetStrategyModel;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 攻略详情
 * 
 * @author Administrator
 * 
 */
public class PetStrategyDetailsActivity extends Activity {
	PetStrategyModel model;
	AsyncImageLoader imageLoader;
	String petName;
	String petGrade;
	String petMoney;
	String petTrait;
	String petContent_data;
	String petImage;
	String username;
	String userId;
	TextView pet_strategy_details_collection;
	String localId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_strategy_details);

		ImageCacheManager cacheMgr = new ImageCacheManager(this);
		imageLoader = new AsyncImageLoader(this, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());

		localId = IMPrefsTools.getStringPrefs(PetStrategyDetailsActivity.this,
				"userId", "");
	}

	protected void onStart() {
		super.onStart();
		initData();
		getAttention();
	}

	/**
	 * 初始化控件
	 */

	private void initView() {
		ImageView back = (ImageView) findViewById(R.id.back);
		TextView title = (TextView) findViewById(R.id.title);
		ImageView pet_strategy_details_image = (ImageView) findViewById(R.id.pet_strategy_details_image);
		TextView pet_strategy_details_comment = (TextView) findViewById(R.id.pet_strategy_details_comment);
		pet_strategy_details_collection = (TextView) findViewById(R.id.pet_strategy_details_collection);
		TextView pet_strategy_details_breed = (TextView) findViewById(R.id.pet_strategy_details_breed);
		TextView pet_strategy_details_ratingbar_num = (TextView) findViewById(R.id.pet_strategy_details_ratingbar_num);
		TextView pet_strategy_details_num = (TextView) findViewById(R.id.pet_strategy_details_num);
		TextView pet_strategy_details_characteristic = (TextView) findViewById(R.id.pet_strategy_details_characteristic);
		TextView pet_strategy_details_content = (TextView) findViewById(R.id.pet_strategy_details_content);
		RatingBar pet_strategy_details_ratingbar = (RatingBar) findViewById(R.id.pet_strategy_details_ratingbar);
		pet_strategy_details_breed.setText(petName);
		pet_strategy_details_ratingbar.setRating((float) (Integer
				.parseInt(petGrade)));
		pet_strategy_details_ratingbar_num.setText(petGrade + "分");
		pet_strategy_details_num.setText(petMoney);
		pet_strategy_details_characteristic.setText(petTrait);
		pet_strategy_details_content.setText("\t\t\t\t"+petContent_data);

		String imageUrl = "http://211.149.198.8:9805" + petImage;
		// 给图片一个tag
		pet_strategy_details_image.setTag(imageUrl);
		// 异步加载图片
		Bitmap bitmap = imageLoader.loadBitmap(pet_strategy_details_image, imageUrl, true);
		if (bitmap != null) {
			pet_strategy_details_image.setImageBitmap(bitmap);
		}else{
			pet_strategy_details_image.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		title.setText("详情");

		pet_strategy_details_comment.setOnClickListener(clickListener);
		pet_strategy_details_collection.setOnClickListener(clickListener);
		back.setOnClickListener(clickListener);
	}

	/**
	 * 加载数据
	 */
	private void initData() {
		Bundle bundle = getIntent().getExtras();
		petName = bundle.getString("petName");
		petGrade = bundle.getString("petGrade");
		petMoney = bundle.getString("petMoney");
		petTrait = bundle.getString("petTrait");
		petContent_data = bundle.getString("petContent_data");
		petImage = bundle.getString("petImage");
		userId = bundle.getString("userId");
		username = bundle.getString("userName");
		initView();
	}

	/**
	 * 点击跳转事件
	 */
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.pet_strategy_details_comment:
				intent = new Intent();
				intent.setClass(PetStrategyDetailsActivity.this,
						PetStrategyCommentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("petName", petName);
				bundle.putString("petContent_data", petContent_data);
				bundle.putString("petTrait", petTrait);
				bundle.putString("petImage", petImage);
				bundle.putString("userId", userId);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
				finish();
				break;
			case R.id.pet_strategy_details_collection:
				attention();
				break;
			case R.id.back:
				back();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 返回
	 */
	private void back() {
		finish();
	}

	/**
	 * 获取收藏状态
	 */
	private void getAttention() {
		String url_send = "http://211.149.198.8:9805/index.php/home/api/HasCollect";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url_send);
			Map<String, String> map = new HashMap<String, String>();
			map.put("issue_id", userId);
			map.put("tel", localId);
			httpPost.putMap(map);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						int status = jsonObject.getInt("status");
						if (status == 1) {// 关注成功
							pet_strategy_details_collection.setText("已收藏");
						} else if (status == -1) {
							pet_strategy_details_collection.setText("收藏");
						}
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
	 * 收藏
	 */
	private void attention() {
		String url_send = "http://211.149.198.8:9805/index.php/home/api/collect";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url_send);
			Map<String, String> map = new HashMap<String, String>();
			map.put("issue_id", userId);
			map.put("tel", localId);
			httpPost.putMap(map);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						int status = jsonObject.getInt("status");
						Toast.makeText(getApplicationContext(),
								jsonObject.getString("message"),
								Toast.LENGTH_SHORT).show();
						Log.e("tag", status + "");
						if (status == 1) {// 收藏成功
							pet_strategy_details_collection.setText("已收藏");
						} else if (status == 2) {
							pet_strategy_details_collection.setText("收藏");
						}
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
