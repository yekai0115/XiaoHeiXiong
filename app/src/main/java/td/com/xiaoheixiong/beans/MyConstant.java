package td.com.xiaoheixiong.beans;


import android.os.Environment;

/**
 * 常量
 */

public class MyConstant {

   // public final static String WEB_SERVICE_BASE = "http://192.168.0.2:8014/";
    public final static String WEB_SERVICE_BASE = "http://transaction.xiaoheixiong.net/";
    public final static String SUCCESS = "000000";


    public static int PIC_DPI2 = 2;
    public static final int DEF_IMG_W = 800;
    public static final int DEF_IMG_H = 640;

    /**
     * 阿里云EndPoint
     */
    public static final String ALI_ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";
    public static final String ALI_KEYID = "LTAI6rTfz7ikikTG";
    public static final String ALI_KEYSECRET = "NRDMFWjDHmgvZGVh18WbWiUWC1KZ1R";


    /**
     * 上传下载的bucketName
     */
    public static final String ALI_BUCKET_PRIVATE = "litterblackbear-private-v1"; //
    public static final String ALI_PUBLIC_BUCKET_PUBLIC = "litterblackbear-public-v1"; //


    public static final String ALI_PUBLIC_URL = "https://litterblackbear-public-v1.oss-cn-shenzhen.aliyuncs.com/";


    /**
     * 本应用的文件
     */
    public final static String APP_HOME_PATH = Environment.getExternalStorageDirectory() + "/xhx/";

    public static final String SHARE_IMAGE_PATH = APP_HOME_PATH + "app_icon.jpg";

}
