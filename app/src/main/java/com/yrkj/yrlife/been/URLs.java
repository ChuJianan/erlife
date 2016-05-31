package com.yrkj.yrlife.been;


/**
 * 接口URL实体类
 * @author CJN
 * @version 1.0
 * @created 2016
 */
public class URLs extends Entity {

	public static final String UTF_8 = "UTF-8";
	public static  String secret_code="";
//	public static String ImageByte="";
	public static String IMAGE_FILE_NAME="";
	public static final String HOST = "http://192.168.0.106:8080/";
//	public static final String HOST = "http://119.10.48.101/";

	public static final String APP_INIT = HOST+"wmmanager/app/pro1/app1/addMember";		//应用初始化地址
	public static final String HOME_ADS = HOST ;		//首页广告地址

	public static final String SECRET_CODE=HOST+"wmmanager/app/pro1/app3/judgeIsExpired";
	public static final String LOGIN=HOST+"wmmanager/app/pro1/app3/login?";//登录
	public static final String CODE_GET=HOST+"wmmanager/app/pro1/app2/send_msg_code?phone=";//绑卡，密码找回获取验证码
	public static final String CODE_GET_Z=HOST+"wmmanager/app/pro1/app2/sendMsgCodeCheck?phone=";//注册、更改手机号获取验证码
	public static final String CODE_GET_F=HOST+"wmmanager/app/pro1/app2/send_msg_findPsw?phone=";//找回密码手机号获取验证码
	public static final String SIGIN=HOST+"wmmanager/app/pro1/app3/register?";//注册
	public static final String USER_INFO=HOST+"wmmanager/app/pro1/app3/updateMember?";//用户信息
	public static final String UPDATE_HEADIMAGE=HOST+"wmmanager/app/pro1/app3/updateHead_Image";//上传头像
	public static final String LOGINOUT=HOST+"wmmanager/app/pro1/app3/logout?secret_code=";//退出登录
	public static final String NEAR=HOST+"wmmanager/app/pro1/app3/nearbyMachine?";//附近网点
	public static final String IMGURL=HOST+"wmmanager";//图片地址
	public static final String CARDS=HOST+"wmmanager/app/pro1/app1/memberCard?";//会员卡
	public static final String BIND_CARDS=HOST+"wmmanager/app/pro1/app1/bindMemberCard?";//绑定会员卡
	public static final String IDEAR_SET=HOST+"wmmanager/app/pro1/app3/feedback";//意见反馈
	public static final String RECORD=HOST+"wmmanager/app/pro1/app3/chargeRecord?";//充值记录
	public static final String CONSUME=HOST+"wmmanager/app/pro1/app3/dealRecord?";//消费记录
	public static final String PAY=HOST+"wmmanager/app/pro1/app2/wxCharge?";//充值
	public static final String QUERY_PAY=HOST+"wmmanager/app/pro1/app2/queryPayment?";//查询充值
	public static final String FindPWD=HOST+"wmmanager/app/pro1/app3/updatePassword?";//修改，找回密码
	public static final String Thread_Login=HOST+"wmmanager/app/pro1/app2/third_Party_Logins?";//第三方登陆



	public static final String WX_ACCESS_TOKEN="https://api.weixin.qq.com/sns/oauth2/access_token?";//获取access_token appid应用唯一标识 secret应用密钥AppSecret code填写第一步获取的code参数 grant_type填authorization_code
	public static final String WX_USER_INFO="https://api.weixin.qq.com/sns/userinfo?";//获取userinfo access_token调用凭证 openid普通用户的标识，对当前开发者帐号唯一  lang国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为zh-CN
	public static final String WX_REFRESH_TOKEN="https://api.weixin.qq.com/sns/oauth2/refresh_token?";//获取userinfo appid应用唯一标识 grant_type填refresh_token refresh_token填写通过access_token获取到的refresh_token参数


	public static final String UPDATE = HOST ;			//更新地址
}
