package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DateUtil;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.beans.MyMember.MymemberBean;
import td.com.xiaoheixiong.beans.earns.MyEarnsBean;

/**
 *
 *
 * @author Administrator
 */
public class AgentEarnsAdapter extends BaseAdapter {

    private HolderView holderView;

    private Context context;
    private LayoutInflater mInflater;
    private List<MyEarnsBean> list;

    private int width;

    public AgentEarnsAdapter(Context context, List<MyEarnsBean> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        width = DimenUtils.getWidth(context);
    }

    public void updateListview(List<MyEarnsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        MyEarnsBean myEarnsBean = (MyEarnsBean) getItem(position);
        if (convertView == null) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.adapter_agent_earns_item, null);
            holderView.img_head_pic = (ImageView) convertView.findViewById(R.id.img_head_pic);
            holderView.tv_membre_name = (TextView) convertView.findViewById(R.id.tv_membre_name);
            holderView.tv_xiaofei = (TextView) convertView.findViewById(R.id.tv_xiaofei);
            holderView.tv_pay_time = (TextView) convertView.findViewById(R.id.tv_pay_time);
            holderView.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }

        holderView.tv_membre_name.setText(myEarnsBean.getName());
        holderView.tv_xiaofei.setText(myEarnsBean.getPayTotal());
        String time=myEarnsBean.getAdd_time();
        time=DateUtil.times(time);
        holderView.tv_pay_time.setText(time);
        holderView.tv_money.setText(myEarnsBean.getMerchant_money());
        Glide.with(context).load(R.mipmap.app_icon)
                .fitCenter()
                .override(DimenUtils.dip2px(context, 50), DimenUtils.dip2px(context, 50))
                .placeholder(R.drawable.pic_nomal_loading_style)
                .transform(new GlideCircleTransform(context))
                .error(R.drawable.pic_nomal_loading_style)
                .into(holderView.img_head_pic);

        return convertView;
    }

    static class HolderView {
        private ImageView img_head_pic;
        private TextView tv_membre_name;
        private TextView tv_xiaofei;
        private TextView tv_pay_time;
        private TextView tv_money;


    }

}
