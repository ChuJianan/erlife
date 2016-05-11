package com.yrkj.yrlife.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Update;
import com.yrkj.yrlife.utils.StringUtils;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * API客户端接口：用于访问网络数据
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";
	
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static String appCookie;
	private static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";
	}
	
	private static String getCookie(YrApplication appContext) {
		if(appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}
	
	private static String getUserAgent(YrApplication appContext) {
		if(appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("iMeeting");
			ua.append('/'+appContext.getPackageInfo().versionName+'_'+appContext.getPackageInfo().versionCode);//App版本
			ua.append("/Android");//手机系统平台
			ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
			ua.append("/"+android.os.Build.MODEL); //手机型号
			ua.append("/"+appContext.getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}
	
	private static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}	
	
	private static GetMethod getHttpGet(String url, String cookie, String userAgent) throws MalformedURLException {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", Uri.parse(url).getHost());
		httpGet.setRequestHeader("Connection","Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}
	
	private static PostMethod getHttpPost(String url, String cookie, String userAgent) throws MalformedURLException {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", Uri.parse(url).getHost());
		httpPost.setRequestHeader("Connection","Keep-Alive");
		httpPost.setRequestHeader("secret_code", cookie);
//		httpPost.setRequestHeader("User-Agent", userAgent);
		httpPost.setRequestHeader("imageName",URLs.IMAGE_FILE_NAME);
		return httpPost;
	}
	
	public static String makeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}
	
	/**
	 * get请求URL
	 * @param appContext
	 * @param url
	 * @return JSON
	 * @throws AppException
	 * @throws JSONException 
	 */
	private static String http_get(YrApplication appContext, String url) throws AppException, JSONException {
		String json = "";
		JSONObject result = new JSONObject(_get(appContext, url));
		if(result.getInt("code") != 001)
			throw AppException.custom(result.getString("message"));
		Iterator<?> it = result.keys();
		while (it.hasNext()) {
			String key = it.next().toString();
			if(!"status".equals(key) && !"reason".equals(key))
				json = result.getString(key);
		}
		return json;
	}
	
	/**
	 * 公用get方法
	 * @param url
	 * @throws AppException 
	 */
	private static String _get(YrApplication appContext, String url) throws AppException {
		if (!StringUtils.isUrl(url)) {
			return "";
		}
		//System.out.println("get_url==> "+url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);			
				int statusCode = httpClient.executeMethod(httpGet);
				if(statusCode != HttpStatus.SC_OK) {
		        	throw AppException.http(statusCode);
		        } else if(statusCode == HttpStatus.SC_OK) {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //保存cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
				responseBody = httpGet.getResponseBodyAsString();
				//System.out.println("XMLDATA=====>"+responseBody);
				break;				
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		
		return responseBody;
	}
	
	/**
	 * 公用post方法
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(YrApplication appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException {
		if (!StringUtils.isUrl(url)) {
			return "";
		}
		//System.out.println("post_url==> "+url);
		String cookie = URLs.secret_code;
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		//post表单参数处理
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
        if(params != null)
        for(String name : params.keySet()){
        	parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
        	//System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
        }
        if(files != null)
        for(String file : files.keySet()){
        	try {
				parts[i++] = new FilePart(file, files.get(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	//System.out.println("post_key_file==> "+file);
        }
		
		String responseBody = "";
		int time = 0;
		do{
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);	        
		        httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));
		        int statusCode = httpClient.executeMethod(httpPost);
		        if(statusCode != HttpStatus.SC_OK) {
		        	throw AppException.http(statusCode);
		        } else if(statusCode == HttpStatus.SC_OK) {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //保存cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		        //System.out.println("XMLDATA=====>"+responseBody);
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
        return responseBody;
	}
	
	/**
	 * post请求URL
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws  
	 */
	public static String http_post(YrApplication appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, JSONException {
		String json = "";
		JSONObject result = new JSONObject(_post(appContext, url, params, files));
		if(result.getInt("status") != 1)
			throw AppException.custom(result.getString("reason"));
		Iterator<?> it = result.keys();
		while (it.hasNext()) {
			String key = it.next().toString();
			if(!"status".equals(key) && !"reason".equals(key))
				json = result.getString(key);
		}
		return json;
	}
	/**
	 * 向服务端发送数据
	 * @param urls
	 * @param gson
	 * @return
	 * @throws IOException
	 */
	public static String setHttp(String urls,String gson) throws IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		String result = null;
		HttpPost post = new HttpPost(urls);
		try {
		StringEntity entity = new StringEntity(gson,HTTP.UTF_8);
		post.setEntity(entity);
		HttpResponse responString = client.execute(post);
		if (responString.getStatusLine().getStatusCode() == 200) {
	        // 获取返回的数据
	        result = EntityUtils.toString(responString.getEntity(), "UTF-8");
	    }
		} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	return result;
	}
	/**
	 * 检查版本更新
	 * @param
	 * @return
	 */
	public static Update checkVersion(final YrApplication appContext) throws AppException {
		String url = makeURL(URLs.UPDATE, new HashMap<String, Object>(){{
			put("mac", appContext.getMacAddress());
		}});
		try{
			return Update.parse(http_get(appContext, url));
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}
	
	/**
	 * 获取网络图片
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) throws AppException {
		if (!StringUtils.isUrl(url)) {
			return null;
		}
		//System.out.println("image_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
		        InputStream inStream = httpGet.getResponseBodyAsStream();
		        bitmap = BitmapFactory.decodeStream(inStream);
		        inStream.close();
		        break;
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		return bitmap;
	}
    public static String http_test(YrApplication appContext,String url)throws AppException, JSONException{
		String json="";
		JSONObject result = new JSONObject(_get(appContext, url));
//		if(result.getInt("code") != 1)
//			throw AppException.custom(result.getString("message"));
		json=result.toString();
		return  json;
	}
}
