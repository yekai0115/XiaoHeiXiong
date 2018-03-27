package td.com.xiaoheixiong.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.KeyBoardUtils;
import td.com.xiaoheixiong.Utils.ListUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.MySignUtil;
import td.com.xiaoheixiong.Utils.signUtil.MD5Util;
import td.com.xiaoheixiong.adapter.HeadLindDetalAdapter;
import td.com.xiaoheixiong.beans.Detail;
import td.com.xiaoheixiong.beans.HeadLineDetal.CommentBase;
import td.com.xiaoheixiong.beans.HeadLineDetal.CommentBean;
import td.com.xiaoheixiong.beans.JsonRootBean;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView2;
import td.com.xiaoheixiong.views.toutiao.NineGridTestLayout;


/**
 *
 */
public class HeadLineDetalctivity extends BaseActivity implements View.OnClickListener, PullableRefreshScrollView2.OnScrollViewListener {

    private ImageView img_back;
    private ImageView img_add;

    private ImageView head_img;
    private TextView name_tv;
    private TextView time_tv;
    private TextView tv_desc;
    private TextView tv_address;

    //转发
    private FrameLayout ll_zhuanfa;
    private TextView tv_zhuanfa;
    private TextView tv_zhuanfa2;
    private ImageView img_zhuanfa;
    private ImageView img_zhuanfa2;


    //评论
    private FrameLayout ll_evaulate;
    private TextView tv_evaluate;
    private TextView tv_evaluate2;
    private ImageView img_evaulate;
    private ImageView img_evaulate2;

    //点赞
    private FrameLayout ll_praise;
    private TextView tv_praise;
    private TextView tv_praise2;
    private ImageView img_praise;
    private ImageView img_praise2;
    private ListView lv;
    private HeadLindDetalAdapter adapter;

    private LinearLayout topBuy;
    private LinearLayout centerBuy;
    private ViewGroup parent_layout;
    private PullableRefreshScrollView2 refreshScrollView;
    private NineGridTestLayout gv_pic;

    private EditText ed_comment;

    /**
     *
     */
    private List<CommentBean> datas = new ArrayList<>();
    private List<String> picDatas = new ArrayList<>();
    private LinearLayout ll_filter;

