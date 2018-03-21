package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.CircleImageTransformation;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.ScreenTools;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.views.CustomImageView;
import td.com.xiaoheixiong.views.Image;
import td.com.xiaoheixiong.views.NineGridlayout;


/**
 * Created by Pan_ on 2015/2/3.
 */
public class MainAdapter extends BaseAdapter {
    private Context context;
    private List<TouTiaoBean> datalist;

    public MainAdapter(Context context, List<TouTiaoBean> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist == null ? 0 : datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    public void updataListView(List<TouTiaoBean> datalist) {
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TouTiaoBean touTiaoBean= datalist.get(position);
        List<String> itemList = touTiaoBean.getImageList();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ninegridlayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivMore = (NineGridlayout) convertView.findViewById(R.id.iv_ngrid_layout);
            viewHolder.ivOne = (CustomImageView) convertView.findViewById(R.id.iv_oneimage);
            viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.zanNum_tv = (TextView) convertView.findViewById(R.id.zanNum_tv);
            viewHolder.head_img = (ImageView) convertView.findViewById(R.id.head_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null==itemList|| itemList.isEmpty()) {
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.GONE);
        } else if (itemList.size() == 1) {
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.VISIBLE);

            handlerOneImage(viewHolder, itemList.get(0));
        } else {
            viewHolder.ivMore.setVisibility(View.VISIBLE);
            viewHolder.ivOne.setVisibility(View.GONE);

            viewHolder.ivMore.setImagesData(itemList);
        }
        viewHolder.tv_desc.setText(touTiaoBean.getDescription());
        viewHolder.time_tv.setText(touTiaoBean.getPublishTime());
        String mercName=touTiaoBean.getMercName();
        if(mercName == null || mercName.trim().length() == 0){
            viewHolder.name_tv.setText("");
        }else{
            viewHolder.name_tv.setText(mercName);
        }
        int realPraise=touTiaoBean.getRealPraise();
        viewHolder.zanNum_tv.setText(realPraise+"");

        String mercImg=touTiaoBean.getMercImg();
        if (StringUtils.isNotBlank(mercImg)) {
            //   glideRequest.load(datas.get(position).get("mercImg")).transform(new GlideCircleTransform(mContext)).into(viewHolder.head_img);

            Picasso.with(context).load(mercImg).transform(new CircleImageTransformation(context)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(viewHolder.head_img);

        } else {
            //    glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(mContext)).into(viewHolder.head_img);

            Picasso.with(context).load(R.mipmap.app_icon).transform(new CircleImageTransformation(context))
                    .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(viewHolder.head_img);
        }


        return convertView;
    }

    private void handlerOneImage(ViewHolder viewHolder, String url) {
        Image image=new Image(url,200,200);
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(context);
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(image.getWidth());
        imageHeight = screentools.dip2px(image.getHeight());
        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }
        ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();
        layoutparams.height = imageHeight;
        layoutparams.width = imageWidth;
        viewHolder.ivOne.setLayoutParams(layoutparams);
        viewHolder.ivOne.setClickable(true);
        viewHolder.ivOne.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        viewHolder.ivOne.setImageUrl(image.getUrl());

    }


    class ViewHolder {
        public NineGridlayout ivMore;
        public CustomImageView ivOne;
        public TextView tv_desc;
        public  TextView name_tv;
        private TextView time_tv;
        private TextView  zanNum_tv;
        private ImageView head_img;
    }
}
