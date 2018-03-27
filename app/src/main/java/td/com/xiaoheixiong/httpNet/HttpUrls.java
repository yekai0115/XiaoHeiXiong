package td.com.xiaoheixiong.httpNet;

import java.io.Serializable;

public class HttpUrls implements Serializable {

    /* 服务器域名 */

    //    public static final String XHX_Rest = "http://wzh.linuxnb.com/";//测试
    //  public static final String XHX_Rest_java = "http://java.linuxnb.com/";

    //生产
    public static final String XHX_Rest = "http://www.xiaoheixiong.net/";


    public static final String XHX_Rest2 = "http://api.xiaoheixiong.net/";

    /* 服务器接口 */

    // 商家进件，普通
    public static final String Mechants_common = HttpUrls.XHX_Rest + "rest/qrmerInf/insert";
    // 商家进件，快捷
    public static final String Mechants_shortcuts = HttpUrls.XHX_Rest + "rest/qrmerInf/quickInsert";
    //行业类别
    public static final String Industry_Categories = HttpUrls.XHX_Rest + "rest/industryType/getAll";
    //上传图片
    public static final String upload_Pic = HttpUrls.XHX_Rest + "rest/pic/shopPicUpload";

    //获取银行地址信息接口：
    public static final String Bank_address = HttpUrls.XHX_Rest + "rest/common/getDicAreaByParentCode";

    // 所有银行分行
    public static final String branch_Bank = HttpUrls.XHX_Rest + "rest/merRecBankBranch/getBankBranch";
    // 所有银行总行
    public static final String Head_Bank = HttpUrls.XHX_Rest + "rest/merRecBankTotal/selectAll";
    // 商家收款上传资料
    public static final String UploadData = HttpUrls.XHX_Rest + "rest/qrmerInf/insert";
    // 注册验证码
    public static final String XHX_YZM = HttpUrls.XHX_Rest + "rest/xhx/register/sendRegValiMsgCode";
    // 注册接口
    public static final String XHX_Register = HttpUrls.XHX_Rest + "rest/xhx/register/mobileRegister";
    // 找回密码
    public static final String XHX_RPWD = HttpUrls.XHX_Rest + "rest/xhx/pwdPro/findLoginPwdCommit";
    // 手机号或者微信登陆
    public static final String XHX_Login = HttpUrls.XHX_Rest + "rest/xhx/login/userLogin";
    // 地图商家
    public static final String XHX_Mechat_Map = HttpUrls.XHX_Rest + "rest/xhx/merIndex/searchLsbCloudMer";
    // 生活圈
    public static final String XHX_Life_Circle = HttpUrls.XHX_Rest + "rest/xhx/merIndex/mainIndex";
    //百度定位城市
    public static final String XHX_position_city = "http://api.map.baidu.com/geocoder/v2/";
    //百度商家红包
    public static final String XHX_hongbao = HttpUrls.XHX_Rest + "rest/redPacket/httpGetAllRedPacket";
    //百度商家打开红包
    public static final String XHX_Open_hongbao = HttpUrls.XHX_Rest + "rest/redPacket/openRedPacket";
    //我的商户
    public static final String XHX_my_mechat = "http://html.xiaoheixiong.net/#/agency";
    //头条
    public static final String XHX_toutiao = HttpUrls.XHX_Rest2 + "headline/getHeadLists";

    //删除头条
    public static final String XHX_delete_toutiao = HttpUrls.XHX_Rest + "/rest/xhx/headlineInfo/delHeadlineInfo";

    //发布头条
    public static final String XHX_add_toutiao = HttpUrls.XHX_Rest2 + "headline/pushHeadLine";

    //新增团团券
    public static final String XHX_add_tuantuan ="http://api.xiaoheixiong.net/activity/addCoupon";

    //删除团团券
    public static final String XHX_delete_tuantuan ="http://api.xiaoheixiong.net/activity/delCoupon";

