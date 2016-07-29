package com.yrkj.yrlife.been;

import java.io.Serializable;
import java.util.Date;


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

	private int version_code;
	private String version_name;
	private String downloadurl;
	private String update_log;
	private String title;
	private String date;
	private String dateStr;
	private String package_size;


	public int getVersion_code() {
		return version_code;
	}

	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getDownloadurl() {
		return downloadurl;
	}

	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}

	public String getUpdate_log() {
		return update_log;
	}

	public void setUpdate_log(String update_log) {
		this.update_log = update_log;
	}

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

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getPackage_size() {
		return package_size;
	}

	public void setPackage_size(String package_size) {
		this.package_size = package_size;
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
