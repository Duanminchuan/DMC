package com.dream.cutepet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义Imageview控件，对宽高进行测量并回调，通过 setOnMeasureListener监听事件
 * 
 * @author 浅念丶往事如梦
 * 
 */
public class MyAlbumImageView extends ImageView {
	private OnMeasureListener onMeasureListener;

	public MyAlbumImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyAlbumImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 将图片测量的大小回调到onMeasureSize()方法中
		if (onMeasureListener != null) {
			onMeasureListener.onMeasureSize(getMeasuredWidth(), getHeight());
		}
	}

	/**
	 * 自定义 控件的宽高 的监听事件
	 * 
	 * @author 浅念丶往事如梦
	 * 
	 */
	public interface OnMeasureListener {
		public void onMeasureSize(int width, int height);
	}

	/**
	 * 设置 imageview对长宽的监听事件
	 * 
	 * @param onMeasureListener
	 */
	public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
		this.onMeasureListener = onMeasureListener;
	}

}
