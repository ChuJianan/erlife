package com.yrkj.yrlife.been;


/**
 * 接口URL实体类
 * @author CJN
 * @version 1.0
 * @created 2016
 */
public class URLs extends Entity {

	public static  String secret_code="";
//	public static String ImageByte="";
	public static String IMAGE_FILE_NAME="";
	public static final String HOST = "http://192.168.0.106:8080/";
//	public static final String HOST = "http://119.10.48.101/";

	public static final String APP_INIT = HOST+"wmmanager/app/pro1/app1/addMember";		//应用初始化地址
	public static final String HOME_ADS = HOST ;		//首页广告地址

	public static final String LOGIN=HOST+"wmmanager/app/pro1/app3/login?";
	public static final String CODE_GET=HOST+"wmmanager/app/pro1/app2/send_msg_code?phone=";
	public static final String SIGIN=HOST+"wmmanager/app/pro1/app3/register?";
	public static final String USER_INFO=HOST+"wmmanager/app/pro1/app3/updateMember?";
	public static final String UPDATE_HEADIMAGE=HOST+"wmmanager/app/pro1/app3/updateHead_Image";
	public static final String LOGINOUT=HOST+"wmmanager/app/pro1/app3/logout?secret_code=";
	public static final String NEAR=HOST+"wmmanager/app/pro1/app3/nearbyMachine?";
	public static final String IMGURL=HOST+"wmmanager";


	public static final String NOTICE = HOST;	//预告地址
	public static final String CLOUD = HOST ;	//会议云地址
	public static final String UPDATE = HOST ;			//更新地址
	public static final String INTERACTION=HOST;//获得互动信息列表
	public static final String SAVE_GUEST=HOST;//提交问题
}
