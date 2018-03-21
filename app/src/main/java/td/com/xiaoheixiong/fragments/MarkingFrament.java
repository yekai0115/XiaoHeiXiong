package td.com.xiaoheixiong.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.MarkingAdapter;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.beans.TuanTuan.TTDetalBean;
import td.com.xiaoheixiong.dialogs.DialogConfirm;
import td.com.xiaoheixiong.eventbus.Msgevent3;
import td.com.xiaoheixiong.eventbus.Msgevent5;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.interfaces.ListItemClickHelp;
import td.com.xiaoheixiong.views.MyListView;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;


/**
 * 营销活动
 */

public class MarkingFrament extends BaseLazyFragment implements PullLayout.OnRefreshListener, ListItemClickHelp {

    private View mainView;
    private Context context;
    private int position;
    private ListView lv_marking;
    private PullLayout refresh_view;


    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private MarkingAdapter adapter;
    private List<TTBean> ttBeanList = new ArrayList<TTBean>();
    private String mercId;
    private int pageNum = 0;


    public MarkingFrament() {
        super();
    }

    public static MarkingFrament newInstance(int position) {
        MarkingFrament orderFrament = new MarkingFrament();
        Bundle b = new Bundle();
        b.putInt("position", position);
        orderFrament.setArguments(b);
        return orderFrament;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        EventBus.getDefault().register(this);
        mercId = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.fragment_marking, container, false);
            setWidget();
            refresh_view.setOnRefreshListener(this);
            context = getActivity();
            adapter = new MarkingAdapter(getActivity(), ttBeanList, this,position);
            lv_marking.setAdapter(adapter);
            isPrepared = true;
            lazyLoad();
        }
        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) mainView.getParent();
        if (parent != null) {
            parent.removeView(mainView);
        }

        return mainView;

    }

    private void setWidget() {
        lv_marking = (MyListView) mainView.findViewById(R.id.lv_marking);
        refresh_view = (PullLayout) mainView.findViewById(R.id.refresh_view);

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getOrderList(1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();// 这个必须这里初始化，要不会空指针


    }

    // 进入采购详情返回后刷新
    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    /**
     *
     *
     * @param state
     */
    private void getOrderList(final int state) {
        showLoadingDialog("");
        HashMap<String, Object> maps = new HashMap<>();
       maps.put("mercId", mercId);
        maps.put("size", 10);
        maps.put("page", pageNum);
        String url="";
        if(position==1){
            url=HttpUrls.XHX_tuantuan_list;
        }else if(position==2){
            url=HttpUrls.XHX_miaomiao_list;
        }

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(url, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        mHasLoadedOnce=true;
                        Log.e("result", result + "");
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.SUCCEED);
                        }
                        if (result == null) {
                            return;
                        }
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            TTDetalBean jsonRootBean = GsonUtil.GsonToBean(oJSON.toString(), TTDetalBean.class);
                            List<TTBean> datas = jsonRootBean.getRSPDATA();//
                            if (state == 2) {
                                ttBeanList.clear();
                            }
                            ttBeanList.addAll(datas);
                            adapter.updateListview(ttBeanList);
                            setListviewHeight(lv_marking);
                        } else {

                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.FAIL);
                        }
                    }
                });
    }


    /**
     * 下拉
     */
    @Override
    public void onRefresh(PullLayout pullToRefreshLayout) {
        getOrderList(2);

    }

    /**
     * 上拉
     */
    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View item, View widget, int position, int which) {
        TTBean ttBean = ttBeanList.get(position);
        String id = ttBean.getId();
        Intent intent;
        switch (which) {
            case R.id.tv_delete://删除
                showConfirmReceiptDialog(id);
                break;
            case R.id.tv_detal://查看详情
                break;
            default:
                break;
        }
    }


    /**
     * 自动匹配listview的高度
     *
     * @param
     */
    private void setListviewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listitemView = listAdapter.getView(i, null, listView);
            listitemView.measure(0, 0);
            totalHeight += listitemView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 60;
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
    }


    /**
     * 删除对话框
     */
    private void showConfirmReceiptDialog(final String order_sn) {
        DialogConfirm alert = new DialogConfirm();
        alert.setListener(new DialogConfirm.OnOkCancelClickedListener() {
            @Override
            public void onClick(boolean isOkClicked) {
                if (isOkClicked) {
                   loadingDialogWhole.show();
                    confirmReceipt(order_sn);
                }

            }
        });
        alert.showDialog(getActivity(), "确认删除吗?", "确定", "取消");
    }

    /**
     * 删除
     *
     */
    private void confirmReceipt(String id) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("id", id);
        String url = "";
        if (position == 1) {
            url = HttpUrls.XHX_delete_tuantuan;
        } else if (position == 2) {//秒秒
            url = HttpUrls.XHX_delete_miaomiao;
        }
        OkHttpClientManager.getInstance(context).requestAsyn(url, OkHttpClientManager.TYPE_GET,
                maps, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        if (jsonObj.get("RSPCOD").equals("000000")) {
                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                            getOrderList(2);
                        } else {

                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(Msgevent5 msgevent3) {
        getOrderList(2);
    }


}
