package td.com.xiaoheixiong.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by andry on 2016/9/26.
 */
public  class RecyclerViewHolder extends RecyclerView.ViewHolder{

    private String TAG = "RecyclerViewHolder";
    private RecyclerViewItemTouchListener mlistener;
    public RecyclerViewHolder(View itemView, RecyclerViewItemTouchListener listener) {
        super(itemView);

        mlistener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: ");
                if(mlistener!=null)
                    mlistener.onItemTouch(v,getAdapterPosition());
            }
        });



        itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
               // Log.d(TAG, "onLongClick: ");
                if(mlistener!=null){
                    mlistener.onTtemLongTouch(v,getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }

}
