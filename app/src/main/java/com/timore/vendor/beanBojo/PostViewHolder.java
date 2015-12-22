package com.timore.vendor.beanBojo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.timore.vendor.R;

/**
 * Created by usear on 12/1/2015.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {
    public View menuDelete;
    public View menuEdit;
    public TextView titleTv;
    public TextView contentTv;
    public TextView dateTv;
    public ImageView logoView;
    public ImageView imageView;
    public ImageButton likeBtn;
    public ImageButton commentBtn;
    public ImageButton menuBtn;
    public View main_menu;
    public View menuReport;

    public PostViewHolder(View itemView) {
        super(itemView);
        titleTv = (TextView) itemView.findViewById(R.id.main_row_name);
        contentTv = (TextView) itemView.findViewById(R.id.main_row_content);
        dateTv = (TextView) itemView.findViewById(R.id.main_row_date);
        logoView = (ImageView) itemView.findViewById(R.id.main_row_logo);
        imageView = (ImageView) itemView.findViewById(R.id.main_row_image);
        menuBtn = (ImageButton) itemView.findViewById(R.id.main_row_menu);
        likeBtn = (ImageButton) itemView.findViewById(R.id.main_row_like);
        commentBtn = (ImageButton) itemView.findViewById(R.id.main_row_comment);
        menuReport = itemView.findViewById(R.id.main_row_report);
        menuDelete = itemView.findViewById(R.id.main_row_delete);
        menuEdit = itemView.findViewById(R.id.main_row_edit);
        main_menu = itemView.findViewById(R.id.main_menu);
    }

}