    //新增秒秒
    public static final String XHX_add_miaomiao ="http://api.xiaoheixiong.net/activity/addKill";

    //删除秒秒
    public static final String XHX_delete_miaomiao ="http://api.xiaoheixiong.net/activity/delKill";

    //团团券列表
    public static final String XHX_tuantuan_list ="http://api.xiaoheixiong.net/activity/queryCoupon";

    //秒券列表
    public static final String XHX_miaomiao_list ="http://api.xiaoheixiong.net/activity/queryKill";


    //商家详情
    public static final String XHX_Mechat_Details = HttpUrls.XHX_Rest + "rest/xhx/merIndex/merIntroduce";
    // 查询商店总店流水记录
    public static final String XHX_HeadOfficeRecord = HttpUrls.XHX_Rest + "rest/entErpriseInf/getEnterprise";
    // 查询商店分店流水记录
    public static final String XHX_BranchRecord = HttpUrls.XHX_Rest + "rest/entRecord/selRecordByDate";
    //  获取版本信息
    public static final String XHX_AppVersion = HttpUrls.XHX_Rest + "rest/xhx/appVersion/getAppVersion";

    // 商家评论提交
    public static final String XHX_Appraise = HttpUrls.XHX_Rest + "rest/xhx/evaluationInfo/saveEvaluationInfo";
    // 商家评论内容
    public static final String XHX_Appraise_Content = HttpUrls.XHX_Rest + "rest/xhx/evaluationInfo/getEvaluationList";
    // 商家评论内容分页
    //  public static final String XHX_Appraise_Content_all = HttpUrls.XHX_Rest + "rest/xhx/evaluationInfo/getEvaluationList";

    // 快捷实名
    public static final String XHX_Real_Name = HttpUrls.XHX_Rest + "rest/xhx/auth/userFastAuth";
    // 微信绑定手机号
    public static final String XHX_Bind_phone = HttpUrls.XHX_Rest + "rest/xhx/register/bindWeixinMobile";
    // 查询用户信息
    public static final String XHX_userInfoDtl = HttpUrls.XHX_Rest + "rest/xhx/user/userInfoDtl";

    // 修改登录密码
    public static final String XHX_change_login_pwd = HttpUrls.XHX_Rest + "rest/xhx/pwdPro/modLoginPwdCommit";
    // 银行卡信息变更
    public static final String XHX_Bank_info_change = HttpUrls.XHX_Rest + "rest/xhx/withdrawBank/change";

    // 是否设置支付密码
    public static final String XHX_jugde_setPaypwd = HttpUrls.XHX_Rest + "rest/xhx/pwdPro/selectPayPassIsSet";
    // 设置支付密码
    public static final String XHX_setPaypwd = HttpUrls.XHX_Rest + "rest/xhx/pwdPro/setPayPass";
    // 修改设置支付密码
    public static final String XHX_change_Paypwd = HttpUrls.XHX_Rest + "rest/xhx/pwdPro/updatePayPass";
    //找回设置支付密码
    public static final String XHX_Getback_Paypwd = HttpUrls.XHX_Rest + "rest/xhx/pwdPro/findPayPass";
    //更换头像
    public static final String XHX_change_Headimg = HttpUrls.XHX_Rest + "rest/xhx/user/updateHeadImg";
    //收款码
    public static final String XHX_collection_code = HttpUrls.XHX_Rest + "rest/xhx/merQr/gen";

    //收款码
    public static final String XHX_collection_code2 ="http://pay.xiaoheixiong.net/public/merInfo";


    //卡卷详情
    public static final String XHX_MarkCardDtl = HttpUrls.XHX_Rest + "rest/xhx/merMarkActi/getMyMarkCardDtl";
    //分享码
    public static final String XHX_shareCode = HttpUrls.XHX_Rest + "rest/xhx/merQr/drowDownloadIMG";
    //收藏
    public static final String XHX_Save = HttpUrls.XHX_Rest + "rest/favourite/save";
    //取消收藏
    public static final String XHX_delete = HttpUrls.XHX_Rest + "rest/favourite/delete";

