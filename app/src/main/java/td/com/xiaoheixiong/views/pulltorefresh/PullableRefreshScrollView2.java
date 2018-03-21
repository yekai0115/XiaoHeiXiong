package td.com.xiaoheixiong.views.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;



import td.com.xiaoheixiong.interfaces.ScrollViewListener;



public class PullableRefreshScrollView2 extends ScrollView implements Pullable
{
	/**是否需要上拉：默认不需要*/
	private Boolean needPullUp=false;

	private ScrollViewListener scrollViewListener = null;

	private OnScrollViewListener onScrollViewListener;

	public PullableRefreshScrollView2(Context context)
	{
		super(context);
	}

	public PullableRefreshScrollView2(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableRefreshScrollView2(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * 下拉
	 */
	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 上拉
	 */
	@Override
	public boolean canPullUp()
	{
//		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
//			return true;
//		else
		if(needPullUp){
			return true;
		}else{
			return false;
		}

	}


	/**
	 * @author zhongqihong
	 * 定义ScrollView滚动时回调的接口
	 * onScroll方法用于返回的myScrollView滑动的Y方向的距离
	 * */
	public interface OnScrollViewListener{
		public void onScroll(int scrollY);
	}
	/**
	 * @author zhongqihong
	 * 设置滚动时候的接口
	 * */
	public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
		this.onScrollViewListener = onScrollViewListener;
	}





	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}

		if (onScrollViewListener!=null) {
			onScrollViewListener.onScroll(y);
		}
	}




}
