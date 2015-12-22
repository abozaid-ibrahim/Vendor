package com.timore.vendor.views;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by usear on 11/30/2015.
 */
public class ChangePasswordDialog extends Dialog {
    private EditText passView;
    private EditText npassView;
    private EditText confPassView;
    private ProgressBar progressBar;

    public ChangePasswordDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_password);
        setTitle(context.getString(R.string.change_password));
        passView = (EditText) findViewById(R.id.pw_oldPassword);
        npassView = (EditText) findViewById(R.id.pw_newPassword);
        confPassView = (EditText) findViewById(R.id.pw_cPassword);
        progressBar = (ProgressBar) findViewById(R.id.submit_progress);
        findViewById(R.id.pw_dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validInputs())
                    updatePassword(v);
                else
                    Snackbar.make(v, context.getString(R.string.checkInputs), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean validInputs() {
        if (passView.getText().toString().isEmpty()) {
            passView.setError(getContext().getString(R.string.enter_field));
            return false;
        } else {
            passView.setError(null);
        }
        if (npassView.getText().toString().isEmpty()) {
            npassView.setError(getContext().getString(R.string.enter_field));
            return false;
        } else {
            npassView.setError(null);
            confPassView.setError(null);
            if (!npassView.getText().toString().equals(confPassView.getText().toString())) {
                confPassView.setError(getContext().getString(R.string.pass_doesnt_match));
                return false;
            }
        }

        return true;
    }

    private void updatePassword(final View view) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().updateUserPassword(App.userId, passView.getText().toString(), npassView.getText().toString(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                progressBar.setVisibility(View.GONE);
                int res = Integer.valueOf(object.get("password").toString());
                if (res > 0) {
                    Snackbar.make(view, "Password Changed", Snackbar.LENGTH_LONG).show();
                    dismiss();
                } else
                    Snackbar.make(view, "Password Changed", Snackbar.LENGTH_LONG).show();
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
