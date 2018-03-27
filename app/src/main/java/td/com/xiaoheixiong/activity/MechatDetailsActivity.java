package td.com.xiaoheixiong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.ShowShopAdapter;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class MechatDetailsActivity extends BaseActivity {

    String mercId, orgcode,imgUrl;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.viewGroup)
    LinearLayout viewGroup;
    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.mechatName_tv)
    TextView mechatNameTv;
    @Bind(R.id.add_img_ll)
    LinearLayout addImgLl;
    @Bind(R.id.call_mechat_tv)
    TextView callMechatTv;
    @Bind(R.id.mechat_adress_tv)
    TextView mechatAdressTv;
    @Bind(R.id.juli_tv)
    TextView juliTv;
    @Bind(R.id.pingjia_tv)
    TextView pingjiaTv;
    @Bind(R.id.mechat_jieshao_tv)
    TextView mechatJieshaoTv;
    @Bind(R.id.jieshao_tv)
    TextView jieshaoTv;
    @Bind(R.id.know_tv)
    TextView knowTv;
    @Bind(R.id.pingjia_rl)
    RelativeLayout pingjiaRl;
    @Bind(R.id.add_pinglun_ll)
    LinearLayout addPinglunLl;
    @Bind(R.id.AddImg_ll)
    LinearLayout AddImg_ll;
    @Bind(R.id.close_tv)
    TextView closeTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.shangpin_ll)
    RelativeLayout shangpinLl;
    @Bind(R.id.caipu_ll)
    LinearLayout caipuLl;
    private ImageView plImg, xingImg1, xingImg2, xingImg3, xingImg4, xingImg5;
    private TextView nameTv, timeTv, contentTv;
    private LinearLayout addImgcontentLl;
    private List<Map<String, Object>> listData, Appraiselist;
    private ArrayList<HashMap<String, Object>> Goodslist;
    private Map<String, Object> MapData;
    private ImageView back_img;
    private TextView title_tv;
    private RequestManager glideRequest;
    private LayoutInflater inflater = null;
    private int PAGE_SIZE = 0;
    ImageView imageView;
    ImageView[] imageViews;
    private String pageNum = "1", pageSize = "3", pageSizes = "20", userId, codes, IsSave, phone;
    public LayoutInflater mInflater;
    private ShowShopAdapter ShowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechat_details);
        ButterKnife.bind(this);
        mInflater = getLayoutInflater();
        userId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        phone = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
        Intent it = getIntent();
        mercId = it.getStringExtra("mercId");
        orgcode = it.getStringExtra("orgcode");
        imgUrl= it.getStringExtra("imgUrl");
        Log.e("", mercId + "   " + orgcode);
        initview();
        getdata();
        getAppraiseData();
        getShowshopData();


    }


    private void initview() {
        Goodslist = new ArrayList<>();


        inflater = LayoutInflater.from(this);
        titleTv.setText("商家详情");
        closeTv.setTextColor(getResources().getColor(R.color.red));
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShowAdapter = new ShowShopAdapter(MechatDetailsActivity.this, Goodslist, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");

            }

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        recyclerView.setAdapter(ShowAdapter);
    }

    private void getdata() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //  orgCode	是	String	商户编号
        //  mercId	是	String	商家id
        // userId           用户ID
        HashMap<String, Object> maps = new HashMap<>();

        maps.put("orgCode", orgcode);
        maps.put("mercId", mercId);
        maps.put("userId", userId);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Mechat_Details, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            MapData = new HashMap<String, Object>();
                            //     listData = new ArrayList<Map<String, Object>>();
                            MapData = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                        }
                        inputData();

                    }
                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getShowshopData() {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        // "orgcode":""      商家唯一标识
        // "goodsStatus":"0"       0未上架 1已上架
        //  "pageNum":"1"           当前页
        //  "pageSize"："10" `       每页条数
        HashMap<String, Object> maps = new HashMap<>();

        maps.put("orgcode", orgcode);
        maps.put("goodsStatus", "1");
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSizes);
        maps.put("recommendStatus", "1");
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_goods_list, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result++ goods", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            Map<String, Object> DetailMap = new HashMap<String, Object>();
                            ArrayList<HashMap<String, Object>> godlist = new ArrayList<HashMap<String, Object>>();
                            DetailMap = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                            godlist = JSON.parseObject(DetailMap.get("list") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });
                            Log.e("Goodslist", Goodslist + "");
                            if (godlist.size() > 0) {
                                Goodslist.clear();
                                for (int i = 0; i < godlist.size(); i++) {
                                    Goodslist.add(godlist.get(i));
                                }
                            }
                            Log.e("Goodslist", Goodslist + "");
                            ShowAdapter.notifyDataSetChanged();

                        }


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getAppraiseData() {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //   associationId //被评论的对象ID(必填)
        //  evaluationType //评论类型：0是商家 1是头条
        //pageNum //当前页码
        //         pageSize // 每页记录数
        HashMap<String, Object> maps = new HashMap<>();

        maps.put("associationId", orgcode);
        maps.put("evaluationType", "0");
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSize);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Appraise_Content, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            //     "amount":2, //评论数量
                            //          "avg_grade":3.5 //评分
                            pingjiaTv.setText(oJSON.get("avg_grade") + "/" + oJSON.get("amount") + "条评价");
                            codes = oJSON.get("avg_grade") + "";
                            Map<String, Object> DetailMap = new HashMap<String, Object>();
                            Appraiselist = new ArrayList<Map<String, Object>>();

                            DetailMap = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                            Appraiselist = JSON.parseObject(DetailMap.get("list") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            Log.e("Appraiselist", Appraiselist + "");
                            inputAppraise();

                        }


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void inputAppraise() {
        // Double codeD = Double.parseDouble(codes);
        //     Log.e("codeD", codeD + "  " );
        BigDecimal xingInt = new BigDecimal(codes).setScale(0, BigDecimal.ROUND_HALF_UP);
        //  int xingInt =  Math.round(codeD);
        int xingInts = xingInt.intValue();
        Log.e("xingInts", xingInts + "  ");
        if (xingInts > 0) {
            for (int i = 0; i < xingInts; i++) {
                ImageView imgView = new ImageView(this);
                imgView.setLayoutParams(new RecyclerView.LayoutParams(35, 35));
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                imgView.setImageResource(R.mipmap.xing_press_icon);
                //相对位置
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 0, 0, 0);
                imgView.setLayoutParams(lp);
                AddImg_ll.addView(imgView);
            }
        }


        for (int i = 0; i < Appraiselist.size(); i++) {
            View view = mInflater.inflate(R.layout.item_pinglun, null);
            plImg = (ImageView) view.findViewById(R.id.pl_img);
            xingImg1 = (ImageView) view.findViewById(R.id.xing_img1);
            xingImg2 = (ImageView) view.findViewById(R.id.xing_img2);
            xingImg3 = (ImageView) view.findViewById(R.id.xing_img3);
            xingImg4 = (ImageView) view.findViewById(R.id.xing_img4);
            xingImg5 = (ImageView) view.findViewById(R.id.xing_img5);
            nameTv = (TextView) view.findViewById(R.id.name_tv);
            timeTv = (TextView) view.findViewById(R.id.time_tv);
            contentTv = (TextView) view.findViewById(R.id.content_tv);
            addImgcontentLl = (LinearLayout) view.findViewById(R.id.add_imgcontent_ll);


            glideRequest = Glide.with(this);
            if (StringUtils.isNotBlank(Appraiselist.get(i).get("mercImg") + "")) {
                glideRequest.load(Appraiselist.get(i).get("mercImg") + "").transform(new GlideCircleTransform(this)).into(plImg);// 设置图片圆角
            } else {
                glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(this)).into(plImg);// 设置图片圆角
            }
            if (!StringUtils.isEmpty((String)Appraiselist.get(i).get("mercName"))) {
                nameTv.setText(Appraiselist.get(i).get("mercName") + "");
            }else{
                nameTv.setText("用户***");
            }

            timeTv.setText(Appraiselist.get(i).get("publishTime") + "");
            contentTv.setText(Appraiselist.get(i).get("description") + "");
            int xing = (int) Appraiselist.get(i).get("grade");
            if (xing == 0) {
                xingImg1.setImageResource(R.mipmap.xing_nomal_icon);
                xingImg2.setImageResource(R.mipmap.xing_gray_icon);
                xingImg3.setImageResource(R.mipmap.xing_gray_icon);
                xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 1) {
                xingImg1.setImageResource(R.mipmap.xing_press_icon);
                xingImg2.setImageResource(R.mipmap.xing_gray_icon);
                xingImg3.setImageResource(R.mipmap.xing_gray_icon);
                xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                xingImg5.setImageResource(R.mipmap.xing_gray_icon);

            } else if (xing == 2) {
                xingImg1.setImageResource(R.mipmap.xing_press_icon);
                xingImg2.setImageResource(R.mipmap.xing_press_icon);
                xingImg3.setImageResource(R.mipmap.xing_gray_icon);
                xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 3) {
                xingImg1.setImageResource(R.mipmap.xing_press_icon);
                xingImg2.setImageResource(R.mipmap.xing_press_icon);
                xingImg3.setImageResource(R.mipmap.xing_press_icon);
                xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 4) {
                xingImg1.setImageResource(R.mipmap.xing_press_icon);
                xingImg2.setImageResource(R.mipmap.xing_press_icon);
                xingImg3.setImageResource(R.mipmap.xing_press_icon);
                xingImg4.setImageResource(R.mipmap.xing_press_icon);
                xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 5) {
                xingImg1.setImageResource(R.mipmap.xing_press_icon);
                xingImg2.setImageResource(R.mipmap.xing_press_icon);
                xingImg3.setImageResource(R.mipmap.xing_press_icon);
                xingImg4.setImageResource(R.mipmap.xing_press_icon);
                xingImg5.setImageResource(R.mipmap.xing_press_icon);
            }

            Object ojt = Appraiselist.get(i).get("imageList");
            Log.e("ojt", ojt + "");
            if (ojt != null) {
                final JSONArray ja = JSONArray.parseArray(Appraiselist.get(i).get("imageList") + "");

                addImgcontentLl.removeAllViews();
                if (ja != null && ja.size() > 0) {
                    // 图片数组转图片集合
                    final String[] urls = ja.toArray(new String[ja.size()]);
                    // 设置点击事件
                  /*  addImgcontentLl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enterPhotoDetailed(urls, 0);
                        }
                    });*/
                    if (!ja.equals("") && ja != null) {
                        for (int j = 0; j < ja.size(); j++) {
                            ImageView imgView = new ImageView(getApplicationContext());
                            imgView.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
                            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(getApplicationContext()).load(ja.get(j)).asBitmap().into(imgView);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(110, 110);
                            lp.setMargins(10, 0, 0, 0);
                            imgView.setLayoutParams(lp);
                            addImgcontentLl.addView(imgView);
                            final int points = j;
                            imgView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //     getBigPicture(url);
                                    enterPhotoDetailed(urls, points);
                                }
                            });
                        }
                    }
                }
            }
            addPinglunLl.addView(view);
        }
    }

    private void inputData() {
        IsSave = MapData.get("favouriteStatus") + "";
        Log.e("IsSave", IsSave + "");
        if (IsSave.equals("0")) {
            closeTv.setVisibility(View.VISIBLE);
            closeTv.setText("关注");
        } else if (IsSave.equals("1")) {
            closeTv.setVisibility(View.VISIBLE);
            closeTv.setText("已关注");
            // closeTv.setEnabled(false);
        }
        glideRequest = Glide.with(this);
        mechatNameTv.setText(MapData.get("merShopName") + "");

//        if (StringUtils.isEmpty(MapData.get("headImgUrl") + "")) {
//            glideRequest.load(imgUrl).transform(new GlideCircleTransform(this)).into(headImg);// 设置图片圆角
//        } else {
//            glideRequest.load(MapData.get("headImgUrl") + "").transform(new GlideCircleTransform(this)).into(headImg);// 设置图片圆角
//        }

        glideRequest.load(imgUrl).transform(new GlideCircleTransform(this)).into(headImg);// 设置图片圆角



        String caiData = MapData.get("labelName") + "";
        if (StringUtils.isNotBlank(caiData)) {
            String[] caidatas = new String[3];
            if (caiData.contains(",")) {
                caidatas = caiData.split(",");
            } else if (caiData.contains("，")) {
                caidatas = caiData.split("，");
            } else if (caiData.contains(";")) {
                caidatas = caiData.split(";");
            } else if (caiData.contains("；")) {
                caidatas = caiData.split("；");
            } else if (caiData.contains(" ")) {
                caidatas = caiData.split(" ");
            } else {
                caidatas = new String[1];
                caidatas[0] = caiData;
            }
            caipuLl.removeAllViews();
            for (int i = 0; i < caidatas.length; i++) {
                TextView text = new TextView(this);
                text.setText(caidatas[i]);
                text.setBackground(getResources().getDrawable(R.drawable.bg_style5));
             //   text.setBackgroundColor(getResources().getColor(R.color.xhx_text_blue));
                text.setPadding(7, 5, 7, 5);
                //相对位置
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 0, 0, 0);
                text.setLayoutParams(lp);
                text.setTextSize(12);
                text.setTextColor(getResources().getColor(R.color.tv_color14));
                caipuLl.addView(text);
            }
        }
        //     caipuTv.setText(MapData.get("labelName") + "");

        if (StringUtils.isNotBlank(MapData.get("merLevel") + "")) {
            int level = Integer.parseInt(MapData.get("merLevel") + "");
            addImgLl.removeAllViews();

            if (level < 5) {
                for (int i = 0; i < level; i++) {
                    ImageView imgView = new ImageView(this);
                    //    imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.xingxing_icon);
                    //相对位置
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40, 40);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    addImgLl.addView(imgView);
                }

            } else if (level >= 5 && level <= 7) {
                for (int i = 0; i < level - 4; i++) {
                    ImageView imgView = new ImageView(this);
                    //   imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.zhuanshi_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40, 40);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    addImgLl.addView(imgView);
                }
            } else if (level >= 8) {
                for (int i = 0; i < level - 7; i++) {
                    ImageView imgView = new ImageView(this);
                    //  imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.huangg_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(46, 36);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    addImgLl.addView(imgView);
                }
            }
        }
        mechatAdressTv.setText(MapData.get("address") + "");
        if (MapData.get("merExplain") != null)
            jieshaoTv.setText(MapData.get("merExplain") + "");
        if (MapData.get("merNotice") != null)
            knowTv.setText(MapData.get("merNotice") + "");
        juliTv.setText(MapData.get("distance") + "");

        listData = new ArrayList<>();
        if (StringUtils.isNotBlank(MapData.get("imgList") + "")) {
            listData = JSON.parseObject(MapData.get("imgList") + "", new TypeReference<List<Map<String, Object>>>() {
            });
            Log.e("listData", listData + "");
            if (listData == null || listData.size() <= 0) {
                return;
            }

            PAGE_SIZE = listData.size();
            imageViews = new ImageView[PAGE_SIZE];
            if (PAGE_SIZE == 1) {
                viewGroup.setVisibility(View.INVISIBLE);
            }
            pager.setAdapter(new Adapter());
            for (int i = 0; i < PAGE_SIZE; i++) {
                imageView = new ImageView(this);
                imageView.setPadding(15, 0, 15, 0);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
                lp.setMargins(4, 0, 4, 0);
                imageView.setLayoutParams(lp);

                imageViews[i] = imageView;
                if (i == 0) {
                    imageViews[i].setBackgroundResource(R.mipmap.page_focuese);
                } else {
                    imageViews[i].setBackgroundResource(R.mipmap.page_unfocused);
                }
                viewGroup.addView(imageView);
            }

            pager.setOnPageChangeListener(new MyListener());

        }
    }

    public void call(String phone) {
        Log.e("", "onSumResult result=" + phone);
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }


    class Adapter extends PagerAdapter {
        private int width;
        public Adapter() {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            width = dm.widthPixels;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return PAGE_SIZE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg1 == (View) arg0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.i_first_setup_item, null);
            ImageView imageViewPage = (ImageView) view.findViewById(R.id.img_dt);
          /*  switch (position) {
                case 0:
                    imageViewPage.setBackgroundResource(R.drawable.banner_img1);
                    break;
                case 1:
                    imageViewPage.setBackgroundResource(R.drawable.banner_img2);
                    break;
                case 2:
                    imageViewPage.setBackgroundResource(R.drawable.banner_img3);
                    break;



                default:
                    break;
            }*/
            Log.e("imgUrl", listData.get(position).get("imgUrl") + "");


            int screenWidth = width;
            ViewGroup.LayoutParams lp =imageViewPage.getLayoutParams();
            lp.width = screenWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageViewPage.setLayoutParams(lp);
            imageViewPage.setMaxWidth(screenWidth);
            imageViewPage.setMaxHeight((int) (screenWidth * 5));// 这里其实可以根据需求而定，我这里测试为最大宽度的1.5倍
            Glide.with(MechatDetailsActivity.this).load(listData.get(position).get("imgUrl") + "")
                    .fitCenter()
                    .into(imageViewPage);

            if (position == PAGE_SIZE - 1) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        goIndex(v);
                    }
                });
            }
            ((ViewPager) container).addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            // mImageList.set(position, null);
        }

    }

    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.mipmap.page_focuese);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.mipmap.page_unfocused);
                }
            }

        }

    }

    public void goIndex(View v) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        goIndex(null);
    }

    private void SaveMerchat() {
        showLoadingDialog("...");
        //  "mercid":"123456",  //用户mercid
        //         "orgcode":"1424541546541641",       //商家orgcode
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercid", userId);
        maps.put("orgcode", orgcode);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Save, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            closeTv.setText("已关注");
                            // closeTv.setEnabled(false);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "关注失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void DeleteMerchat() {
        showLoadingDialog("...");
        //  "mercid":"123456",  //用户mercid
        //         "orgcode":"1424541546541641",       //商家orgcode
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercid", userId);
        maps.put("orgcode", orgcode);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_delete, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            closeTv.setText("关注");
                            // closeTv.setEnabled(false);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "取消关注失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.back_img, R.id.pager, R.id.viewGroup, R.id.call_mechat_tv, R.id.pingjia_rl, R.id.close_tv, R.id.shangpin_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.close_tv:
                if (phone.equals("") || phone == null) {
                    Intent it = new Intent(this, LoginActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    if (closeTv.getText().equals("关注")) {
                        SaveMerchat();
                    } else if (closeTv.getText().equals("已关注")) {
                        DeleteMerchat();
                    }
                }
                break;
            case R.id.pager:
                break;
            case R.id.viewGroup:
                break;
            case R.id.call_mechat_tv:
                call(MapData.get("merContactTel") + "");
                break;
            case R.id.pingjia_rl:

                //   Intent it = new Intent(this, AppraiseActivity.class);
                Intent it = new Intent(this, AppraiseInfoActivity.class);
                it.putExtra("orgcode", orgcode);
                startActivity(it);
                break;

            case R.id.shangpin_ll:
                Intent it2 = new Intent(this, DianPuShangPinActivity.class);
                it2.putExtra("orgcode", orgcode);
                startActivity(it2);

                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    /**
     * 进入图片详情页
     *
     * @param position 角标
     */
    protected void enterPhotoDetailed(String[] urls, int position) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }
}
