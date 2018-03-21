package td.com.xiaoheixiong.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

import java.util.Set;

/**
 * 
 * FUNCTION：缓存工具类
 * 
 * 
 */
@SuppressLint("CommitPrefEdits")
public class MyCacheUtil {

	// 缓存对象
	private static SharedUtile my;

	/**
	 * 构造方法
	 * 上下文F
	 * @param
	 */
	public MyCacheUtil(Context context) {

		// 取得缓存对象的实例化
		my = SharedUtile.getMySharedPreferencesObejct(context);

	}

	public static Editor setshared(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bbusers",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		return editor;
	}

	//
	public static SharedPreferences getshared(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bbusers",
				Context.MODE_PRIVATE);
		return preferences;
	}

	/**
	 * 1、存储字符串
	 * 存储值的名字
	 * 存储的值
	 * @param
	 * @param
	 */
	public void putString(String key, String values) {
		// 存储字符串
		my.putString(key, values);
	}

	/**
	 * 2、存储整型
	 * 
	 * @param
	 * @param
	 */
	public void putInt(String key, int values) {
		// 存储整型
		my.putInt(key, values);
	}

	/**
	 * 3、存储浮点型
	 * 
	 * @param
	 * @param
	 */
	public void putFolat(String key, float values) {
		// 存储浮点型
		my.putFloat(key, values);
	}

	/**
	 * 4、存储布尔型
	 * 
	 * @param
	 * @param
	 */
	public void putBoolean(String key, boolean values) {
		// 存储布尔型
		my.putBoolean(key, values);
	}

	/**
	 * 5、存储长整型
	 * 
	 * @param
	 * @param
	 */
	public void putLong(String key, long values) {
		// 存储长整型
		my.putLong(key, values);
	}

	/**
	 * 6、存储Set集合
	 * 
	 * @param
	 * @param
	 */
	public void putStringSet(String key, Set<String> values) {
		// 存储Set集合
		my.putStringSet(key, values);
	}

	/**
	 * 0、删除数据
	 * 
	 * @param
	 */
	public void remove(String key) {
		// 删除数据
		my.remove(key);
	}

	/**
	 * 1、取得字符串
	 * 
	 * @param
	 */
	public String getString(String key) {
		// 取得字符串
		return my.getString(key);
	}

	/**
	 * 2、取得浮点型
	 * 
	 * @param
	 */
	public float getFloat(String key) {
		// 取得浮点型
		return my.getFloat(key);
	}

	/**
	 * 3、取得布尔型
	 * 
	 * @param
	 */
	public boolean getBoolean(String key) {
		// 取得布尔型
		return my.getBoolean(key);
	}

	/**
	 * 4、取得长整型
	 * 
	 * @param
	 */
	public long getLong(String key) {
		// 取得长整型
		return my.getLong(key);
	}

	/**
	 * 5、取得整型
	 * 
	 * @param
	 */
	public int getInt(String key) {
		// 取得整型
		return my.getInt(key);
	}

	/**
	 * 6、取得Set容器
	 * 
	 * @param
	 */
	public Set<String> getStringSet(String key) {
		// 取得容器
		return my.getStringSet(key);
	}

	/**
	 * 提交
	 */
	public void commit() {
		// 提交设置
		my.commit();

	}

	/**
	 * 7、添加对象
	 * 
	 * @param
	 * @param
	 */
	public void putObject(Object obj, String key) {
		// 存储对象
		my.putObject(obj, key);
	}

	/**
	 * 7、取得对象
	 * 
	 * @param
	 */
	public Object getObject(String key) {
		// 取得对象
		return my.getObject(key);
	}

	/**
	 * 8、添加图片
	 * 
	 * @param
	 * @param
	 */
	public void putBitmap(Bitmap bitmap, String key) {
		// 存储图片
		my.putBitmap(bitmap, key);
	}

	/**
	 * 8、取得图片
	 * 
	 * @param
	 */
	public Bitmap getBitmap(String key) {
		// 取得图片
		return my.getBitmap(key);
	}

}
