package td.com.xiaoheixiong.httpNet;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import td.com.xiaoheixiong.Utils.signUtil.SignaTureUtils;

public class OkHttpClientManager {
	private static final MediaType MEDIA_TYPE_JSON = MediaType
			.parse("application/x-www-form-urlencoded; charset=utf-8");// mdiatype//

	private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");// mdiatype//

	private static final String TAG = OkHttpClientManager.class.getSimpleName();
	// private static final String BASE_URL = "http://xxx.com/openapi";//
	// 请求接口根地址
	private static volatile OkHttpClientManager mInstance;// 单利引用
	public static final int TYPE_GET = 0;// get请求
	public static final int TYPE_POST_JSON = 1;// post请求参数为json
	public static final int TYPE_POST_FORM = 2;// post请求参数为表单
	public static final int TYPE_POST_FILE = 3;// post请求参数为文件

	private static OkHttpClient mOkHttpClient;// okHttpClient 实例
	private Handler okHttpHandler;// 全局处理子线程和M主线程通信

	public static final int NO_SIGN = 66, HOST_javaMpay = 0;

	/**
	 * 初始化OkHttpClientManager
	 */
	public OkHttpClientManager(Context context) {
		// 初始化OkHttpClient
		mOkHttpClient = new OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)// 设置超时时间
				/*
				 * .readTimeout(10, TimeUnit.SECONDS)// 设置读取超时时间
				 * .writeTimeout(10, TimeUnit.SECONDS)// 设置写入超时时间
				 */ .build();
		// 初始化Handler
		okHttpHandler = new Handler(context.getMainLooper());
	}

	/**
	 * 获取单例引用
	 *
	 * @return
	 */
	public static OkHttpClientManager getInstance(Context context) {

		OkHttpClientManager inst = mInstance;
		if (inst == null) {
			synchronized (OkHttpClientManager.class) {
				inst = mInstance;
				if (inst == null) {
					inst = new OkHttpClientManager(context.getApplicationContext());
					mInstance = inst;
				}
			}
		}
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("ddHHMMss");
		String dateString = formatter.format(currentTime);
		Log.e("timeStart", dateString);
		return inst;
	}

	/**
	 * okHttp同步请求统一入口
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param requestType
	 *            请求类型
	 * @param paramsMap
	 *            请求参数
	 * @return
	 */
	public static String requestSyn(String actionUrl, int requestType, HashMap<String, Object> paramsMap) {
		String json = null;

		switch (requestType) {
		case TYPE_GET:
			// requestGetBySyn(actionUrl, paramsMap);
			json = requestGetBySyn(actionUrl, paramsMap);
			break;
		case TYPE_POST_JSON:
			json = requestPostBySyn(actionUrl, paramsMap);
			break;
		case TYPE_POST_FORM:
			json = requestPostBySynWithForm(actionUrl, paramsMap);
			break;
		}
		return json;
	}

	/**
	 * okHttp get同步请求
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @return
	 */
	private static String requestGetBySyn(String actionUrl, HashMap<String, Object> paramsMap) {
		StringBuilder tempParams = new StringBuilder();
		String json = null;
		try {
			// 处理参数
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				// 对参数进行URLEncoder
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key) + "", "utf-8")));
				pos++;
			}
			// 补全请求地址
			String requestUrl = String.format("%s?%s", actionUrl, tempParams.toString());
			Log.e("requestUrl", requestUrl);
			// 创建一个请求
			Request request = addHeaders().url(requestUrl).build();
			// 创建一个Call
			final Call call = mOkHttpClient.newCall(request);
			// 执行请求
			final Response response = call.execute();
			json = response.body().string();
			Log.e("json", response.body().string());

		} catch (Exception e) {
			Log.e(TAG, e.toString());
			Log.e("TAG", e.toString());
		}
		return json;
	}

	/**
	 * okHttp post同步请求
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @return
	 */
	private static String requestPostBySyn(String actionUrl, HashMap<String, Object> paramsMap) {
		String json = null;
		try {
			// 处理参数
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key) + "", "utf-8")));
				pos++;
			}
			// 补全请求地址
			// String requestUrl = String.format("%s/%s", BASE_URL, actionUrl);
			// 生成参数
			String params = tempParams.toString();
			// 创建一个请求实体对象 RequestBody
			RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
			// 创建一个请求
			final Request request = addHeaders().url(actionUrl).post(body).build();
			// 创建一个Call
			final Call call = mOkHttpClient.newCall(request);
			// 执行请求
			Response response = call.execute();
			// 请求执行成功
			if (response.isSuccessful()) {
				// 获取返回数据 可以是String，bytes ,byteStream
				Log.e(TAG, "response ----->" + response.body().string());
				json = response.body().string();
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return json;
	}

	/**
	 * okHttp post同步请求表单提交
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @return
	 */
	private static String requestPostBySynWithForm(String actionUrl, HashMap<String, Object> paramsMap) {
		String json = null;
		try {
			// 创建一个FormBody.Builder
			FormBody.Builder builder = new FormBody.Builder();
			for (String key : paramsMap.keySet()) {
				// 追加表单信息
				builder.add(key, paramsMap.get(key).toString());
			}
			// 生成表单实体对象
			RequestBody formBody = builder.build();
			// 补全请求地址
			// String requestUrl = String.format("%s/%s", BASE_URL, actionUrl);
			// 创建一个请求
			final Request request = addHeaders().url(actionUrl).post(formBody).build();
			// 创建一个Call
			final Call call = mOkHttpClient.newCall(request);
			// 执行请求
			Response response = call.execute();
			if (response.isSuccessful()) {
				Log.e(TAG, "response ----->" + response.body().string());
				json = response.body().string();
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return json;
	}

	/**
	 * okHttp异步请求统一入口
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param requestType
	 *            请求类型
	 * @param paramsMap
	 *            请求参数
	 * @param callBack
	 *            请求返回数据回调
	 * @param <T>
	 *            数据泛型
	 **/
	public <T> Call requestAsyn(String actionUrl, int requestType, HashMap<String, Object> paramsMap, int type,
                                ReqCallBack<T> callBack) {
		Call call = null;
		Log.e("type", type + "");
		switch (type) {
		case HOST_javaMpay:// HOST_javaMpay
			paramsMap = SignaTureUtils.MD5signaTure(paramsMap);
			break;

		case 1:

			break;
		case 2:

			break;
		case NO_SIGN:

			break;

		}
		Log.e("paramsMap+++", paramsMap + "");
		switch (requestType) {
		case TYPE_GET:
			call = requestGetByAsyn(actionUrl, paramsMap, callBack);
			break;
		case TYPE_POST_JSON:
			call = requestPostByAsyn(actionUrl, paramsMap, callBack);
			break;
		case TYPE_POST_FORM:
			call = requestPostByAsynWithForm(actionUrl, paramsMap, callBack);
			break;

		/*
		 * case TYPE_POST_FILE: call = upload(Context context, actionUrl,
		 * Map<String, String> paramsMap, Map<String, File> files, final
		 * ReqCallBack<T> callBack); break;
		 */

		}

		return call;
	}

	/**
	 * okHttp get异步请求
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @param callBack
	 *            请求返回数据回调
	 * @param <T>
	 *            数据泛型
	 * @return
	 */
	private <T> Call requestGetByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
                                      final ReqCallBack<T> callBack) {
		StringBuilder tempParams = new StringBuilder();
		try {
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key) + "", "utf-8")));
				pos++;
			}
			String requestUrl = String.format("%s?%s", actionUrl, tempParams.toString());
			final Request request = addHeaders().url(requestUrl).build();
			final Call call = mOkHttpClient.newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					failedCallBack("访问失败", callBack);
					Log.e(TAG, e.toString());
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.isSuccessful()) {
						String string = response.body().string();
						Log.e(TAG, "response ----->" + string);
						successCallBack((T) string, callBack);
					} else {
						failedCallBack("服务器错误", callBack);
					}
				}
			});
			return call;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return null;
	}

	/**
	 * okHttp post异步请求
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @param callBack
	 *            请求返回数据回调
	 * @param <T>
	 *            数据泛型
	 * @return
	 */
	public <T> Call requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
                                      final ReqCallBack<T> callBack) {
		try {
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key) + "", "utf-8")));
				pos++;
			}
			String params = tempParams.toString();
			Log.e("params", params + "");
			RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
			// String requestUrl = String.format("%s/%s", BASE_URL, actionUrl);
			Log.e("tag", "Call");
			final Request request = addHeaders().url(actionUrl).post(body).build();
			Log.e("tag+", "Call....");
			final Call call = mOkHttpClient.newCall(request);
			Log.e("tag++", "CallEnd");
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					failedCallBack("访问失败", callBack);
					Log.e(TAG, e.toString());
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.isSuccessful()) {
						String string = response.body().string();
						Log.e(TAG, "response ----->" + string);
						successCallBack((T) string, callBack);
					} else {
						failedCallBack("服务器错误", callBack);
					}
				}
			});
			return call;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return null;
	}

	/**
	 * okHttp post异步请求表单提交
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @param callBack
	 *            请求返回数据回调
	 * @param <T>
	 *            数据泛型
	 * @return
	 */
	private <T> Call requestPostByAsynWithForm(String actionUrl, HashMap<String, Object> paramsMap,
                                               final ReqCallBack<T> callBack) {
		try {
			FormBody.Builder builder = new FormBody.Builder();
			for (String key : paramsMap.keySet()) {
				builder.add(key, paramsMap.get(key).toString());
			}
			RequestBody formBody = builder.build();
			// String requestUrl = String.format("%s/%s", BASE_URL, actionUrl);
			final Request request = addHeaders().url(actionUrl).post(formBody).build();
			final Call call = mOkHttpClient.newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					failedCallBack("访问失败", callBack);
					Log.e(TAG, e.toString());
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.isSuccessful()) {
						String string = response.body().string();
						Log.e(TAG, "response ----->" + string);
						successCallBack((T) string, callBack);
					} else {
						failedCallBack("服务器错误", callBack);
					}
				}
			});
			return call;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return null;
	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 *            url
	 * @param files
	 *            上传的文件files
	 *            回调
	 */
	public <T> Call upload(Context context, String url, HashMap<String, Object> paramsMap, Map<String, File> files,
                           int type, final ReqCallBack<T> callBack) {

		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
		// 添加上传文件
		try {
			if (files != null) {
				for (String key : files.keySet()) {
					File file = files.get(key);
					// MediaType.parse() 里面是上传的文件类型。
					RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
					String filename = file.getName();
					// 参数分别为， 请求key ，文件名称 ， RequestBody
					requestBody.addFormDataPart("file", filename, body);
				}

			}
			// 添加参数

			if (paramsMap == null) {
				// map 里面是请求中所需要的 key 和 value
				paramsMap = new HashMap<>();
			}
			paramsMap = SignaTureUtils.MD5signaTure(paramsMap);
			for (Map.Entry entry : paramsMap.entrySet()) {
				requestBody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
			}
			Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
			final Call call = mOkHttpClient.newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					failedCallBack("访问失败", callBack);
					Log.e(TAG, e.toString());
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.isSuccessful()) {
						String string = response.body().string();
						Log.e(TAG, "response ----->" + string);
						successCallBack((T) string, callBack);
					} else {
						failedCallBack("服务器错误", callBack);
					}
				}
			});
			return call;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return null;
	}

	/**
	 * okHttp get异步请求
	 * 
	 * @param actionUrl
	 *            接口地址
	 * @param paramsMap
	 *            请求参数
	 * @param callBack
	 *            请求返回数据回调
	 * @param <T>
	 *            数据泛型 添加请求头
	 * @return
	 */
	@SuppressWarnings("unused")
	public <T> Call requestGetAddheaderByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
                                              HashMap<String, Object> HeadMap, final ReqCallBack<T> callBack) {
		StringBuilder tempParams = new StringBuilder();
		try {
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key) + "", "utf-8")));
				pos++;
			}
			String requestUrl = String.format("%s?%s", actionUrl, tempParams+"");
			Log.e("TAGurl", requestUrl+"");
			Request request = null;
			if (HeadMap != null) {
				Request.Builder builder = new Request.Builder();
				for (String key : HeadMap.keySet()) {
				//	builder = new Request.Builder();
					builder.addHeader(key, HeadMap.get(key) + "");
				}
				request = builder.url(requestUrl).build();

			} else {
				request = addHeaders().url(requestUrl).build();
			}
			
			final Call call = mOkHttpClient.newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					failedCallBack("访问失败", callBack);
					Log.e(TAG, e.toString());
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.isSuccessful()) {
						String string = response.body().string();
						Log.e(TAG, "response ----->" + string);
						successCallBack((T) string, callBack);
					} else {
						failedCallBack("服务器错误", callBack);
					}
				}
			});
			return call;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return null;
	}

	public interface ReqCallBack<T> {
		/**
		 * 响应成功
		 */
		void onReqSuccess(T result);

		/**
		 * 响应失败
		 */
		void onReqFailed(String errorMsg);
	}

	/**
	 * 统一为请求添加头信息
	 * 
	 * @return
	 */
	private static Request.Builder addHeaders() {
		Request.Builder builder = new Request.Builder();

		/*
		 * Request.Builder builder = new Request.Builder() .addHeader("okhttp",
		 * "ok") .addHeader("platform", "2") .addHeader("phoneModel",
		 * Build.MODEL) .addHeader("systemVersion", Build.VERSION.RELEASE) ;
		 */
		return builder;
	}

	/**
	 * 统一同意处理成功信息
	 * 
	 * @param result
	 * @param callBack
	 * @param <T>
	 */
	private <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
		okHttpHandler.post(new Runnable() {
			@Override
			public void run() {

				if (callBack != null) {
					Log.e("callBack", "callBack.." + result);
					callBack.onReqSuccess(result);
				}

			}
		});
	}

	/**
	 * 统一处理失败信息
	 * 
	 * @param errorMsg
	 * @param callBack
	 * @param <T>
	 */
	private <T> void failedCallBack(final String errorMsg, final ReqCallBack<T> callBack) {
		okHttpHandler.post(new Runnable() {
			@Override
			public void run() {
				if (callBack != null) {
					callBack.onReqFailed(errorMsg);
				}
			}
		});
	}

}
