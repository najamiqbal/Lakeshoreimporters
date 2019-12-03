package com.dleague.lakeshoreimporters.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.GetCustomerQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.UpdateCustomerMutation;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.type.CustomerUpdateInput;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.GET_CUSTOMER;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;
import static com.dleague.lakeshoreimporters.utils.Constants.UPDATE_CUSTOMER;

public class EditCustomerInfo extends AppCompatActivity implements NetworkCallbacks {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView tvToolbarTitle;
    @BindView(R.id.et_ec_firstname)
    EditText etFirstName;
    @BindView(R.id.et_ec_lastname)
    EditText etLastName;
    @BindView(R.id.et_ec_email)
    EditText etEmail;
    @BindView(R.id.et_ec_phone)
    EditText etPhone;

    @BindView(R.id.iv_ec_firstname)
    ImageView ivFirstName;
    @BindView(R.id.iv_ec_lastname)
    ImageView ivLastName;
    @BindView(R.id.iv_ec_email)
    ImageView ivEmail;
    @BindView(R.id.iv_ec_phone)
    ImageView ivPhone;
    @BindView(R.id.btn_ec_update)
    Button btnUpdate;
    @BindView(R.id.ccp_reg)
    CountryCodePicker countryCodePicker;

    NetworkCalls networkCalls;
    DialogBuilder dialogBuilder;
    boolean isFN, isLN, isEmail, isPhone, isBtnEnable;
    View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_info);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvToolbarTitle.setText("Edit Profile");
        init();
        getCustomer();
    }

    @OnClick(R.id.iv_ec_firstname)
    void setEditFN() {
        if (isFN) {
            isFN = false;
            etFirstName.setEnabled(false);
            ivFirstName.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        } else {
            isFN = true;
            isBtnEnable = true;
            etFirstName.setEnabled(true);
            btnUpdate.setBackgroundColor(getResources().getColor(R.color.themeBlue));
            ivFirstName.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        }
    }

    @OnClick(R.id.iv_ec_lastname)
    void setEditLN() {
        if (isLN) {
            isLN = false;
            etLastName.setEnabled(false);
            ivLastName.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        } else {
            isLN = true;
            isBtnEnable = true;
            btnUpdate.setBackgroundColor(getResources().getColor(R.color.themeBlue));
            etLastName.setEnabled(true);
            ivLastName.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        }
    }

    @OnClick(R.id.iv_ec_email)
    void setEditEmail() {
        if (isEmail) {
            isEmail = false;
            etEmail.setEnabled(false);
            ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        } else {
            isEmail = false;
            etEmail.setEnabled(true);
            isBtnEnable = true;
            btnUpdate.setBackgroundColor(getResources().getColor(R.color.themeBlue));
            ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        }
    }

    @OnClick(R.id.iv_ec_phone)
    void setEditPhone() {
        if (isPhone) {
            isPhone = false;
            etPhone.setEnabled(false);
            countryCodePicker.setEnabled(false);
            ivPhone.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        } else {
            isPhone = true;
            isBtnEnable = true;
            countryCodePicker.setEnabled(true);
            btnUpdate.setBackgroundColor(getResources().getColor(R.color.themeBlue));
            etPhone.setEnabled(true);
            ivPhone.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        }
    }

    @OnClick(R.id.btn_ec_update)
    void setUpdateButton() {
        if(isBtnEnable) {
            CustomerUpdateInput input = CustomerUpdateInput.builder()
                    .firstName(etFirstName.getText().toString())
                    .lastName(etLastName.getText().toString())
                    .email(etEmail.getText().toString())
                    .phone("+" + countryCodePicker.getSelectedCountryCode() + etPhone.getText().toString())
                    .build();
            updateCustomer(input);
        }
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if (responseCode == GET_CUSTOMER) {
            handleGetCustomerReseponse(response);
        }

        if (responseCode == UPDATE_CUSTOMER) {
            handleUpdateCustomerReseponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG,exception.getMessage());
    }

    private void init() {
        networkCalls = new NetworkCalls(this, this);
        dialogBuilder = new DialogBuilder(this);
        rootView = findViewById(android.R.id.content);
    }

    private void updateCustomer(CustomerUpdateInput input) {
        networkCalls.updateCustomer(AppSpace.sharedPref.readValue(SESSION, "0"), input);
    }

    private void getCustomer() {
        networkCalls.getCustomer(AppSpace.sharedPref.readValue(SESSION, "0"));
    }

    private void handleGetCustomerReseponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetCustomerQuery.Data data = (GetCustomerQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data.customer())) {
                etFirstName.setText(data.customer().firstName());
                etLastName.setText(data.customer().lastName());
                etEmail.setText(data.customer().email());
                etPhone.setText(data.customer().phone());
            }
        }
    }

    private void handleUpdateCustomerReseponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            UpdateCustomerMutation.Data data = (UpdateCustomerMutation.Data) response.data();
            if(data.customerUpdate().customerUserErrors().size() > 0){
                MessageUtil.showSnackbarMessage(rootView, data.customerUpdate().customerUserErrors().get(0).message());
            }else if(Validations.isStringNotEmptyAndNull(data.customerUpdate().customer().id())) {
                finish();
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

}
