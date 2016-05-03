package com.dream.cutepet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView {
	private OnMeasureListener onMeasureListener;

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		// 将图片测量的大小回调到onMeasureSize()方法中
		if (onMeasureListener != null) {
			onMeasureListener.onMeasureSize(getMeasuredWidth(), getHeight());
		}
		super.onMeasure(widthMeasureSpec, expandSpec);
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
