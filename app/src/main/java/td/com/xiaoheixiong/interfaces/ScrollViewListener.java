package td.com.xiaoheixiong.interfaces;


import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView2;

public interface ScrollViewListener {
  
    void onScrollChanged(PullableRefreshScrollView2 scrollView, int x, int y, int oldx, int oldy);
  
} 

