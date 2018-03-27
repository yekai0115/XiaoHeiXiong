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

import java.util.ArrayList;
import java.util.List;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.EmojiUtil;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.beans.HeadLineDetal.CommentBean;
import td.com.xiaoheixiong.views.countdown.CountdownView;

public class HeadLindDetalAdapter extends BaseAdapter {

	private Context mContext;
	private List<CommentBean> list = new ArrayList<CommentBean>();
	public HeadLindDetalAdapter() {
		super();
	}

/**
 * 获取列表数据
 * @param list
 */
	public void setList(List<CommentBean> list){
		this.list = list;
		this.notifyDataSetChanged();
	}

	public HeadLindDetalAdapter(Context mContext, List<CommentBean> list) {
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
		CommentBean commentBean=list.get(position);
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adpter_headline_detal, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.head_img);
			holder.tv_name = (TextView) convertView.findViewById(R.id.name_tv);//昵称
			holder.tv_shop_name = (TextView) convertView.findViewById(R.id.time_tv);//内容


			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String nickName=commentBean.getNickName();
		String content=EmojiUtil.getEmoji(mContext,commentBean.getComments());
		holder.tv_shop_name.setText(content);
		if(StringUtils.isEmpty(nickName)){
			holder.tv_name.setText("用户***");
		}else{
			holder.tv_name.setText(commentBean.getNickName());
		}
		String headImg=commentBean.getHeadImg();
		if(StringUtils.isEmpty(headImg)){
			Glide.with(mContext).load(R.mipmap.app_icon)
					.centerCrop()
					//.override(DimenUtils.dip2px(mContext, 170), DimenUtils.dip2px(mContext, 117))
					.transform(new GlideCircleTransform(mContext))
					.placeholder(R.drawable.pic_nomal_loading_style)
					.error(R.drawable.pic_nomal_loading_style)
					.into(holder.imageView);
		}else{
			Glide.with(mContext).load(commentBean.getHeadImg())
					.centerCrop()
					//.override(DimenUtils.dip2px(mContext, 170), DimenUtils.dip2px(mContext, 117))
					.transform(new GlideCircleTransform(mContext))
					.placeholder(R.drawable.pic_nomal_loading_style)
					.error(R.drawable.pic_nomal_loading_style)
					.into(holder.imageView);
		}

		return convertView;
	}


	class ViewHolder{
		ImageView imageView;
		TextView tv_name;
		TextView tv_shop_name;
	}

}
