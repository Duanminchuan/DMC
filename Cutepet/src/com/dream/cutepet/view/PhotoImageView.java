package com.dream.cutepet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PhotoImageView extends ImageView {
	
	public PhotoImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PhotoImageView(Context context) {
		super(context);
	}

	public PhotoImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(widthSize, widthSize);
	}
}
