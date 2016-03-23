package com.timore.vendor.control;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.adapters.CommentsRecyclerAdapter;
import com.timore.vendor.adapters.PostsRecyclerAdapter;
import com.timore.vendor.beanBojo.Comment;
import com.timore.vendor.beanBojo.CommentViewHolder;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.PostViewHolder;
import com.timore.vendor.views.FullScreenImageActivity;
import com.timore.vendor.views.PostActivity;

import org.parceler.Parcels;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by usear on 12/1/2015.
 */
public class PostActions {
    public static void setData(final PostsRecyclerAdapter adapter, final Context context, final PostViewHolder holder, final Post post, final int position) {
        holder.userNameTv.setText(post.getUsername());
        holder.subjectTv.setText(post.getTitle());
        holder.contentTv.setText(post.getContent());
        holder.dateTv.setText(post.getDate_insert());
        holder.menuEdit.setVisibility(post.getUser_id() == App.userId ? View.VISIBLE : View.GONE);
        holder.menuDelete.setVisibility(post.getUser_id() == App.userId ? View.VISIBLE : View.GONE);
        Image.obj(context).setImage(holder.imageView, post.getFile(), R.drawable.logomin_trans);
        Image.obj(context).setImage(holder.logoView, post.getUser_image(), R.drawable.usericon);
        holder.likeBtn.setImageResource(post.getLike() == 1 ? R.drawable.liked : R.drawable.like);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getLike() == 1) {
                    UserActions.getInstance(context).unLike(adapter, position, post.getId());
                    holder.likeBtn.setImageResource(R.drawable.like);
                    post.setLike(0);
                } else {
                    UserActions.getInstance(context).like(adapter, position, post.getId());
                    holder.likeBtn.setImageResource(R.drawable.liked);
                    post.setLike(1);

                }
            }
        });
        holder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(context, holder);
            }
        });


        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(context, PostActivity.class);
                postIntent.putExtra(PostActivity.POST, Parcels.wrap(post));
                context.startActivity(postIntent);

            }
        });
        holder.menuReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostActions.report(adapter, context, post, position);
                switchMenu(context, holder);

            }
        });
        holder.menuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostActions.deletePost(adapter, context, post, position);

                switchMenu(context, holder);
            }
        });
        holder.menuEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.editPost(position, context, post);

                switchMenu(context, holder);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent postIntent = new Intent(context, FullScreenImageActivity.class);
                postIntent.putExtra(VAR.KEY_URL, post.getFile());
                context.startActivity(postIntent);

            }
        });

        View.OnClickListener userProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoUserProfile(context, post.getUser_id(), post.getUsername());

            }
        };
        holder.userNameTv.setOnClickListener(userProfile);
        holder.logoView.setOnClickListener(userProfile);
    }

    private static void switchMenu(Context context, PostViewHolder holder) {
        if (holder.main_menu.getVisibility() != View.VISIBLE) {
            holder.menuBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            holder.main_menu.setVisibility(View.VISIBLE);
            holder.main_menu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_menu));

        } else {
            holder.main_menu.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out));
            holder.main_menu.setVisibility(View.GONE);
            holder.menuBtn.setImageResource(R.drawable.menu);
        }
    }

    private static void deletePost(final PostsRecyclerAdapter adapter, final Context context, Post post, final int position) {
        Retrofit.getInstance().deletePost(1, App.userId, post.getId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (Integer.valueOf(object.get("sucess").toString()) > 0) {
                    if (adapter != null) {//adapter will ve null if it in comments act
                        adapter.data.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                    Toast.makeText(context, context.getString(R.string.deleted_post), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, context.getString(R.string.cant_delete_post), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Toast.makeText(context, context.getString(R.string.no_net), Toast.LENGTH_LONG).show();

            }
        });
    }

    private static void report(final PostsRecyclerAdapter adapter, final Context context, Post post, final int position) {
        Retrofit.getInstance().reportPost(1, App.userId, post.getId(), "", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (Integer.valueOf(object.get("id").toString()) > 0) {
                    if (adapter != null) {//adapter will ve null if it in comments act
                        adapter.data.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                    Toast.makeText(context, context.getString(R.string.reported_post), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, context.getString(R.string.cant_report_post), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Toast.makeText(context, context.getString(R.string.no_net), Toast.LENGTH_LONG).show();

            }
        });
    }


    public static void setComments(final CommentsRecyclerAdapter adapter, final Context context, final CommentViewHolder holder, final Comment post, final int position) {
        holder.titleTv.setText(post.getUsername());

        holder.contentTv.setText(post.getComment());
        holder.dateTv.setText(post.getDate_insert());

        Image.obj(context).setImage(holder.logoView, post.getImage(), R.drawable.user);


        holder.logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoUserProfile(context, post.getId(), post.getUsername());

            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment(context, adapter, post, holder.deleteBtn, position);
            }
        });
    }

    private static void deleteComment(final Context context, final CommentsRecyclerAdapter adapter, Comment post, final View view, final int position) {
        Retrofit.getInstance().deleteComment(1, post.getId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                boolean succ = Integer.valueOf(object.get("sucess").toString()) > 0;
                if (succ) {
                    Snackbar.make(view, "removed", Snackbar.LENGTH_LONG).show();
                    adapter.data.remove(position);
                    adapter.notifyItemRemoved(position);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Snackbar.make(view, context.getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

            }
        });
    }


}
