package td.com.xiaoheixiong.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MeasureUtils;
import td.com.xiaoheixiong.Utils.UiUtils;
import td.com.xiaoheixiong.views.NoScrollGridView;

public class CircleGridAdapter extends BaseAdapter {

    //private String[] mFiles;
    private LayoutInflater mLayoutInflater;
    private List<String> list;
    private Context mcontext;
private ImageLoader imageLoader;
    public CircleGridAdapter(Context context, List<String> datas) {
        this.list = datas;
        this.mcontext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_gridview_circle, null);
            //	convertView = mLayoutInflater.inflate(R.layout.item_gridview_circle,parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.album_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 根据屏幕宽度动态设置图片宽高
        int width = MeasureUtils.getWidth(UiUtils.getContext());
        int imageWidth = (width / 3 - 60);

        holder.imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        final String url = getItem(position);
      //  imageLoader.init(ImageLoaderConfiguration.createDefault(mcontext));

    //    ImageLoader.getInstance().displayImage(url, holder.imageView);
        Picasso.with(mcontext).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(holder.imageView);


    /*    holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBigPicture(url);
            }
        });
*/
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
      /* 点击图片放大查看 */

    public void getBigPicture(String url) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
        final AlertDialog dialog = new AlertDialog.Builder(mcontext).create();
        ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
        if (url != null) {
            Glide.with(mcontext).load(url).asBitmap().into(img);
            dialog.setView(imgEntryView); // 自定义dialog
            dialog.show();
        }
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });

    }
}
