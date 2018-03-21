package td.com.xiaoheixiong.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

/**
 * Created by andry on 2016/9/22.
 */
public class TuantuanRecyclerviewAdapter extends RecyclerView.Adapter<TuantuanRecyclerviewAdapter.mViewHolder> {

    public ArrayList<HashMap<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;
    private List<View> viewList;

    // View view;
    public TuantuanRecyclerviewAdapter(Context context, ArrayList<HashMap<String, Object>> list, List<View> viewDatas, RecyclerViewItemTouchListener listener) {
        datas = list;
        mlistener = listener;
        mContext = context;
        viewList = viewDatas;
    }

    //创建新View，被LayoutManager所调用
    mViewHolder holder;

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType < viewList.size()) {
            holder = new mViewHolder(viewList.get(viewType), mlistener, viewType);
            return holder;
        }
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_miaomiao_recyclerview, viewGroup, false);

        return new mViewHolder(view, mlistener, viewType);


    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final mViewHolder viewHolder, int position) {
        Log.e("datas++Tuantuan", datas + "");
        if (position >= viewList.size()) {
            if (viewHolder != null && datas.size() > 0) {

                Glide.with(mContext).load(datas.get(position).get("mainImgUrl")).asBitmap().into(viewHolder.img);
                viewHolder.name_tv.setText(datas.get(position).get("merShortName") + "");
                viewHolder.KJ_title_tv.setText(datas.get(position).get("markTitle") + "");
                viewHolder.KJF_title_tv.setText(datas.get(position).get("markSubhead") + "");
                viewHolder.keyong_tv1.setText(datas.get(position).get("markExplain") + "");
                String date = datas.get(position).get("validEndDate") + "";
                Log.e("date", date + "");

                date = date + " " + "23:59:59";//"yyyy-MM-dd HH:mm:ss")
                int time = GetDateUtils.getTimeInterval(date);//获取时间差，单位秒
                Log.e("time", time + "");
                //   long time4 = (long) 150 * 24 * 60 * 60 * 1000;
                //    long time = (long) days * 24 * 60 * 60;
                long times = (long) time * 1000;
                viewHolder.time_CountdownView.start(times);
            }
        }
    }
    //获取数据的数量

    public void setOnItemListener(RecyclerViewItemTouchListener listener) {
        mlistener = listener;
    }

    @Override
    public int getItemCount() {
       /* if (datas.size() <= 0)
            return 0;
        else*/
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
       /* if (datas.size() <= 0) {
            return VIEW_TYPE;
        }*/
        return position;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class mViewHolder extends RecyclerViewHolder {
        private String TAG = "ViewHolder";
        ImageView img;
        TextView name_tv, time_tv, KJ_title_tv, KJF_title_tv, keyong_tv1, keyong_tv2;
        CountdownView time_CountdownView;

        public mViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);

            if (Viewtype != VIEW_TYPE) {
                img = (ImageView) view.findViewById(R.id.img);
                name_tv = (TextView) view.findViewById(R.id.name_tv);
                time_CountdownView = (CountdownView) view.findViewById(R.id.time_CountdownView);
                KJ_title_tv = (TextView) view.findViewById(R.id.KJ_title_tv);
                KJF_title_tv = (TextView) view.findViewById(R.id.KJF_title_tv);
                keyong_tv1 = (TextView) view.findViewById(R.id.keyong_tv1);
                //   keyong_tv2 = (TextView) view.findViewById(R.id.keyong_tv2);
                //    RequestManager glideRequest = Glide.with(mContext);
                //   glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(img);
            }
        }
    }
}


