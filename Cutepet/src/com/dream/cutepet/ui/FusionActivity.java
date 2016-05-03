package com.dream.cutepet.ui;

import java.net.MalformedURLException;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.dream.cutepet.R;
import com.dream.cutepet.adapter.FusionAdapter;
import com.dream.cutepet.model.FusionModel;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;
import com.dream.cutepet.util.SharedPreferencesUtil;

/**
 * 个人中心的时光轴界面
 * 
 * @author Administrator
 * 
 */
public class FusionActivity extends Activity {
	ImageView iv_back;
	ListView listView;
	List<FusionModel> data;
	FusionAdapter adapter;
	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fusion);
		
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		String tok=SharedPreferencesUtil.getData(this);
		if(tok != null && !tok.equals("")){
			username=tok.split(",")[1];
		}
		String url="http://211.149.198.8:9805/index.php/home/api/fusion";
		try {
			HttpPost httpPost=HttpPost.parseUrl(url);
			httpPost.putString("tel", username);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				public void start() {
					
				}
				public void end(String result) {
					Log.e("bbbbbbbbbb", result);
					data=FusionModel.getJson(result);
					initView();
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
		iv_back = (ImageView) findViewById(R.id.iv_fusion_return);
		iv_back.setOnClickListener(listener);
		
		listView = (ListView) findViewById(R.id.lv_fusion_listview);
		adapter = new FusionAdapter(this,data);
		listView.setAdapter(adapter);
	}
	
	/**
	 * 返回
	 */
	private void back() {
		finish();
	}
	
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_fusion_return:
				back();
				break;
			default:
				break;
			}
		}
	};
}
