package com.dream.cutepet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 关于萌宠
 * 
 * @author Administrator
 *
 */
public class AboutActivity extends Activity {

	ImageView back;
	LinearLayout linear_update;
	LinearLayout linear_grade;
	TextView title;

	AlertDialog customDialog;

	LinearLayout linear_complain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		title = (TextView) findViewById(R.id.title);
		back=(ImageView) findViewById(R.id.back);
		title.setText("关于萌宠");
		linear_update = (LinearLayout) findViewById(R.id.linear_update);
		linear_grade = (LinearLayout) findViewById(R.id.linear_grade);
		linear_complain = (LinearLayout) findViewById(R.id.linear_complain);

		back.setOnClickListener(ocl);
		linear_update.setOnClickListener(ocl);
		linear_grade.setOnClickListener(ocl);
		linear_complain.setOnClickListener(ocl);
	}

	OnClickListener ocl = new OnClickListener() {
		Intent intent = new Intent();
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.linear_complain:
				// 举报与投诉
				intent.setClass(AboutActivity.this, ComplainActivity.class);
				startActivity(intent);
				break;
			case R.id.linear_update:
				// 检查版本更新
				intent.setClass(AboutActivity.this, UpdateActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};

}
