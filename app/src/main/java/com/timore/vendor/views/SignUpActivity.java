package com.timore.vendor.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.control.App;
import com.timore.vendor.control.NetworkLoading;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.VAR;

import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends SuperActivity implements View.OnClickListener {

    Button submit;
    EditText userNameEt;
    EditText userPassEt;
    EditText confEmailEt;
    EditText emailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        super.setToolBar(findViewById(R.id.toolbar), true);

        submit = (Button) findViewById(R.id.signup_submit);
        userNameEt = (EditText) findViewById(R.id.signup_userName);
        userPassEt = (EditText) findViewById(R.id.signup_userPassword);
        confEmailEt = (EditText) findViewById(R.id.signup_confEmail);
        emailEt = (EditText) findViewById(R.id.signup_userEmail);

        submit.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        NetworkLoading.startLoading(this);
        Retrofit.getInstance().register("1", userNameEt.getText().toString().trim(),
                emailEt.getText().toString().trim(),
                userPassEt.getText().toString().trim(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject s, Response response) {
                        Retrofit.res(s + "", response);
                        App.print(s + "");
                        NetworkLoading.stopLoading();

                        int id = 0;
                        if ((id = Integer.valueOf(s.get("id").toString())) > 0) {
                            getSharedPreferences(VAR.PREF_NAME, 0).edit().putInt(VAR.KEY_USER_ID, id).commit();
                            App.userId = id;
                            Intent main = new Intent(SignUpActivity.this, MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        NetworkLoading.stopLoading();

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
        return emailEt.getText().toString().trim().equals(confEmailEt.getText().toString().trim());

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
