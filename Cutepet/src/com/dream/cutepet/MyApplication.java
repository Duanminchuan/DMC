package com.dream.cutepet;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.wxlib.util.SysUtil;
import com.dream.cutepet.server.ChattingCustomAdviceSample;
import com.dream.cutepet.server.ContactsUICustomSample;
import com.dream.cutepet.server.ConversationListUICustomSample;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class MyApplication extends Application {
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT,
				ChattingCustomAdviceSample.class);
		AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
		AdviceBinder.bindAdvice(PointCutEnum.CONTACTS_UI_POINTCUT, ContactsUICustomSample.class);
		// Application.onCreate中，首先执行这部分代码，以下代码固定在此处，不要改动，这里return是为了退出Application.onCreate！！！
		if (mustRunFirstInsideApplicationOnCreate()) {
			// todo 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
			return;
		}
		// SDK初始化
		final String APP_KEY = "23331616";

		// 必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
		SysUtil.setApplication(this);
		if (SysUtil.isTCMSServiceProcess(this)) {
			return;
		}
		
		// 第一个参数是Application Context
		// 这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
		if (SysUtil.isMainProcess(this)) {
			YWAPI.init(this, APP_KEY);
		}
		
	}

	// 云旺OpenIM的DEMO用到的Application上下文实例
	private static Context sContext;

	public static Context getContext() {
		return sContext;
	}

	private boolean mustRunFirstInsideApplicationOnCreate() {
		// 必须的初始化
		SysUtil.setApplication(this);
		sContext = getApplicationContext();
		return SysUtil.isTCMSServiceProcess(sContext);
	}
}
