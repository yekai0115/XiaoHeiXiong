package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.Utils.views.GlideRoundTransform;
import td.com.xiaoheixiong.beans.home.GroupMmerMarkList;
import td.com.xiaoheixiong.beans.home.YouYouList;
import td.com.xiaoheixiong.views.countdown.CountdownView;
import td.com.xiaoheixiong.views.ratingstar.RatingStarView;

/**
 * 商品列表适配器
 *
 * @author Administrator
 */
public class YouYouAdapter extends BaseAdapter {

    private HolderView holderView;

    private Context context;
    private LayoutInflater mInflater;
    private List<YouYouList> list;


    public YouYouAdapter(Context context, List<YouYouList> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

    }

    public void updateListview(List<YouYouList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        YouYouList youList = (YouYouList) getItem(position);
        if (convertView == null) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.adapter_youyou, null);
            holderView.img_shop_pic = (ImageView) convertView.findViewById(R.id.img_shop_pic);
            holderView.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holderView.ratingStarView = (RatingStarView) convertView.findViewById(R.id.ratingStarView);
            holderView.tv_star_num = (TextView) convertView.findViewById(R.id.tv_star_num);
            holderView.tv_money_per = (TextView) convertView.findViewById(R.id.tv_money_per);
            holderView.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holderView.tv_hot = (TextView) convertView.findViewById(R.id.tv_hot);
            holderView.tv_shop_type = (TextView) convertView.findViewById(R.id.tv_shop_type);
            holderView.view = (View) convertView.findViewById(R.id.view);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        String labelName = youList.getLabelName();
        if (StringUtils.isEmpty(labelName)) {
            holderView.tv_shop_type.setVisibility(View.INVISIBLE);
        } else {
            holderView.tv_shop_type.setVisibility(View.VISIBLE);
            holderView.tv_shop_type.setText(youList.getLabelName());
        }

        holderView.tv_shop_name.setText(youList.getMerShortName());
        String distance=youList.getDistance();
        if(StringUtils.isEmpty(distance)){
            holderView.tv_distance.setVisibility(View.GONE);
        }else{
            holderView.tv_distance.setVisibility(View.VISIBLE);
            holderView.tv_distance.setText("距离"+youList.getDistance());
        }

        Glide.with(context).load(youList.getMainImgUrl())
                .centerCrop()
                .override(DimenUtils.dip2px(context, 88), DimenUtils.dip2px(context, 88))
                .transform(new GlideRoundTransform(context, 4))
                .placeholder(R.drawable.pic_nomal_loading_style)
                .error(R.drawable.pic_nomal_loading_style)
                .into(holderView.img_shop_pic);
        if (position == list.size() - 1) {
            holderView.view.setVisibility(View.GONE);
        } else {
            holderView.view.setVisibility(View.VISIBLE);
        }


        try {
            Random rand = new Random();
            int num = rand.nextInt(1000) + 10;
            holderView.tv_hot.setText("人气" + num);
            Random rand2 = new Random();
            int num2 = rand2.nextInt(100) + 10;
            holderView.tv_money_per.setText("¥"+num2+"/人");

            float Max = 5, Min = 4.0f;
            BigDecimal db = new BigDecimal(Math.random() * (Max - Min) + Min);
            String range=db.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
            holderView.ratingStarView.setRating(Float.valueOf(range));
            holderView.tv_star_num.setText(range);
        }catch (Exception e){

        }
        return convertView;
    }

    static class HolderView {
        private ImageView img_shop_pic;

        private TextView tv_shop_name;
        private RatingStarView ratingStarView;
        private TextView tv_star_num;

        private TextView tv_money_per;
        private TextView tv_distance;
        private TextView tv_hot;
        private TextView tv_shop_type;
        private View view;

    }

}
