package td.com.xiaoheixiong.wxapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import td.com.xiaoheixiong.Utils.AppManager;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.BaseActivity;
import td.com.xiaoheixiong.activity.BindWeixinMobileActivity;
import td.com.xiaoheixiong.activity.LoginActivity;
import td.com.xiaoheixiong.activity.ViewPagerMainActivity;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.wechatLogin.Config;
import td.com.xiaoheixiong.wechatLogin.OkHttpUtils;
import td.com.xiaoheixiong.wechatLogin.WeChatInfo;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    public int WX_LOGIN = 1;
    private String deviceId = "", androidId = "", lng = "", lat = "", version = "";
    private IWXAPI iwxapi;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getSupportActionBar().hide();
        // 隐藏状态栏
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //          WindowManager.LayoutParams.FLAG_FULLSCREEN);
        editor = MyCacheUtil.setshared(this);

        deviceId = MyCacheUtil.getshared(this).getString("deviceId", "");
        androidId = MyCacheUtil.getshared(this).getString("androidId", "");
        lng = MyCacheUtil.getshared(this).getString("lng", "");
        lat = MyCacheUtil.getshared(this).getString("lat", "");

        iwxapi = WXAPIFactory.createWXAPI(this, Config.APP_ID, false);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi.handleIntent(getIntent(), this);
        version = getVersion();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }


    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        //微信登录为getType为1，分享为0
        if (baseResp.getType() == WX_LOGIN) {
            //登录回调
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String code = String.valueOf(resp.code);
                    //获取用户信息
                    getAccessToken(code);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                    break;
                default:
                    break;
            }
        } else {
            //分享成功回调
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //分享成功
                    Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //分享取消
                    Toast.makeText(WXEntryActivity.this, "分享取消", Toast.LENGTH_LONG).show();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //分享拒绝
                    Toast.makeText(WXEntryActivity.this, "分享拒绝", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        finish();
    }

    private void getAccessToken(String code) {
        //获取授权
        String http = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Config.APP_ID + "&secret=" + Config.APP_SERECET + "&code=" + code + "&grant_type=authorization_code";
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    access = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //获取个人信息
                String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId + "";
                OkHttpUtils.ResultCallback<WeChatInfo> resultCallback = new OkHttpUtils.ResultCallback<WeChatInfo>() {
                    @Override
                    public void onSuccess(WeChatInfo response) {
                        Log.e("TAG", response + "");
                        //Toast.makeText(WXEntryActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        WechatLoginGo(response);

                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                };
                OkHttpUtils.get(getUserInfo, resultCallback);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        };
        OkHttpUtils.get(http, resultCallback);
    }

    private void WechatLoginGo(WeChatInfo weChatInfo) {
        //     showLoadingDialog("登录中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        //     phoneNumber		String	手机号	clientType 不为3时必填
        //    clientType	是	String	登录类别	1安卓 2 iOS 3微信
        //    password   String	密码	clientType 不为3时必填
        //    curVersion   String	版本号
        //     appType	是	String	客户端的版本	1安卓  2iOS
        //    openId		String	微信openid	clientType 为3时必填
        //     unionId		String	微信	clientType 为3时必填
        //    deviceId		String	设备id	用于判断当前登录设备与上次登录设备是否一致
        //    lng		String	经度
        //    lat		String	纬度

        editor.putString("unionId", weChatInfo.getUnionid() + "");
        editor.putString("openId", weChatInfo.getOpenid() + "");
        editor.commit();

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("phoneNumber", "");
        maps.put("password", "");
        maps.put("clientType", "3");
        maps.put("curVersion", version);
        maps.put("appType", "1");
        maps.put("openId", weChatInfo.getOpenid());
        maps.put("unionId", weChatInfo.getUnionid());
        maps.put("deviceId", deviceId);
        maps.put("lng", lng);
        maps.put("lat", lat);

        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Login, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        //   loadingDialogWhole.dismiss();
                        //    detail={"ALLOWBX":"01","EXTENDCOUNT":"0","ISAREAAGENT":"0","ISMER":"01","MERCNUM":"M0081744","MERSTS":"6",
                        // "NOCARDFEERATE":"0.5","NOTICEMESSAGE":"加油哟！您还差5个用户即可尊享0费率！","OEMID":"XhxiongOEM","PHONENUMBER":"13457816903",
                        // "STS":"2 ","TXNSTS":"1","agentId":"Age690315109265","logNum":"0"
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();

                        com.alibaba.fastjson.JSONObject oJSON = JSON.parseObject(result + "");

                        Log.e("IntegralLogin", map + "");
                        Log.e("oJSON", oJSON + "");
                        // “000000” 成功 “VL0003”用户不存在 “VL0005” 用户已经退出登陆 其他 请求失败
                        if (oJSON == null) {
                            Intent it = new Intent(WXEntryActivity.this, LoginActivity.class);
                            startActivity(it);
                            finish();
                            return;
                        }
                        if (oJSON.get("RSPCOD").equals("000000")) {// 跳转到积分主页
                            if (oJSON.get("detail") == null) {
                                Intent it = new Intent(WXEntryActivity.this, BindWeixinMobileActivity.class);
                                startActivity(it);
                                finish();
                            } else {
                                map = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                                });
                                editor.putString("ALLOWBX", map.get("ALLOWBX") + "");
                                editor.putString("EXTENDCOUNT", map.get("EXTENDCOUNT") + "");
                                editor.putString("ISAREAAGENT", map.get("ISAREAAGENT") + "");
                                editor.putString("ISMER", map.get("ISMER") + "");
                                editor.putString("MERCNUM", map.get("MERCNUM") + "");
                                editor.putString("MERSTS", map.get("MERSTS") + "");
                                editor.putString("ACTNAM", map.get("ACTNAM") + "");
                                editor.putString("ISAREAAGENT", map.get("ISAREAAGENT") + "");
                                editor.putString("ISMER", map.get("ISMER") + "");
                                editor.putString("isAgent", map.get("isAgent") + "");
                                editor.putString("agentLevel", map.get("agentLevel") + "");
                                editor.putString("headImgUrl", map.get("headImgUrl") + "");

                                editor.putString("NOCARDFEERATE", map.get("NOCARDFEERATE") + "");
                                editor.putString("NOTICEMESSAGE", map.get("NOTICEMESSAGE") + "");
                                editor.putString("OEMID", map.get("OEMID") + "");
                                editor.putString("PHONENUMBER", map.get("PHONENUMBER") + "");
                                editor.putString("STS", map.get("STS") + "");
                                editor.putString("TXNSTS", map.get("TXNSTS") + "");
                                editor.putString("agentId", map.get("agentId") + "");
                                editor.putString("logNum", map.get("logNum") + "");
                                editor.putString("lng", lng + "");
                                editor.putString("lat", lat + "");
                                editor.commit();
                                Intent it = new Intent(WXEntryActivity.this, ViewPagerMainActivity.class);
                                startActivity(it);
                                finish();
                            }


                          /*  if (result.get("UNIONID") != null) {
                                Log.e("", "oUNIONID:" + result.get("UNIONID"));
                                editor.putString("UNIONID", results.get("UNIONID").toString());
                            }*/


                        } else {
                            Toast.makeText(WXEntryActivity.this, oJSON.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        //  loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(WXEntryActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
