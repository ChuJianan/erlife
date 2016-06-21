package com.yrkj.yrlife.been;

import java.io.Serializable;


import com.google.gson.JsonSyntaxException;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.utils.JsonUtils;

/**
 * 应用程序更新实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable{
	
	public final static String UTF8 = "UTF-8";
	
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	private String title;
	private String date;
	private String package_size;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPackage_size() {
		return package_size;
	}

	public void setPackage_size(String package_size) {
		this.package_size = package_size;
	}

	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	
	public static Update parse(String jsonString) throws AppException {
		Update update = null;
        try {        	
        	update = JsonUtils.fromJson(jsonString, Update.class);
        } catch (JsonSyntaxException e) {
			throw AppException.json(e);
        }
        return update;       
	}
}
