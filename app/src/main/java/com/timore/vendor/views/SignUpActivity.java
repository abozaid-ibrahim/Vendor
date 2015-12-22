package com.timore.vendor.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.VAR;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends SuperActivity implements View.OnClickListener {

    @Bind(R.id.submit_progress)
    ProgressBar progressBar;
    @Bind(R.id.signup_submit)
    Button submit;
    @Bind(R.id.signup_userName)
    EditText userNameEt;
    @Bind(R.id.signup_userPassword)
    EditText userPassEt;
    @Bind(R.id.signup_confEmail)
    EditText confEmailEt;
    @Bind(R.id.signup_userEmail)
    EditText emailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        super.setToolBar(findViewById(R.id.toolbar), true);
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_submit:
                signUp(v);
                break;
            default:
                System.err.println("ONCLICK DEFAULT CASE");
                break;
        }
    }

    private void signUp(View view) {
        App.hideSoftInput(this);
        if (validInputs())
            completeSignup(view);
        else
            Snackbar.make(findViewById(R.id.main_layout), getString(R.string.checkInputs), Snackbar.LENGTH_SHORT).show();
    }

    private void completeSignup(final View view) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().register("1", userNameEt.getText().toString().trim(),
                emailEt.getText().toString().trim(),
                userPassEt.getText().toString().trim(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject s, Response response) {
                        Retrofit.res(s + "", response);
                        App.print(s + "");
                        progressBar.setVisibility(View.GONE);

                        int id = 0;
                        if ((id = Integer.valueOf(s.get("id").toString())) > 0) {
                            getSharedPreferences(VAR.PREF_NAME, 0).edit().putInt(VAR.KEY_USER_ID, id).commit();
                            App.userId=id;
                            Intent main = new Intent(SignUpActivity.this, MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        progressBar.setVisibility(View.GONE);

                        if (!App.isConnected(SignUpActivity.this))
                            Snackbar.make(view, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private boolean validInputs() {
        if (userNameEt.getText().toString().isEmpty()) {
            userNameEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            userNameEt.setError(null);
        }
        if (userPassEt.getText().toString().isEmpty()) {
            userPassEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            userPassEt.setError(null);
        }
        if (emailEt.getText().toString().isEmpty()) {

            emailEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            if (Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailEt.getText().toString().trim()))
                emailEt.setError(null);
            else {
                emailEt.setError(getString(R.string.enter_valid_email));
                return false;
            }
        }
        if (confEmailEt.getText().toString().isEmpty()) {
            confEmailEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            confEmailEt.setError(null);
        }
        if (!emailEt.getText().toString().trim().equals(confEmailEt.getText().toString().trim())) {
//            emailEt.setError(getstrin);
            return false;
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.err.println("ON ITEM SELECTED");
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}
