package td.com.xiaoheixiong.crash;

import android.app.Activity;



import java.util.ArrayList;
import java.util.List;

import td.com.xiaoheixiong.Utils.BNaviDemoApplication;

public class ActivityManager {
	private static ActivityManager mActivityManager;
	private List<Activity> activityList;
	private BNaviDemoApplication baseApplication= BNaviDemoApplication.getInstance();

	public List<Activity> getActivityList() {
		return activityList;
	}

	private ActivityManager() {
		activityList = new ArrayList<Activity>();
	}

	public static ActivityManager getInstance() {
		if (mActivityManager == null) {
			mActivityManager = new ActivityManager();
		}
		return mActivityManager;
	}

	public void add(Activity activity) {
		this.activityList.add(activity);
	}

	public void remove(Activity activity) {
		this.activityList.remove(activity);
	}

	public void closeAllActivity() {
		for (Activity activity : activityList) {
			if (!activity.isFinishing())
				activity.finish();
		}
		this.activityList.clear();
	}
	public void exit() {
		closeAllActivity();
	}
}
