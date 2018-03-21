package td.com.xiaoheixiong.Utils.signUtil;

import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class SignaTureUtils {
	final static String signkeyMd5 = "d4a45e4482ac463a38f3sd324few42f";// MD5
	final String signkeyAes = "f15f1ede25a2471998ee06edba7d2e29";// AES

	public static String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
		String dateString = formatter.format(currentTime);
		Log.e("shijian", dateString);

		return dateString;
	}

	public static HashMap<String, Object> MD5signaTure(HashMap<String, Object> map) {
		Log.e("map", map+"");
		String timestamp = getStringDateMerge();
		String sign;
		try {
			StringBuilder Params = new StringBuilder();
			if (map != null) {
				 Object[] keys = map.keySet().toArray();
			        Arrays.sort(keys);
				for (int i = 0; i < keys.length; i++) {
					if("".equals(map.get(keys[i]))){
						continue;
					}
					if (keys[i].equals("sign")) {
						continue;
					}
				//	Params.append(keys[i]).append("=").append(map.get(keys[i])).append("&");
					Params.append(keys[i]).append("=").append(URLEncoder.encode(String.valueOf(map.get(keys[i])), "utf-8")).append("&");
		        }
			}else{
				
			}
			Params.append("timestamp="+timestamp);
			Params.append("&key="+ signkeyMd5);
			sign = MD5Util.MD5(Params.toString());
			Log.e("Params", Params+"");
			Log.e("sign", sign+"");
			System.out.println(sign);
			System.out.println(Params);
			map.remove("timestamp");
			map.put("sign", sign);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;

	}
	
	
	public static String MD5signaTure2(HashMap<String, Object> map) {
		Log.e("map", map+"");
		String timestamp = getStringDateMerge();
		String sign = null;
		try {
			StringBuilder Params = new StringBuilder();
			if (map != null) {
				 Object[] keys = map.keySet().toArray();
			        Arrays.sort(keys);
				for (int i = 0; i < keys.length; i++) {
					if("".equals(map.get(keys[i]))){
						continue;
					}
					if (keys[i].equals("sign")) {
						continue;
					}
				//	Params.append(keys[i]).append("=").append(map.get(keys[i])).append("&");
					Params.append(keys[i]).append("=").append(URLEncoder.encode(String.valueOf(map.get(keys[i])), "utf-8")).append("&");
		        }
			}else{
				
			}
			Params.append("timestamp="+timestamp);
			Params.append("&key="+ signkeyMd5);
			sign = MD5Util.MD5(Params.toString());
			Log.e("Params", Params+"");
			Log.e("sign", sign+"");
			System.out.println(sign);
			System.out.println(Params);
			map.remove("timestamp");
			map.put("sign", sign);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sign;

	}

	private String AESsignaTure(JSONObject jsonObj) {
		// TODO Auto-generated method stub

		// JSONObject jsonObj = new JSONObject();

		String aa = null;
		try {
			aa = DESKey.AES_Encode(jsonObj.toString(), signkeyAes);
			aa = URLEncoder.encode(aa);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aa;
	}

}