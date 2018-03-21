/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package td.com.xiaoheixiong.Utils;

import android.app.Application;
import android.content.Context;


import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;

import td.com.xiaoheixiong.crash.CrashHandler;
import td.com.xiaoheixiong.crash.CreateFile;


public class BNaviDemoApplication extends Application {
    private static Context mContext;
    private static BNaviDemoApplication instance;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        //    networkType = setNetworkType();
        // 启动定位
        //   LBSLocation.getInstance(this).startLocation();
        mContext = getApplicationContext();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);


        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
        CrashHandler.getInstance().init(this, new File(CreateFile.CRASH_PATH), null);
        CreateFile creat = new CreateFile(getInstance());
        creat.Create();

        PgyCrashManager.register(getApplicationContext());
    }

    public static BNaviDemoApplication getInstance() {
        return instance;
    }
}
