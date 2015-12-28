package com.timore.vendor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.control.Image;

import java.util.List;

/**
 * Created by usear on 11/29/2015.
 */
public class PostsGridAdapter extends BaseAdapter {
    private Context context;
    List<Post> data;

    public PostsGridAdapter(Context context, List<Post> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_grid_posts, parent, false);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.titleTv.setText(data.get(position).getTitle());
        holder.contentTv.setText(data.get(position).getContent());
        Image.obj(context).setImage(holder.postIv, data.get(position).getFile(),R.drawable.logomin_trans);

        return convertView;
    }

    class ViewHolder {
        ImageView postIv;
        TextView titleTv;
        TextView contentTv;

        public ViewHolder(View view) {
            titleTv = (TextView) view.findViewById(R.id.post_grow_title);
            contentTv = (TextView) view.findViewById(R.id.post_grow_content);
            postIv = (ImageView) view.findViewById(R.id.post_grow_image);
            view.setTag(this);

        }
    }
}
