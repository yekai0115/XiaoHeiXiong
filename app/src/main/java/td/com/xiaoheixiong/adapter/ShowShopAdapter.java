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

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

/**
 * Created by andry on 2016/9/22.
 */
public class ShowShopAdapter extends RecyclerView.Adapter<ShowShopAdapter.mViewHolder> {

    private String TAG = "MainARecyclerViewAdapter";
    public ArrayList<HashMap<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    public List<View> viewDatas;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;


    // View view;
    public ShowShopAdapter(Context context, ArrayList<HashMap<String, Object>> list, RecyclerViewItemTouchListener listener) {
        datas = list;
        mlistener = listener;
        mContext = context;
    }

    mViewHolder holder;

    //创建新View，被LayoutManager所调用
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_show_shop, viewGroup, false);

        return new mViewHolder(view, mlistener, viewType);


    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int position) {
        Log.e("datas++", datas + "");

        if (viewHolder != null && datas.size() > 0) {

           // commoPrice=10.00, recommendStatus=1, commoName=上课
            Log.e("position", position + "");
            Log.e("commoName", datas.get(position).get("commoName") + "" + "");
            Glide.with(mContext).load(datas.get(position).get("mainImgUrl")).asBitmap().into(viewHolder.head_img);
            viewHolder.name_tv.setText(datas.get(position).get("commoName") + "");
            viewHolder.price_tv.setText(datas.get(position).get("commoPrice") + "元");

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
        return position;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class mViewHolder extends RecyclerViewHolder {
        private String TAG = "ViewHolder";
        ImageView head_img;
        TextView name_tv, price_tv;

        public mViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);

            if (Viewtype != VIEW_TYPE) {
                head_img = (ImageView) view.findViewById(R.id.head_img);
                name_tv = (TextView) view.findViewById(R.id.name_tv);
                price_tv = (TextView) view.findViewById(R.id.price_tv);
            }
        }
    }
}


