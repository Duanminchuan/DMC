package com.dream.cutepet.wxapi;

import com.dream.cutepet.constants.ConstantsWX;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, ConstantsWX.APP_ID, false);
		api.registerApp(ConstantsWX.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq resp) {
		Log.e("onReq", "onReq");
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		Log.e("onResp", "onResp");
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Log.e("onResp", "发送成功");
			Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Log.e("onResp", "分享取消");
			Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			Toast.makeText(this, "分享被拒绝", Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			Log.e("onResp", "分享返回");
			Toast.makeText(this, "分享返回", Toast.LENGTH_LONG).show();
			break;
		}
	}

}
