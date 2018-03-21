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

import java.util.List;
import java.util.Random;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.beans.home.GroupMmerMarkList;
import td.com.xiaoheixiong.views.countdown.CountdownView;

/**
 * 商品列表适配器
 *
 * @author Administrator
 */
public class TuanTuanAdapter extends BaseAdapter {

    private HolderView holderView;

    private Context context;
    private LayoutInflater mInflater;
    private List<TTBean> list;

    private int width;

    public TuanTuanAdapter(Context context, List<TTBean> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        width = DimenUtils.getWidth(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TTBean groupMmerMarkList = (TTBean) getItem(position);
        if (convertView == null) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.adapter_tuantuan, null);
            holderView.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holderView.cd_time = (CountdownView) convertView.findViewById(R.id.cd_time);
            holderView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holderView.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holderView.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holderView.tv_add_persons = (TextView) convertView.findViewById(R.id.tv_add_persons);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }

        holderView.tv_name.setText(groupMmerMarkList.getName());
   //     holderView.tv_shop_name.setText(groupMmerMarkList.getMerShortName());
        String amount=groupMmerMarkList.getPrice();
//        if(amount.contains(".")){
//            amount = amount.substring(0,amount.indexOf("."));
//        }
        holderView.tv_money.setText("¥"+amount);
        Glide.with(context).load(groupMmerMarkList.getMainImg())
                .fitCenter()
                .override(DimenUtils.dip2px(context, width), DimenUtils.dip2px(context, 156))
                .placeholder(R.drawable.pic_nomal_loading_style)
                .error(R.drawable.pic_nomal_loading_style)
                .into(holderView.imageView);

//        String date=groupMmerMarkList.getValidEndDate();
//        date = date + " " + "23:59:59";//"yyyy-MM-dd HH:mm:ss")
//        int time = GetDateUtils.getTimeInterval(date);//获取时间差，单位秒
//        long times = (long) time * 1000;
//        holderView.cd_time.start(times);
//
//        Random rand = new Random();
//        int num=rand.nextInt(100) + 10;
//        holderView.tv_add_persons.setText("已拼"+num+"人");
        return convertView;
    }

    static class HolderView {
       private ImageView imageView;
        private CountdownView cd_time;

        private TextView tv_name;

        private TextView tv_shop_name;

        private TextView tv_money;
        private TextView tv_add_persons;


    }

}
