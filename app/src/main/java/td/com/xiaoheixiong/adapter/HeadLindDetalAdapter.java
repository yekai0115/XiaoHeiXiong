package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.views.countdown.CountdownView;

public class HeadLindDetalAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> list = new ArrayList<String>();
	public HeadLindDetalAdapter() {
		super();
	}

/**
 * 获取列表数据
 * @param list
 */
	public void setList(List<String> list){
		this.list = list;
		this.notifyDataSetChanged();
	}

	public HeadLindDetalAdapter(Context mContext, List<String> list) {
		super();
		this.mContext = mContext;
		this.list = list;
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
		ViewHolder holder = null;
		String title=list.get(position);
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adpter_headline_detal, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.head_img);
			holder.tv_name = (TextView) convertView.findViewById(R.id.name_tv);
			holder.tv_shop_name = (TextView) convertView.findViewById(R.id.time_tv);


			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_shop_name.setText(title);
//		Glide.with(mContext).load(merMarkList.getMainImg())
//				.centerCrop()
//				.override(DimenUtils.dip2px(mContext, 170), DimenUtils.dip2px(mContext, 117))
//				.placeholder(R.drawable.pic_nomal_loading_style)
//				.error(R.drawable.pic_nomal_loading_style)
//				.into(holder.imageView);
		return convertView;
	}


	class ViewHolder{
		ImageView imageView;
		TextView tv_name;
		TextView tv_shop_name;
	}

}
