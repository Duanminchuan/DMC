package com.dream.cutepet.server;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.appmonitor.tiptool.DensityUtil;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.utility.YWIMImageUtils;
import com.dream.cutepet.R;

/**
 * 会话列表自定义
 * @author Administrator
 *
 */
public class ChattingCustomAdviceSample extends IMChattingPageUI {

	public ChattingCustomAdviceSample(Pointcut pointcut) {
		super(pointcut);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置消息气泡背景图，需要.9图
	 * 
	 * @param conversation
	 *            当前消息所在会话
	 * @param message
	 *            需要设置背景的消息
	 * @param self
	 *            是否是自己发送的消息，true：自己发送的消息， false：别人发送的消息
	 * @return 0: 默认背景 －1:透明背景（无背景） >0:使用用户设置的背景图
	 */

	@Override
	public int getLeftImageMsgBackgroundResId() {
		return R.drawable.chat_ubble;
	}

	@Override
	public int getLeftTextMsgBackgroundResId() {
		return R.drawable.chat_ubble;
	}

	@Override
	public int getRightImageMsgBackgroundResId() {
		return R.drawable.chat_send_background;
	}

	@Override
	public int getRightTextMsgBackgroundResId() {
		return R.drawable.chat_send_background;
	}

	/**
	 * 返回自定义发送消息的文字颜资源Id
	 * 
	 * @return 颜色资源Id
	 */
	@Override
	public int getCustomRightTextColorId() {
		return R.color.chat_send_color;
	}

	/**
	 * 返回自定义接收消息文字颜色资源Id
	 * 
	 * @return 颜色资源Id
	 */
	@Override
	public int getCustomLeftTextColorId() {
		return R.color.chat_from_color;
	}

	@Override
	public int getChattingBackgroundResId() {
		// TODO Auto-generated method stub
		return R.color.background_huise;
	}

	@Override
	public boolean needHideFaceView() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getDefaultHeadImageResId() {
		// TODO Auto-generated method stub
		return R.drawable.icon_tx;
	}
	/**
	 * getView方法内，返回View之前，对［聊天界面的右边消息item的View］做最后调整,如调整View的Padding。
	 * 
	 * @param msg
	 * @param rightItemParentView
	 * @param fragment
	 * @param conversation
	 */
	@Override
	public void modifyRightItemParentViewAfterSetValue(YWMessage msg,
			RelativeLayout rightItemParentView, Fragment fragment,
			YWConversation conversation) {
		Context context = fragment.getActivity();
		if (msg != null
				&& rightItemParentView != null
				&& (msg.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE || msg
						.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF)) {
			rightItemParentView.setPadding(
					rightItemParentView.getPaddingLeft(),
					rightItemParentView.getPaddingTop(),
					DensityUtil.px2dip(context, 39),
					rightItemParentView.getPaddingBottom());
		}
	}

	/**
	 * getView方法内，返回View之前，对［聊天界面的左边消息item的View］做最后调整,如调整View的Padding。
	 * 
	 * @param msg
	 * @param leftItemParentView
	 * @param fragment
	 * @param conversation
	 */
	@Override
	public void modifyLeftItemParentViewAfterSetValue(YWMessage msg,
			RelativeLayout leftItemParentView, Fragment fragment,
			YWConversation conversation) {
		Context context = fragment.getActivity();
		if (msg != null
				&& leftItemParentView != null
				&& (msg.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE || msg
						.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF)) {
			leftItemParentView.setPadding(DensityUtil.px2dip(context, 39),
					leftItemParentView.getPaddingTop(),
					leftItemParentView.getPaddingRight(),
					leftItemParentView.getPaddingBottom());
		}
	}

	/**
	 * 
	 * 用于更灵活地加工［左边图片消息］的Bitmap用于显示，SDK内部会缓存之，后续直接使用缓存的Bitmap显示。例如：对图像进行［裁减］，［
	 * 圆角处理］等等 重要：使用该方法时： 1.请将 {@link needRoundChattingImage}设为return
	 * false(不裁剪圆角)，两者是互斥关系 2.建议将{@link getLeftImageMsgBackgroundResId}
	 * 设为return－1（背景透明）
	 * 
	 * @param input
	 *            网络获取的聊天图片
	 * @return 供显示的Bitmap
	 */
	public Bitmap processBitmapOfLeftImageMsg(Bitmap input) {
		int bestMultipleTimes = getBestMultipleTimes(input.getWidth()
				* input.getHeight());
		int width = input.getWidth() * bestMultipleTimes;
		int height = input.getHeight() * bestMultipleTimes;
		input = Bitmap.createScaledBitmap(input, width, height, false);
		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 为提高性能，对取得的resource图片做缓存
		Bitmap distBitmap = YWIMImageUtils
				.getFromCacheOrDecodeResource(R.drawable.chat_ubble);
		NinePatch np = new NinePatch(distBitmap,
				distBitmap.getNinePatchChunk(), null);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rectSrc = new Rect(0, 0, width, height);
		final RectF rectDist = new RectF(0, 0, width, height);
		np.draw(canvas, rectDist);
		canvas.drawARGB(0, 0, 0, 0);
		// 设置Xfermode
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
		canvas.drawBitmap(input, rectSrc, rectSrc, paint);
		return output;
	}

	private static final int[] THUMNAIL_SIZE = { 91700, 367500 };
	private static final int[] ROUND_RADIUS_MULTIPLE = { 2, 1 };

	/**
	 * 
	 * 智能的根据图片的尺寸计算合适的背景图尺寸
	 * 
	 * @param currentSize
	 * @return
	 */
	public static int getBestMultipleTimes(int currentSize) {
		int min = Math.abs(currentSize - THUMNAIL_SIZE[0]);
		int j = 0;
		for (int i = 1; i < THUMNAIL_SIZE.length; i++) {
			if (Math.abs(currentSize - THUMNAIL_SIZE[i]) < min) {
				min = Math.abs(currentSize - THUMNAIL_SIZE[i]);
				j = i;
			}
		}
		return ROUND_RADIUS_MULTIPLE[j];
	}

	/**
	 * 用于更灵活地加工［右边图片消息］的Bitmap用于显示，SDK内部会缓存之，后续直接使用缓存的Bitmap显示。例如：对图像进行［裁减］，［
	 * 圆角处理］等等 重要：使用该方法时： 1.请将 {@link needRoundChattingImage}设为return
	 * false(不裁剪圆角)，两者是互斥关系 2.建议将{@link getRightImageMsgBackgroundResId}
	 * 设为return－1（背景透明）
	 * 
	 * @param input
	 *            网络获取的聊天图片
	 * @return 供显示的Bitmap
	 */
	public Bitmap processBitmapOfRightImageMsg(Bitmap input) {
		int bestMultipleTimes = getBestMultipleTimes(input.getWidth()
				* input.getHeight());
		int width = input.getWidth() * bestMultipleTimes;
		int height = input.getHeight() * bestMultipleTimes;
		input = Bitmap.createScaledBitmap(input, width, height, false);
		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 为提高性能，对取得的resource图片做缓存
		Bitmap distBitmap = YWIMImageUtils
				.getFromCacheOrDecodeResource(R.drawable.chat_send_background);
		NinePatch np = new NinePatch(distBitmap,
				distBitmap.getNinePatchChunk(), null);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rectSrc = new Rect(0, 0, width, height);
		final RectF rectDist = new RectF(0, 0, width, height);
		np.draw(canvas, rectDist);
		canvas.drawARGB(0, 0, 0, 0);
		// 设置Xfermode
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
		canvas.drawBitmap(input, rectSrc, rectSrc, paint);
		return output;
	}

	/**
	 * isv需要返回自定义的view. openIMSDK会回调这个方法，获取用户设置的view. Fragment 聊天界面的fragment
	 */
	@Override
	public View getCustomTitleView(final Fragment fragment,
			final Context context, LayoutInflater inflater,
			final YWConversation conversation) {
		// 单聊和群聊都会使用这个方法，所以这里需要做一下区分
		// 本demo示例是处理单聊，如果群聊界面也支持自定义，请去掉此判断

		// TODO 重要：必须以该形式初始化view---［inflate(R.layout.**, new
		// RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局**中的高度和宽度无效，均变为wrap_content
		View view = inflater.inflate(R.layout.activity_dialog,
				new RelativeLayout(context), false);
		TextView textTittle = (TextView) view.findViewById(R.id.title);
		ImageView imageBack = (ImageView) view.findViewById(R.id.back);
		textTittle.setTextColor(Color.parseColor("#333333"));
		imageBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				YWIMKit imKit=LoginSampleHelper.getInstance().getIMKit();
				Intent intent = imKit.getConversationActivityIntent();
				fragment.getActivity().startActivity(intent);
//					Intent intent = new Intent(fragment.getActivity(), ChatActivityFragment.class);
//					fragment.getActivity().startActivity(intent);
			}
		});
		return view;
	}
}
