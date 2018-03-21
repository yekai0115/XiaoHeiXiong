package td.com.xiaoheixiong.fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.dialogs.LoadingDialogWhole;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;

public class BaseFragment extends Fragment {
	protected String tag = "BaseFragment";
	private int index;
	/** 无按钮的进度dialog */
	protected LoadingDialogWhole loadingDialogWhole;
	private OneButtonDialogWhite button;
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void switchContent(Fragment from, Fragment to, int fragment, int num) {

		// if (from != to) {
		// from = to;
		// FragmentTransaction transaction =
		// getFragmentManager().beginTransaction().setCustomAnimations(
		// R.anim.slide_left_in, R.anim.slide_left_out);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			transaction.add(fragment, to); // 隐藏当前的fragment，add下一个到Activity中

			if (num == 1) {
				transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out).show(to).hide(from)
						.commit();
			} else if (num == 0) {
				transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out).show(to).hide(from)
				.commit();
			}

			Log.e("", "fragmentTo");
		} else {
			
			if (num == 1) {
				transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out).show(to).hide(from)
						.commit();
			} else if (num == 0) {
				transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out).show(to).hide(from)
				.commit();
			}
			Log.e("++", "fragmentTo++");
		}

	}
	
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/* 点击图片放大查看 */

	public void getBigPicture(Bitmap b) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
		final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
		ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
		if (b != null) {
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			int scaleWidth = display.getWidth();
			int height = b.getHeight();// 图片的真实高度
			int width = b.getWidth();// 图片的真实宽度
			LayoutParams lp = (LayoutParams) img.getLayoutParams();
			lp.width = scaleWidth;// 调整宽度
			lp.height = (height * scaleWidth) / width;// 调整高度
			img.setLayoutParams(lp);
			img.setImageBitmap(b);
			dialog.setView(imgEntryView); // 自定义dialog
			dialog.show();
		}
		// 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
		imgEntryView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				if (dialog.isShowing()) {
					dialog.cancel();
				}
			}
		});
		
	}

	/**获取相册图片资源Uri转string路径      **/
	public static String getRealFilePath(final Context context, final Uri uri ) {
	    if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
	
	public static Map<String, String> reflect(Object e, String entiyName) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {

			// System.out.println("tt");
			Log.e("reflect", entiyName);
			Class cls = e.getClass();
			// System.out.println("---" + e.getClass().getName());
			Field[] fields = cls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				f.setAccessible(true);
				// System.out.println("属性名:" + f.getName() + " 属性值:" + f.get(e));
				Log.e("reflect", "属性名:" + f.getName() + " 属性值:" + f.get(e));
				if (entiyName == null || entiyName.equals("")) {
					map.put(f.getName(), (String) f.get(e));
				} else {
					map.put(entiyName + "." + f.getName(), (String) f.get(e));
				}
			}
		}catch (Exception e1){
			Log.e("Exception", e1.getMessage());
		}
		return map;
	}
	
	/**
	 * 先加载数据显示dialog
	 * 
	 * @param msg
	 */
	public void showLoadingDialog(String msg) {
		loadingDialogWhole = new LoadingDialogWhole(getActivity(), R.style.CustomDialog, msg);
		loadingDialogWhole.setCancelable(false);
		loadingDialogWhole.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		loadingDialogWhole.setCanceledOnTouchOutside(false);
		loadingDialogWhole.show();
	}
	
	public void showPermission() {
		 button = new OneButtonDialogWhite(getActivity(), "为保证应用正常使用，需开启应用相机和存储权限！", "前往设置", new OnMyDialogClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri packageURI = Uri.parse("package:"+ "td.com.xiaoheixiong");
		               Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
		               startActivity(intent); 
					Log.e("ddd", "jin....");
					button.dismiss();
				}

			});
		 button.setCancelable(false);
		 button.setCanceledOnTouchOutside(false);
		 button.show();
		}
	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view,
								   RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.right = space;
			outRect.bottom = space;

			// Add top margin only for the first item to avoid double space between items
			if (parent.getChildPosition(view) != 0)
				outRect.top = space;
		}

	}
}