    private String MERCNUM;
    private Context context;
    private String id;
    private String mercImg;//头像
    private String mercName;
    private String description;
    private String create_time;
    private int page = 0;
    private String location_desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlin_detal);
        //     EventBus.getDefault().register(this);
        context = this;
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        mercImg = intent.getStringExtra("mercImg");
        mercName = intent.getStringExtra("mercName");
        description = intent.getStringExtra("description");
        create_time = intent.getStringExtra("create_time");
        location_desc = intent.getStringExtra("location_desc");
        String imgs = getIntent().getStringExtra("imageList");
        picDatas = ListUtils.getList(imgs);
        initView();
    }


    private void initView() {

        img_back = (ImageView) findViewById(R.id.img_back);
        img_add = (ImageView) findViewById(R.id.img_add);
        tv_address = (TextView) findViewById(R.id.tv_address);
        ed_comment = (EditText) findViewById(R.id.ed_comment);
        head_img = (ImageView) findViewById(R.id.head_img);
        name_tv = (TextView) findViewById(R.id.name_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        if (StringUtils.isEmpty(mercImg)) {
            Glide.with(context).load(R.mipmap.app_icon)
                    .fitCenter()
                    .transform(new GlideCircleTransform(this))
                    .placeholder(R.drawable.pic_nomal_loading_style)
                    .error(R.drawable.pic_nomal_loading_style)
                    .into(head_img);
        } else {
            Glide.with(context).load(mercImg)
                    .fitCenter()
                    .transform(new GlideCircleTransform(this))
                    .placeholder(R.drawable.pic_nomal_loading_style)
                    .error(R.drawable.pic_nomal_loading_style)
                    .into(head_img);
        }

        name_tv.setText(mercName);
        time_tv.setText(create_time);
        tv_desc.setText(description);
        if (StringUtils.isEmpty(location_desc)) {
            tv_address.setVisibility(View.GONE);
        } else {
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(location_desc);
        }


        gv_pic = (NineGridTestLayout) findViewById(R.id.gv_pic);
        topBuy = (LinearLayout) findViewById(R.id.top_buy);
        centerBuy = (LinearLayout) findViewById(R.id.center_buy);
        parent_layout = (RelativeLayout) findViewById(R.id.parent_layout);
        refreshScrollView = (PullableRefreshScrollView2) findViewById(R.id.refreshScrollView);
        refreshScrollView.setOnScrollViewListener(this);
        tv_zhuanfa2 = (TextView) topBuy.findViewById(R.id.tv_zhuanfa);
        tv_evaluate2 = (TextView) topBuy.findViewById(R.id.tv_evaluate);
        tv_praise2 = (TextView) topBuy.findViewById(R.id.tv_praise);


        ll_zhuanfa = (FrameLayout) findViewById(R.id.ll_zhuanfa);
        tv_zhuanfa = (TextView) findViewById(R.id.tv_zhuanfa);
        img_zhuanfa = (ImageView) topBuy.findViewById(R.id.img_zhuanfa);
        img_zhuanfa2 = (ImageView) centerBuy.findViewById(R.id.img_zhuanfa);


        ll_evaulate = (FrameLayout) findViewById(R.id.ll_evaulate);
        tv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        img_evaulate = (ImageView) topBuy.findViewById(R.id.img_evaulate);
        img_evaulate2 = (ImageView) centerBuy.findViewById(R.id.img_evaulate);


        ll_filter = (LinearLayout) findViewById(R.id.ll_filter);
        ll_praise = (FrameLayout) findViewById(R.id.ll_praise);
        tv_praise = (TextView) findViewById(R.id.tv_praise);
        img_praise = (ImageView) topBuy.findViewById(R.id.img_praise);
        img_praise2 = (ImageView) centerBuy.findViewById(R.id.img_praise);
        lv = (ListView) findViewById(R.id.lv);
        parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScroll(refreshScrollView.getScrollY());// 这一步很关键，因为在这里我们手动调用了onScroll方法
                        // 开始时初始化界面没滑动，mScrollView.getScrollY()为0,所以此时优先调用一次onScroll方法
                        // 如果我们不在初始化界面监听并手动调用该方法的话，onScroll方法，只会在滑动的时候产生回调
                        // 一开始并不会产生回调，也就会导致topBuy（顶部的布局）和centerBuy(中间的布局)无法在第一时间内整个界面一开始并没有滑动就重合
                        // 就会看见有两个相同的布局，一个在顶部，一个在中间左右，而我们一旦滑动就会触发回调onScroll方法，此时Math.max(scrollY,centerBuy.getTop)
                        // 取最大值肯定就是centerBuy.getTop(),那么接着利用layout方法，重新绘制topBuy（顶部布局），使得顶部布局正好与中间布局重合在一起，从而使得
                        // 所以，如果一开始我们就手动调用一次
                        // onScroll(mScrollView.getScrollY());就能在第一时间内使得两个布局重合，给人感觉就是一个布局，实际上是
                        // 两个布局，只是重新绘制了顶部布局位置，使它重叠在中间布局上面，给人感觉就是顶部布局消失了一样
                    }
                });

        centerBuy.findViewById(R.id.ll_zhuanfa).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_zhuanfa.setVisibility(View.VISIBLE);
                        img_evaulate.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_zhuanfa2.setVisibility(View.VISIBLE);
                        img_evaulate2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                        tv_zhuanfa.setTextColor(getResources().getColor(R.color.red));
                        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.red));
                        tv_evaluate.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise2.setTextColor(getResources().getColor(R.color.tv_color9));



                    }
                });
        centerBuy.findViewById(R.id.ll_evaulate).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_evaulate.setVisibility(View.VISIBLE);
                        img_zhuanfa.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_evaulate2.setVisibility(View.VISIBLE);
                        img_zhuanfa2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                        tv_zhuanfa.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate.setTextColor(getResources().getColor(R.color.red));
                        tv_evaluate2.setTextColor(getResources().getColor(R.color.red));
                        tv_praise.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise2.setTextColor(getResources().getColor(R.color.tv_color9));


                        getEvaluateList();
                    }
                });
        ;
        centerBuy.findViewById(R.id.ll_praise).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        img_evaulate.setVisibility(View.GONE);
                        img_zhuanfa.setVisibility(View.GONE);
                        img_praise.setVisibility(View.VISIBLE);

                        img_evaulate2.setVisibility(View.GONE);
                        img_zhuanfa2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.VISIBLE);


                        tv_zhuanfa.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise.setTextColor(getResources().getColor(R.color.red));
                        tv_praise2.setTextColor(getResources().getColor(R.color.red));
                    }
                });
        ;

        topBuy.findViewById(R.id.ll_zhuanfa).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_zhuanfa.setVisibility(View.VISIBLE);
                        img_evaulate.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_zhuanfa2.setVisibility(View.VISIBLE);
                        img_evaulate2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                        tv_zhuanfa.setTextColor(getResources().getColor(R.color.red));
                        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.red));
                        tv_evaluate.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise2.setTextColor(getResources().getColor(R.color.tv_color9));

                    }
                });
        topBuy.findViewById(R.id.ll_evaulate).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_evaulate.setVisibility(View.VISIBLE);
                        img_zhuanfa.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_evaulate2.setVisibility(View.VISIBLE);
                        img_zhuanfa2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                        tv_zhuanfa.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate.setTextColor(getResources().getColor(R.color.red));
                        tv_evaluate2.setTextColor(getResources().getColor(R.color.red));
                        tv_praise.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise2.setTextColor(getResources().getColor(R.color.tv_color9));
                        getEvaluateList();
                    }
                });
        ;
        topBuy.findViewById(R.id.ll_praise).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_zhuanfa.setVisibility(View.GONE);
                        img_evaulate.setVisibility(View.GONE);
                        img_praise.setVisibility(View.VISIBLE);

                        img_zhuanfa2.setVisibility(View.GONE);
                        img_evaulate2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.VISIBLE);

                        tv_zhuanfa.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_evaluate2.setTextColor(getResources().getColor(R.color.tv_color9));
                        tv_praise.setTextColor(getResources().getColor(R.color.red));
                        tv_praise2.setTextColor(getResources().getColor(R.color.red));
                    }
                });


        adapter = new HeadLindDetalAdapter(context, datas);
        lv.setAdapter(adapter);


        gv_pic.setIsShowAll(false);
        gv_pic.setUrlList(picDatas);

        img_zhuanfa.setVisibility(View.GONE);
        img_evaulate.setVisibility(View.VISIBLE);
        img_praise.setVisibility(View.GONE);
        img_zhuanfa2.setVisibility(View.GONE);
        img_evaulate2.setVisibility(View.VISIBLE);
        img_praise2.setVisibility(View.GONE);


        tv_zhuanfa.setTextColor(getResources().getColor(R.color.tv_color9));
        tv_zhuanfa2.setTextColor(getResources().getColor(R.color.tv_color9));
        tv_evaluate.setTextColor(getResources().getColor(R.color.red));
        tv_evaluate2.setTextColor(getResources().getColor(R.color.red));
        tv_praise.setTextColor(getResources().getColor(R.color.tv_color9));
        tv_praise2.setTextColor(getResources().getColor(R.color.tv_color9));


        getEvaluateList();


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddHeadLineActivity.class);
                startActivity(intent);
            }
        });
        ed_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
                View contentView = LayoutInflater.from(context).inflate(R.layout.commit_layout, null);
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
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                            Toast.makeText(getApplicationContext(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                        } else {
                            bottomDialog.dismiss();
                            evaulate(content, id);
                        }
                    }
                });
                bottomDialog.setCanceledOnTouchOutside(true);
                bottomDialog.setCancelable(true);
                bottomDialog.show();
            }
        });


