package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.beans.home.MerMarkList;
import td.com.xiaoheixiong.views.countdown.CountdownView;

public class MiaoMiaoAdapter extends BaseAdapter {

	private Context mContext;
	private List<TTBean> list = new ArrayList<TTBean>();
	public MiaoMiaoAdapter() {
		super();
	}
	private int maxNum=2;
/**
 * 获取列表数据
 * @param list
 */
	public void setList(List<TTBean> list){
		this.list = list;
		this.notifyDataSetChanged();
	}

	public MiaoMiaoAdapter(Context mContext, List<TTBean> list) {
		super();
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		if(list==null||list.isEmpty()){
			return 0;
		}else if(list.size()>=maxNum){
			return maxNum;
		}else{
			return list.size();
		}
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
		ViewHolder holder = null;
		TTBean merMarkList=list.get(position);
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_miaomiao, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder.cd_time = (CountdownView) convertView.findViewById(R.id.cd_time);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(merMarkList.getName());
		holder.tv_shop_name.setText(merMarkList.getShopName());
		String amount=merMarkList.getPrice();
//		if(amount.contains(".")){
//			amount = amount.substring(0,amount.indexOf("."));
//		}
		holder.tv_money.setText("¥"+amount);
		Glide.with(mContext).load(merMarkList.getMainImg())
				.centerCrop()
				.override(DimenUtils.dip2px(mContext, 170), DimenUtils.dip2px(mContext, 117))
				.placeholder(R.drawable.pic_nomal_loading_style)
				.error(R.drawable.pic_nomal_loading_style)
				.into(holder.imageView);
		String date=merMarkList.getEndTime();
		date = date + " " + "23:59:59";//"yyyy-MM-dd HH:mm:ss")
		int time = GetDateUtils.getTimeInterval(date);//获取时间差，单位秒
		long times = (long) time * 1000;
		holder.cd_time.start(times);
		return convertView;
	}


	class ViewHolder{
		ImageView imageView;
		CountdownView cd_time;
		TextView tv_name;
		TextView tv_shop_name;
		TextView tv_money;
	}

}
