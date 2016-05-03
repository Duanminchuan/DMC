package com.dream.cutepet.fragment;

import java.net.MalformedURLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.PetStrategyDetailsActivity;
import com.dream.cutepet.R;
import com.dream.cutepet.adapter.PetStrategyBaseAdapter;
import com.dream.cutepet.model.PetStrategyModel;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.view.RefreshableView;
import com.dream.cutepet.view.RefreshableView.PullToRefreshListener;

/**
 * 宠物攻略fragment
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ValidFragment")
public class PetStrategyFragment extends Fragment {
	ListView listView;
	List<PetStrategyModel> data;
	PetStrategyBaseAdapter adapter = new PetStrategyBaseAdapter();
	String getData;
	TextView tv_resources, tv_strategy;
	View view;
	private String tel;
	Context context;

	private RefreshableView refreshableView;
	
	private boolean isRefresh = false;

	public PetStrategyFragment() {

	}

	public PetStrategyFragment(Context context) {
		this.context = context;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_pet_strategy, null);

		getData();
		
		initView();

		return view;
	}

	/**
	 * 加载listview的数据
	 */
	private void initView() {
		listView = (ListView) view.findViewById(R.id.pet_strategy_listview);
		listView.setOnItemClickListener(itemClickListener);
		
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.banner);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		imageView.setAdjustViewBounds(true);
		imageView.setScaleType(ScaleType.FIT_XY);
		listView.addHeaderView(imageView);
		
		adapter = new PetStrategyBaseAdapter(data, context);
		listView.setAdapter(adapter);

		refreshableView = (RefreshableView) view
				.findViewById(R.id.pet_strategy_refreshLayout);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					isRefresh = true;
					getData();
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
	}

	/**
	 * 点击跳转
	 */
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (checkLogin()) {
				PetStrategyModel model = new PetStrategyModel();
				model = data.get(position);
				String petName = model.getPet_strategy_comment_chinese_name();
				String petGrade = model.getGrade();
				String petMoney = model.getMoney();
				String petTrait = model.getTrait();
				String petContent_data = model.getPet_strategy_content_data();
				String petImage = model.getPet_strategy_image();
				String userId = model.getId();
				String userName = model.getUsername();

				Bundle bundle = new Bundle();
				bundle.putString("petName", petName);
				bundle.putString("petGrade", petGrade);
				bundle.putString("petMoney", petMoney);
				bundle.putString("petTrait", petTrait);
				bundle.putString("petContent_data", petContent_data);
				bundle.putString("petImage", petImage);
				bundle.putString("userId", userId);
				bundle.putString("userName", userName);
				bundle.putString("tel", tel);

				Intent intent = new Intent();
				intent.setClass(context, PetStrategyDetailsActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
			}
		}
	};

	/**
	 * 初始化数据
	 */
	private void getData() {
		String url = "http://211.149.198.8:9805/index.php/home/api/getStrategy";
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				public void start() {
				}

				public void end(String result) {
					Log.e("result", result);
					data = PetStrategyModel.setJson(result);
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

	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	private boolean checkLogin() {
		String tok = SharedPreferencesUtil.getData(context);
		if (tok != null) {
			tel = tok.split(",")[1];
			return true;
		}
		Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
		return false;
	}
}
