package td.com.xiaoheixiong.Utils.permissionManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * @Description:
 * @Author James Lin
 * @Date 2016/12/5 16:28
 */
public class PermissionsCheckerUtil {

    private static Context mContext;

    public PermissionsCheckerUtil(Context context) {
        mContext = context;
    }

    // 判断权限集合
    public static boolean lacksPermissions(Context mContext, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContext,permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private static boolean lacksPermission(Context mContext, String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }
}
