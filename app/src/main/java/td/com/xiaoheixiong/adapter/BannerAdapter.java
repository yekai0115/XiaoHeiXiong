package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import java.util.LinkedList;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.beans.home.Adlist;


/**
 * 首页banner适配器
 */
public class BannerAdapter extends PagerAdapter {
    private List<Adlist> list;
    private Context context;
    private HolderView holderView;
    private LayoutInflater mInflater;
    private LinkedList<View> mViewCache = null;
    private int width;

    public BannerAdapter(Context context, List<Adlist> subjectsInfos) {
        this.context = context;
        this.list = subjectsInfos;
        this.mInflater = LayoutInflater.from(context);
        this.mViewCache = new LinkedList<>();
        width = DimenUtils.getWidth(context);
    }

    public void update(List<Adlist> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View convertView = null;
        if (mViewCache.size() == 0) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.banner_item_view, null, false);
            holderView.iv_banner = (ImageView) convertView.findViewById(R.id.iv_banner);
            convertView.setTag(holderView);
        } else {
            convertView = mViewCache.removeFirst();
            holderView = (HolderView) convertView.getTag();
        }
        final Adlist bean = list.get(position);
        String banner = (bean.getImgUrl()).trim();
        Glide.with(context).load(banner)
                .fitCenter()
                .override(width, DimenUtils.dip2px(context, 540))
                .placeholder(R.drawable.pic_nomal_loading_style)
                .error(R.drawable.pic_nomal_loading_style)
                .into(holderView.iv_banner);
        // container.addView(convertView);


        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return convertView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);

    }

    static class HolderView {

        private ImageView iv_banner;
    }
}