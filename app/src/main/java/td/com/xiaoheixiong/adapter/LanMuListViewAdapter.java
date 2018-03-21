package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;

/**
 * Created by andry on 2016/10/27.
 */
public class LanMuListViewAdapter extends BaseAdapter {

    public String TAG = "LanMuListViewAdapter";
    public Context mContext;
    public LayoutInflater mLayoutInflater;
    public ArrayList<HashMap<String, Object>> mListData;
    public ArrayList<RadioButton> radioButtons;

    public LanMuListViewAdapter(Context context, ArrayList<HashMap<String, Object>> listdata) {
        mListData = listdata;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        radioButtons = new ArrayList<RadioButton>();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_shangpin_type, null);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.fenlei_rb);
            convertView.setTag(holder);
            holder.radioButton.setTag(position);
            radioButtons.add(holder.radioButton);
            if (position == 0) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);

            }

        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.radioButton.setTag(position);
        }
        holder.radioButton.setText(mListData.get(position).get("typeName")+"");
        return convertView;
    }

    public void setEable(int position) {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i) != null)
                if ((int) (radioButtons.get(i).getTag()) == position) {
                    radioButtons.get(i).setChecked(true);
                } else {
                    radioButtons.get(i).setChecked(false);
                }
        }

    }

    public class ViewHolder {

        RadioButton radioButton;
    }
}