    //点赞
    public static final String XHX_Zan = HttpUrls.XHX_Rest2 + "headline/updatePraise";


    //评论
    public static final String XHX_evaulate = HttpUrls.XHX_Rest2 + "headline/pushHeadlineReply";

    //评论列表
    public static final String XHX_evaulate_list = HttpUrls.XHX_Rest2 + "headline/getHeadlineReply";



    //领取卡券
    public static final String XHX_GetMerMark = HttpUrls.XHX_Rest + "rest/xhx/merMarkActi/userGetMerMark";
    //妙秒查看更多
    public static final String XHX_MerMarkList = HttpUrls.XHX_Rest + "rest/xhx/merIndex/getMerMarkList";
    //商品分类
    public static final String XHX_goods_selectType = HttpUrls.XHX_Rest + "rest/xhx/goods/selectType";
    //分类查询商品,//商品列表
    public static final String XHX_goods_list = HttpUrls.XHX_Rest + "rest/xhx/goods/list";

    //游戏卡券领取接口
    public static final String XHX_GetGameMerMark =  HttpUrls.XHX_Rest +"rest/xhx/merMarkActi/userGetGameMerMark";


/*web链接*/

    // 我的会员
    public static final String XHX_mvp = "http://html.xiaoheixiong.net/#/Member";
    // 我的粉丝
    public static final String XHX_fans = "http://html.xiaoheixiong.net/#/Member";
    // 我的动态
    public static final String XHX_Dongtai = "http://html.xiaoheixiong.net/#/dynamic";
    //  我的钱包
    public static final String XHX_money = "http://html.xiaoheixiong.net/#/wallet";
    // 我的积分
    public static final String XHX_points = "http://html.xiaoheixiong.net/#/integral";
    // 我的足迹
    public static final String XHX_footmark = "http://html.xiaoheixiong.net/#/footmark";
    //   我的关注
    public static final String XHX_guanzhu = "http://html.xiaoheixiong.net/#/attention";
    // 我的商户
    public static final String XHX_Merchat = "http://html.xiaoheixiong.net/#/Merchant";
    //   我的代理
    public static final String XHX_agency = "http://html.xiaoheixiong.net/#/agency";
    // 收款码
    public static final String XHX_PayCode = "http://html.xiaoheixiong.net/#/paymentCode";
    // 我的卡券
    public static final String XHX_cardTicket = "http://html.xiaoheixiong.net/#/cardTicket";
    //  店铺管理
    public static final String XHX_storeManagement = "http://html.xiaoheixiong.net/#/storeManagement";
    // 我的商品
    public static final String XHX_MyShop = "http://html.xiaoheixiong.net/#/goods";
    //我的账本
    public static final String XHX_Mybooks = "http://html.xiaoheixiong.net/#/ledgers";

    // 店铺卡券
    public static final String XHX_shopCard = "http://html.xiaoheixiong.net/#/shopCard";
    // 分享
    public static final String XHX_share = "http://html.xiaoheixiong.net/#/share";
    // 店铺红包
    public static final String XHX_redEnvelope = "http://html.xiaoheixiong.net/#/redEnvelope";
    // 店铺营销
    public static final String XHX_marketing = "http://html.xiaoheixiong.net/#/marketing";

    // 交易管理：
    public static final String XHX_tradingManagement = "http://html.xiaoheixiong.net/#/tradingManagement";

    //  我的收益
    public static final String XHX_earnings = "http://html.xiaoheixiong.net/#/earnings";
    //  意见与反馈
    public static final String XHX_opinions = "http://html.xiaoheixiong.net/#/opinions";
    //  关于公司
    public static final String XHX_aboutCompany = "http://html.xiaoheixiong.net/#/about";
    //  隐私条款
    public static final String XHX_privacyPolicy = "http://html.xiaoheixiong.net/#/privacyPolicy";

}