//        ed_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
//
//                {
//                    String inputWord = ed_comment.getText().toString();
//                    KeyBoardUtils.closeKeybord(ed_comment, context);
//                    if (!StringUtils.isBlank(inputWord)) {
//                        evaulate(inputWord, id);
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_zhuanfa://转发
                img_zhuanfa.setVisibility(View.VISIBLE);
                img_evaulate.setVisibility(View.GONE);
                img_praise.setVisibility(View.GONE);

                img_zhuanfa2.setVisibility(View.VISIBLE);
                img_evaulate2.setVisibility(View.GONE);
                img_praise2.setVisibility(View.GONE);
                break;
            case R.id.ll_evaulate://评论
                img_evaulate.setVisibility(View.VISIBLE);
                img_zhuanfa.setVisibility(View.GONE);
                img_praise.setVisibility(View.GONE);

                img_evaulate2.setVisibility(View.VISIBLE);
                img_zhuanfa2.setVisibility(View.GONE);
                img_praise2.setVisibility(View.GONE);
                getEvaluateList();
                break;
            case R.id.ll_praise://筛选
                img_praise.setVisibility(View.VISIBLE);
                img_evaulate.setVisibility(View.GONE);
                img_praise.setVisibility(View.GONE);

                img_praise2.setVisibility(View.VISIBLE);
                img_evaulate2.setVisibility(View.GONE);
                img_praise2.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 200;
        ((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
    }


    @Override
    public void onScroll(int scrollY) {
//        System.out.println("ScrollY---->" + scrollY);
//        System.out.println("centerBuy_getTop中间布局离ScrollView距离:------->"+ centerBuy.getTop());
        int topBuyParentTop = Math.max(scrollY, centerBuy.getTop());
//        System.out.println("topBuy顶部布局离ScrollView顶部距离:" + topBuyParentTop);
        topBuy.layout(0, topBuyParentTop, topBuy.getWidth(), topBuyParentTop + topBuy.getHeight());

    }


    private void getEvaluateList() {
        HashMap<String, Object> mapss = new HashMap<>();
        //  mercId //当前登录的用户ID, 例如：M0081997,
        //         id //被评论的头条ID
        mapss.put("id", id);
        mapss.put("size", 10);
        mapss.put("page", page);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_evaulate_list, OkHttpClientManager.TYPE_GET,
                mapss, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        if (jsonObj.get("RSPCOD").equals("000000")) {
                            try {
                                CommentBase jsonRootBean = GsonUtil.GsonToBean(jsonObj.toString(), CommentBase.class);
                                List<CommentBean> detail = jsonRootBean.getRSPDATA();
                                datas.clear();
                                datas.addAll(detail);
                                adapter.notifyDataSetChanged();
                                tv_evaluate.setText("评论" + datas.size());
                                tv_evaluate2.setText("评论" + datas.size());
                                setListviewHeight(lv);
                            } catch (Exception e) {

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObj.getString("RSPMSG"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 评论
     *
     * @param content
     * @param id
     */
    private void evaulate(String content, String id) {
        HashMap<String, Object> mapss = new HashMap<>();
        //  mercId //当前登录的用户ID, 例如：M0081997,
        //         id //被评论的头条ID
        mapss.put("headline_id", id);
        mapss.put("mer_id", MERCNUM);
        mapss.put("comments", content);
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_evaulate, OkHttpClientManager.TYPE_GET,
                mapss, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        try {
                            if (jsonObj.get("RSPCOD").equals("000000")) {
                                Toast.makeText(getApplicationContext(), jsonObj.getString("RSPMSG"), Toast.LENGTH_SHORT).show();
                                getEvaluateList();
                            }
                            Toast.makeText(getApplicationContext(), jsonObj.getString("RSPMSG"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}