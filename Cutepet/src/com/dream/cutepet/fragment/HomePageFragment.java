package com.dream.cutepet.fragment;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dream.cutepet.PersonalDetailsActivity;
import com.dream.cutepet.R;
import com.dream.cutepet.adapter.HomePageAdapter;
import com.dream.cutepet.adapter.HomePageAdapter.CallParise;
import com.dream.cutepet.adapter.HomePageAdapter.SetMessage;
import com.dream.cutepet.cache.AsyncImageLoader;
import com.dream.cutepet.cache.ImageCacheManager;
import com.dream.cutepet.model.PersonageModel;
import com.dream.cutepet.model.PetStoreModel;
import com.dream.cutepet.ui.PetStoreActivity;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.view.RefreshableView;
import com.dream.cutepet.view.RefreshableView.PullToRefreshListener;

/**
 * 主页frament
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ValidFragment")
public class HomePageFragment extends Fragment implements CallParise,
		SetMessage {
	public HomePageFragment() {

	}

	public HomePageFragment(Context context) {
		this.context = context;
	}

	LinearLayout llayout_petStore;
	LayoutInflater inflater;
	List<PetStoreModel> data_petStore;
	List<PersonageModel> data_personage = new ArrayList<PersonageModel>();
	ListView listView;
	HomePageAdapter adapter;
	private AsyncImageLoader imageLoader;
	private String username;
	private View view;
	Bitmap bitmap;
	private String url_top = "http://211.149.198.8:9805";
	LinearLayout message_linearlayout;
	EditText input_message;
	TextView ensure_send;
	TextView cancel_send;
	String getInput;
	HorizontalScrollView scrollView;
	Context context;
	RefreshableView refreshableView;// 下拉刷新
	private boolean isRefresh = false;// 是否刷新
	View header;
	LayoutInflater inflater_header;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.activity_homepage, null);
		ImageCacheManager cacheMgr = new ImageCacheManager(context);
		imageLoader = new AsyncImageLoader(context, cacheMgr.getMemoryCache(),
				cacheMgr.getPlacardFileCache());
		inflater_header = LayoutInflater.from(context);

		initView();
		
		initStoreData();

		initPersonalData();

		return view;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0011:
				scrollView.scrollTo(200, 0);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化宠物店数据
	 */
	private void initStoreData() {
		String URL_store = "http://211.149.198.8:9805/index.php/home/api/getPetStore";
		try {
			HttpPost post_store = HttpPost.parseUrl(URL_store);
			post_store.send();
			post_store.setOnSendListener(new OnSendListener() {
				public void start() {
				}

				public void end(String result) {
					data_petStore = PetStoreModel.setJson(result);
					initPetStoreView();
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化主人寄语数据
	 */
	private void initPersonalData() {
		String URL_store = "http://211.149.198.8:9805/index.php/home/api/getPersonal";
		try {
			HttpPost post_store = HttpPost.parseUrl(URL_store);
			post_store.send();
			post_store.setOnSendListener(new OnSendListener() {
				public void start() {
				}

				public void end(String result) {
					data_personage = PersonageModel.setJson(result);
					adapter.setData(data_personage);
					if (!isRefresh) {
						adapter.notifyDataSetChanged();
					}
					if (refreshableView.header != null) {
						refreshableView.header.setVisibility(View.GONE);
					}
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		refreshableView = (RefreshableView) view
				.findViewById(R.id.home_pager_refreshLayout);
		message_linearlayout = (LinearLayout) view
				.findViewById(R.id.message_linearlayout);
		input_message = (EditText) view.findViewById(R.id.input_message);
		ensure_send = (TextView) view.findViewById(R.id.ensure_send);
		cancel_send = (TextView) view.findViewById(R.id.cancel_send);

		listView = (ListView) view.findViewById(R.id.lv_homepage_listview);
		listView.setOnItemClickListener(itemClickListener);
	}

	/**
	 * listview的item点击事件
	 */
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (checkLogin()) {
				// 跳转到主人寄语
				Intent intent = new Intent(context,
						PersonalDetailsActivity.class);

				PersonageModel mo = data_personage.get(position - 1);
				Bundle bundle = new Bundle();
				bundle.putString("name", mo.getName());
				bundle.putString("petlogo", mo.getPicture_icon());
				bundle.putString("petname", mo.getPicture_name());
				bundle.putString("content", mo.getContent());

				intent.putExtras(bundle);
				startActivityForResult(intent, 1357);
			}
		}
	};

	/*
	 * 跳转到宠物商店
	 */
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (checkLogin()) {
				int index = (Integer) v.getTag();
				Intent intent = new Intent(context, PetStoreActivity.class);
				intent.putExtra("index", index);
				intent.putExtra("username", data_petStore.get(index)
						.getUsername());
				intent.putExtra("name", data_petStore.get(index).getName());
				intent.putExtra("address", data_petStore.get(index)
						.getAddress());
				intent.putExtra("type", data_petStore.get(index).getType());
				intent.putExtra("icon", url_top
						+ data_petStore.get(index).getLogo());
				startActivityForResult(intent, 0);
			}
		}
	};

	/**
	 * 初始化 宠物店的画廊
	 */
	@SuppressLint("InflateParams")
	private void initPetStoreView() {
		if(header == null){
			header = inflater_header.inflate(R.layout.activity_homepage_header,
					null);
			scrollView = (HorizontalScrollView) header
					.findViewById(R.id.scrollview);
			handler.sendEmptyMessage(0011);

			inflater = LayoutInflater.from(context);
			llayout_petStore = (LinearLayout) header
					.findViewById(R.id.llayout_homepage_petStore);
			for (int i = 0; i < data_petStore.size(); i++) {
				View view_item = inflater_header.inflate(
						R.layout.activity_homepage_item, null);
				ImageView img = (ImageView) view_item
						.findViewById(R.id.iv_homepage_item_picture);

				final String imgUrl = url_top + data_petStore.get(i).getLogo();
				// 给 ImageView 设置一个 tag
				img.setTag(imgUrl);
				// 异步加载图片
				bitmap = imageLoader.loadBitmap(img, imgUrl, true);
				if (bitmap != null) {
					img.setImageBitmap(bitmap);
				} else {
					img.setImageResource(R.drawable.friends_sends_pictures_no);
				}

				TextView txt = (TextView) view_item
						.findViewById(R.id.tv_homepage_item_word);
				txt.setText(data_petStore.get(i).getName());
				view_item.setTag(i);
				view_item.setOnClickListener(listener);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 0, 10, 0);
				view_item.setLayoutParams(params);

				llayout_petStore.addView(view_item);
			}
		}
		listView.addHeaderView(header, null, false);
		adapter = new HomePageAdapter(context, data_personage, this, this);
		listView.setAdapter(adapter);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					isRefresh = true;
					initPersonalData();
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
	}

	@Override
	public void click(View v) {
		switch (v.getId()) {
		case R.id.tv_homepage_personage_like:
			if (checkLogin()) {
				int position = (Integer) v.getTag();
				setParise(position);
			}
			break;
		case R.id.tv_homepage_personage_getMessage:
			if (checkLogin()) {
				int position = (Integer) v.getTag();
				setMessage(position);
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 点赞功能
	 * 
	 * @param position
	 */
	private void setParise(int position) {
		String url = "http://211.149.198.8:9805/index.php/home/api/uploadPraise_personal";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			Map<String, String> map = new HashMap<String, String>();
			map.put("user_id", username);
			map.put("issue_id", data_personage.get(position).getId() + "");
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
						Toast.makeText(context,
								jsonObject.getString("message"),
								Toast.LENGTH_SHORT).show();
						initPersonalData();
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
	 * 留言功能
	 * 
	 * @param position
	 * @param content
	 * @param time
	 */
	private void setMessage(final int position) {
		message_linearlayout.setVisibility(View.VISIBLE);
		ensure_send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String url = "http://211.149.198.8:9805/index.php/home/api/uploadMessage";
				String content = input_message.getText().toString();
				Date nowDate = new Date();
				String time = nowDate.toString();
				if ((content != null) && (!("".equals(content)))) {
					try {
						HttpPost httpPost = HttpPost.parseUrl(url);
						Map<String, String> map = new HashMap<String, String>();
						map.put("mine_name", username);
						map.put("issue_id", data_personage.get(position)
								.getId() + "");
						map.put("content", content);
						map.put("time", time);
						httpPost.putMap(map);
						httpPost.send();
						httpPost.setOnSendListener(new OnSendListener() {
							public void start() {
							}

							public void end(String result) {
								Toast.makeText(context, "留言成功",
										Toast.LENGTH_SHORT).show();
							}
						});
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
				message_linearlayout.setVisibility(View.GONE);
			}
		});
		cancel_send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				message_linearlayout.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 判断账号是否登录
	 * 
	 * @return
	 */
	private boolean checkLogin() {
		String tok = SharedPreferencesUtil.getData(context);
		if (tok != null && !tok.equals("")) {
			username = tok.split(",")[1];
			return true;
		}
		Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
		return false;
	}

}
