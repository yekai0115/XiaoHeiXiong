package td.com.xiaoheixiong.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

/**
 * Created by andry on 2016/9/22.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.mViewHolder> {

    private String TAG = "MainARecyclerViewAdapter";
    public ArrayList<HashMap<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;
    private List<View> viewList;

    // View view;
    public MainRecyclerViewAdapter(Context context, ArrayList<HashMap<String, Object>> list, List<View> viewDatas, RecyclerViewItemTouchListener listener) {
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
        Log.e("viewType", viewList.size() + "  " + viewType);
        if (viewType < viewList.size()) {
            holder = new mViewHolder(viewList.get(viewType), mlistener, viewType);
            return holder;
        }
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_a_item, viewGroup, false);
        return new mViewHolder(view, mlistener, viewType);

    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int position) {
        Log.e("datas++", datas + "");
        if (position >= viewList.size()) {
            if (viewHolder != null && datas.size() > 0) {
                Log.e("datas+", datas + "");

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
        return position;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class mViewHolder extends RecyclerViewHolder {
        private String TAG = "ViewHolder";
        ImageView img;
        TextView name_tv, juli_tv;
        LinearLayout zhizun_ll, cai_ll;

        public mViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);

            if (Viewtype != VIEW_TYPE) {
                img = (ImageView) view.findViewById(R.id.img);
                name_tv = (TextView) view.findViewById(R.id.name_tv);

                juli_tv = (TextView) view.findViewById(R.id.juli_tv);
                zhizun_ll = (LinearLayout) view.findViewById(R.id.zhizun_ll);
                cai_ll = (LinearLayout) view.findViewById(R.id.cai_ll);
                //RequestManager glideRequest = Glide.with(mContext);
                //  glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(img);
            }
        }

    }
}


