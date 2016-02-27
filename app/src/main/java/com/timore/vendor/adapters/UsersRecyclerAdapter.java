package com.timore.vendor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.timore.vendor.R;
import com.timore.vendor.beanBojo.User;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.UserActions;
import com.timore.vendor.control.Utils;

import java.util.List;

/**
 * Created by Abuzeid on 11/27/2015.
 */
public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {
    Context context;
    List<User> data;

    public UsersRecyclerAdapter(Context context, List<User> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_rv_search, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titleTv.setText(data.get(position).getUsername());
        holder.dateTv.setText(data.get(position).getDate_insert());
        holder.followBtn.setText(data.get(position).getFollow() == 0 ? context.getString(R.string.follow) : context.getString(R.string.unfollow));
        Image.obj(context).setImage(holder.logoView,data.get(position).getImage(),R.drawable.usericon);
//        Log.i("image",data.get(position).getImage());

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(holder.getAdapterPosition()).getFollow() == 0) {
                    UserActions.getInstance(context).followUser(data.get(holder.getAdapterPosition()).getId());
                    holder.followBtn.setText(context.getString(R.string.unfollow));
                    data.get(holder.getAdapterPosition()).setFollow(1);
                    notifyItemChanged(holder.getAdapterPosition());

                }else{
                    UserActions.getInstance(context).unFollowUser(data.get(holder.getAdapterPosition()).getId());
                    holder.followBtn.setText(context.getString(R.string.follow));
                    data.get(holder.getAdapterPosition()).setFollow(0);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoUserProfile(context, data.get(holder.getAdapterPosition()).getId(), data.get(holder.getAdapterPosition()).getUsername());
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
        Button addBtn;
        Button followBtn;
        Button messageBtn;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleTv = (TextView) itemView.findViewById(R.id.search_row_name);
            dateTv = (TextView) itemView.findViewById(R.id.search_row_date);
            logoView = (ImageView) itemView.findViewById(R.id.search_row_logo);
            addBtn = (Button) itemView.findViewById(R.id.search_row_add);
            followBtn = (Button) itemView.findViewById(R.id.search_row_follow);
            messageBtn = (Button) itemView.findViewById(R.id.search_row_message);
        }
    }
}
