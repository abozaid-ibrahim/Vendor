package com.timore.vendor.views;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;

import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by usear on 11/30/2015.
 */
public class ForgetPasswordDialog extends Dialog {
    private EditText emailView;
    private ProgressBar progressBar;

    public ForgetPasswordDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_forget_password);
        setTitle(context.getString(R.string.forgetPassword));
        emailView = (EditText) findViewById(R.id.fpw_email);
        progressBar = (ProgressBar) findViewById(R.id.submit_progress);
        findViewById(R.id.fpw_dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validInputs())
                    updatePassword(v);
                else
                    Snackbar.make(v, context.getString(R.string.checkInputs), Snackbar.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.fpw_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private boolean validInputs() {

        if (emailView.getText().toString().isEmpty()) {
            emailView.setError(getContext().getString(R.string.enter_field));
            return false;
        } else {
            emailView.setError(null);
            if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailView.getText().toString())) {
                emailView.setError(getContext().getString(R.string.enter_valid_email));
                return false;
            }
        }

        return true;
    }

    private void updatePassword(final View view) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().forgetPass(App.userId, emailView.getText().toString(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        Retrofit.res(object + "", response);
                        progressBar.setVisibility(View.GONE);
                        int res = Integer.valueOf(object.get("success").toString());
                        if (res > 0) {
                            emailView.setText("");
                            Snackbar.make(view, getContext().getString(R.string.newpass2mail), Snackbar.LENGTH_LONG).show();
                            dismiss();
                        } else
                            Snackbar.make(view, getContext().getString(R.string.email_not_registered), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        progressBar.setVisibility(View.GONE);
                        if (!App.isConnected(getContext()))
                            Snackbar.make(view, getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }


}
