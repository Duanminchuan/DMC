package com.dream.cutepet.fragment;

import com.alibaba.mobileim.YWIMKit;
import com.dream.cutepet.AllPageActivity;
import com.dream.cutepet.R;
import com.dream.cutepet.server.LoginSampleHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ChatActivityFragment extends FragmentActivity{
	FrameLayout frameLayout;
	RadioGroup radioGroup;
	YWIMKit imKit;
	FragmentManager fm;
	FragmentTransaction transaction;
	Fragment fragMessage;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_chat_fragment);
		frameLayout=(FrameLayout)findViewById(R.id.chat_framelayout);
		radioGroup=(RadioGroup)findViewById(R.id.chat_radio);
		RadioButton radioButton=(RadioButton)findViewById(R.id.chat_message);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
		radioButton.setChecked(true);
	}
	/**
	 * 底部菜单点击事件
	 */
	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			imKit = LoginSampleHelper.getInstance().getIMKit();
			 fm =getSupportFragmentManager();
			 transaction = fm.beginTransaction();
			switch (checkedId) {
			case R.id.chat_message:
				fragMessage = imKit.getConversationFragment();
				transaction.replace(R.id.chat_framelayout, fragMessage);
				break;
			case R.id.chat_contacts:
				Fragment fragContacts = imKit.getContactsFragment();
				transaction.replace(R.id.chat_framelayout, fragContacts);
				break;
			case R.id.chat_home:
				Intent intent=new Intent(ChatActivityFragment.this, AllPageActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
			transaction.commit();
		}
	};
	
}
