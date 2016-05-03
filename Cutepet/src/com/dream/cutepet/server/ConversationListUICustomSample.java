package com.dream.cutepet.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.dream.cutepet.AllPageActivity;
import com.dream.cutepet.R;

/**
 * 最近联系人自定义
 * 
 * @author Administrator
 * 
 */
public class ConversationListUICustomSample extends IMConversationListUI {

	public ConversationListUICustomSample(Pointcut pointcut) {
		super(pointcut);
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public View getCustomConversationListTitle(final Fragment fragment,
			Context context, LayoutInflater inflater) {
//		 TextView textView=new TextView(context);
//		 textView.setText("最近联系人");
//		 textView.setGravity(Gravity.CENTER);
//		 textView.setTextColor(Color.BLACK);
//		 textView.setBackgroundColor(Color.parseColor("#FFDA44"));
//		 textView.setTextSize(18);
		View view = inflater.inflate(R.layout.activity_dialog,
				new LinearLayout(context), false);
		TextView textTittle = (TextView) view.findViewById(R.id.title);
		ImageView imageBack = (ImageView) view.findViewById(R.id.back);
		textTittle.setText("最近联系人");
		textTittle.setTextColor(Color.parseColor("#333333"));
		imageBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(fragment.getActivity(), AllPageActivity.class);
					fragment.getActivity().startActivity(intent);
			}
		});
	
		return view;
	}
}
