package com.dream.cutepet.cache;

import java.io.File;
import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;

/**
 * 图片文件缓存
 * 
 * @author Jinyun Hou
 * 
 */
public class FileCache {

	/** 缓存文件目录 */
	private File mCacheDir;

	/**
	 * 创建缓存文件目录，如果有SD卡，则使用SD，如果没有则使用系统自带缓存目录
	 * 
	 * @param context
	 * @param cacheDir
	 *            图片缓存的一级目录
	 * @param dir
	 */
	public FileCache(Context context, File cacheDir, String dir) {
		mCacheDir = new File(cacheDir, dir);

		if (!mCacheDir.exists())
			mCacheDir.mkdirs();
	}

	@SuppressWarnings("deprecation")
	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		// String filename=String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		String filename = URLEncoder.encode(url);
		File f = new File(mCacheDir, filename);
		Log.e("文件缓存", "path = " + f.getPath());
		return f;
	}

	/**
	 * 清除缓存文件
	 */
	public void clear() {
		File[] files = mCacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

}