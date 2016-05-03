package com.dream.cutepet;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.dream.cutepet.adapter.DynamicDetailsBaseAdapter;
import com.dream.cutepet.adapter.ShowDynamicDetailPicAdapter;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.DynamicDetailsModel;
import com.dream.cutepet.model.SquareModel;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.MyListUtil;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.TimeUtil;
import com.dream.cutepet.view.MyGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DynamicDetailsActivity extends Activity {
	List<SquareModel> squareData;
	DynamicDetailsModel dynamicDetailsData = new DynamicDetailsModel();

	DynamicDetailsBaseAdapter adapter;
	ListView listView;
	RadioGroup radioGroup_bottom;
	String str_edit;
	EditText dynamic_details_edit;
	String urlTop = "http://211.149.198.8:9805";
	AsyncImageLoader imageLoader;
	TextView dynamic_details_nickname;
	TextView dynamic_details_time;
	TextView dynamic_details_address;
	TextView dynamic_details_content;
	TextView dynamic_details_like;
	TextView tv_add_attention;
	MyGridView dynamic_details_image;
	LinearLayout llayout_details_icon;
	private String username;
	private String id;
	private String uid;
	private String portraitUrl;
	private String imageUrl;
	private String theNickname;
	private String theTime;
	private String theAddress;
	private String theContent;
	private String thePraise;
	List<String> data_icon = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_details);

		ImageCacheManager cacheMgr = new ImageCacheManager(this);
		imageLoader = new AsyncImageLoader(this, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());

		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("theId");
		uid = bundle.getString("theUsername");
		portraitUrl = bundle.getString("thePortrait");
		imageUrl = bundle.getString("thePicture");
		theNickname = bundle.getString("theNickname");
		theTime = bundle.getString("theTime");
		theAddress = bundle.getString("theAddress");
		theContent = bundle.getString("theContent");
		username = bundle.getString("tel");
		thePraise = bundle.getString("praise");

		initView();
	}

	protected void onStart() {
		super.onStart();
		getData_icon();
		getData_comment();
		getAttention();
	}

	@SuppressLint("InflateParams")
	private void initView() {
		ImageView back = (ImageView) findViewById(R.id.back);
		ImageView send = (ImageView) findViewById(R.id.send);
		back.setOnClickListener(clickListener);
		send.setOnClickListener(clickListener);

		dynamic_details_edit = (EditText) findViewById(R.id.dynamic_details_edit);
		listView = (ListView) findViewById(R.id.dynamic_details_listview);
		TextView title = (TextView) findViewById(R.id.title);
		title.setTextColor(Color.rgb(51, 51, 51));
		title.setText("详情");
		TextView menu_hide = (TextView) findViewById(R.id.menu_hide);
		menu_hide.setText("写说说");
		menu_hide.setVisibility(View.VISIBLE);
		menu_hide.setOnClickListener(clickListener);

		// 加上headerview
		LayoutInflater inflater = LayoutInflater.from(this);
		View dynamic_details_headerview = inflater.inflate(
				R.layout.dynamic_details_head_view, null);

		dynamic_details_nickname = (TextView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_nickname);
		dynamic_details_time = (TextView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_time);
		dynamic_details_address = (TextView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_address);
		dynamic_details_content = (TextView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_content);
		dynamic_details_like = (TextView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_praise_num);
		ImageView dynamic_details_portrait = (ImageView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_portrait);
		llayout_details_icon = (LinearLayout) dynamic_details_headerview
				.findViewById(R.id.llayout_details_icon);
		tv_add_attention = (TextView) dynamic_details_headerview
				.findViewById(R.id.add_attention);
		tv_add_attention.setOnClickListener(clickListener);

		dynamic_details_image = (MyGridView) dynamic_details_headerview
				.findViewById(R.id.dynamic_details_image);
		List<String> list = MyListUtil.changeStringToList(imageUrl, ",");
		ShowDynamicDetailPicAdapter adapter1 = new ShowDynamicDetailPicAdapter(
				getApplicationContext(), list, dynamic_details_image);
		// 加载说说图片
		if (list.size() == 1) {
			dynamic_details_image.setNumColumns(1);
		} else if (list.size() == 2) {
			dynamic_details_image.setNumColumns(2);
		} else if (list.size() > 2) {
			dynamic_details_image.setNumColumns(3);
		} else {
			dynamic_details_image.setVisibility(View.GONE);
		}
		dynamic_details_image.setAdapter(adapter1);

		if (!TextUtils.isEmpty(theNickname) && !theNickname.equals("null")) {
			dynamic_details_nickname.setText(theNickname);
		} else {
			dynamic_details_nickname.setText(uid + "");
		}

		Date date = TimeUtil.changeTime(theTime);
		dynamic_details_time.setText(TimeUtil.showTime(date));
		dynamic_details_address.setText(theAddress);
		dynamic_details_content.setText(theContent);
		dynamic_details_like.setText(thePraise + "");
		dynamic_details_like.setOnClickListener(clickListener);
		Log.i("portraitUrl", portraitUrl);
		if (!TextUtils.isEmpty(portraitUrl) && !portraitUrl.equals("null")) {
			String url_portrait = urlTop + portraitUrl;
			dynamic_details_portrait.setTag(url_portrait);
			Bitmap bt_icon = imageLoader.loadBitmap(dynamic_details_portrait,
					url_portrait, false);
			if (bt_icon != null) {
				Bitmap cc_tx = BitmapUtil.toRoundBitmap(bt_icon);
				dynamic_details_portrait.setImageBitmap(cc_tx);
			} else {
				dynamic_details_portrait.setImageResource(R.drawable.icon_tx);
			}
		} else {
			dynamic_details_portrait.setImageResource(R.drawable.icon_tx);
		}

		listView.addHeaderView(dynamic_details_headerview);
		adapter = new DynamicDetailsBaseAdapter(
				dynamicDetailsData.getMessage(), this);
		listView.setAdapter(adapter);
	}

	/**
	 * 加载点赞头像数据
	 * 
	 * @param num
	 */
	private void initPraiseIcon() {
		LayoutParams params = new LayoutParams(getResources()
				.getDimensionPixelSize(R.dimen.px80), getResources()
				.getDimensionPixelSize(R.dimen.px80));
		params.setMargins(0, 0, 8, 0);
		llayout_details_icon.removeAllViews();
		for (int i = 0; i < data_icon.size(); i++) {
			ImageView child = new ImageView(DynamicDetailsActivity.this);
			String tx = data_icon.get(i);
			if (!TextUtils.isEmpty(tx) && !tx.equals("null")) {
				String url_img = urlTop + tx;
				child.setTag(url_img);
				Bitmap bt1 = imageLoader.loadBitmap(child, url_img, false);
				if (bt1 != null) {
					Bitmap cc_tx = BitmapUtil.toRoundBitmap(bt1);
					child.setImageBitmap(cc_tx);
				} else {
					child.setImageResource(R.drawable.icon_tx);
				}
			} else {
				child.setImageResource(R.drawable.icon_tx);
			}
			child.setScaleType(ScaleType.FIT_XY);
			child.setAdjustViewBounds(true);
			child.setLayoutParams(params);
			llayout_details_icon.addView(child);
		}
		dynamic_details_like.setText(data_icon.size() + "");
	}

	/**
	 * 点赞功能
	 * 
	 * @param position
	 */
	private void setParise() {
		String url = "http://211.149.198.8:9805/index.php/home/api/uploadPraise_square";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			Map<String, String> map = new HashMap<String, String>();
			map.put("user_id", username);
			map.put("issue_id", id);
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
						Toast.makeText(getApplicationContext(),
								jsonObject.getString("message"),
								Toast.LENGTH_SHORT).show();
						getData_icon();
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
	 * 获取点赞头像数据
	 */
	private void getData_icon() {
		String url = "http://211.149.198.8:9805/index.php/home/api/getPraise_square_icon";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			httpPost.putString("issue_id", id);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					try {
						// 清空头像的数组
						data_icon.clear();
						JSONObject jsonObject = new JSONObject(result);
						if (jsonObject.getInt("status") == 1) {
							JSONArray arr = jsonObject.getJSONArray("message");
							for (int i = 0; i < arr.length(); i++) {
								data_icon.add(arr.getString(i));
							}
						}
						initPraiseIcon();
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
	 * 获取评论数据
	 */
	private void getData_comment() {
		String url = "http://211.149.198.8:9805/index.php/home/api/getSquareComment";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			httpPost.putString("issue_id", id);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					dynamicDetailsData = DynamicDetailsModel.setJson(result);
					adapter.setData(dynamicDetailsData.getMessage());
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送评论
	 */
	private void send() {
		String content = dynamic_details_edit.getText().toString().trim();
		String url_send = "http://211.149.198.8:9805/index.php/home/api/uploadSquareComment";
		if (!TextUtils.isEmpty(content)) {
			try {
				HttpPost httpPost = HttpPost.parseUrl(url_send);
				Map<String, String> map = new HashMap<String, String>();
				map.put("uid", username);
				map.put("issue_id", id);
				map.put("content", content);
				map.put("create_time", TimeUtil.changeTimeToGMT(new Date()));
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
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("message"),
									Toast.LENGTH_SHORT).show();
							dynamic_details_edit.setText("");
							getData_comment();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(), "评论内容不能为空！",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getAttention() {
		String url_send = "http://211.149.198.8:9805/index.php/home/api/hasAttention";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url_send);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", username);
			map.put("friend_username", uid);
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
							tv_add_attention.setText("已关注");
						} else if (status == -1) {
							tv_add_attention.setText("+关注");
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
	 * 关注
	 */
	private void attention() {
		String url_send = "http://211.149.198.8:9805/index.php/home/api/attention";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url_send);
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", username);
			map.put("friend_username", uid);
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
						if (status == 1) {// 关注成功
							tv_add_attention.setText("已关注");
						} else if (status == -1) {
							tv_add_attention.setText("+关注");
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

	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				back();
				break;
			case R.id.send:
				send();
				break;
			case R.id.dynamic_details_praise_num:
				setParise();
				break;
			case R.id.add_attention:
				attention();
				break;
			case R.id.menu_hide:
				Intent intent = new Intent();
				intent.setClass(DynamicDetailsActivity.this,
						WriteTalkActivity.class);
				startActivity(intent);
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
}