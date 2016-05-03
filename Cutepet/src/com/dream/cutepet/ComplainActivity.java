package com.dream.cutepet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 举报与投诉
 * 
 * @author Administrator
 *
 */
public class ComplainActivity extends Activity {
	ImageView image_complain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain);
		image_complain = (ImageView) findViewById(R.id.image_complain);
		image_complain.setOnClickListener(ocl);
	}

	OnClickListener ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_complain:
				finish();
				break;

			default:
				break;
			}
		}
	};
}
