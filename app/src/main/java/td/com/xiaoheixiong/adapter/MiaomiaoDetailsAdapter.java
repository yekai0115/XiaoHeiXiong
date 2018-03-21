package td.com.xiaoheixiong.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;

import cn.iwgang.countdownview.CountdownView;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

/**
 * Created by andry on 2016/9/22.
 */
public class MiaomiaoDetailsAdapter extends RecyclerView.Adapter<MiaomiaoDetailsAdapter.mViewHolder> {

    private String TAG = "MainARecyclerViewAdapter";
    public ArrayList<HashMap<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;


    // View view;
    public MiaomiaoDetailsAdapter(Context context, ArrayList<HashMap<String, Object>> list, RecyclerViewItemTouchListener listener) {
        datas = list;
        mlistener = listener;
        mContext = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_miaomiao_details, viewGroup, false);

        return new mViewHolder(view, mlistener, viewType);


    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int position) {
        Log.e("datas++", datas + "");

        if (viewHolder != null && datas.size() > 0) {
            //   ImageView img;
            //  TextView KQ_title_tv, KQF_title_tv, keyong_tv1, keyong_tv2, rob_tv;
            //  CountdownView time_CountdownView;
            Glide.with(mContext).load(datas.get(position).get("mainImgUrl") + "").asBitmap().into(viewHolder.img);
            viewHolder.KQ_title_tv.setText(datas.get(position).get("markTitle") + "");
            viewHolder.KQF_title_tv.setText(datas.get(position).get("markSubhead") + "");
            viewHolder.keyong_tv1.setText(datas.get(position).get("markExplain") + "");
            String Enddate = datas.get(position).get("validEndDate") + "";
            String date = Enddate + " " + "23:59:59";//"yyyy-MM-dd HH:mm:ss")
            int time = GetDateUtils.getTimeInterval(date);//获取时间差，单位秒
            Log.e("time", time + "");
            long times = (long) time * 1000;
            viewHolder.time_CountdownView.start(times);

            String markType = datas.get(position).get("markType") + "";
            if (markType.equals("0")) {
                viewHolder.djq_tv.setText("代金券");
            } else if (markType.equals("1")) {
                viewHolder.djq_tv.setText("折扣券");
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
        ImageView img;
        TextView KQ_title_tv, KQF_title_tv, keyong_tv1, keyong_tv2, rob_tv, djq_tv;

        CountdownView time_CountdownView;

        public mViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);

            if (Viewtype != VIEW_TYPE) {
                img = (ImageView) view.findViewById(R.id.img);
                KQ_title_tv = (TextView) view.findViewById(R.id.KQ_title_tv);
                KQF_title_tv = (TextView) view.findViewById(R.id.KQF_title_tv);
                keyong_tv1 = (TextView) view.findViewById(R.id.keyong_tv1);
                keyong_tv2 = (TextView) view.findViewById(R.id.keyong_tv2);
                djq_tv = (TextView) view.findViewById(R.id.djq_tv);

                rob_tv = (TextView) view.findViewById(R.id.rob_tv);
                time_CountdownView = (CountdownView) view.findViewById(R.id.time_CountdownView);
            }
        }
    }
}


