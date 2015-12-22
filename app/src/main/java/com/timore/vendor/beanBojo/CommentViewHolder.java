package com.timore.vendor.beanBojo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.timore.vendor.R;

/**
 * Created by usear on 12/1/2015.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView deleteBtn;
    public ImageView logoView;
    public TextView titleTv;
    public TextView contentTv;
    public TextView dateTv;

    public CommentViewHolder(View itemView) {
        super(itemView);
        deleteBtn = (ImageView) itemView.findViewById(R.id.commentItem_delete);
        titleTv = (TextView) itemView.findViewById(R.id.commentItem_title);
        contentTv = (TextView) itemView.findViewById(R.id.commentItem_content);
        dateTv = (TextView) itemView.findViewById(R.id.commentItem_date);
        logoView = (ImageView) itemView.findViewById(R.id.commentItem_image);

    }
}
