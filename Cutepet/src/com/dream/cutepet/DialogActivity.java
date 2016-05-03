/*package com.dream.cutepet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.dream.cutepet.adapter.DialogAdapter;
import com.dream.cutepet.model.DialogCharData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

*//**
 * 对话框
 * 
 * @author Administrator
 * 
 *//*
public class DialogActivity extends Activity {
	ViewPager faceViewPager;
	ArrayList<View> viewPagerItems = new ArrayList<View>();
	LinearLayout layout;
	ListView listView;
	Button btnSend;// 发送消息按钮
	DialogAdapter dialogAdapter;// 消息视图的Adapter
	DialogCharData charData;
	List<DialogCharData> list_char = new ArrayList<DialogCharData>();// 消息对象数组
	EditText editText;
	TextView textTittle;
	ImageView imageMenu, imageBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		initView();// 初始化view
		initData();// 初始化数据
	}

	*//**
	 * 初始化控件
	 *//*
	public void initView() {
		textTittle = (TextView) findViewById(R.id.title);
		imageMenu = (ImageView) findViewById(R.id.menu_hide);
		imageBack = (ImageView) findViewById(R.id.back);
		listView = (ListView) findViewById(R.id.listview_dialog);
		btnSend = (Button) findViewById(R.id.btn_send);

		imageMenu.setVisibility(View.INVISIBLE);
		imageBack= (ImageView) findViewById(R.id.back);
		listView = (ListView) findViewById(R.id.listview_dialog);
		btnSend = (Button) findViewById(R.id.btn_send);
		
		textTittle.setText("比蒙宠物");
		textTittle.setTextColor(Color.parseColor("#333333"));
		editText = (EditText) findViewById(R.id.edit_send);

		imageBack.setOnClickListener(clickListener);
		btnSend.setOnClickListener(clickListener);
		listView.setOnItemClickListener(itemClickListener);
	}

	*//**
	 * listView 点击事件
	 *//*
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			editText.clearFocus();
			InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			input.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
			Log.i("taag", "listview");
		}
	};
	*//**
	 * 控件的点击事件
	 *//*
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_send:
				editText.requestFocus();
				editText.setCursorVisible(true);
				send();
				InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				input.hideSoftInputFromWindow(v.getWindowToken(), 0);
				break;
			case R.id.activity_dialog:
				editText.clearFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				break;
			case R.id.edit_send:
				editText.setCursorVisible(true);
				editText.requestFocus();
				break;
			case R.id.back:
				finish();
				break;
			default:
				break;
			}
		}
	};

	*//**
	 * 加载消息
	 *//*
	public void initData() {
		for (int i = 0; i < 2; i++) {
			charData = new DialogCharData();
			if (i % 2 == 0) {
				charData.setComMsg(true);// 收到的消息
				charData.setFromMessage("hello");
			} else {
				charData.setComMsg(false);// 自己发送的消息
				charData.setSendMessage("hi");
			}
			list_char.add(charData);
		}
		dialogAdapter = new DialogAdapter(list_char, this);
		listView.setAdapter(dialogAdapter);
	}

	*//**
	 * 发送消息
	 * 
	 * @throws IOException
	 *//*
	private void send() {
		String msg = editText.getText().toString().trim();
		if (msg != null && !msg.equals("")) {
			charData = new DialogCharData();
			charData.setSendMessage(msg);
			charData.setComMsg(false);
			list_char.add(charData);
			dialogAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
			editText.setText("");// 清空编辑框数据
			listView.setSelection(listView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
		}
	}
}
*/