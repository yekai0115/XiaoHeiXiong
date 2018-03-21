package td.com.xiaoheixiong.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

public class MyCityDBHelper {

    Context context;
    private CityDB mCityDB;

    public MyCityDBHelper(Context context) {
        super();
        this.context = context;
    }

    public synchronized CityDB getCityDB() {
        if (mCityDB == null)
            mCityDB = openCityDB();
        return mCityDB;
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "td.com.xiaoheixiong"
                + File.separator + CityDB.CITY_DB_NAME;

        File db = new File(path);
        if (!db.exists()) {
            L.i("db is not exists");

            if (!new File(path).exists()) {
                try {
                    boolean flag = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/databases/").mkdirs();
                    boolean newFile = new File(path).createNewFile();
                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        InputStream in = context.getAssets().open("city.db");
                        byte[] buffer = new byte[1024];
                        int readBytes = 0;
                        while ((readBytes = in.read(buffer)) != -1)
                            out.write(buffer, 0, readBytes);
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return new CityDB(context, path);
    }
}