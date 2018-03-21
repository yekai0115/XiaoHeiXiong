package td.com.xiaoheixiong.beans.MultiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import me.drakeet.multitype.ItemViewBinder;
import td.com.xiaoheixiong.R;


/**
 * Created by lenovo on 2017/10/27.
 */

public class gridViewBinder extends ItemViewBinder<Griditem, gridViewBinder.ViewHolder> {

    @Override
    protected
    @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_griditem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Griditem griditem) {
        holder.setData(griditem);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private
        @NonNull
        ImageView img;
        private
        @NonNull
        TextView text;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            text = (TextView) itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),getLayoutPosition()+"", Toast.LENGTH_SHORT).show();
                }
            });

        }


        void setData(@NonNull final Griditem griditem) {
            img.setImageResource(griditem.ImgId);
            text.setText(griditem.text);
        }
    }


}