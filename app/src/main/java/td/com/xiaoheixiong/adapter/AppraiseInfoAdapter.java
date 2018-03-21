package td.com.xiaoheixiong.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.activity.ImagePagerActivity;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

/**
 * Created by andry on 2016/9/22.
 */
public class AppraiseInfoAdapter extends RecyclerView.Adapter<AppraiseInfoAdapter.mViewHolder> {

    private String TAG = "MainARecyclerViewAdapter";
    public List<Map<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;

    // View view;
    public AppraiseInfoAdapter(Context context, List<Map<String, Object>> list, RecyclerViewItemTouchListener listener) {
        datas = list;
        mlistener = listener;
        mContext = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


//        if (VIEW_TYPE == viewType) {
//            view = inflater.inflate(R.layout.component_nodata_reload, viewGroup, false);
//
//            return new mViewHolder(view,mlistener,viewType);
//        }


        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pinglun, viewGroup, false);

        return new mViewHolder(view, mlistener, viewType);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int position) {
        Log.e("datas++", datas + "");
        if (viewHolder != null && datas.size() > 0) {
            RequestManager glideRequest = Glide.with(mContext);
            if (StringUtils.isNotBlank(datas.get(position).get("mercImg") + "")) {
                glideRequest.load(datas.get(position).get("mercImg") + "").transform(new GlideCircleTransform(mContext)).into(viewHolder.pl_img);// 设置图片圆角
            } else {
                glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(viewHolder.pl_img);// 设置图片圆角
            }
            if (datas.get(position).get("mercName") != null) {
                viewHolder.name_tv.setText(datas.get(position).get("mercName") + "");
            } else {
                viewHolder.name_tv.setText("匿名用户");
            }

            viewHolder.time_tv.setText(datas.get(position).get("publishTime") + "");
            viewHolder.content_tv.setText(datas.get(position).get("description") + "");
            int xing = (int) datas.get(position).get("grade");
            if (xing == 0) {
                viewHolder.xingImg1.setImageResource(R.mipmap.xing_nomal_icon);
                viewHolder.xingImg2.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg3.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 1) {
                viewHolder.xingImg1.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg2.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg3.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg5.setImageResource(R.mipmap.xing_gray_icon);

            } else if (xing == 2) {
                viewHolder.xingImg1.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg2.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg3.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 3) {
                viewHolder.xingImg1.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg2.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg3.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg4.setImageResource(R.mipmap.xing_gray_icon);
                viewHolder.xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 4) {
                viewHolder.xingImg1.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg2.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg3.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg4.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg5.setImageResource(R.mipmap.xing_gray_icon);
            } else if (xing == 5) {
                viewHolder.xingImg1.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg2.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg3.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg4.setImageResource(R.mipmap.xing_press_icon);
                viewHolder.xingImg5.setImageResource(R.mipmap.xing_press_icon);
            }
            Object ojt = datas.get(position).get("imageList");
            if (ojt != null) {
                viewHolder.add_imgcontent_ll.removeAllViews();
                final JSONArray ja = JSONArray.parseArray(datas.get(position).get("imageList") + "");

                // 图片数组转图片集合
                final String[] urls = ja.toArray(new String[ja.size()]);
                // 设置点击事件
               /* viewHolder.add_imgcontent_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enterPhotoDetailed(urls, 0);
                    }
                });*/

                if (!ja.equals("") && ja != null) {
                    for (int j = 0; j < ja.size(); j++) {
                        ImageView imgView = new ImageView(mContext);
                        imgView.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
                        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(mContext).load(ja.get(j)).asBitmap().into(imgView);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(110, 110);
                        lp.setMargins(10, 0, 0, 0);
                        imgView.setLayoutParams(lp);
                        viewHolder.add_imgcontent_ll.addView(imgView);
                        final String url = ja.get(j) + "";
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

    }
    //获取数据的数量

    public void setOnItemListener(RecyclerViewItemTouchListener listener) {
        mlistener = listener;
    }

    @Override
    public int getItemCount() {
        if (datas.size() <= 0)
            return 0;
        else
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
        return super.getItemViewType(position);
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class mViewHolder extends RecyclerViewHolder {
        private String TAG = "ViewHolder";


        ImageView pl_img, xingImg1, xingImg2, xingImg3, xingImg4, xingImg5;
        TextView name_tv, time_tv, content_tv;
        LinearLayout add_imgcontent_ll;

        public mViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);
            pl_img = (ImageView) view.findViewById(R.id.pl_img);
            xingImg1 = (ImageView) view.findViewById(R.id.xing_img1);
            xingImg2 = (ImageView) view.findViewById(R.id.xing_img2);
            xingImg3 = (ImageView) view.findViewById(R.id.xing_img3);
            xingImg4 = (ImageView) view.findViewById(R.id.xing_img4);
            xingImg5 = (ImageView) view.findViewById(R.id.xing_img5);

            name_tv = (TextView) view.findViewById(R.id.name_tv);
            time_tv = (TextView) view.findViewById(R.id.time_tv);
            content_tv = (TextView) view.findViewById(R.id.content_tv);
            add_imgcontent_ll = (LinearLayout) view.findViewById(R.id.add_imgcontent_ll);

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


