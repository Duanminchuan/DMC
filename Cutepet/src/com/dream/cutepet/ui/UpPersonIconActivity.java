package com.dream.cutepet.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dream.cutepet.R;
import com.dream.cutepet.adapter.ShowPhotoNoCheckAdapter;
import com.dream.cutepet.util.HttpPost;
import com.dream.cutepet.util.HttpPost.OnSendListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UpPersonIconActivity extends Activity implements OnClickListener{
	private GridView mGridView;
	private List<String> list;
	private ShowPhotoNoCheckAdapter adapter;
	private TextView tv_cancel, tv_sure;

	private String username;
	private String url = "http://211.149.198.8:9805/index.php/home/api/uploadPersonIcon";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphoto_item);

		username = getIntent().getStringExtra("tel");

		list = getIntent().getStringArrayListExtra("data");

		initView();
	}
	
	/**
	 * 上传头像
	 * 
	 * @param img
	 */
	private void updateIcon(String img) {
		File file = new File(img);
		try {
			HttpPost httpPost = HttpPost.parseUrl(url);
			httpPost.putFile("file", file, file.getName(), null);
			httpPost.putString("tel", username);
			httpPost.send();
			httpPost.setOnSendListener(new OnSendListener() {
				@Override
				public void start() {
				}

				@Override
				public void end(String result) {
					Log.i("result", result);
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
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		mGridView = (GridView) findViewById(R.id.gv_showphoto_item_gridview);
		adapter = new ShowPhotoNoCheckAdapter(getApplicationContext(), list,
				mGridView);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(clickListener);

		tv_cancel = (TextView) findViewById(R.id.tv_showphoto_cancel);
		tv_sure = (TextView) findViewById(R.id.tv_showphoto_sure);
		tv_cancel.setOnClickListener(this);
		tv_sure.setVisibility(View.GONE);
	}

	OnItemClickListener clickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String img = list.get(position);
			updateIcon(img);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_showphoto_cancel:
			finish();
			break;
		default:
			break;
		}
	}
}
