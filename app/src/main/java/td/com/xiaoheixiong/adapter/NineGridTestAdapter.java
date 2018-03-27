package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.CircleImageTransformation;
import td.com.xiaoheixiong.Utils.ListUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.interfaces.ListItemClickHelp;
import td.com.xiaoheixiong.views.toutiao.NineGridTestLayout;


/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class NineGridTestAdapter extends BaseAdapter {

    private Context mContext;
    private List<TouTiaoBean> mList;
    protected LayoutInflater inflater;
    ViewHolder holder = null;
    private ListItemClickHelp callback;
    private int type;

    public NineGridTestAdapter(Context context, ListItemClickHelp callback, int type) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.callback = callback;
        this.type = type;
    }

    public void setList(List<TouTiaoBean> list) {
        mList = list;
    }

    public void updataListView(List<TouTiaoBean> datalist) {
        this.mList = datalist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return getListSize(mList);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {


        TouTiaoBean touTiaoBean = mList.get(position);
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.item_bbs_nine_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.layout.setIsShowAll(mList.get(position).isShowAll);
        ArrayList list = ListUtils.getList(mList.get(position).getImages());
        holder.layout.setUrlList(list);


        holder.tv_desc.setText(touTiaoBean.getDescription());
        holder.time_tv.setText(touTiaoBean.getCreate_time());
        String nickName = touTiaoBean.getNickName();
        if (nickName == null || nickName.trim().length() == 0) {
            holder.name_tv.setText("用户***");
        } else {
            holder.name_tv.setText(nickName);
        }

        String location_desc = touTiaoBean.getLocation_desc();
        if (!StringUtils.isEmpty(location_desc)) {
            holder.tv_address.setText(location_desc);
            holder.tv_address.setVisibility(View.VISIBLE);
        } else {
            holder.tv_address.setVisibility(View.GONE);
        }

        final String realPraise = touTiaoBean.getReal_praise();
        if (!StringUtils.isEmpty(realPraise)) {
            holder.zanNum_tv.setText(realPraise);
        }

        String forwardNum = touTiaoBean.getForwardNum();
        if (!StringUtils.isEmpty(forwardNum)) {
            holder.tv_zhuanfa.setText("");
        } else {
            holder.tv_zhuanfa.setText(forwardNum);
        }

        String replyNum = touTiaoBean.getReplyNum();
        if (!StringUtils.isEmpty(replyNum)) {
            holder.tv_evaluate.setText("");
        } else {
            holder.tv_evaluate.setText(replyNum);
        }
        boolean isPraise= touTiaoBean.isPraise();
        if(isPraise){
            holder.zan_img.setImageResource(R.drawable.headline_praise1);
        }else{
            holder.zan_img.setImageResource(R.drawable.headline_praise);
        }


        String mercImg = touTiaoBean.getHeadImg();
        if (StringUtils.isNotBlank(mercImg)) {
            //   glideRequest.load(datas.get(position).get("mercImg")).transform(new GlideCircleTransform(mContext)).into(viewHolder.head_img);

            Picasso.with(mContext).load(mercImg)
                    .transform(new CircleImageTransformation(mContext))
                    .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(holder.head_img);

        } else {
            //    glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(viewHolder.head_img);

            Picasso.with(mContext).load(R.mipmap.app_icon).transform(new CircleImageTransformation(mContext))
                    .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(holder.head_img);
        }
        if (type == 2) {
            holder.img_delete.setVisibility(View.VISIBLE);
            holder.tv_see.setVisibility(View.GONE);
        } else {
            holder.img_delete.setVisibility(View.GONE);
            holder.tv_see.setVisibility(View.VISIBLE);
        }


        final View view = convertView;
        final int p = position;
        final int one = holder.zanNum_tv.getId();
        final int two = holder.head_img.getId();
        final int three = holder.zan_ll.getId();
        final int four = holder.img_delete.getId();
        final int five = holder.tv_zhuanfa.getId();
        final int six = holder.tv_evaluate.getId();
        final int seven = holder.ll_detal.getId();

        holder.zanNum_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, one);
            }
        });
        holder.head_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, two);
            }
        });
        holder.zan_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, three);
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, four);
            }
        });

        holder.tv_zhuanfa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, five);
            }
        });
        holder.tv_evaluate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, six);
            }
        });

        holder.ll_detal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, seven);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        NineGridTestLayout layout;
        private TextView tv_desc;
        private TextView name_tv;
        private TextView time_tv;
        private TextView zanNum_tv;
        private ImageView head_img;
        private ImageView img_delete;
        private LinearLayout zan_ll;
        private LinearLayout ll_detal;
        private ImageView zan_img;
        private TextView tv_zhuanfa;
        private TextView tv_evaluate;
        private TextView tv_address;
        private TextView tv_see;

        public ViewHolder(View view) {
            layout = (NineGridTestLayout) view.findViewById(R.id.layout_nine_grid);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            name_tv = (TextView) view.findViewById(R.id.name_tv);
            time_tv = (TextView) view.findViewById(R.id.time_tv);
            zanNum_tv = (TextView) view.findViewById(R.id.zanNum_tv);
            head_img = (ImageView) view.findViewById(R.id.head_img);
            zan_ll = (LinearLayout) view.findViewById(R.id.zan_ll);
            zan_img = (ImageView) view.findViewById(R.id.zan_img);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            tv_zhuanfa = (TextView) view.findViewById(R.id.tv_zhuanfa);
            tv_evaluate = (TextView) view.findViewById(R.id.tv_evaluate);
            ll_detal = (LinearLayout) view.findViewById(R.id.ll_detal);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_see = (TextView) view.findViewById(R.id.tv_see);
        }
    }

    private int getListSize(List<TouTiaoBean> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
