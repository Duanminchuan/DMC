package com.dream.cutepet.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 接口工具类
 * 
 * @author 段闽川
 * 
 */
public class HttpTools {

	// public static String HTTP_HOST =
	// "http://211.149.198.8:9805/index.php/home/api/";
	public static String HTTP_HOST = "http://211.149.198.8:9805/index.php/home/api/";
	public static String LOGIN_URL = "login";
	public static String REGISTER_URL = "register";
	public static String VERIFY_URL = "verify";
	public static String TOKEN_URL = "token";

	String verify;
	String register;
	String login;
	String message;
	String userdata;
	String demand;

	public int status = 0;

	/**
	 * 回调方法
	 * 
	 * @param mListener
	 */
	/*
	 * 登录
	 */
	public void LoginAccount(String tel, String password) {
		new AnyTask().execute(tel, password);
	}

	/*
	 * 获取验证码
	 */
	public void VerifyAccount(String tel, String verify) {
		new verifyTask().execute(tel, verify);
	}

	/*
	 * 注册
	 */
	public void RegisterAccount(String tel, String verify, String password) {
		new registerTask().execute(tel, verify, password);
	}

	/*
	 * 用户资料
	 */
	/*
	 * public void UserdataAccount(String tel, String token) { new
	 * userdataTask().execute(tel, token); }
	 */

	/*
	 * 用户资料查询
	 */
	public void DemandAccount(String tel, String token) {
		new registerTask().execute(tel, token);
	}

