package com.dream.cutepet;

import java.util.List;

import com.dream.cutepet.adapter.ShowPhotoNoCheckAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectShowPhotoActivity extends Activity implements
		OnClickListener {
	private GridView mGridView;
	private List<String> list;
	private ShowPhotoNoCheckAdapter adapter;
	private TextView tv_cancel, tv_sure;
	private int flog;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphoto_item);

		list = getIntent().getStringArrayListExtra("data");
		flog = getIntent().getIntExtra("flog", 0);
		bundle = getIntent().getExtras();
		
		initView();
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
			Intent intent = new Intent();
			if (flog == 1) {
				intent.setClass(SelectShowPhotoActivity.this,
						PersonalReleaseActivity.class);
			} else if (flog == 2) {
				intent.setClass(SelectShowPhotoActivity.this,
						ReleaseActivity.class);
			}else if (flog == 3) {
				intent.setClass(SelectShowPhotoActivity.this,
						PetStrategyCommentActivity.class);
				intent.putExtras(bundle);
			}
			intent.putExtra("path", img);
			startActivity(intent);
			finish();
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
