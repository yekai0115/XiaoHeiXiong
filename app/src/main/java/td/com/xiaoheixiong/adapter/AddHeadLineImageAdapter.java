package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import td.com.xiaoheixiong.R;

public class AddHeadLineImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> list = new ArrayList<String>();
	private OnDeleteImageListener mListener;
	public AddHeadLineImageAdapter() {
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

	public AddHeadLineImageAdapter(Context mContext, List<String> list) {
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
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_addheadline_pic, null);
			holder = new ViewHolder();
			holder.item_grida_image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			holder.item_grida_delete = (ImageView) convertView.findViewById(R.id.item_grida_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_grida_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.click(position);
			}
		});
		if (isShowAddItem(position)) {//最后一张
			Glide.with(mContext).load(R.drawable.barter_add).fitCenter()
					.placeholder(R.drawable.barter_add)
                    .error(R.drawable.barter_add)
                    .into(holder.item_grida_image);
			holder.item_grida_image.setBackgroundResource(R.color.bg_color);
			holder.item_grida_delete.setVisibility(View.GONE);
		} else {
			Glide.with(mContext).load(list.get(position)).fitCenter()
					.placeholder(R.drawable.barter_add)
					.error(R.drawable.pic_nomal_loading_style)
					.into(holder.item_grida_image);
			holder.item_grida_image.setBackgroundResource(R.color.bg_color);
			holder.item_grida_delete.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	/**
	 * 判断当前下标是否是最大值
	 * @param position  当前下标
	 * @return
	 */
	private boolean isShowAddItem(int position)
	{
		int size = list == null ? 0 : list.size();
		return position == size-1;
	}

	class ViewHolder{
		ImageView item_grida_image,item_grida_delete;
	}
	public interface OnDeleteImageListener{
		public void click(int position);
	}
	public void setOnDeleteImageListener(OnDeleteImageListener listener){
		this.mListener=listener;
	}
}
