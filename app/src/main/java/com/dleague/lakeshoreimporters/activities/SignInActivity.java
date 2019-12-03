package com.dleague.lakeshoreimporters.activities;

import android.app.AlertDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.CreateUserAccessTokenMutation;
import com.dleague.lakeshoreimporters.CustomerRecoverMutation;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.UserDTO;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.HelperMethods;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_RECOVER;
import static com.dleague.lakeshoreimporters.utils.Constants.LOGIN_CUSTOMER;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;

public class SignInActivity extends AppCompatActivity implements NetworkCallbacks {

    @BindView(R.id.et_signin_email)
    EditText etEmail;
    @BindView(R.id.et_signin_password)
    EditText etPassword;

    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private View rootView;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        initObjs();
    }

    @OnClick(R.id.tv_dont_have_account)
    void setDontHaveAccount() {
        HelperMethods.hideKeyboard(this);
        launchSignUpScreen();
    }

    @OnClick(R.id.tv_foreget_pass)
    void setForgetPassword() {
        getEmail();
    }

    @OnClick(R.id.btn_login)
    void setLogin() {
        HelperMethods.hideKeyboard(this);
        if (fieldsValidation()) {
            email = etEmail.getText().toString();
            UserDTO userDTO = new UserDTO(1L, "", "",
                    etEmail.getText().toString(), etPassword.getText().toString());
            createUser(userDTO);
        }
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if (responseCode == LOGIN_CUSTOMER) {
            handleCallbackResponse(response);
        }
        if (responseCode == CUSTOMER_RECOVER) {
            handleRecoverPasswordResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG, exception.getMessage());
    }

    private void initObjs() {
        rootView = findViewById(android.R.id.content);
        dialogBuilder = new DialogBuilder(this);
        networkCalls = new NetworkCalls(this, this);
    }

    private boolean fieldsValidation() {
        if (Validations.isStringEmpty(etEmail.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter Email.");
            return false;
        } else if (Validations.isStringEmpty(etPassword.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter Password.");
            return false;
        }

        return true;
    }

    private void createUser(UserDTO userDTO) {
        if (Validations.isObjectNotEmptyAndNull(userDTO)) {
            networkCalls.loginUser(userDTO);
        }
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            CreateUserAccessTokenMutation.Data data = (CreateUserAccessTokenMutation.Data) response.data();
            if (data.customerAccessTokenCreate().customerUserErrors().size() > 0) {
                MessageUtil.showSnackbarMessage(rootView, data.customerAccessTokenCreate().customerUserErrors().get(0).message());
            } else {
                if (!Validations.isStringEmpty(data.customerAccessTokenCreate().customerAccessToken().accessToken())) {
                    UserDTO userDTO = new UserDTO(1L, "", "",
                            etEmail.getText().toString(), "");
                    AppSpace.localDbOperations.storeUserData(userDTO);
                    AppSpace.sharedPref.writeValue(CUSTOMER_EMAIL, email);
                    AppSpace.sharedPref.writeValue(SESSION, data.customerAccessTokenCreate().customerAccessToken().accessToken());
                    launchNextScreen();
                }
            }
        }
    }

    private void handleRecoverPasswordResponse(Response response) {
        CustomerRecoverMutation.Data data = (CustomerRecoverMutation.Data) response.data();
        if(Validations.isObjectNotEmptyAndNull(data) && Validations.isObjectNotEmptyAndNull(data.customerRecover())) {
            if (data.customerRecover().customerUserErrors().size() > 0) {
                MessageUtil.showSnackbarMessage(rootView, data.customerRecover().customerUserErrors().get(0).message());
            } else {
                MessageUtil.showSnackbarMessage(rootView, "We've sent you an email with a link to update your password.");
            }
        }else{
            MessageUtil.showSnackbarMessage(rootView, "Invalid Data.");
        }
    }

    private void launchNextScreen() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }


    private void launchSignUpScreen() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        finish();
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

    public void getEmail() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.custom_layout, null);
        TextView textMsg = view.findViewById(R.id.hintText);
        textMsg.setText("Email");
        alert.setView(view);
        final EditText inputkey = view.findViewById(R.id.input_key);
        inputkey.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        inputkey.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        Button btn_done = view.findViewById(R.id.btn_c_done);
        Button btn_cancle = view.findViewById(R.id.btn_c_cancle);
        alert.setView(view);
        final AlertDialog b = alert.create();
        b.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        b.show();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String etKey = inputkey.getText().toString();
                if (!etKey.isEmpty()) {
                    networkCalls.recoverCustomer(etKey);
                    b.dismiss();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid device name!", Snackbar.LENGTH_LONG).show();
                    b.dismiss();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }
}

