package com.dream.cutepet.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.dream.cutepet.model.ImageBeanModel;

public class MyListUtil {

	/**
	 * 根据指定的标识切割字符，并转换成list
	 * 
	 * @param str
	 * @param split
	 * @return
	 */
	public static List<String> changeStringToList(String str, String split) {
		List<String> data = new ArrayList<String>();
		if (!TextUtils.isEmpty(str) && !str.equals("null")) {
			if (str.indexOf(split) != -1) {// 判断是否只有一条数据
				String[] temp = str.split(split);
				for (String t : temp) {
					data.add(t);
				}
			} else {
				data.add(str);
			}
		}
		return data;
	}

	/**
	 * 将相册的Hashmap对象转换成 List对象
	 * 
	 * @param mGruopMap
	 * @return
	 */
	public static List<ImageBeanModel> subGroupOfImage(
			Map<String, List<String>> mGruopMap) {
		List<ImageBeanModel> list = new ArrayList<ImageBeanModel>();
		if (mGruopMap.size() > 0) {
			Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<String>> entry = it.next();
				ImageBeanModel mImageBean = new ImageBeanModel();
				String key = entry.getKey();
				List<String> value = entry.getValue();
				mImageBean.setFolderName(key);
				mImageBean.setImageCounts(value.size());
				// 获取该组的第一张图片地址
				if (value.size() == 0) {
					mImageBean.setTopImagePath("");
				} else {
					mImageBean.setTopImagePath(value.get(0));
				}
				list.add(mImageBean);
			}
		}
		return list;
	}

}
