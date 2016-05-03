package com.dream.cutepet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 检查版本更新
 * @author Administrator
 *
 */
public class UpdateActivity extends Activity {
	ImageView back;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		back = (ImageView) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText("检查最新版本");
		back.setOnClickListener(ocl);
	}

	OnClickListener ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			default:
				break;
			}
		}
	};
}
