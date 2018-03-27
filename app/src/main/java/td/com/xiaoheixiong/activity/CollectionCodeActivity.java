package td.com.xiaoheixiong.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.CommonUtils;
import td.com.xiaoheixiong.Utils.FileUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.MySignUtil;
import td.com.xiaoheixiong.Utils.SavePictureUtil;
import td.com.xiaoheixiong.Utils.permissionManager.PermissionsCheckerUtil;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CollectionCodeActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.code_img)
    ImageView codeImg;

    @Bind(R.id.ll_code)
    LinearLayout ll_code;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.code_share_img)
    ImageView code_share_img;

    @Bind(R.id.saveImg_tv)
    TextView saveImgTv;

    private String MERCNUM, type, ACTNAM;
    private Bitmap bitmap;
    public static final File PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    private OneButtonDialogWhite button;

    private String url;
    private String headImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_code);
        ButterKnife.bind(this);
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        ACTNAM = MyCacheUtil.getshared(this).getString("ACTNAM", "");
        headImgUrl = MyCacheUtil.getshared(this).getString("headImgUrl", "");
        Intent it = getIntent();
        type = it.getStringExtra("type");
        Log.e("type", type + "");
        initview();

    }

    private void initview() {

        if (type.equals("0")) {
            titleTv.setText("付款");
            ll_code.setVisibility(View.VISIBLE);
            saveImgTv.setVisibility(View.GONE);
//            final String MD5_MER_SIGNKEY = "f15f1ede28a24DFRDe123@@1ba7d2e29";
//            Map params=new HashMap();
////http://www.xiaoheixiong.net/xhx/web/merPay/scan?sign=A03D59EADE2BB24469B20864E149FFDD&mercId=M00000062&custMercId=null
//            params.put("mercId",MERCNUM);
//            //    params.put("custMercId","");
//            String sign = MySignUtil.sign(params, MD5_MER_SIGNKEY);
//            url="http://www.xiaoheixiong.net/xhx/web/merPay/scan?sign="+sign+"&mercId="+MERCNUM+"&custMercId=null";
//            url = "http://pay.xiaoheixiong.net/public/getOpenid_uid?mer_id=" + MERCNUM;
//            File appIconfile = createFile();
//            if (StringUtils.isEmpty(headImgUrl)) {
//                headImgUrl = appIconfile.getAbsolutePath();
//            }
//
//            final String filePath = getFileRoot(CollectionCodeActivity.this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    boolean success = CommonUtils.createQRImage(url, 200, 200, headImgUrl, filePath);
//                    if (success) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                bitmap = BitmapFactory.decodeFile(filePath);
//                                codeImg.setImageBitmap(bitmap);
//                            }
//                        });
//                    }
//                }
//            }).start();

        } else if (type.equals("1")) {
            titleTv.setText("分享码");
            ll_code.setVisibility(View.GONE);
            saveImgTv.setVisibility(View.VISIBLE);
          //  getData();
        }
        getData();
    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }


    private void getData() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //  mercId //商户ID(必填), 例如：M0081997,
        //          custMercId // 收营员ID，可为空
        HashMap<String, Object> maps = new HashMap<>();


        String HttpUrl = null;
        if (type.equals("0")) {//付款码
            HttpUrl = HttpUrls.XHX_collection_code2;
            maps.put("mer_id", MERCNUM);
        } else if (type.equals("1")) {//分享码
            HttpUrl = HttpUrls.XHX_shareCode;
            maps.put("mercId", MERCNUM);
            maps.put("custMercId", "");
            maps.put("rebuildFlag", "1");

            if (ACTNAM.equals("") || ACTNAM.equals("null")) {
                maps.put("mercName", "游客");
            } else {
                maps.put("mercName", ACTNAM);
            }
        }
        //   参数
        //  "mercId":,
        //          "custMercId":'',
        //         "rebuildFlag":'1',
        //         "mercName"
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrl, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();
                        if (result.equals("") || result == null) {
                            return;
                        }
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            if (type.equals("1")) {
                                final String imgUrl = oJSON.get("detail") + "";
                                Glide.with(CollectionCodeActivity.this).load(imgUrl).asBitmap().into(code_share_img);
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {

                                        bitmap = returnBitmap(imgUrl);
                                    }
                                }).start();
                            } else {
                                url = "http://pay.xiaoheixiong.net/public/getOpenid_uid?mer_id=" + MERCNUM;
                                String title=oJSON.getString("detail");
                                tv_name.setText(title);
                                File appIconfile = createFile();
                                if (StringUtils.isEmpty(headImgUrl)) {
                                    headImgUrl = appIconfile.getAbsolutePath();
                                }

                                final String filePath = getFileRoot(CollectionCodeActivity.this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean success = CommonUtils.createQRImage(url, 250, 250, headImgUrl, filePath);
                                        if (success) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    bitmap = BitmapFactory.decodeFile(filePath);
                                                    codeImg.setImageBitmap(bitmap);
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), oJSON.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @OnClick({R.id.back_img, R.id.saveImg_tv,R.id.code_img,R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.saveImg_tv:
                if (PermissionsCheckerUtil.lacksPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    button = new OneButtonDialogWhite(this, "为保证应用正常使用，需开启应用存储权限！", "前往设置", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            button.dismiss();
                        }

                    });
                    button.setCancelable(false);
                    button.setCanceledOnTouchOutside(false);
                    button.show();
                    return;
                }
                ;
                saveImageToGallery(bitmap);
                // SavePictureUtil.saveImageToGallery(this, bitmap, SavePictureUtil.ScannerType.MEDIA);
                //  MediaStore.Images.Media.insertImage(this.getContentResolver(),
                //          file.getAbsolutePath(), response.getName(), null);

                // MediaScannerConnection.scanFile(mContext, new String[]{file.getAbsolutePath()}, null, null);
                break;
            case R.id.code_img:
                if (PermissionsCheckerUtil.lacksPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    button = new OneButtonDialogWhite(this, "为保证应用正常使用，需开启应用存储权限！", "前往设置", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            button.dismiss();
                        }

                    });
                    button.setCancelable(false);
                    button.setCanceledOnTouchOutside(false);
                    button.show();
                    return;
                }
                ;
                saveImageToGallery(bitmap);
                break;
            case R.id.tv_save:
                if (PermissionsCheckerUtil.lacksPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    button = new OneButtonDialogWhite(this, "为保证应用正常使用，需开启应用存储权限！", "前往设置", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            button.dismiss();
                        }

                    });
                    button.setCancelable(false);
                    button.setCanceledOnTouchOutside(false);
                    button.show();
                    return;
                }
                ;
                saveImageToGallery(bitmap);
                break;
        }
    }

    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
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

    public void saveImageToGallery(Bitmap bmp) {
        if (bmp == null) {
            Toast.makeText(getApplicationContext(), "保存失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 首先保存图片

        File appDir = new File(PATH, "");
        if (appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        Log.e("PATH", PATH.toString());
        Log.e("file", file.toString());
        Log.e("bmp", bmp.toString());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "保存失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "保存失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "保存失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

		/*
         * // 其次把文件插入到系统图库 try {
		 * MediaStore.Images.Media.insertImage(this.getContentResolver(),
		 * file.getAbsolutePath(), fileName, null); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); }
		 */

        // 最后通知图库更新
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        this.sendBroadcast(intent);
        Toast.makeText(getApplicationContext(), "图片保存成功！", Toast.LENGTH_SHORT).show();
    }


    private File createFile() {
        File file = new File(MyConstant.APP_HOME_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        File appIconfile = new File(MyConstant.SHARE_IMAGE_PATH);
        try {
            if (!appIconfile.exists()) {
                appIconfile.createNewFile();
            }
            FileUtils.copyAssetsFile(getApplicationContext(), "app_icon.png", appIconfile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appIconfile;
    }

}
