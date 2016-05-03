package com.dream.cutepet.view;

import java.util.ArrayList;
import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.model.PointModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RegistureGesturesView extends View {
	List<PointModel> point;
	List<Integer> point_num = new ArrayList<Integer>();

	// 圆的半径
	float r = 8;
	// 定义边距
	float margin_v = r;
	float margin_h = r;
	float padding_v = r;
	float padding_h = r;

	Paint paint_cicle;
	Paint paint_line;
	Paint paint_text;
	Paint paint_newCicle;
	Paint paint_newCicle_Point;

	float startX_new, startY_new, stopX_new, stopY_new;

	/**
	 * 通过密码 重绘
	 * 
	 * @param point_num
	 */
	public void setPoint_num(List<Integer> point_num) {
		this.point_num = point_num;
		invalidate();
	}

	/**
	 * 获取手势密码集合
	 * 
	 * @return
	 */
	public List<Integer> getPoint_num() {
		return point_num;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) r * 10, (int) r * 10);
	}

	@SuppressLint("DrawAllocation") public RegistureGesturesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 设置圆的画笔
		paint_cicle = new Paint();
		paint_cicle.setStrokeWidth(2);
		// 设置绘制风格
		paint_cicle.setStyle(Paint.Style.STROKE);
		paint_cicle.setColor(getResources().getColor(
				android.R.color.darker_gray));
		// 设置是否抗锯齿
		paint_cicle.setAntiAlias(true);

		// 设置圆的画笔
		paint_newCicle = new Paint();
		// 设置绘制风格
		paint_newCicle.setColor(getResources().getColor(R.color.newCicle));
		// 设置是否抗锯齿
		paint_newCicle.setAntiAlias(true);

		// 设置圆的画笔
		paint_newCicle_Point = new Paint();
		// 设置绘制风格
		paint_newCicle_Point.setColor(getResources().getColor(
				android.R.color.darker_gray));
		// 设置是否抗锯齿
		paint_newCicle_Point.setAntiAlias(true);

		paint_line = new Paint();
		paint_line.setStrokeWidth(4);
		paint_line.setColor(getResources().getColor(R.color.newCicle));
		// 设置是否抗锯齿
		paint_line.setAntiAlias(true);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 每次绘制时，都重新new一个圆的集合对象，防止数据重复添加
		point = new ArrayList<PointModel>();
		// 纵向的添加标记量
		int count_y = 0;
		// 横向的添加表计量
		int count_x = 0;
		// 循环绘制9个圆
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				count_y++;
				count_x = 0;
			} else {
				count_x++;
			}
			float point_x = margin_h + r + (2 * r + padding_h) * count_x;
			float point_y = margin_v + r + (2 * r + padding_v) * (count_y - 1);

			canvas.drawCircle(point_x, point_y, r, paint_cicle);
			canvas.drawCircle(point_x, point_y, 4, paint_newCicle_Point);

			// 每次绘制一个圆，则向集合中添加该圆的属性，圆的位置对应（i+1）的值
			PointModel model = new PointModel(point_x, point_y, r);
			point.add(model);
		}

		// 绘制 经过的实心圆
		for (int a = 0; a < point_num.size(); a++) {
			float x_x = point.get(point_num.get(a)).getPoint_x();
			float x_y = point.get(point_num.get(a)).getPoint_y();
			canvas.drawCircle(x_x, x_y, r, paint_newCicle);
		}

		for (int m = 0; m < point_num.size() - 1; m++) {
			startX_new = point.get(point_num.get(m)).getPoint_x();
			startY_new = point.get(point_num.get(m)).getPoint_y();
			stopX_new = point.get(point_num.get(m + 1)).getPoint_x();
			stopY_new = point.get(point_num.get(m + 1)).getPoint_y();
			canvas.drawLine(startX_new, startY_new, stopX_new, stopY_new,
					paint_line);
		}
	}
}
