package td.com.xiaoheixiong.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import td.com.xiaoheixiong.beans.MyConstant;


/**
 * Created by StormShadow on 2017/3/28.
 * Knowledge is power.
 */

public class ImgSetUtil {
    public static Date getYesterdayDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 将Bitmap转换成指定大小
     */
    public static Bitmap adjustBitmapSize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    public static Uri geturi(Context context, Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }






    public static String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    public static String getImgKeyString(String userId) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy/MMdd");
        String dateStr = dateFormat.format(new Date());

        long curTime = System.currentTimeMillis();
        Timestamp tsTemp = new Timestamp(curTime);
        String timeStamp =  tsTemp.toString();
        String timeStampMd5 = ImgSetUtil.md5(timeStamp);

        return dateStr + userId + "/" + timeStampMd5 + ".jpg";
    }

    /**
     *
     * @param userId
     * @return
     */
    public static String getZipKeyString(String userId) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy/MMdd");
        String dateStr = dateFormat.format(new Date());

        long curTime = System.currentTimeMillis();
        Timestamp tsTemp = new Timestamp(curTime);
        String timeStamp =  tsTemp.toString();
        String timeStampMd5 = ImgSetUtil.md5(timeStamp);

        return dateStr + userId + "/" + timeStampMd5 + ".zip";
    }





    public static String getImgKeyString() {
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy/MMdd");
        String dateStr = dateFormat.format(new Date());

        long curTime = System.currentTimeMillis();
        Timestamp tsTemp = new Timestamp(curTime);
        String timeStamp =  tsTemp.toString();
        String timeStampMd5 = ImgSetUtil.md5(timeStamp);
        return dateStr + "/" + timeStampMd5 + ".jpg";
//        return dateStr + userId + "/" + timeStampMd5 + ".jpg";
    }
    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            URL myFileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }


    public static Bitmap getAdjustCompressBitmapByPath(String path) {
        Bitmap bitmap1;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 4;
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            bitmap1 = adjustBitmapSize(bitmap, 30, 30);
        }catch (Exception e){
            return null;
        }
        return bitmap1;
    }
}
