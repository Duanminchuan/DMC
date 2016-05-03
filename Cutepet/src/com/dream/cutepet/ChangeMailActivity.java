package com.dream.cutepet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 修改邮箱
 * 
 * @author Administrator
 *
 */
public class ChangeMailActivity extends Activity {

	ImageView back;
	TextView title, menu_hide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_mail);
		title=(TextView)findViewById(R.id.title);
		menu_hide=(TextView) findViewById(R.id.menu_hide);
		title.setText("修改邮箱");
		menu_hide.setText("保存");
		menu_hide.setVisibility(View.VISIBLE);
		back = (ImageView) findViewById(R.id.back);
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