	/*
	 * 登录接口
	 */
	@SuppressWarnings("unused")
	private String postconn(String tel, String password) throws JSONException {
		StringBuilder builder = new StringBuilder();// 初始化一个StrngBuilder的对象
		String httpHost = "http://211.149.198.8:9805/index.php/home/api/login";
		String urltel = "tel=";
		String passwordkey = "password=";
		try {
			String passwordString = URLEncoder.encode(password, "utf-8");
			String oldString = URLEncoder.encode(tel, "utf-8");
			String urlName = httpHost + "?" + urltel + tel + "&" + passwordkey + password;// url资源地址
			URL url = new URL(urlName);// 根据url地址创建一个url的对象

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");// get方法获取
			connection.setConnectTimeout(5000);
			connection.connect();

			if (connection.getResponseCode() == 200) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = bufferedReader.readLine();
				while (line != null && line.length() > 0) {
					builder.append(line);
					line = bufferedReader.readLine();// 读取出来
					login = builder.toString();
				}
				login = builder.toString();

				/*
				 * JSONObject object = new JSONObject(login); message =
				 * object.getString("message"); status =
				 * object.getInt("status");
				 */
				/*
				 * 关闭流
				 */
				inputStream.close();
				bufferedReader.close();
				Log.i("message=====>", login);
				return login;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		login = "error";
		return login;

	}

	/*
	 * 自定义登录监听
	 */
	OnHttpListener mListener;

	public void setOnHttpListener(OnHttpListener mListener) {
		this.mListener = mListener;
	}

	/*
	 * 异步
	 */
	class AnyTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {

			try {
				return postconn(arg0[0], arg0[1]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return login;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mListener != null) {
				mListener.end(result);
			}
		}
	}

	public interface OnHttpListener {
		void start();

		void end(String result);

	}

	public String getMessage() {

		return message;
	}

	/*
	 * 获取验证码接口
	 */
	private String getverify(String tel) throws ProtocolException, IOException {
		StringBuilder builder = new StringBuilder();
		String httpHost = "http://211.149.198.8:9805/index.php/home/api/verify";
		String urltel = "tel=";

		String urlName = httpHost + "?" + urltel + tel;// url资源地址

		try {
			URL url = new URL(urlName); // 根据url地址创建一个url的对象

			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.connect();
			connection.getInputStream();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while (line != null && line.length() > 0) {
				builder.append(line);
				line = bufferedReader.readLine();// 读取出来

				verify = builder.toString();

				Log.i("verify=====>", verify);

				/*
				 * 关闭流
				 */
				inputStream.close();
				bufferedReader.close();
				return verify;

			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		verify = "error";
		return verify;
	}

	/*
	 * 自定义获取验证码监听
	 */
	OnverifyListener verifyListener;

	public void setOnverifyListener(OnverifyListener verifyListener) {
		this.verifyListener = verifyListener;
	}

	class verifyTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... arg0) {
			try {
				return getverify(arg0[0]);
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return verify;
		}

		protected void onPostExecute(String result) {
			if (verifyListener != null) {
				verifyListener.end(result);
			}
		}
	}

	public interface OnverifyListener {
		void start();

		void end(String result);
	}

	/*
	 * 注册接口
	 */
	private String getregister(String tel, String verify, String password) {
		StringBuilder builder = new StringBuilder();
		String httpHost = "http://211.149.198.8:9805/index.php/home/api/register";
		String urltel = "tel=";
		String verifykey = "verify=";
		String passwordkey = "password=";

		String urlName = httpHost + "?" + urltel + tel + "&" + verifykey + verify + "&" + passwordkey + password;
		try {
			URL url = new URL(urlName);

			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.connect();
			connection.getInputStream();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while (line != null && line.length() > 0) {
				builder.append(line);
				line = bufferedReader.readLine();// 读取出来
				register = builder.toString();
				Log.i("register=====>", register);

				/*
				 * 关闭流
				 */
				inputStream.close();
				bufferedReader.close();

				return register;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		register = "error";
		return register;

	}

	/*
	 * 自定义注册监听
	 */
	OnregisterListener registerListener;

	public void setOnregisterListener(OnregisterListener registerListener) {
		this.registerListener = registerListener;
	}

	class registerTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			return getregister(arg0[0], arg0[1], arg0[2]);
		}

		protected void onPostExecute(String result) {
			if (registerListener != null) {
				registerListener.end(result);
			}
		}
	}

	public interface OnregisterListener {
		void start();

		void end(String result);
	}

	/*
	 * 
	 * 用户资料接口
	 */
	GetIssueListener inssueListener = new GetIssueListener() {

		@Override
		public void getIssue(String result) {

		}
	};

	public String getuserdata(String tel, String token, String logo, String nickname, String sex, String birth,
			String constellation, String occupation, String corporation, String site, String hometown, String mail,
			String personality, String attention, String fans, String enshrine) {
		StringBuilder builder = new StringBuilder();
		String httpHost = "http://211.149.198.8:9805/index.php/home/api/userdata";
		String urltel = "tel=";
		String tokenkey = "token=";
		String logokey = "logo=";
		String nicknamekey = "nickname=";
		String sexkey = "sex=";
		String birthkey = "birth=";
		String constellationkey = "constellation=";
		String occupationkey = "occupation=";
		String corporationkey = "corporation=";
		String sitekey = "site=";
		String hometownkey = "hometown=";
		String mailkey = "mail=";
		String personalitykey = "personality=";
		String attentionkey = "attention=";
		String fanskey = "fans=";
		String enshrinekey = "enshrine=";

		String urlName = httpHost + "?" + urltel + tel + "&" + tokenkey + token + "&" + logokey + logo + "&"
				+ nicknamekey + nickname + "&" + sexkey + sex + "&" + birthkey + birth + "&" + constellationkey
				+ constellation + "&" + occupationkey + occupation + "&" + corporationkey + corporation + "&" + sitekey
				+ site + "&" + hometownkey + hometown + "&" + mailkey + mail + "&" + personalitykey + personality
				+ attentionkey + attention + "&" + fanskey + fans + "&" + enshrinekey + enshrine;

		try {
			URL url = new URL(urlName);

			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("post");
			connection.setConnectTimeout(5000);
			connection.connect();
			connection.getInputStream();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while (line != null && line.length() > 0) {
				builder.append(line);
				line = bufferedReader.readLine();// 读取出来
				userdata = builder.toString();
				Log.i("register=====>", userdata);

				/*
				 * 关闭流
				 */
				inputStream.close();
				bufferedReader.close();

				return userdata;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userdata = "error";
		return userdata;
	}

	/*
	 * 自定义用户资料监听
	 */
	/*
	 * OnuserdataListener userdataListener;
	 * 
	 * public void setOnuserdataListener(OnuserdataListener userdataListener) {
	 * this.userdataListener = userdataListener; }
	 * 
	 * class userdataTask extends AsyncTask<String, Void, String> {
	 * 
	 * @Override protected String doInBackground(String... arg0) { return
	 * getuserdata(arg0[0], arg0[1]); }
	 * 
	 * protected void onPostExecute(String result) { if (userdataListener !=
	 * null) { userdataListener.end(result); } } }
	 * 
	 * public interface OnuserdataListener { void start();
	 * 
	 * void end(String result); }
	 */

	// 用户资料查询
	public String setuserdata(String tel, String token) {
		StringBuilder builder = new StringBuilder();
		String httpHost = "http://211.149.198.8:9805/index.php/home/api/demand";
		String telkey = "tel=";
		String tokenkey = "token=";

		String urlName = httpHost + "?" + telkey + tel + "&" + tokenkey + token;

		try {
			URL url = new URL(urlName);

			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.connect();
			connection.getInputStream();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while (line != null && line.length() > 0) {
				builder.append(line);
				line = bufferedReader.readLine();// 读取出来
				demand = builder.toString();
				Log.i("demand=====>", demand);

				/*
				 * 关闭流
				 */
				inputStream.close();
				bufferedReader.close();
				return demand;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		demand = "error";
		return demand;
	}

	/*
	 * 自定义一个查询用户资料的监听
	 */
	OndemandListener demandListener;

	public void setOndemandListener(OndemandListener demandListener) {
		this.demandListener = demandListener;
	}

	// 异步
	class demandTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			return setuserdata(arg0[0], arg0[1]);
		}

		protected void onPostExecute(String result) {
			if (registerListener != null) {
				registerListener.end(result);
			}
		}
	}

	public interface OndemandListener {
		void start();

		void end(String result);
	}

	// 发布回调接口
	public interface IssueListener {
		void getMessage(String result);
	}

	// 发送文字加文件的方法
	public void issue(final String httpUrl, final Map<String, String> map, final ArrayList<File> files,
			final IssueListener issueListener) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*
				 * Map<String, String> fileMap=new HashMap<String, String>();
				 * for(int i=0;i<files.size();i++){ File file=files.get(i);
				 * String key="image"+i; String value=file.getAbsolutePath();
				 * fileMap.put(key, value); } StringBuilder stringBuilder = new
				 * StringBuilder();
				 */
				// stringBuilder.append(upload(httpUrl, map, fileMap)) ;
				StringBuilder stringBuilder = new StringBuilder();

				BufferedReader in = null;
				String end = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";
				try {
					URL url = new URL(httpUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setUseCaches(false);
					connection.setConnectTimeout(10000); // 连接超时为10秒
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Accept-Encoding:", "gzip,deflate,sdch");
					connection.setRequestProperty("Accept-Language:", "zh-CN,zh;q=0.8,en;q=0.6");
					connection.setRequestProperty("User-Agent:",
							"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");
					connection.setRequestProperty("accept", "*/*");
					connection.setRequestProperty("connection", "Keep-Alive");
					connection.setRequestProperty("Charset", "UTF-8");
					connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);// 设置请求数据类型并设置boundary部分；
					connection.connect();
					// 获取输出流
					DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
					Set<Map.Entry<String, String>> paramEntrySet = map.entrySet();
					Iterator<Entry<String, String>> paramIterator = paramEntrySet.iterator();
					while (paramIterator.hasNext()) {
						Map.Entry<String, String> entry = paramIterator.next();

						String key = entry.getKey();
						String value = entry.getValue();

						ds.writeBytes(twoHyphens + boundary + end);
						// Log.i("charset before", "" + value);
						// Log.i("charset after", "" +
						// value.getBytes(Charset.forName("UTF-8")));
						ds.writeBytes("Content-Disposition: form-data; " + "name=\"" + key + "\"" + end);
						ds.writeBytes(end);
						ds.write(value.getBytes(Charset.forName("UTF-8")));
						ds.writeBytes(end);

						// ds.writeBytes(twoHyphens + boundary + twoHyphens +
						// end);
						ds.flush();
					}

					for (int i = 0; i < files.size(); i++) {
						ds.writeBytes(twoHyphens + boundary + end);
						ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\""
								+ "image" + i + ".png" + "\"" + end);
						ds.writeBytes(end);

						// * 取得文件的FileInputStream *//*
						FileInputStream fStream = new FileInputStream(files.get(i).getAbsolutePath());
						// * 设置每次写入1024bytes *//*
						int bufferSize = 1024;
						byte[] buffer = new byte[bufferSize];
						int length = -1;
						// * 从文件读取数据至缓冲区 *//*
						while ((length = fStream.read(buffer)) != -1) {
							// * 将资料写入DataOutputStream中 *//*
							ds.write(buffer, 0, length);
							ds.flush();// 刷新数据
						}
						ds.writeBytes(end);
						ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
						ds.flush();
						fStream.close();
					}
					ds.close();

					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					int statusCode = connection.getResponseCode();
					if (statusCode == HttpURLConnection.HTTP_OK) {
						char[] buf = new char[1024];
						int len = -1;
						while ((len = in.read(buf, 0, buf.length)) != -1) {
							stringBuilder.append(buf, 0, len);
						}
						String result = stringBuilder.toString();
						Log.i("result", result);
						issueListener.getMessage(result);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 获取动态的回调接口
	 */
	public interface GetIssueListener {
		void getIssue(String result);
	}

	/**
	 * 此为获取动态的方法 httpUrl为访问网址 map为所需要传递的参数集合 getIssueListener为回调监听
	 */
	public void getIssue(final String httpUrl, final Map<String, String> map, final GetIssueListener getIssueListener) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				StringBuilder stringBuilder = new StringBuilder();
				BufferedReader in = null;
				String end = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";
				try {
					URL url = new URL(httpUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setUseCaches(false);
					connection.setConnectTimeout(10000); // 连接超时为10秒
					connection.setRequestMethod("POST");
					// connection.setRequestProperty("Accept-Encoding:",
					// "gzip,deflate,sdch");
					// connection.setRequestProperty("Accept-Language:",
					// "zh-CN,zh;q=0.8,en;q=0.6");
					// connection
					// .setRequestProperty(
					// "User-Agent:",
					// "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36
					// (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");
					// connection.setRequestProperty("accept", "*/*");
					// connection.setRequestProperty("connection",
					// "Keep-Alive");
					connection.setRequestProperty("Charset", "UTF-8");
					connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);// 设置请求数据类型并设置boundary部分；
					connection.connect();
					// 获取输出流
					DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
					Set<Map.Entry<String, String>> paramEntrySet = map.entrySet();
					Iterator<Entry<String, String>> paramIterator = paramEntrySet.iterator();
					while (paramIterator.hasNext()) {
						Map.Entry<String, String> entry = paramIterator.next();

						String key = entry.getKey();
						String value = entry.getValue();
						Log.i("getIssue", "key = " + key + ",value = " + value);
						ds.writeBytes(twoHyphens + boundary + end);
						ds.writeBytes("Content-Disposition: form-data; " + "name=\"" + key + "\"" + end);
						ds.writeBytes(end);
						ds.write(value.getBytes(Charset.forName("UTF-8")));
						ds.writeBytes(end);
						ds.flush();
					}
					ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
					ds.close();

					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					int statusCode = connection.getResponseCode();
					if (statusCode == HttpURLConnection.HTTP_OK) {
						char[] buf = new char[1024];
						int len = -1;
						while ((len = in.read(buf, 0, buf.length)) != -1) {
							stringBuilder.append(buf, 0, len);
						}
						String result = stringBuilder.toString();
						Log.i("result", result);
						getIssueListener.getIssue(result);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.i("Upload", "result===========>" + stringBuilder.toString());
			}
		}).start();
	}
}
