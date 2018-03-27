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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.EmojiUtil;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.AddHeadLineActivity;
import td.com.xiaoheixiong.activity.HeadLineDetalctivity;
import td.com.xiaoheixiong.activity.LoginActivity;
import td.com.xiaoheixiong.activity.TransmitHeadlinActivity;
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
    private ImageView back_img, right_img, set_img;
    private RelativeLayout title_right_rl;
    private View view;
    private PullLayout refresh_view;
    private PullableListView lv_toutiao;

    private NineGridTestAdapter adapter;
    private String pageSize = "10", MERCNUM;
    private int pageNum = 0;
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
        right_img = (ImageView) view.findViewById(R.id.right_img2);
        set_img = (ImageView) view.findViewById(R.id.set_img);
        right_img.setVisibility(View.VISIBLE);
        set_img.setVisibility(View.GONE);
        title_tv.setText("头条");
        refresh_view = (PullLayout) view.findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_toutiao = (PullableListView) view.findViewById(R.id.lv_toutiao);
        touTiaoBeanList = new ArrayList<>();
        adapter = new NineGridTestAdapter(getActivity(), this, 1);
        adapter.setList(touTiaoBeanList);
        lv_toutiao.setAdapter(adapter);

        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHeadLineActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getdata(final int state) {
        showLoadingDialog("...");
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("page", pageNum);
        maps.put("size", pageSize);
        maps.put("checkPraiseId", MERCNUM);
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_toutiao, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        if (state == 1) {
                            touTiaoBeanList.clear();
                        } else if (state == 2) {
                            touTiaoBeanList.clear();
                            refresh_view.refreshFinish(PullLayout.SUCCEED);
                        } else {
                            refresh_view.loadmoreFinish(PullLayout.SUCCEED);
                        }
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            try {
                                JsonRootBean jsonRootBean = GsonUtil.GsonToBean(oJSON.toString(), JsonRootBean.class);
                                Detail detail = jsonRootBean.getDetail();
                                List<TouTiaoBean> list = detail.getLists();
                                pages = detail.getTotalPage();
                                touTiaoBeanList.addAll(list);
                            } catch (Exception e) {

                            }

                        } else {
                            Toast.makeText(getContext(), oJSON.getString("RSPDATA"), Toast.LENGTH_SHORT).show();
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

        pageNum = 0;
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
    public void onClick(View item, View widget, final int position, int which) {
        final TouTiaoBean bean = touTiaoBeanList.get(position);
        final String realPraise = bean.getReal_praise() == null ? "0" : bean.getReal_praise();
        final String id = bean.getId() + "";
        String mercImg = bean.getHeadImg();//头像
        String mercName = bean.getNickName();//商户名/昵称
        String imageList = bean.getImages();
        String description = bean.getDescription();
        String create_time = bean.getCreate_time();
        String location_desc = bean.getLocation_desc();
        final TextView zanNum_tv = (TextView) item.findViewById(R.id.zanNum_tv);
        final ImageView zan_img = (ImageView) view.findViewById(R.id.zan_img);
        final LinearLayout zan_ll = (LinearLayout) view.findViewById(R.id.zan_ll);
        String MOBILE = MyCacheUtil.getshared(getActivity()).getString("PHONENUMBER", "");
        Intent intent;
        switch (which) {
            case R.id.ll_detal://详情
                intent = new Intent(getActivity(), HeadLineDetalctivity.class);
                intent.putExtra("id", id);
                intent.putExtra("mercImg", mercImg);
                intent.putExtra("mercName", mercName);
                intent.putExtra("create_time", create_time);
                intent.putExtra("imageList", imageList);
                intent.putExtra("description", description);
                intent.putExtra("location_desc", location_desc);
                startActivity(intent);
                break;
            case R.id.tv_evaluate://评论
                if (StringUtils.isEmpty(MOBILE)) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    final Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
                    View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.commit_layout, null);
                    bottomDialog.setContentView(contentView);
                    ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                    layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                    contentView.setLayoutParams(layoutParams);
                    bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

                    final EditText editText = (EditText) bottomDialog.findViewById(R.id.edit_input);
                    final TextView tv_evaluate = (TextView) bottomDialog.findViewById(R.id.tv_evaluate);
                    editText.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(editText, 0);
                        }
                    });
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    tv_evaluate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String content = editText.getText().toString();
                            if (StringUtils.isEmpty(content)) {
                                Toast.makeText(getContext(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                            } else {
                                bottomDialog.dismiss();
                                String text=EmojiUtil.getString(content);
                                evaulate(text, id, bean);
                            }
                        }
                    });
                    bottomDialog.setCanceledOnTouchOutside(true);
                    bottomDialog.setCancelable(true);
                    bottomDialog.show();
                }
                break;
            case R.id.tv_zhuanfa://转发
                if (StringUtils.isEmpty(MOBILE)) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), TransmitHeadlinActivity.class);
                    intent.putExtra("mercImg", mercImg);
                    intent.putExtra("mercName", mercName);
                    intent.putExtra("imageList", imageList);
                    intent.putExtra("description", description);
                    startActivity(intent);
                }
                break;
            case R.id.zan_ll:
                if (StringUtils.isEmpty(MOBILE)) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    String mercId = MyCacheUtil.getshared(getContext()).getString("MERCNUM", "");
                    HashMap<String, Object> mapss = new HashMap<>();
                    //  mercId //当前登录的用户ID, 例如：M0081997,
                    //         id //被评论的头条ID
                    mapss.put("headlineId", id);
                    mapss.put("mercId", mercId);
                    mapss.put("state", 1);

                    OkHttpClientManager.getInstance(getContext()).requestAsyn(HttpUrls.XHX_Zan, OkHttpClientManager.TYPE_GET,
                            mapss, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                                @Override
                                public void onReqSuccess(Object result) {
                                    // TODO Auto-generated method stub
                                    Log.e("result", result + "");
                                    JSONObject jsonObj = new JSONObject().parseObject(result + "");
                                    int Rzan = Integer.valueOf(realPraise) + 1;// 最新点赞数

                                    if (jsonObj.get("RSPCOD").equals("000000")) {
                                        touTiaoBeanList.get(position).setPraise(true);
                                        touTiaoBeanList.get(position).setReal_praise(Rzan + "");
                                    }else{
                                        Toast.makeText(getContext(), jsonObj.getString("RSPDATA"), Toast.LENGTH_SHORT).show();
                                    }
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onReqFailed(String errorMsg) {
                                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;

        }

    }

    /**
     * 评论
     *
     * @param content
     * @param id
     */
    private void evaulate(String content, final String id, final TouTiaoBean bean) {
        HashMap<String, Object> mapss = new HashMap<>();
        //  mercId //当前登录的用户ID, 例如：M0081997,
        //         id //被评论的头条ID
        mapss.put("headline_id", id);
        mapss.put("mer_id", MERCNUM);
        mapss.put("comments", content);
        OkHttpClientManager.getInstance(getContext()).requestAsyn(HttpUrls.XHX_evaulate, OkHttpClientManager.TYPE_GET,
                mapss, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        try {
                            if (jsonObj.get("RSPCOD").equals("000000")) {
                                Toast.makeText(getContext(), jsonObj.getString("RSPMSG"), Toast.LENGTH_SHORT).show();
                                getdata(2);

                                String mercImg = bean.getHeadImg();//头像
                                String mercName = bean.getNickName();//商户名/昵称
                                String imageList = bean.getImages();
                                String description = bean.getDescription();
                                String create_time = bean.getCreate_time();
                                String location_desc = bean.getLocation_desc();

                                Intent intent = new Intent(getActivity(), HeadLineDetalctivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("mercImg", mercImg);
                                intent.putExtra("mercName", mercName);
                                intent.putExtra("create_time", create_time);
                                intent.putExtra("imageList", imageList);
                                intent.putExtra("description", description);
                                intent.putExtra("location_desc", location_desc);
                                startActivity(intent);


                            }
                            Toast.makeText(getContext(), jsonObj.getString("RSPMSG"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
