package com.dream.cutepet.view;

import java.util.ArrayList;
import java.util.List;

import com.dream.cutepet.R;
import com.dream.cutepet.model.PointModel;
import com.dream.cutepet.util.GesturesUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 自定义 的 手势密码 控件
 * 
 * @author 浅念丶往事如梦
 * 
 */
public class MyGesturesView extends View {
	Paint paint_cicle;
	Paint paint_line;
	Paint paint_newCicle;
	Paint paint_newCicle_Point;

	private List<PointModel> point;
	private List<Integer> point_num = new ArrayList<Integer>();

	float width;
	// 圆的半径
	float r;
	// 定义边距
	float margin_v;
	float margin_h;
	float padding_v;
	float padding_h;

	float startX, startY, stopX, stopY;
	float startX_new, startY_new, stopX_new, stopY_new;

	boolean flog = false;

	/**
	 * 通过密码 重绘
	 * 
	 * @param point_num
	 */
	public void setPoint_num() {
		point_num = new ArrayList<Integer>();
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// 获取屏幕长宽
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		float gm_width = wm.getDefaultDisplay().getWidth();
		width = gm_width;

		if (widthSize >= heightSize) {
			widthSize = heightSize;
		} else {
			heightSize = widthSize;
		}

		r = (float) (width / 10);
		margin_v = (float) (r / 1.5);
		margin_h = (float) (r / 1.5);
		padding_v = (float) (r / 1.5);
		padding_h = (float) (r / 1.5);

		// 获取 当前的绝对长度
		float s = r * 6 + 4 * margin_v;
		// 向上取整
		int length = (int) Math.ceil(s);

		setMeasuredDimension(length, length);
	}

	public MyGesturesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 设置圆的画笔
		paint_cicle = new Paint();
		paint_cicle.setStrokeWidth(5);
		// 设置绘制风格
		paint_cicle.setStyle(Paint.Style.STROKE);
		paint_cicle.setColor(getResources().getColor(android.R.color.black));
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
		paint_newCicle_Point.setStrokeWidth(20);
		// 设置绘制风格
		paint_newCicle_Point.setColor(getResources().getColor(
				R.color.newCicle_Point));
		// 设置是否抗锯齿
		paint_newCicle_Point.setAntiAlias(true);

		paint_line = new Paint();
		paint_line.setStrokeWidth(10);
		paint_line.setColor(getResources().getColor(R.color.newCicle_Point));
		// 设置是否抗锯齿
		paint_line.setAntiAlias(true);

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
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
			// canvas.drawCircle(point_x, point_y, 6, new Paint());

			// 每次绘制一个圆，则向集合中添加该圆的属性，圆的位置对应（i+1）的值
			PointModel model = new PointModel(point_x, point_y, r);
			point.add(model);
		}

		// 绘制 经过的实心圆
		for (int a = 0; a < point_num.size(); a++) {
			float x_x = point.get(point_num.get(a)).getPoint_x();
			float x_y = point.get(point_num.get(a)).getPoint_y();
			canvas.drawCircle(x_x, x_y, r, paint_newCicle);
			canvas.drawCircle(x_x, x_y, 20, paint_newCicle_Point);
		}

		// 线条一：循环绘制移动到圆的线，并连接
		for (int m = 0; m < point_num.size() - 1; m++) {
			startX_new = point.get(point_num.get(m)).getPoint_x();
			startY_new = point.get(point_num.get(m)).getPoint_y();
			stopX_new = point.get(point_num.get(m + 1)).getPoint_x();
			stopY_new = point.get(point_num.get(m + 1)).getPoint_y();
			canvas.drawLine(startX_new, startY_new, stopX_new, stopY_new,
					paint_line);
		}

		// 线条二：绘制每次最后一次经过的圆的圆心到鼠标移动的位置的线条
		if (flog) {
			if (!point_num.isEmpty()) {
				canvas.drawLine(point.get(point_num.get(point_num.size() - 1))
						.getPoint_x(),
						point.get(point_num.get(point_num.size() - 1))
								.getPoint_y(), stopX, stopY, paint_line);
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int num;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			point_num.clear();
			// 获取判断是否经过圆的方法返回的值
			num = GesturesUtil.PassCircle(x, y, point);
			if (num != -1) {
				// 如果该值不为-1，则返回的是经过圆的位置值
				point_num.add(num);
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			// 获取判断是否经过圆的方法返回的值
			num = GesturesUtil.PassCircle(x, y, point);
			if (num != -1) {
				// 如果该值不为-1，则返回的是经过圆的位置值
				if (!GesturesUtil.hasNumber(num, point_num)) {
					point_num.add(num);
				}
			}
			flog = false;
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			// 获取判断是否经过圆的方法返回的值
			num = GesturesUtil.PassCircle(x, y, point);
			if (num != -1) {
				// 如果该值不为-1，则返回的是经过圆的位置值
				if (!GesturesUtil.hasNumber(num, point_num)) {
					point_num.add(num);
					invalidate();
				}
			}
			flog = true;
			stopX = x;
			stopY = y;
			invalidate();
			break;

		default:
			break;
		}
		return true;
	}

}
