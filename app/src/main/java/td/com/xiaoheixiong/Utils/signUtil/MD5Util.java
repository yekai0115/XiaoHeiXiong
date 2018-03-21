package td.com.xiaoheixiong.Utils.signUtil;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


public class MD5Util {

	    public final static String MD5(String s) {
	        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
	        try {
	            byte[] btInput = s.getBytes();
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // 使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	  /*  public final static String EncoderByMd5(String str){
	    	String newstr= null;
			try {
				//确定计算方法
		    	 MessageDigest md5 = MessageDigest.getInstance("MD5");
			
	    	    BASE64Encoder base64en = new BASE64Encoder();
	    	     //加密后的字符串
	    	     newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
	    	         
	    	return newstr;
	    }*/
	    
	    /**
	     * 签名字符串
	     * @param text 需要签名的字符串
	     * @param key 密钥
	     * @param input_charset 编码格式
	     * @return 签名结果
	     */
	    public static String sign(String text, String key, String input_charset) {
	    	text = text + key;
	    	System.out.println(text);
	        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	    }
	    
	    /**
	     * 签名字符串
	     * @param text 需要签名的字符串
	     * @param sign 签名结果
	     * @param key 密钥
	     * @param input_charset 编码格式
	     * @return 签名结果
	     */
	    public static boolean verify(String text, String sign, String key, String input_charset) {
	    	text = text + key;
	    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
	    	if(mysign.equals(sign)) {
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
	    }

	    /**
	     * @param content
	     * @param charset
	     * @return
	     * @throws UnsupportedEncodingException
	     */
	    private static byte[] getContentBytes(String content, String charset) {
	        if (charset == null || "".equals(charset)) {
	            return content.getBytes();
	        }
	        try {
	            return content.getBytes(charset);
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
	        }
	    }
}
