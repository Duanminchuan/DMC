package com.dream.cutepet;

import java.util.ArrayList;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.login.YWLoginState;
import com.dream.cutepet.constants.ConstantsWB;
import com.dream.cutepet.constants.ConstantsWX;
import com.dream.cutepet.server.LoginSampleHelper;
import com.dream.cutepet.util.AccessTokenKeeper;
import com.dream.cutepet.util.BitmapUtil;
import com.dream.cutepet.util.GesturesUtil;
import com.dream.cutepet.util.SharedPreferencesUtil;
import com.dream.cutepet.util.Util;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 设置
 * 
 * @author Administrator
 * 
 */
public class SetActivity extends Activity {

	ImageView back;
	LinearLayout linearlayout_share;
	TextView title;

	RadioButton button_weixin;
	RadioButton button_qq;
	RadioButton button_weibo;
	RadioButton button_qqkongjian;
	RadioButton button_pnegyouquan;
	Button button_cancel;

	PopupWindow popupWindow;

	Builder builder;
	AlertDialog alertDialog;
	LinearLayout linear_out;
	ToggleButton toggleButton;
	/** 微博微博分享接口实例 */
	private IWeiboShareAPI mWeiboShareAPI = null;
	WindowManager.LayoutParams lp;
	private static String appId;
	private Tencent mTencent;
	QzoneShare qzoneShare;
	QQShare qqShare;

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		appId = "1105249013";
		// 初始化分享代码
		mTencent = Tencent.createInstance(appId, this);
		qzoneShare = new QzoneShare(getApplicationContext(),
				mTencent.getQQToken());
		qqShare = new QQShare(getApplicationContext(), mTencent.getQQToken());
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK
				.createWeiboAPI(this, ConstantsWB.APP_KEY);
		// 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
		// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
		// NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
		mWeiboShareAPI.registerApp();
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(ocl);

		linearlayout_share = (LinearLayout) findViewById(R.id.linearlayout_share);
		linearlayout_share.setOnClickListener(ocl);

		linear_out = (LinearLayout) findViewById(R.id.linear_out);
		linear_out.setOnClickListener(ocl);
		if (checkisLogin()) {
			linear_out.setVisibility(View.VISIBLE);
		} else {
			linear_out.setVisibility(View.GONE);
		}

		title = (TextView) findViewById(R.id.title);
		title.setText("设置");

		lp = getWindow().getAttributes();
		toggleButton = (ToggleButton) findViewById(R.id.ToggleButton);
		toggleButton.setOnClickListener(ocl);
		if (!GesturesUtil.hasPassword(getApplicationContext())) {
			toggleButton.setChecked(false);
		}
		toggleButton.setChecked(GesturesUtil
				.IsGestures(getApplicationContext()));

		builder = new AlertDialog.Builder(this);
		alertDialog = builder.create();

