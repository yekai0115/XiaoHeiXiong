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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.views.RecyclerViewHolder;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    int count;
    private View xiaobianRecommendView;
    private Context context;
    private List<String> datas;
    private List<View> viewList;
    public RecyclerViewItemTouchListener mlistener;
    //  public ArrayList<HashMap<String, Object>> datas;

    public HomeAdapter(Context context, List<String> mDatas, List<View> viewList, RecyclerViewItemTouchListener listener) {
        this.context = context;
        this.datas = mDatas;
        this.viewList = viewList;
    }

    @Override
    public int getItemViewType(int position) {
        return position ;
    }

    MyViewHolder holder;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("viewType", viewList.size() + "  " + viewType);
        if (viewType < viewList.size()) {
            holder = new MyViewHolder(viewList.get(viewType), viewType);
            return holder;
        }
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_home, parent,
                false), viewType);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position >= viewList.size()) {
            if (holder.tv != null) {
                position = position - viewList.size();
                holder.tv.setText(datas.get(position));
            }
        }


       /* if (position >= viewList.size()) {
            if (holder != null && datas.size() > 0) {
                Log.e("datas+position", datas + ""+position);
                position = position - 3;
                // Glide.with(context).load(datas.get(position).get("mainImgUrl")).asBitmap().into(holder.img);
                // viewHolder.img.setImageResource(datas.get(position).get("mainImgUrl"));
                holder.name_tv.setText(datas.get(position).get("merShortName") + "");
                holder.juli_tv.setText(datas.get(position).get("distance") + "");
                String caiData = datas.get(position).get("merTypeLabel") + "";
                String[] caidatas = caiData.split(",");
                holder.cai_ll.removeAllViews();
                for (int i = 0; i < caidatas.length; i++) {
                    TextView text = new TextView(context);
                    text.setText(caidatas[i]);
                    text.setBackgroundColor(context.getResources().getColor(R.color.xhx_text_blue));
                    text.setPadding(5, 5, 5, 5);
                    //相对位置
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    text.setLayoutParams(lp);
                    text.setTextSize(14);
                    text.setTextColor(context.getResources().getColor(R.color.white));
                    holder.cai_ll.addView(text);
                }
                int level = Integer.parseInt(datas.get(position).get("merLevel") + "");
                holder.zhizun_ll.removeAllViews();
                if (level < 6) {
                    for (int i = 0; i < level; i++) {
                        ImageView imgView = new ImageView(context);
                        imgView.setLayoutParams(new RecyclerView.LayoutParams(50, 50));
                        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imgView.setImageResource(R.mipmap.xingxing_icon);
                        //相对位置
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(10, 0, 0, 0);
                        imgView.setLayoutParams(lp);
                        holder.zhizun_ll.addView(imgView);
                    }

                } else if (level == 6) {
                    ImageView imgView = new ImageView(context);
                    imgView.setLayoutParams(new RecyclerView.LayoutParams(50, 50));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.zhuanshi_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    holder.zhizun_ll.addView(imgView);
                } else if (level == 7) {
                    ImageView imgView = new ImageView(context);
                    imgView.setLayoutParams(new RecyclerView.LayoutParams(50, 50));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.huangg_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    holder.zhizun_ll.addView(imgView);
                } else if (level == 8) {
                    ImageView imgView = new ImageView(context);
                    imgView.setLayoutParams(new RecyclerView.LayoutParams(50, 50));
                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgView.setImageResource(R.mipmap.zhiz_icon);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    imgView.setLayoutParams(lp);
                    holder.zhizun_ll.addView(imgView);
                }
            }
        }*/


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view, int count) {
            super(view);
            if (view == null) {
                return;
            }
            Log.e("hxl", "count==============" + count);
            tv = (TextView) view.findViewById(R.id.id_num);

        }
    }

//自定义的ViewHolder，持有每个Item的的所有界面元素
   /* public class MyViewHolder extends RecyclerViewHolder {
        private String TAG = "ViewHolder";
        ImageView img;
        TextView name_tv, juli_tv, tv;
        LinearLayout zhizun_ll, cai_ll;

        public MyViewHolder(View view, RecyclerViewItemTouchListener listener, int Viewtype) {
            super(view, listener);
            if (view == null) {
                return;
            }
            //   tv = (TextView) view.findViewById(R.id.id_num);

            img = (ImageView) view.findViewById(R.id.img);
            name_tv = (TextView) view.findViewById(R.id.name_tv);

            juli_tv = (TextView) view.findViewById(R.id.juli_tv);
            zhizun_ll = (LinearLayout) view.findViewById(R.id.zhizun_ll);
            cai_ll = (LinearLayout) view.findViewById(R.id.cai_ll);
            //RequestManager glideRequest = Glide.with(context);
            //  glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(context)).into(img);

        }

    }*/
}

