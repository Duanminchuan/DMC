package com.dream.cutepet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

public class BitmapUtil extends Activity {
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * 剪切头像并压缩成圆形头像
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressLint("ResourceAsColor")
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;

		if (width <= height) {// 判断是以宽还是以高为基准进行裁剪
			roundPx = width / 2;// 圆心坐标
			top = 0;// 图矩形的top点
			bottom = width;// 图矩形的bottom点
			left = 0;// 图矩形的left点
			right = width;// 图矩形的right点
			height = width;
			dst_left = 0;// 圆矩形的left点
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();

		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);// 定义图片的矩形

		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);// 定义屏幕上图片的矩形

		final RectF rectF = new RectF(dst_left, dst_top, dst_right, dst_bottom);// 定义圆矩形

		paint.setAntiAlias(true);// 设置抗锯齿

		canvas.drawARGB(0, 0, 0, 0);// 清屏

		paint.setColor(0xff424242);// 设置颜色

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 以roudPx为圆心画一个圆

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置图像合成模式
		canvas.drawBitmap(bitmap, src, dst, paint);// 在画布上画图
		// 第一个Rect 代表要绘制的bitmap 区域，第二个 Rect 代表的是要将bitmap 绘制在屏幕的什么地方

		// 得到图片原始的高宽
		int roundHeight = output.getHeight();
		int roundWidth = output.getWidth();
		// 设定图片新的高宽
		int newHeight = 80;
		int newWidth = 80;
		// 计算缩放因子
		float heightScale = ((float) newHeight) / roundHeight;
		float widthScale = ((float) newWidth) / roundWidth;
		// 新建立矩阵
		Matrix matrix = new Matrix();
		matrix.postScale(heightScale, widthScale);
		// 设置图片的旋转角度
		// matrix.postRotate(-30);
		// 设置图片的倾斜
		// matrix.postSkew(0.1f, 0.1f);
		// 将图片大小压缩
		// 压缩后图片的宽和高以及kB大小均会变化
		Bitmap newBitmap = Bitmap.createBitmap(output, 0, 0, roundWidth,
				roundHeight, matrix, true);
		return newBitmap;
	}

	/**
	 * 将图片进行比例压缩
	 * 
	 * @param srcPath
	 *            图片的路径
	 * @param img_height
	 *            压缩后的图片高
	 * @param img_width
	 *            压缩后的图片宽
	 * @return 压缩好的bitmap型图片
	 */
	public static Bitmap getimage(String srcPath, float img_height,
			float img_width) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = img_height;
		float ww = img_width;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 将图片进行比例压缩
	 * 
	 * @param image
	 *            bitmap型的图片
	 * @param img_height
	 *            压缩后的图片的高
	 * @param img_width
	 *            压缩后的图片的宽
	 * @return 压缩好的 bitmap型图片
	 */
	public static Bitmap comp(Bitmap image, float img_height, float img_width) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 100) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 20, baos);// 这里压缩20%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = img_height;
		float ww = img_width;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		// 设置缩放比例
		newOpts.inSampleSize = be;
		// 降低图片从ARGB888到RGB565
		newOpts.inPreferredConfig = Config.RGB_565;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		// 压缩好比例大小后再进行质量压缩
		return compressImage(bitmap);
	}

	/**
	 * 压缩图片的质量
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			// 重置baos即清空baos
			baos.reset();
//			// 每次都减少10
//			options -= 10;
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * 将bitmap型图片暂存为file对象
	 * 
	 * @param bm
	 *            bitmap对象图片
	 * @param bitName
	 * @return
	 * @throws IOException
	 */
	public static File saveMyBitmap(Bitmap bm) {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		File file = new File(sdDir.getPath() + File.separator + "101temp.png");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 删除暂存的图片文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			}
			file.delete();
		}
	}
	
	/**
	 * 获取bitmap型图片
	 * 
	 * @param pathString
	 *            图片的sd卡地址
	 * @return
	 */
	public static Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
		}
		return bitmap;
	}

}
