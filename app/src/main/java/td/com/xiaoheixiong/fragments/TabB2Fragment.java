package td.com.xiaoheixiong.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.HeadLineDetalctivity;
import td.com.xiaoheixiong.adapter.MainAdapter;
import td.com.xiaoheixiong.adapter.NineGridTestAdapter;
import td.com.xiaoheixiong.beans.Detail;
import td.com.xiaoheixiong.beans.JsonRootBean;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.dialogs.nicedialog.BaseNiceDialog;
import td.com.xiaoheixiong.dialogs.nicedialog.NiceDialog;
import td.com.xiaoheixiong.dialogs.nicedialog.ViewConvertListener;
import td.com.xiaoheixiong.dialogs.nicedialog.ViewHolder;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.interfaces.ListItemClickHelp;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableListView;


public class TabB2Fragment extends BaseFragment implements PullLayout.OnRefreshListener, ListItemClickHelp {


    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private View view;
    private PullLayout refresh_view;
    private PullableListView lv_toutiao;

    private NineGridTestAdapter adapter;
    private String pageSize = "10", MERCNUM;
    private int pageNum = 1;
    private int pages;


    private List<TouTiaoBean> touTiaoBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_b_new, container, false);
        MERCNUM = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        initview();
        getdata(1);
        return view;
    }

    private void initview() {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        back_img = (ImageView) view.findViewById(R.id.back_img);
        back_img.setVisibility(View.GONE);
        title_right_rl = (RelativeLayout) view.findViewById(R.id.title_right_rl);
        right_img = (ImageView) view.findViewById(R.id.right_img);
        title_right_rl.setVisibility(View.GONE);
        title_tv.setText("头条");
        refresh_view = (PullLayout) view.findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_toutiao = (PullableListView) view.findViewById(R.id.lv_toutiao);
        touTiaoBeanList = new ArrayList<>();
        adapter = new NineGridTestAdapter(getActivity(), this, 1);
        adapter.setList(touTiaoBeanList);
        lv_toutiao.setAdapter(adapter);

    }

    private void getdata(final int state) {
        showLoadingDialog("...");
        HashMap<String, Object> maps = new HashMap<>();
        // maps.put("mercId", MERCNUM);
        maps.put("mercId", "");
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSize);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_toutiao, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            JsonRootBean jsonRootBean = GsonUtil.GsonToBean(oJSON.toString(), JsonRootBean.class);
                            Detail detail = jsonRootBean.getDetail();
                            List<TouTiaoBean> list = detail.getList();
                            pages = detail.getPages();
                            if (state == 1) {
                                touTiaoBeanList.clear();
                            } else if (state == 2) {
                                touTiaoBeanList.clear();
                                refresh_view.refreshFinish(PullLayout.SUCCEED);
                            } else {
                                refresh_view.loadmoreFinish(PullLayout.SUCCEED);
                            }
                            touTiaoBeanList.addAll(list);
                        }
                        adapter.updataListView(touTiaoBeanList);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();


                        if (state == 1) {

                        } else if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.FAIL);
                        } else {
                            refresh_view.loadmoreFinish(PullLayout.FAIL);
                            pageNum--;
                        }
                    }
                });
    }


    @Override
    public void onRefresh(PullLayout pullToRefreshLayout) {

        pageNum = 1;
        getdata(2);
    }

    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {
        if (pageNum >= pages) {
            refresh_view.loadmoreFinish(PullLayout.SUCCEED);
            Toast.makeText(getContext(), "全部加载完成！", Toast.LENGTH_SHORT).show();
        } else {
            pageNum++;
            getdata(3);
        }

    }


    @Override
    public void onClick(View item, View widget, int position, int which) {
        TouTiaoBean bean = touTiaoBeanList.get(position);
        final int realPraise = bean.getRealPraise();
        final String id = bean.getId() + "";
        final TextView zanNum_tv = (TextView) item.findViewById(R.id.zanNum_tv);
        final ImageView zan_img = (ImageView) view.findViewById(R.id.zan_img);
        final LinearLayout zan_ll = (LinearLayout) view.findViewById(R.id.zan_ll);
        switch (which) {
            case R.id.tv_evaluate:
                Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.commit_layout, null);
                bottomDialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

                final EditText editText = (EditText) bottomDialog.findViewById(R.id.edit_input);
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, 0);
                    }
                });
                bottomDialog.setCanceledOnTouchOutside(true);
                bottomDialog.setCancelable(true);
                bottomDialog.show();
                break;
            case R.id.tv_zhuanfa://转发

                String mercImg=bean.getMercImg();//头像
                String mercName=bean.getMercName();
                List<String> imageList=bean.getImageList();
                String description= bean.getDescription();

                Intent intent=new Intent(getActivity(), HeadLineDetalctivity.class);
                intent.putExtra("mercImg",mercImg);
                intent.putExtra("mercName",mercName);
                intent.putStringArrayListExtra("imageList",(ArrayList<String>) imageList);
                intent.putExtra("description",description);
                startActivity(intent);
                break;
            case R.id.zan_ll:
                String mercId = MyCacheUtil.getshared(getContext()).getString("MERCNUM", "");
                HashMap<String, Object> mapss = new HashMap<>();
                //  mercId //当前登录的用户ID, 例如：M0081997,
                //         id //被评论的头条ID
                mapss.put("id", id);
                mapss.put("mercId", mercId);

                OkHttpClientManager.getInstance(getContext()).requestAsyn(HttpUrls.XHX_Zan, OkHttpClientManager.TYPE_GET,
                        mapss, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                            @Override
                            public void onReqSuccess(Object result) {
                                // TODO Auto-generated method stub
                                Log.e("result", result + "");
                                JSONObject jsonObj = new JSONObject().parseObject(result + "");
                                int Rzan = realPraise + 1;// 最新点赞数
                                zanNum_tv.setText(Rzan + "");
                                zan_img.setImageResource(R.drawable.headline_praise1);
                                zan_ll.setEnabled(false);
                            }

                            @Override
                            public void onReqFailed(String errorMsg) {
                                // TODO Auto-generated method stub
                            }
                        });

                break;

        }

    }

}
