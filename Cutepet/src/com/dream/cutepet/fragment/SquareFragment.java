package com.dream.cutepet.fragment;

import java.net.MalformedURLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.dream.cutepet.DynamicDetailsActivity;
import com.dream.cutepet.R;
import com.dream.cutepet.adapter.SquareBaseAdapter;
import com.dream.cutepet.model.SquareModel;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.view.RefreshableView;
import com.dream.cutepet.view.RefreshableView.PullToRefreshListener;

/**
 * 广场的fragment
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ValidFragment")
public class SquareFragment extends Fragment {
	ViewPager viewPager;
	List<SquareModel> data;
	SquareBaseAdapter adapter = new SquareBaseAdapter();
	ListView listView;
	ImageView square_image;
	ImageView square_mid_image_1;
	ImageView square_mid_image_2;
	ImageView square_mid_image_3;
	ImageView square_mid_image_4;
	RadioGroup group, group_top;
	View view;
	ViewFlipper viewFlipper;
	ImageView square_mid_more;
	GestureDetector gestureDetector;

	String squareId;
	String squareUsername;
	String squarePortrait;
	String squareNickname;
	String squareTime;
	String squareAddress;
	String squareContent;
	String squarePicture;
	String squarePraise;

	private String username;

	RefreshableView refreshableView;
	private boolean isRefresh = false;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_square_dynamic, null);
		
		initView();
		
		getData();
		
		return view;
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint({ "NewApi", "InflateParams" })
	private void initView() {
		listView = (ListView) view.findViewById(R.id.square_listview);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View squareHeaderView = inflater.inflate(R.layout.square_head_view,
				null);
		listView.addHeaderView(squareHeaderView, null, false);
		square_mid_more = (ImageView) squareHeaderView
				.findViewById(R.id.square_mid_more);
		viewFlipper = (ViewFlipper) squareHeaderView
				.findViewById(R.id.square_mid_view_flipper);
		adapter = new SquareBaseAdapter(data, getActivity());
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(itemClickListener);
		square_mid_more.setOnClickListener(clickListener);

		refreshableView = (RefreshableView) view
				.findViewById(R.id.square_refreshableview);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					// isRefresh = true;
					getData();
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
	}

	/*
	 * more的点击事件
	 */
	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.square_mid_more:
				show_next();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 显示下一组图片的方法
	 */
	private void show_next() {
		viewFlipper.showNext();
	}

	/**
	 * 判断账号是否登录
	 * 
	 * @return
	 */
	private boolean checkLogin() {
		String tok = SharedPreferencesUtil.getData(getActivity());
		if (tok != null && !tok.equals("")) {
			username = tok.split(",")[1];
			return true;
		}
		Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
		return false;
	}

	/*
	 * listview点击事件
	 */
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (checkLogin()) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), DynamicDetailsActivity.class);

				SquareModel model = data.get(position - 1);

				squareId = model.getSquare_id();
				squareUsername = model.getSquare_username();
				squarePortrait = model.getSquare_portrait();
				squareNickname = model.getSquare_neckname();
				squareTime = model.getSquare_comment_time();
				squareAddress = model.getSquare_address();
				squareContent = model.getSquare_comment_content();
				squarePicture = model.getSquare_image();
				squarePraise = model.getSquare_praise_num();

				Bundle bundle = new Bundle();
				bundle.putString("theId", squareId);
				bundle.putString("theUsername", squareUsername);
				bundle.putString("thePortrait", squarePortrait);
				bundle.putString("theNickname", squareNickname);
				bundle.putString("theTime", squareTime);
				bundle.putString("theAddress", squareAddress);
				bundle.putString("theContent", squareContent);
				bundle.putString("thePicture", squarePicture);
				bundle.putString("praise", squarePraise);
				bundle.putString("tel", username);

				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
			}
		}
	};

	/**
	 * 获取数据
	 */
	private void getData() {
		String url = "http://211.149.198.8:9805/index.php/home/api/getTalk";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				public void start() {
				}

				public void end(String result) {
					data = SquareModel.setJson(result);
					adapter.setData(data);
					if(!isRefresh){
						adapter.notifyDataSetChanged();	
					}
					if(refreshableView.header != null){
						refreshableView.header.setVisibility(View.GONE);
					}
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
