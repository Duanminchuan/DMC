package com.dream.cutepet.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.cutepet.R;
import com.dream.cutepet.adapter.ShowPhotoAdapter;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;

public class ShowImageActivity extends Activity implements OnClickListener {
	private GridView mGridView;
	private List<String> list;
	private ShowPhotoAdapter adapter;
	private TextView tv_cancel, tv_sure;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
	
	private String username,title;
	private String actionUrl = "http://211.149.198.8:9805/index.php/home/api/uploadPhoto";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphoto_item);
		
		username = getIntent().getStringExtra("tel");
		title = getIntent().getStringExtra("title");
			
		list = getIntent().getStringArrayListExtra("data");

		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		mGridView = (GridView) findViewById(R.id.gv_showphoto_item_gridview);
		adapter = new ShowPhotoAdapter(getApplicationContext(), list, mGridView);
		mGridView.setAdapter(adapter);

		tv_cancel = (TextView) findViewById(R.id.tv_showphoto_cancel);
		tv_sure = (TextView) findViewById(R.id.tv_showphoto_sure);
		tv_cancel.setOnClickListener(this);
		tv_sure.setOnClickListener(this);
	}
	
	/**
	 * 上传
	 */
	private void IsSure() {
		//获取选中的Item的position
		List<Integer> num = adapter.getSelectItems();
		//创建一个存储file对象的list
		List<File> img_data = new ArrayList<File>();
		for (int i = 0; i < num.size(); i++) {
			//获取图片选中的路径
			String path = list.get(num.get(i));
			img_data.add(new File(path));
		}
		try {
			HttpPost httpPost = HttpPost.parseUrl(actionUrl);
			Map<String, String> msg = new HashMap<String, String>();
			msg.put("tel", username);
			msg.put("albumname", title);
			msg.put("time", format.format(new Date()));
			msg.put("quantity", img_data.size() + "");
			for (File temp : img_data) {
				httpPost.putMap(msg);
				httpPost.putFile(temp.getName(), temp, temp.getName(), null);
			}
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					Log.i("result", "result = " + result);
					try {
						JSONObject jsonObject = new JSONObject(result);
						Toast.makeText(getApplicationContext(),
								jsonObject.getString("message"),
								Toast.LENGTH_SHORT).show();
						finish();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_showphoto_cancel:
			finish();
			break;
		case R.id.tv_showphoto_sure:
			IsSure();
			break;
		default:
			break;
		}
	}

}
