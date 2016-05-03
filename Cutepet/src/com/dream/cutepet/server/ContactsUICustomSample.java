package com.dream.cutepet.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMContactsUI;
import com.dream.cutepet.R;

public class ContactsUICustomSample extends IMContactsUI {

	public ContactsUICustomSample(Pointcut pointcut) {
		super(pointcut);
	}

	/**
	 * 返回联系人自定义标题
	 * 
	 * @param fragment
	 * @param context
	 * @param inflater
	 * @return
	 */
	@SuppressLint("ResourceAsColor")
	@Override
	public View getCustomTitle(final Fragment fragment, final Context context,
			LayoutInflater inflater) {
		// TODO 重要：必须以该形式初始化customView---［inflate(R.layout.**, new
		// RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局xml**中定义的高度和宽度无效，均被默认的wrap_content替代
		LinearLayout customView = (LinearLayout) inflater.inflate(
				R.layout.activity_chat_contacts_tittle, new LinearLayout(
						context), false);
		TextView title = (TextView) customView
				.findViewById(R.id.contacts_tittle);
		title.setText("联系人");
		title.setTextSize(18);
		title.setBackgroundColor(Color.parseColor("#FFDA44"));
		title.setTextColor(Color.BLACK);
		title.setGravity(Gravity.CENTER);
		return customView;
	}
}
