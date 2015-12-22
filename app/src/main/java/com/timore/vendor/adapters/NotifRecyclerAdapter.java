package com.timore.vendor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Notif;
import com.timore.vendor.control.Utils;

import java.util.List;

/**
 * Created by Abuzeid on 11/27/2015.
 */
public class NotifRecyclerAdapter extends RecyclerView.Adapter<NotifRecyclerAdapter.ViewHolder> {
    Context context;
    List<Notif> data;

    public NotifRecyclerAdapter(Context context, List<Notif> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_notif, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.titleTv.setText(data.get(position).getPost());
//        holder.dateTv.setText(data.get(position).getPost());
        holder.dateTv.setText(data.get(position).getContent());
//


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Utils.gotoUserProfile(context,data.get(position).getParent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView dateTv;
        ImageView logoView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleTv = (TextView) itemView.findViewById(R.id.notifItem_title);
            dateTv = (TextView) itemView.findViewById(R.id.notifItem_content);
            logoView = (ImageView) itemView.findViewById(R.id.notifItem_image);
        }
    }
}
