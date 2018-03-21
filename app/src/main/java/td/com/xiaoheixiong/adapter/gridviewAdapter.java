package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import td.com.xiaoheixiong.R;

public class gridviewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	private Integer[] imgId;
	public gridviewAdapter(Context context, ArrayList<HashMap<String, Object>> list, Integer[] imgId) {
		this.mContext = context;
		this.list = list;
		this.imgId = imgId;
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int arg0) {

		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);
			holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.text.setTextSize(16);
			convertView.setTag(holder);
		}
		holder.text.setText(list.get(position).get("templateName")+"");
		if (imgId.length-1 < position){
			holder.grid_img.setImageResource(R.mipmap.msg_icon);
		}else {
			holder.grid_img.setImageResource(imgId[position]);

		}
		return convertView;
	}

	class ViewHolder {
		TextView text;
		ImageView grid_img;
	}
}
