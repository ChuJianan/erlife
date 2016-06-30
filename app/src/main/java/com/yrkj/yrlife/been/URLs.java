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
//	public static final String HOST = "http://192.168.3.2:8080/";
//	public static final String HOST = "http://40057179.nat123.net/";
	public static final String HOST = "http://119.10.48.101/";

	public static final String APP_INIT = HOST+"wmmanager/app/pro1/app1/addMember";		//应用初始化地址
	public static final String HOME_ADS = HOST+"wmmanager/app/pro1/app1/loadBannerImage" ;		//首页广告地址

	public static final String SECRET_CODE=HOST+"wmmanager/app/pro1/app3/judgeIsExpired";
	public static final String LOGIN=HOST+"wmmanager/app/pro1/app3/login?";//登录
	public static final String CODE_GET=HOST+"wmmanager/app/pro1/app2/send_msg_code?phone=";//绑卡，密码找回获取验证码
	public static final String CODE_GET_B=HOST+"wmmanager/app/pro1/app2/sendMsgJudge?phone=";//绑定手机号获取验证码
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
	public static final String MyIDEAR_SET=HOST+"wmmanager/app/pro1/app3/myFeedback?";//我的意见反馈
	public static final String RECORD=HOST+"wmmanager/app/pro1/app3/chargeRecord?";//充值记录
	public static final String CONSUME=HOST+"wmmanager/app/pro1/app3/dealRecord?";//消费记录
	public static final String PAY=HOST+"wmmanager/app/pro1/app2/wxCharge?";//充值
	public static final String QUERY_PAY=HOST+"wmmanager/app/pro1/app2/queryPayment?";//查询充值
	public static final String FindPWD=HOST+"wmmanager/app/pro1/app3/updatePassword?";//修改，找回密码
	public static final String Thread_Login=HOST+"wmmanager/app/pro1/app2/third_Party_Logins?";//第三方登陆
	public static final String Thread_Login_JUDGE=HOST+"wmmanager/app/pro1/app2/third_Login_judge?";//第三方登陆绑定手机号
	public static final String WASH_NO_CARD=HOST+"wmmanager/app/pro1/app1/unCardWashing?";//无卡洗车
	public static final String Load204Info=HOST+"wmmanager/app/pro1/app1/load204Info?";//实时消费金额
	public static final String PAYCONFIRM=HOST+"wmmanager/app/pro1/app1/payConfirm?";//无卡洗车结算
	public static final String NEWS=HOST+"wmmanager/app/pro1/app1/systemMessage?";//新闻
	public static final String RATE=HOST+"wmmanager/app/pro1/app1/remarkStar?";//评星
	public static final String Code_Check=HOST+"wmmanager/app/pro1/app3/regCheckCode?";//注册验证码验证
	public static final String IsWashing=HOST+"wmmanager/app/pro1/app1/queryWhichStep?";//是否正在洗车



	public static final String WX_ACCESS_TOKEN="https://api.weixin.qq.com/sns/oauth2/access_token?";//获取access_token appid应用唯一标识 secret应用密钥AppSecret code填写第一步获取的code参数 grant_type填authorization_code
	public static final String WX_USER_INFO="https://api.weixin.qq.com/sns/userinfo?";//获取userinfo access_token调用凭证 openid普通用户的标识，对当前开发者帐号唯一  lang国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为zh-CN
	public static final String WX_REFRESH_TOKEN="https://api.weixin.qq.com/sns/oauth2/refresh_token?";//获取userinfo appid应用唯一标识 grant_type填refresh_token refresh_token填写通过access_token获取到的refresh_token参数


	public static final String UPDATE = "http://192.168.0.102:8080/jfinal_demo/";			//更新地址
}