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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

/**
 * Created by andry on 2016/9/22.
 */
public class YouyouRecyclerViewAdapter extends RecyclerView.Adapter<YouyouRecyclerViewAdapter.mViewHolder> {

    private String TAG = "MainARecyclerViewAdapter";
    public ArrayList<HashMap<String, Object>> datas;
    public RecyclerViewItemTouchListener mlistener;
    //  private BadgeView badge;
    private static final int VIEW_TYPE = -1;
    private static Context mContext;


    // View view;
    public YouyouRecyclerViewAdapter(Context context, ArrayList<HashMap<String, Object>> list, RecyclerViewItemTouchListener listener) {
        datas = list;
        mlistener = listener;
        mContext = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_a_item, viewGroup, false);

        return new mViewHolder(view, mlistener, viewType);


    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int position) {
        Log.e("datas++", datas + "");

        if (viewHolder != null && datas.size() > 0) {
            Log.e("datas+", datas + "");
            Glide.with(mContext).load(datas.get(position).get("mainImgUrl")).asBitmap().into(viewHolder.img);
            // viewHolder.img.setImageResource(datas.get(position).get("mainImgUrl"));
            viewHolder.name_tv.setText(datas.get(position).get("merShortName") + "");
            viewHolder.juli_tv.setText(datas.get(position).get("distance") + "");

            String caiData = datas.get(position).get("merTypeLabel") + "";

            if (StringUtils.isNotBlank(caiData)) {
                String[] caidatas = new String[3];
                //  String[] fuhao = {" ", ",", "，", ".", ";", "；", ":", "：", "/", "'", "‘"};
          /*  for (int i = 0; i < ; i++) {
                Pattern e =  Pattern.compile(caiData);
            }
*/
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
                }else {
                   caidatas = new String[1];
                    caidatas[0] = caiData;
                }
                viewHolder.cai_ll.removeAllViews();
                for (int i = 0; i < caidatas.length; i++) {
                    TextView text = new TextView(mContext);
                    text.setText(caidatas[i]);
                    text.setBackgroundColor(mContext.getResources().getColor(R.color.xhx_text_blue));
                    text.setPadding(5, 5, 5, 5);
                    //相对位置
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    text.setLayoutParams(lp);
                    text.setTextSize(14);
                    text.setTextColor(mContext.getResources().getColor(R.color.white));
                    viewHolder.cai_ll.addView(text);
                }
            }
            //          merLevel =1 1星商家
            //          merLevel =2 2星商家
            //          merLevel =3 3星商家
            //          merLevel =4 4星商家
            //          merLevel = 5 1 钻

            //         merLevel =6 2钻商家
            //         merLevel =7 3钻商家
            //         merLevel =8 1皇冠商家
            //         merLevel =9 2皇冠商家
            //         merLevel =10 3皇冠商家
            int level = Integer.parseInt(datas.get(position).get("merLevel") + "");
            viewHolder.zhizun_ll.removeAllViews();
            if (level < 5) {
                for (int i = 0; i < level; i++) {
                    ImageView imgView = new ImageView(mContext);
                    //    imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.xingxing_icon);
                    //相对位置
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(48, 48);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    viewHolder.zhizun_ll.addView(imgView);
                }

            } else if (level >= 5 && level <= 7) {
                for (int i = 0; i < level - 4; i++) {
                    ImageView imgView = new ImageView(mContext);
                    //   imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.zhuanshi_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(48, 48);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    viewHolder.zhizun_ll.addView(imgView);
                }
            } else if (level >= 8) {
                for (int i = 0; i < level - 7; i++) {
                    ImageView imgView = new ImageView(mContext);
                    //  imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.huangg_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(52, 46);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    viewHolder.zhizun_ll.addView(imgView);
                }
            } /*else if (level == 8) {
                ImageView imgView = new ImageView(mContext);
                //     imgView.setLayoutParams(new RecyclerView.LayoutParams(30, 30));
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                imgView.setImageResource(R.mipmap.zhiz_icon);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(48, 48);
                lp.setMargins(10, 0, 0, 0);
                imgView.setLayoutParams(lp);
                viewHolder.zhizun_ll.addView(imgView);
            }*/

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
                RequestManager glideRequest = Glide.with(mContext);
                glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(img);
            }
        }
    }
}


