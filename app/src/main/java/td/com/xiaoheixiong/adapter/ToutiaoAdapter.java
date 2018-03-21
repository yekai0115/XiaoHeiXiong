package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.ImagePagerActivity;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.NoScrollGridView;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;


/**
 * Created by andry on 2016/9/22.
 */
public class ToutiaoAdapter extends RecyclerView.Adapter<ToutiaoAdapter.mViewHolder> {

    private String TAG = "MainARecyclerViewAdapter";
    public static ArrayList<HashMap<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;
    View view;
    private RequestManager glideRequest;

    // View view;
    public ToutiaoAdapter(Context context, ArrayList<HashMap<String, Object>> datas, RecyclerViewItemTouchListener listener) {
        this.datas = datas;
        mlistener = listener;
        mContext = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


//        if (VIEW_TYPE == viewType) {
//            view = inflater.inflate(R.layout.component_nodata_reload, viewGroup, false);
//
//            return new mViewHolder(view,mlistener,viewType);
//        }
        if (datas.get(viewType).get("pic") != null && datas.get(viewType).get("pic").equals("1")) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_game_img, viewGroup, false);

        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_toutiao, viewGroup, false);
        }
        return new mViewHolder(view, mlistener, viewType);


    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final mViewHolder viewHolder, final int position) {
        Log.e("datas++****", datas + "  " + datas.size());
        String ISAREAAGENT = MyCacheUtil.getshared(mContext).getString("ISAREAAGENT", "");
        Log.e("ISAREAAGENT", ISAREAAGENT + "");
        if (viewHolder != null && datas.size() > 0) {

            if (datas.get(position).get("pic") != null && datas.get(position).get("pic").equals("1")) {
                return;
            } else {
                Log.e("datas+", datas + "");
                //    Glide.with(mContext).load(datas.get(position).get("mercImg")).asBitmap().into(viewHolder.head_img);

                glideRequest = Glide.with(mContext);
                if (StringUtils.isNotBlank(datas.get(position).get("mercImg")+"")) {
                 //   glideRequest.load(datas.get(position).get("mercImg")).transform(new GlideCircleTransform(mContext)).into(viewHolder.head_img);

                    Picasso.with(mContext).load((String) datas.get(position).get("mercImg")).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(viewHolder.head_img);

                } else {
                //    glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(viewHolder.head_img);

                    Picasso.with(mContext).load(R.mipmap.app_icon).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(viewHolder.head_img);
                }



                // viewHolder.img.setImageResource(datas.get(position).get("mainImgUrl"));
                Log.e("mercName", datas.get(position).get("mercName") + " ");
                if (datas.get(position).get("mercName") == null) {
                    viewHolder.name_tv.setText("匿名用户");
                } else {
                    viewHolder.name_tv.setText(datas.get(position).get("mercName") + "");
                }

                viewHolder.shenfen_tv.setText(datas.get(position).get("identityDesc") + "");
                viewHolder.content_tv.setText(datas.get(position).get("description") + "");
                viewHolder.lianjie_tv.setVisibility(View.GONE);
                viewHolder.time_tv.setText(datas.get(position).get("publishTime") + "");
                final JSONArray ja = JSONArray.parseArray(datas.get(position).get("imageList") + "");

                if (ja != null && ja.size() > 0) {
                    final List<String> list = new ArrayList<>();
                    for (int i = 0; i < ja.size(); i++) {
                        list.add(ja.get(i) + "");
                    }
                    ;
                    Log.e("listimg", list + "");
                    if (list.size() > 0) {
                        // 有：设置Adapter显示图片
                        viewHolder.noScrollGridView.setVisibility(View.VISIBLE);
                        // 图片数组转图片集合
                        //      final String[] urls = bean.imgList.toArray(new String[bean.imgList.size()]);
                        viewHolder.noScrollGridView.setAdapter(new CircleGridAdapter(mContext, list));


                        // 图片数组转图片集合
                        final String[] urls = list.toArray(new String[list.size()]);
                        Log.e("urls", urls + "");
                        // 设置点击事件
                        viewHolder.noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {
                                enterPhotoDetailed(urls, position);
                            }
                        });
                    } else {
                        // 否：隐藏
                        viewHolder.noScrollGridView.setVisibility(View.GONE);
                    }
                } else {
                    // 否：隐藏
                    viewHolder.noScrollGridView.setVisibility(View.GONE);
                }

                //zan_img = (ImageView) view.findViewById(R.id.zan_img);
                final String Praise = datas.get(position).get("realPraise") + "";
                viewHolder.zanNum_tv.setText(Praise);
                final String id = datas.get(position).get("id") + "";
                viewHolder.zan_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("position+++", position + "  ");
                        String mercId = MyCacheUtil.getshared(mContext).getString("MERCNUM", "");
                        HashMap<String, Object> mapss = new HashMap<>();
                        //  mercId //当前登录的用户ID, 例如：M0081997,
                        //         id //被评论的头条ID
                        mapss.put("id", id);
                        mapss.put("mercId", mercId);

                        OkHttpClientManager.getInstance(mContext).requestAsyn(HttpUrls.XHX_Zan, OkHttpClientManager.TYPE_GET,
                                mapss, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                                    @Override
                                    public void onReqSuccess(Object result) {
                                        // TODO Auto-generated method stub
                                        Log.e("result", result + "");
                                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                                        int Rzan = Integer.parseInt(Praise) + 1;// 最新点赞数
                                        viewHolder.zanNum_tv.setText(Rzan + "");
                                        viewHolder.zan_img.setImageResource(R.mipmap.zan_press_icon);
                                        viewHolder.zan_ll.setEnabled(false);
                                    }

                                    @Override
                                    public void onReqFailed(String errorMsg) {
                                        // TODO Auto-generated method stub
                                    }
                                });
                    }

                });
            }

        }
    }

    //获取数据的数量

    public void setOnItemListener(RecyclerViewItemTouchListener listener) {
        mlistener = listener;
    }

    @Override
    public int getItemCount() {
//        if(datas.size() <= 0)
//            return 1;
//        else
        return datas.size();
    }

    /**
     * 获取条目 View填充的类型
     * 默认返回0
     * 将lists为空返回-1
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
//        if (datas.size() <= 0) {
//            return VIEW_TYPE;
//        }
        //  return super.getItemViewType(position);
        return position;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class mViewHolder extends RecyclerViewHolder {
        private String TAG = "ViewHolder";
        ImageView head_img, zan_img, game_img;
        TextView name_tv, shenfen_tv, content_tv, lianjie_tv, time_tv, zanNum_tv;
        NoScrollGridView noScrollGridView;
        LinearLayout zan_ll;

        public mViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);
            //   int n = (datas.size() - 5) / 6;
            //    for (int i = 0; i < datas.size(); i++) {
            //      int m = 5 + 6 * i;
            if (datas.get(Viewtype).get("pic") != null && datas.get(Viewtype).get("pic").equals("1")) {
                Log.e("pic++", datas.get(Viewtype).get("pic") + "");

                game_img = (ImageView) view.findViewById(R.id.game_img);
            } else {
                Log.e("pic++@@@Viewtype", "jin..." + Viewtype);
                Log.e("pic++@@@datas", datas.get(Viewtype) + "jin...");
                head_img = (ImageView) view.findViewById(R.id.head_img);
                name_tv = (TextView) view.findViewById(R.id.name_tv);
                shenfen_tv = (TextView) view.findViewById(R.id.shenfen_tv);
                content_tv = (TextView) view.findViewById(R.id.content_tv);
                lianjie_tv = (TextView) view.findViewById(R.id.lianjie_tv);
                time_tv = (TextView) view.findViewById(R.id.time_tv);
                noScrollGridView = (NoScrollGridView) view.findViewById(R.id.noScrollGridView);
                zan_ll = (LinearLayout) view.findViewById(R.id.zan_ll);
                zan_img = (ImageView) view.findViewById(R.id.zan_img);
                zanNum_tv = (TextView) view.findViewById(R.id.zanNum_tv);
            }
/*
            int nn = datas.size() / 5;
            for (int i = 0; i < nn; i++) {
                HashMap<String, Object> picMap = new HashMap<String, Object>();

                int mm = 5*(i+1) + i;
                datas.add(mm,picMap);
                notifyItemInserted(mm);
                notifyItemRangeChanged(mm,datas.size()-5);
                //   Listdata.add(m, picMap);
            }*/
        }


    }


    /**
     * 进入图片详情页
     *
     * @param position 角标
     */
    protected void enterPhotoDetailed(String[] urls, int position) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
    /* 点击图片放大查看 */

    public void getBigPicture(String url) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
        if (url != null) {
            Glide.with(mContext).load(url).asBitmap().into(img);
            dialog.setView(imgEntryView); // 自定义dialog
            dialog.show();
        }
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });

    }
}


