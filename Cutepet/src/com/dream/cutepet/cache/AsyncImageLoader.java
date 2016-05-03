package com.dream.cutepet.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dream.cutepet.util.BitmapUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * 寮傛鍔犺浇鍥剧墖璧勬簮
 * 
 * @author Jinyun.Hou
 * 
 */
public class AsyncImageLoader {

	/** 内存缓存 */
	private MemoryCache mMemoryCache;
	/** 文件缓存 */
	private FileCache mFileCache;
	/** 线程池 */
	private ExecutorService mExecutorService;

	/**
	 * 是否加载圆形图片，加载圆形选择false
	 */
	private boolean flog;

	private Map<ImageView, String> mImageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	/** 保存正在加载图片的url */
	private List<LoadPhotoTask> mTaskQueue = new ArrayList<LoadPhotoTask>();

	/**
	 * 默认采用一个大小为5的线程池
	 * 
	 * @param context
	 * @param memoryCache
	 *            所采用的高速缓存
	 * @param fileCache
	 *            所采用的文件缓存
	 */
	public AsyncImageLoader(Context context, MemoryCache memoryCache,
			FileCache fileCache) {
		mMemoryCache = memoryCache;
		mFileCache = fileCache;
		mExecutorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * 根据url加载相应的图片
	 * 
	 * @param url
	 * @param flag
	 * @return 濡傛灉缂撳瓨閲屾湁鍒欑洿鎺ヨ繑鍥烇紝濡傛灉娌℃湁鍒欏紓姝ヤ粠鏂囦欢鎴栫綉缁滅鑾峰彇
	 */
	public Bitmap loadBitmap(ImageView imageView, String url, boolean flag) {
		this.flog = flag;
		Bitmap bitmap = null;
		//判断sd卡中是否有，有则加载
		File f = mFileCache.getFile(url);
		if(!f.exists()){
			mImageViews.put(imageView, url);
			
			bitmap = mMemoryCache.get(url);
			if (bitmap == null) {
				Log.i("bitmap", "加入图片下载队列");
				enquequeLoadPhoto(url, imageView);
			}
		}
		bitmap = ImageUtil.decodeFile(f);
		return bitmap;
	}

	/**
	 * 加入图片下载队列
	 * 
	 * @param url
	 */
	private void enquequeLoadPhoto(String url, ImageView imageView) {
		// 濡傛灉浠诲姟宸茬粡瀛樺湪锛屽垯涓嶉噸鏂版坊鍔�
		if (isTaskExisted(url))
			return;
		LoadPhotoTask task = new LoadPhotoTask(url, imageView);
		synchronized (mTaskQueue) {
			mTaskQueue.add(task);
		}
		mExecutorService.submit(task);
	}

	/**
	 * 判断下载队列中是否已经存在该任务
	 * 
	 * @param url
	 * @return
	 */
	private boolean isTaskExisted(String url) {
		if (url == null)
			return false;
		synchronized (mTaskQueue) {
			int size = mTaskQueue.size();
			for (int i = 0; i < size; i++) {
				LoadPhotoTask task = mTaskQueue.get(i);
				if (task != null && task.getUrl().equals(url))
					return true;
			}
		}
		return false;
	}

	/**
	 * 从缓存文件或者网络端获取图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapByUrl(String url) {
		File f = mFileCache.getFile(url);
		Bitmap b = ImageUtil.decodeFile(f);
		if (b != null)
			return b;
		return ImageUtil.loadBitmapFromWeb(url, f);
	}

	/**
	 * 判断该ImageView是否已经被复用
	 * 
	 * @param imageView
	 * @param url
	 * @return
	 */
	private boolean imageViewReused(ImageView imageView, String url) {
		String tag = mImageViews.get(imageView);
		if (tag == null || !tag.equals(url))
			return true;
		return false;
	}

	private void removeTask(LoadPhotoTask task) {
		synchronized (mTaskQueue) {
			mTaskQueue.remove(task);
		}
	}

	class LoadPhotoTask implements Runnable {
		private String url;
		private ImageView imageView;

		LoadPhotoTask(String url, ImageView imageView) {
			this.url = url;
			this.imageView = imageView;
		}

		@Override
		public void run() {
			if (imageViewReused(imageView, url)) {
				removeTask(this);
				return;
			}
			Bitmap bmp = getBitmapByUrl(url);
			mMemoryCache.put(url, bmp);
			if (imageViewReused(imageView, url)) {
				removeTask(this);
				return;
			}
			removeTask(this);
			BitmapDisplayer bd = new BitmapDisplayer(bmp, imageView, url);
			Activity a = (Activity) imageView.getContext();
			a.runOnUiThread(bd);
		}

		public String getUrl() {
			return url;
		}
	}

	/**
	 * Used to display bitmap in the UI thread
	 * 
	 */
	class BitmapDisplayer implements Runnable {
		private Bitmap bitmap;
		private ImageView imageView;
		private String url;

		public BitmapDisplayer(Bitmap b, ImageView imageView, String url) {
			bitmap = b;
			this.imageView = imageView;
			this.url = url;
		}

		public void run() {
			if (imageViewReused(imageView, url))
				return;
			if (bitmap != null){
				if (flog) {
					imageView.setImageBitmap(bitmap);
				} else {
					imageView.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));
				}
			}
		}
	}

	/**
	 * 释放资源
	 */
	public void destroy() {
		mMemoryCache.clear();
		mMemoryCache = null;
		mImageViews.clear();
		mImageViews = null;
		mTaskQueue.clear();
		mTaskQueue = null;
		mExecutorService.shutdown();
		mExecutorService = null;
	}
}