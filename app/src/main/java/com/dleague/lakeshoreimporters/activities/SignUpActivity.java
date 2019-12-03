package com.dleague.lakeshoreimporters.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.RegisterUserMutation;
import com.dleague.lakeshoreimporters.dtos.UserDTO;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.HelperMethods;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CREATE_CUSTOMER;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;

public class SignUpActivity extends AppCompatActivity implements NetworkCallbacks {

    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_signup_email)
    EditText etEmail;
    @BindView(R.id.et_signup_password)
    EditText etPassword;

    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private View rootView;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        initObjs();
    }

    @OnClick(R.id.btn_signup)void setSignUp(){
        HelperMethods.hideKeyboard(this);
        if(fieldsValidation()){
            email = etEmail.getText().toString();
            UserDTO userDTO = new UserDTO(1L,etFirstName.getText().toString(), etLastName.getText().toString(),
                    etEmail.getText().toString(), etPassword.getText().toString());
            createUser(userDTO);
        }
    }

    @OnClick(R.id.tv_already_registered)void setDontHaveAccount(){
        HelperMethods.hideKeyboard(this);
        launchSignInScreen();
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if(responseCode == CREATE_CUSTOMER){
            handleCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG, exception.getMessage());
    }

    private void initObjs(){
        rootView = findViewById(android.R.id.content);
        dialogBuilder = new DialogBuilder(this);
        networkCalls = new NetworkCalls(this, this);
    }

    private boolean fieldsValidation(){
        if(Validations.isStringEmpty(etFirstName.getText().toString())){
            MessageUtil.showSnackbarMessage(rootView,"Please enter First Name.");
            return false;
        }else if(Validations.isStringEmpty(etLastName.getText().toString())){
            MessageUtil.showSnackbarMessage(rootView,"Please enter Last Name.");
            return false;
        }else if(Validations.isStringEmpty(etEmail.getText().toString())){
            MessageUtil.showSnackbarMessage(rootView,"Please enter Email.");
            return false;
        }else if(Validations.isStringEmpty(etPassword.getText().toString())){
            MessageUtil.showSnackbarMessage(rootView,"Please enter Password.");
            return false;
        }

        return  true;
    }

    private void createUser(UserDTO userDTO){
        if(Validations.isObjectNotEmptyAndNull(userDTO)) {
            networkCalls.createUser(userDTO);
        }
    }

    private void handleCallbackResponse(Response response){
        if(Validations.isObjectNotEmptyAndNull(response)){
            RegisterUserMutation.Data data = (RegisterUserMutation.Data) response.data();
            if(data.customerCreate().customerUserErrors().size()>0){
                MessageUtil.showSnackbarMessage(rootView, data.customerCreate().customerUserErrors().get(0).message());
            }else{
                if (!Validations.isStringEmpty(data.customerCreate().customer().id())) {
                    UserDTO userDTO = new UserDTO(1L, data.customerCreate().customer().firstName(), data.customerCreate().customer().lastName(),
                            data.customerCreate().customer().email(), "");
                    AppSpace.localDbOperations.storeUserData(userDTO);
                    AppSpace.sharedPref.writeValue(CUSTOMER_EMAIL, email);
                    AppSpace.sharedPref.writeValue(SESSION, data.customerCreate().customer().id());
                    launchNextScreen();
                }
            }
        }
    }

    private void launchNextScreen(){
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }


    private void launchSignInScreen() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    private void showLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.loadingDialog("Loading...");
            }
        });
    }

    private void hideLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismissDialog();
            }
        });
    }
}

