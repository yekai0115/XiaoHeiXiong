package td.com.xiaoheixiong.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


import td.com.xiaoheixiong.Utils.signUtil.MD5Util;


/**
 * MD5加密类
 * @author alan
 * @version 创建时间：2016年9月26日 下午5:48:17
 */
public class MySignUtil {


	/**
	 * MD5加签 通过字典序排放，然后加上加密值，进行md5加密
	 * @author alan
	 * @param params 要加密的参数
	 * @param singKey 加密密钥
	 * @return 返回的加密后的字符串
	 */
	 public static String sign(Map<String, String> params,String singKey){
        List<String> keys = new ArrayList<String>(params.keySet());
        StringBuilder sb = new StringBuilder();
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            sb.append(params.get(key));
            sb.append("&");
        }
        sb.append("keys="+singKey);
        //sb.setLength(sb.length() - 1);
        System.out.println(sb.toString());
       // logger.info("排序后值"+sb.toString());
        String res = MD5Util.MD5(sb.toString());
        return res;
    }
	 
	 /**
	  * MD5加签 通过字典序排放，然后加上加密值，进行md5加密 对参与加密的value值做URLecode操作后才加密,并且排除空值
	  * @param params
	  * @param singKey
	  * @return
	  */
	 public static String sign4EncodeData(Map<String, String> params,String singKey){
		 	Map<String, String> eMap = new HashMap<String, String>();
		 	List<String> keys = new ArrayList<String>(params.keySet());
	        String value;
		 	for(String key : keys){
		 		value=params.get(key);
		 		if(StringUtils.isNotBlank(value)){
		 			try {
		 				value=URLEncoder.encode(value, "UTF-8");
		 			} catch (UnsupportedEncodingException e) {
		 				value=params.get(key);
					//	logger.error("shop");
					}
		 			eMap.put(key, value);
		 		}
		 		
	        }
	        return sign(eMap,singKey);
	    }
	
	/**
	 * 进行URLEncoder
	 * @author alan
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        } 
	}
	
	public static void main(String[] avgs){
		Map<String, String> params = new HashMap<String, String>();
		params.put("actid", "aa");
		params.put("tranAmt", "500");
		params.put("withdrawFlag", "1");
		params.put("payPassword", "123");
		String res = sign(params,"999999");
		System.out.println(res);
	}
	
    
}
