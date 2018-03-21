package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DateUtil;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.views.GlideRoundTransform;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.beans.home.YouYouList;
import td.com.xiaoheixiong.interfaces.ListItemClickHelp;
import td.com.xiaoheixiong.views.ratingstar.RatingStarView;

/**
 * 商品列表适配器
 *
 * @author Administrator
 */
public class MarkingAdapter extends BaseAdapter {

    private HolderView holderView;

    private Context context;
    private LayoutInflater mInflater;
    private List<TTBean> list;
    private ListItemClickHelp callback;
    private int type;
    public MarkingAdapter(Context context, List<TTBean> list, ListItemClickHelp callback,int type) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.callback=callback;
        this.type=type;
    }

    public void updateListview(List<TTBean> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TTBean youList = (TTBean) getItem(position);
        if (convertView == null) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.adapter_marking_item, null);
            holderView.img_shop_pic = (ImageView) convertView.findViewById(R.id.img_shop_pic);
            holderView.tv_card_name = (TextView) convertView.findViewById(R.id.tv_card_name);
            holderView.tv_card_type = (TextView) convertView.findViewById(R.id.tv_card_type);
            holderView.tv_card_num = (TextView) convertView.findViewById(R.id.tv_card_num);
            holderView.tv_card_state = (TextView) convertView.findViewById(R.id.tv_card_state);
            holderView.tv_shuoming = (TextView) convertView.findViewById(R.id.tv_shuoming);
            holderView.tv_startTime = (TextView) convertView.findViewById(R.id.tv_startTime);
            holderView.tv_endTime = (TextView) convertView.findViewById(R.id.tv_endTime);
            holderView.tv_detal = (TextView) convertView.findViewById(R.id.tv_detal);
            holderView.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holderView.ll_time = (LinearLayout) convertView.findViewById(R.id.ll_time);

            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }

        holderView.tv_card_name.setText(youList.getName());
        if(type==1){//团团
            holderView.tv_card_num.setText(youList.getEnterNum());
            holderView.ll_time.setVisibility(View.GONE);
        }else if(type==2){//秒秒
            holderView.tv_card_num.setText(youList.getCardNum());
            holderView.ll_time.setVisibility(View.VISIBLE);
            String startTime=youList.getCreateTime();
            startTime= DateUtil.getYearMonthDay(DateUtil.genYearMonthDayFromStr(startTime));
            holderView.tv_startTime.setText(startTime);
            holderView.tv_endTime.setText(youList.getEndTime());
        }
        holderView.tv_shuoming.setText(youList.getDescription());

        Glide.with(context).load(youList.getMainImg())
                .centerCrop()
                .override(DimenUtils.dip2px(context, 100), DimenUtils.dip2px(context, 100))
                .transform(new GlideRoundTransform(context, 4))
                .placeholder(R.drawable.pic_nomal_loading_style)
                .error(R.drawable.pic_nomal_loading_style)
                .into(holderView.img_shop_pic);


        final View view = convertView;
        final int p = position;
        final int one = holderView.tv_detal.getId();
        final int two = holderView.tv_delete.getId();
        holderView.tv_detal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, one);
            }
        });
        holderView.tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, two);
            }
        });

        return convertView;
    }

    static class HolderView {
        private ImageView img_shop_pic;
        private TextView tv_card_name;
        private TextView tv_card_type;
        private TextView tv_card_num;
        private TextView tv_card_state;
        private TextView tv_shuoming;
        private TextView tv_startTime;
        private TextView tv_endTime;
        private TextView tv_detal;
        private TextView tv_delete;
        private LinearLayout ll_time;

    }

}
