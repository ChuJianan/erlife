package com.yrkj.yrlife.been;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.text.SimpleDateFormat;

/**
 * 实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@Table(name = "entity")
public abstract class Entity extends Base {

	public final static SimpleDateFormat SDF_IN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat SDF_OUT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Column(name = "id",isId=true,autoGen=true)
	protected int id;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
