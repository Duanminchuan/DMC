package com.dream.cutepet;

import java.util.ArrayList;
import java.util.List;

import com.dream.cutepet.adapter.ShowPhotoAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class WriteTalkUpLoadShowPhotoActivity extends Activity implements
		OnClickListener {
	private GridView mGridView;
	private List<String> list;
	private ShowPhotoAdapter adapter;
	private TextView tv_cancel, tv_sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphoto_item);

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_showphoto_cancel:
			finish();
			break;
		case R.id.tv_showphoto_sure:
			isSure();
			break;
		default:
			break;
		}
	}

	/**
	 * 确定选中图片
	 */
	private void isSure() {
		// 获取选中的Item的position
		List<Integer> num = adapter.getSelectItems();
		// 创建一个存储file对象的list
		ArrayList<String> checked_path = new ArrayList<String>();
		for (int i = 0; i < num.size(); i++) {
			// 获取图片选中的路径
			String path = list.get(num.get(i));
			checked_path.add(path);
		}
		Intent intent = new Intent(WriteTalkUpLoadShowPhotoActivity.this,
				WriteTalkActivity.class);
		intent.putStringArrayListExtra("checked_path", checked_path);
		startActivity(intent);
		finish();
	}
}