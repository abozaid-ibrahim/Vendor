package com.timore.vendor.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.timore.vendor.R;
import com.timore.vendor.beanBojo.PostImage;
import com.timore.vendor.control.CameraImage;

import java.util.List;

/**
 * Created by Abuzeid on 11/27/2015.
 */
public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.ViewHolder> {
    public List<PostImage> data;
    Context context;

    public PostImagesAdapter(Context context, List<PostImage> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_post_images, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            holder.logoView.setImageBitmap(BitmapFactory.decodeFile(CameraImage.photoFile.getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logoView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            logoView = (ImageView) itemView.findViewById(R.id.post_images_item_iv);
        }
    }
}
