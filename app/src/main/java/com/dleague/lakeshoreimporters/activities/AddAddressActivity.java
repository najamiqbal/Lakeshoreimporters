package com.dleague.lakeshoreimporters.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.CreateCustomerAddressMutation;
import com.dleague.lakeshoreimporters.GetCollectionsByIdNextQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CREATE_ADDRESS;

public class AddAddressActivity extends AppCompatActivity implements NetworkCallbacks {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView toolbarTitle;
    @BindView(R.id.et_ad_first_name)
    EditText etFirstName;
    @BindView(R.id.et_ad_last_name)
    EditText etLastName;
    @BindView(R.id.et_ad_company)
    EditText etComapny;
    @BindView(R.id.et_ad_address1)
    EditText etAddress1;
    @BindView(R.id.et_ad_address2)
    EditText etAddress2;
    @BindView(R.id.et_ad_city)
    EditText etCity;
    @BindView(R.id.et_ad_state)
    EditText etState;
    @BindView(R.id.et_ad_country)
    EditText etCountry;
    @BindView(R.id.et_ad_postal_code)
    EditText etPostalCode;
    @BindView(R.id.et_ad_phone)
    EditText etPhone;

    private View rootView;
    private NetworkCalls networkCalls;
    private AddressDTO addressDTO;
    private DialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        toolbarTitle.setText("Add New Address");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initObj();
    }

    private void initObj() {
        rootView = findViewById(android.R.id.content);
        dialogBuilder = new DialogBuilder(this);
        networkCalls = new NetworkCalls(this, this);
    }

    @OnClick(R.id.btn_add_address)void setAddAddress(){
        if(!etAddress1.getText().toString().isEmpty() || !etAddress2.getText().toString().isEmpty()){
            addressDTO = new AddressDTO(etFirstName.getText().toString(), etLastName.getText().toString(), etComapny.getText().toString(),
                    etAddress1.getText().toString(), etAddress2.getText().toString(), etCity.getText().toString(), etState.getText().toString()
                    , etCountry.getText().toString(), etPostalCode.getText().toString(), etPhone.getText().toString());
            setAddress();
        }else{
            MessageUtil.showSnackbarMessage(rootView, "Please enter address.");
        }
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if(responseCode == CREATE_ADDRESS){
            handleCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
    }

    private void setAddress(){
        networkCalls.createAddress(addressDTO);
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            CreateCustomerAddressMutation.Data data = (CreateCustomerAddressMutation.Data) response.data();
            if(data.customerAddressCreate().customerUserErrors().size()>0){
                MessageUtil.showSnackbarMessage(rootView, data.customerAddressCreate().customerUserErrors().get(0).message());
            }else {
                if (Validations.isObjectNotEmptyAndNull(data)) {
                    if (!Validations.isStringEmpty(data.customerAddressCreate().customerAddress().id())) {
//                        AppSpace.localDbOperations.StoreAddress(addressDTO);
                        finish();
                    } else {
                        MessageUtil.showSnackbarMessage(rootView, "Address not set.");
                    }
                }
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
