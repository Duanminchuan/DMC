package com.dream.cutepet;

import com.dream.cutepet.ui.GesturesRegisterActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 账户管理
 * 
 * @author Administrator
 *
 */
public class AccountActivity extends Activity {
	ImageView back;
	TextView title,menu_hide;
	LinearLayout linear_security;
	LinearLayout linear_change_password;
	LinearLayout linear_change_number;
	LinearLayout linear_change_mail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		back = (ImageView) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText("账户管理");
		menu_hide=(TextView) findViewById(R.id.menu_hide);
		menu_hide.setText("保存");
		menu_hide.setVisibility(View.VISIBLE);
		linear_security = (LinearLayout) findViewById(R.id.linear_security);
		linear_change_password = (LinearLayout) findViewById(R.id.linear_change_password);
		linear_change_number = (LinearLayout) findViewById(R.id.linear_change_number);
		linear_change_mail = (LinearLayout) findViewById(R.id.linear_change_mail);

		back.setOnClickListener(ocl);
		linear_security.setOnClickListener(ocl);
		linear_change_password.setOnClickListener(ocl);
		linear_change_number.setOnClickListener(ocl);
		linear_change_mail.setOnClickListener(ocl);
	}

	OnClickListener ocl = new OnClickListener() {
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.linear_change_password:
				// 修改密码
				intent.setClass(AccountActivity.this, ChangePasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.linear_change_number:
				// 修改手机号
				intent.setClass(AccountActivity.this, ChangeNumberActivity.class);
				startActivity(intent);
				break;
			case R.id.linear_change_mail:
				// 修改邮箱
				intent.setClass(AccountActivity.this, ChangeMailActivity.class);
				startActivity(intent);
				break;
			case R.id.linear_security:
				// 手势密码设置
				intent.setClass(AccountActivity.this, GesturesRegisterActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
}
