package td.com.xiaoheixiong.fragments;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.dialogs.LoadingDialogWhole;

public abstract class BaseLazyFragment extends Fragment {
	/** Fragment当前状态是否可见 */
	protected boolean isVisible;
	protected LoadingDialogWhole loadingDialogWhole;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	/** * 可见 */
	protected void onVisible() {
		lazyLoad();
	}

	/** * 不可见 */
	protected void onInvisible() {
	}

	/** * 延迟加载 * 子类必须重写此方法 */
	protected abstract void lazyLoad();
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
	}

	/**
	 * 先加载数据显示dialog
	 *
	 * @param msg
	 */
	public void showLoadingDialog(String msg) {
		loadingDialogWhole = new LoadingDialogWhole(getActivity(), R.style.CustomDialog, msg);
		loadingDialogWhole.setCancelable(false);
//		loadingDialogWhole.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
		loadingDialogWhole.setCanceledOnTouchOutside(true);
		loadingDialogWhole.show();
	}
	
}
