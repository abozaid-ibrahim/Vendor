package com.timore.vendor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.PostViewHolder;
import com.timore.vendor.control.PostActions;

import java.util.List;

/**
 * Created by Abuzeid on 11/27/2015.
 */
public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    public List<Post> data;
    Context context;

    public PostsRecyclerAdapter(Context context, List<Post> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.row_rv_post, parent, false));
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        PostActions.setData(this,context, holder, data.get(position), position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
