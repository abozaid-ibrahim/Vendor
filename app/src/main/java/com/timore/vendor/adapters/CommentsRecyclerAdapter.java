package com.timore.vendor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Comment;
import com.timore.vendor.beanBojo.CommentViewHolder;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.PostViewHolder;
import com.timore.vendor.control.PostActions;

import java.util.List;

/**
 * Created by Abuzeid on 11/27/2015.
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public List<Comment> data;
    Context context;
    private Post post;

    public CommentsRecyclerAdapter(Context context, Post post, List<Comment> data) {
        this.context = context;
        this.data = data;
        this.post = post;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_comment, parent, false));

        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.row_rv_post, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CommentViewHolder) {
            PostActions.setComments(this, context, (CommentViewHolder) holder, data.get(position - 1), position - 1);

        } else if (holder instanceof PostViewHolder) {
            PostActions.setData(null, context, (PostViewHolder) holder, post, position);
            ((PostViewHolder) holder).commentBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }
}