		api = WXAPIFactory.createWXAPI(this, ConstantsWX.APP_ID, false);
		api.registerApp(ConstantsWX.APP_ID);
	}

	OnClickListener ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.linearlayout_share:
				// 设置背景颜色变暗
				lp.alpha = 0.3f;
				getWindow().setAttributes(lp);
				popWindow();
				break;
			case R.id.button_cancel:
				// 销毁弹出框
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				popupWindow.dismiss();
				break;
			case R.id.linear_out:
				creatAlertDialog();
				break;
			case R.id.ToggleButton:
				if (checkisLogin()) {
					setGestures();
				} else {
					toggleButton.setChecked(false);
				}
				break;
			case R.id.button_weibo:
				// 微博分享
				shareSinWeiBo();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				popupWindow.dismiss();
				break;
			case R.id.button_qq:
				// QQ分享
				shareQQ();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				popupWindow.dismiss();
				break;
			case R.id.button_qqkongjian:
				// QQ空间分享
				shareWebPageQzone();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				popupWindow.dismiss();
				break;
			case R.id.button_weixin:
				// 微信分享
				shareSinWeiXin();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				popupWindow.dismiss();
				break;
			case R.id.button_pnegyouquan:
				// 微信朋友圈分享
				shareWXFriend();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				popupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 微信朋友圈分享
	 */
	private void shareWXFriend() {
		// 初始化一个WXWebpageObject对象
		WXWebpageObject webpageObject = new WXWebpageObject();
		// 设置分享的页面url
		webpageObject.webpageUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.dream.cutepet";

		// 用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题，描述
		WXMediaMessage message = new WXMediaMessage(webpageObject);
		message.title = "CutePet 应用链接";
		message.description = "CutePet是款生活、资讯类型的app应用,主要为用户提供跟宠物相关的资讯信息。在CutePet中你可查看萌宠的资讯。可以在上面进行聊天、留言、评论、发表说说等功能。";
		// 设置分享的图片
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo);
		message.thumbData = BitmapUtil.bmpToByteArray(bitmap, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = message;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}

	/**
	 * 分享微信
	 */
	private void shareSinWeiXin() {
		// 初始化一个WXWebpageObject对象
		WXWebpageObject webpageObject = new WXWebpageObject();
		// 设置分享的页面url
		webpageObject.webpageUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.dream.cutepet";

		// 用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题，描述
		WXMediaMessage message = new WXMediaMessage(webpageObject);
		message.title = "CutePet 应用链接";
		message.description = "CutePet是款生活、资讯类型的app应用,主要为用户提供跟宠物相关的资讯信息。在CutePet中你可查看萌宠的资讯。可以在上面进行聊天、留言、评论、发表说说等功能。";
		// 设置分享的图片
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo);
		message.thumbData = BitmapUtil.bmpToByteArray(bitmap, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = message;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/**
	 * 设置手势密码
	 */
	private void setGestures() {
		if (GesturesUtil.hasPassword(getApplicationContext())) {
			// 如果设置了密码，才可以开启锁屏
			GesturesUtil.setGesturesUser(toggleButton.isChecked(),
					getApplicationContext());
			if (toggleButton.isChecked()) {
				Toast.makeText(getApplicationContext(), "开启锁屏成功！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "关闭锁屏成功！",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "未设置锁屏密码！",
					Toast.LENGTH_SHORT).show();
			toggleButton.setChecked(false);
		}
	}

	/**
	 * QQ空间分享
	 */
	private void shareWebPageQzone() {
		final Bundle params = new Bundle();
		int test = R.string.share_app;
		String test_demo = getString(test);
		int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "CutePet 应用链接");
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, test_demo);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"http://a.app.qq.com/o/simple.jsp?pkgname=com.dream.cutepet");
		params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,
				QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls
				.add("http://211.149.198.8:9805/Public/image/upload/2016-03-02/logo.png");
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		doShareToQzone(params);
	}

	/**
	 * QQ分享
	 */
	private void shareQQ() {
		Bundle params = new Bundle();
		int test = R.string.share_app;
		String test_demo = getString(test);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, "CutePet 应用链接");
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, test_demo);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "CutePet");
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"http://a.app.qq.com/o/simple.jsp?pkgname=com.dream.cutepet");
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
				"http://211.149.198.8:9805/Public/image/upload/2016-03-02/logo.png");
		doShareToQQ(params);
	}

	/**
	 * 用异步方式启动分享
	 * 
	 * @param params
	 */
	private void doShareToQzone(final Bundle params) {
		// QZone分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (qzoneShare != null) {
					qzoneShare.shareToQzone(SetActivity.this, params,
							qZoneShareListener);
				}
			}
		});
	}

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				if (mTencent != null) {
					mTencent.shareToQQ(SetActivity.this, params,
							qZoneShareListener);
				}
			}
		});
	}

	/**
	 * 回调
	 */
	IUiListener qZoneShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			Util.toastMessage(SetActivity.this, "分享完成");
		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(SetActivity.this, "分享失败：" + e.errorMessage, "e");
			// 分享错误
		}

		@Override
		public void onComplete(Object response) {
			Util.toastMessage(SetActivity.this, "分享成功");
		}

	};
	/**
     * 创建多媒体（网页）消息对象。
     * @return 多媒体（网页）消息对象。
    */
    private WebpageObject getWebpageObj() {
            WebpageObject mediaObject = new WebpageObject();
            mediaObject.actionUrl = ("http://a.app.qq.com/o/simple.jsp?pkgname=com.dream.cutepet");
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = ("CutePet 应用链接");
            mediaObject.description = getString(R.string.share_app);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                            R.drawable.logo);
            mediaObject.setThumbImage(bmp);
            return mediaObject;
    }
	/**
	 * 微博分享
	 */
	protected void shareSinWeiBo() {
		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.mediaObject = getWebpageObj();
		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		AuthInfo authInfo = new AuthInfo(this, ConstantsWB.APP_KEY,
				ConstantsWB.REDIRECT_URL, ConstantsWB.SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper
				.readAccessToken(getApplicationContext());
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}
		mWeiboShareAPI.sendRequest(this, request, authInfo, token,
				new WeiboAuthListener() {

					@Override
					public void onWeiboException(WeiboException arg0) {
					}

					@Override
					public void onComplete(Bundle bundle) {
						Oauth2AccessToken newToken = Oauth2AccessToken
								.parseAccessToken(bundle);
						AccessTokenKeeper.writeAccessToken(
								getApplicationContext(), newToken);
					}

					@Override
					public void onCancel() {
					}
				});
	}
	/**
	 * 底部弹出框popupWindow
	 */
	@SuppressLint("InflateParams")
	public void popWindow() {
		View view = getLayoutInflater().inflate(R.layout.activity_popupwindow,
				null);

		// 获取屏幕宽度
		WindowManager wm = this.getWindowManager();
		@SuppressWarnings("deprecation")
		int width = wm.getDefaultDisplay().getWidth();
		// int height = wm.getDefaultDisplay().getHeight();

		popupWindow = new PopupWindow(view, width, 400, true);

		button_weixin = (RadioButton) view.findViewById(R.id.button_weixin);
		button_qq = (RadioButton) view.findViewById(R.id.button_qq);
		button_weibo = (RadioButton) view.findViewById(R.id.button_weibo);
		button_qqkongjian = (RadioButton) view
				.findViewById(R.id.button_qqkongjian);
		button_pnegyouquan = (RadioButton) view
				.findViewById(R.id.button_pnegyouquan);
		button_cancel = (Button) view.findViewById(R.id.button_cancel);

		button_weixin.setOnClickListener(ocl);
		button_qq.setOnClickListener(ocl);
		button_weibo.setOnClickListener(ocl);
		button_qqkongjian.setOnClickListener(ocl);
		button_pnegyouquan.setOnClickListener(ocl);
		button_cancel.setOnClickListener(ocl);
		// 显示位置
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < 400) {
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
						popupWindow.dismiss();
					}
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void creatAlertDialog() {
		alertDialog.setTitle("退出登录");
		alertDialog.setMessage("你确定要退出登录吗？");
		alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		alertDialog.setButton("Confirm",
				new android.content.DialogInterface.OnClickListener() {

					@SuppressWarnings("static-access")
					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						Toast.makeText(SetActivity.this, "I'am sure",
								Toast.LENGTH_SHORT).show();
						SharedPreferencesUtil exit = new SharedPreferencesUtil();
						exit.deleteData(getApplicationContext());
						logout();
						finish();
					}
				});
		alertDialog.setButton2("Cancel",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						Toast.makeText(SetActivity.this, "it is a joke",
								Toast.LENGTH_SHORT).show();

					}
				});

		alertDialog.show();
	}

	/**
	 * 判断 是否 处于登录 状态
	 * 
	 * @return 登录，返回true
	 */
	private boolean checkisLogin() {
		String result = SharedPreferencesUtil.getData(getApplicationContext());
		if (result == null || result.equals("")) {// 判断获取的token值是否为空
			Toast.makeText(getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			return true;
		}
	}

	// 退出阿里云聊天
	public void logout() {
		// openIM SDK提供的登录服务
		YWIMKit mIMKit = LoginSampleHelper.getInstance().getIMKit();
		if (mIMKit == null) {
			return;
		}
		IYWLoginService mLoginService = mIMKit.getLoginService();
		mLoginService.logout(new IWxCallback() {
			// 此时logout已关闭所有基于IMBaseActivity的OpenIM相关Actiivity，s
			@Override
			public void onSuccess(Object... arg0) {
				LoginSampleHelper.getInstance().setAutoLoginState(
						YWLoginState.idle);
			}

			@Override
			public void onProgress(int arg0) {

			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}
}
