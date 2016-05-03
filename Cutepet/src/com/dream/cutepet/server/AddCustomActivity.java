package com.dream.cutepet.server;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import android.app.Activity;
import android.os.Bundle;

public class AddCustomActivity extends Activity {
	IYWContactService contactService;
	String apkey = "23331616";
	String userId;
	String mRemarkName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
		contactService = imKit.getContactService();

	}

	/**
	 * 添加联系人
	 */
	@SuppressWarnings("unused")
	private void addCustom() {
		String remark = null;
		IWxCallback callback = new IWxCallback() {

			@Override
			public void onSuccess(Object... arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		};
		contactService.addContact(userId, apkey, mRemarkName,remark,callback);
	}
/**
 * 回应添加联系人好友
 */
	@SuppressWarnings("unused")
	private void backMessage() {
		String mRetMsg = null;
		IWxCallback callback = new IWxCallback() {
			@Override
			public void onSuccess(Object... arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		};
		contactService.ackAddContact(userId, apkey, true, mRetMsg, callback);
	}
}
