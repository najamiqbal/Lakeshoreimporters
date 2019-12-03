package com.dleague.lakeshoreimporters.activities;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.UpdateCustomerMutation;
import com.dleague.lakeshoreimporters.dtos.responsedto.UpdatePasswordResponse;
import com.dleague.lakeshoreimporters.httpcalls.HttpNetworkCall;
import com.dleague.lakeshoreimporters.httpcalls.HttpResponseCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.type.CustomerUpdateInput;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.UPDATE_PASSWORD;

public class ChangePassword extends AppCompatActivity implements HttpResponseCallbacks {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView tvToolbarTitle;
    @BindView(R.id.et_cp_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_cp_confirm_password)
    EditText etConfirdNewPAssword;
    @BindView(R.id.btn_cp_update)
    Button btnUpdatePassword;

    private View rootView;
    private HttpNetworkCall networkCalls;
    private DialogBuilder dialogBuilder;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvToolbarTitle.setText("Change Password");
        init();
    }

    @OnClick(R.id.btn_cp_update)
    void setBtnUpdatePassword() {

        if (!etNewPassword.getText().toString().isEmpty()) {
            if (!etConfirdNewPAssword.getText().toString().isEmpty()) {
                if (etNewPassword.getText().toString().equals(etConfirdNewPAssword.getText().toString())) {
                    password = etNewPassword.getText().toString();
                    etNewPassword.setText("");
                    etConfirdNewPAssword.setText("");
                    updateCustomer();
                } else {
                    MessageUtil.showSnackbarMessage(rootView, "Password Don't Match!");
                }
            } else {
                MessageUtil.showSnackbarMessage(rootView, "Please Enter Confirm New Password!");
            }
        } else {
            MessageUtil.showSnackbarMessage(rootView, "Please Enter New Password!");
        }
    }

    private void init() {
        networkCalls = new HttpNetworkCall(this, this);
        dialogBuilder = new DialogBuilder(this);
        rootView = findViewById(android.R.id.content);
    }

    private void updateCustomer() {
        new ChangePasswordTask().execute();
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Object object) {
        hideLoadingDialog();
        if (responseCode == UPDATE_PASSWORD) {
            handleUpdateCustomerReseponse(object);
        }
    }

    @Override
    public void onFailure(int responseCode, String message) {
        hideLoadingDialog();
        Log.e(LOG_TAG, message);
    }

    private void handleUpdateCustomerReseponse(Object object) {
        if (Validations.isObjectNotEmptyAndNull(object)) {
            UpdatePasswordResponse data = (UpdatePasswordResponse) object;
            if (data.isStatus()) {
                MessageUtil.showSnackbarMessage(rootView, "Password Changed Successfully.");
            } else {
                MessageUtil.showSnackbarMessage(rootView, "Password Changing Failed.");
            }
        }
    }

    private void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.loadingDialog("Loading...");
            }
        });
    }

    private void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismissDialog();
            }
        });
    }

    public class ChangePasswordTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            networkCalls.updateCustomerPassword(AppSpace.sharedPref.readValue(CUSTOMER_ID, "0"), password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
