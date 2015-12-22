package com.timore.vendor.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.adapters.CommentsRecyclerAdapter;
import com.timore.vendor.beanBojo.Comment;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.SuperActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostActivity extends SuperActivity implements View.OnClickListener {
    /*Views*/
    @Bind(R.id.post_recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.activity_progress)
    ProgressBar progressBar;
    @Bind(R.id.post_et_comment)
    EditText commentEt;
    @Bind(R.id.main_layout)
    View main;


    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        super.setToolBar(findViewById(R.id.toolbar), true);
        ButterKnife.bind(this);

        init();
        findViewById(R.id.submit_comment).setOnClickListener(this);

    }


    private void init() {
        post = Parcels.unwrap(getIntent().getExtras().getParcelable(POST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleItemDecoration());
        getComments();

    }

    private void getComments() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getPostComments(1, post.getId(), new Callback<List<Comment>>() {
                    @Override
                    public void success(List<Comment> comments, Response response) {
                        Retrofit.res(comments + "", response);
                        progressBar.setVisibility(View.GONE);
                        if (comments != null ) {
                            recyclerView.setAdapter(new CommentsRecyclerAdapter(PostActivity.this,post, comments));
                        } else System.err.println("COMMENTS NLLLLLLLLLLLLLLLLLL");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        progressBar.setVisibility(View.GONE);
                        if (!App.isConnected(PostActivity.this))
                            Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                }

        );
    }

    private void sendComment(String comment) {
        App.hideSoftInput(this);

        progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().comment(1, App.userId, post.getId(), comment, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                progressBar.setVisibility(View.GONE);
                boolean succ = Integer.valueOf(object.get("id").toString()) > 0;
                if (succ) {
                    commentEt.setText("");
                    Snackbar.make(main, "Done", Snackbar.LENGTH_LONG).show();
                    getComments();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                progressBar.setVisibility(View.GONE);
                if (!App.isConnected(PostActivity.this))
                    Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_comment:
                if (!commentEt.getText().toString().isEmpty())
                    sendComment(commentEt.getText().toString());
                else
                    Snackbar.make(main, getString(R.string.enter_field), Snackbar.LENGTH_LONG).show();

                break;
        }
    }
}
