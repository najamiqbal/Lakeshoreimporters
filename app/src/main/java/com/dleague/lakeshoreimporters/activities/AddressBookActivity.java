package com.dleague.lakeshoreimporters.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.DeleteCustomerAddressMutation;
import com.dleague.lakeshoreimporters.GetCustomerAddressesQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.UpdateDefaultCustomerAddressMutation;
import com.dleague.lakeshoreimporters.adapters.AddressAdapter;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.DEFAULT_ADDRESS_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.DELETE_ADDRESS;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_CUSTOMER_ADDRESSES;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;
import static com.dleague.lakeshoreimporters.utils.Constants.SET_DEFAULT_ADDRESS;

public class AddressBookActivity extends AppCompatActivity implements ItemClickListener, NetworkCallbacks {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView toolbarTitle;
    @BindView(R.id.recyclerview_addresses)
    RecyclerView recyclerView;
    @BindView(R.id.iv_add_address)
    ImageView ivAddAddress;
    private View rootView;
    private AddressAdapter addressAdapter;
    private List<AddressDTO> addressDTOList;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        ButterKnife.bind(this);
        toolbarTitle.setText("Address Book");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        getCustomerAddresses();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Validations.isObjectNotEmptyAndNull(addressDTOList)) {
            addressDTOList.clear();
            addressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, Object object) {
        AddressDTO addressDTO = (AddressDTO) object;
        if(position == 1){//Set As Default
            AppSpace.sharedPref.writeValue(DEFAULT_ADDRESS_ID, addressDTO.getCursor());
            setDefaultAddress(addressDTO.getAddressId());
            addressAdapter.notifyDataSetChanged();
        }else if(position == 2){//Edit
            Intent intent = new Intent(this, EditAddressActivity.class);
            intent.putExtra("editAddress", addressDTO);
            startActivity(intent);
        }else if(position == 3){//Delete
            deleteAddress(addressDTO.getAddressId());
        }
    }

    @OnClick(R.id.iv_add_address)void setAddAddress(){
        startActivity(new Intent(this, AddAddressActivity.class));
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if(responseCode == GET_CUSTOMER_ADDRESSES){
            handleCallbackResponse(response);
        }

        if(responseCode == SET_DEFAULT_ADDRESS){
            handleDefaultAddressCallbackResponse(response);
        }

        if(responseCode == DELETE_ADDRESS){
            handleDeleteAddressCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG, exception.getMessage());
    }

    private void init() {
        rootView = findViewById(android.R.id.content);
        addressDTOList = new ArrayList<>();
        dialogBuilder = new DialogBuilder(this);
        networkCalls = new NetworkCalls(this, this);
        addressAdapter = new AddressAdapter(this, addressDTOList, this);
    }


    private void getCustomerAddresses() {
        networkCalls.getCustomerAddresses(AppSpace.sharedPref.readValue(SESSION, "0"));
    }

    private void setDefaultAddress(String addressId) {
        networkCalls.setDefaultCustomerAddress(addressId);
    }

    private void deleteAddress(String addressId) {
        networkCalls.deleteCustomerAddress(addressId);
    }


    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetCustomerAddressesQuery.Data data = (GetCustomerAddressesQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data)) {
                parseAddresses(data.customer());
                setAdapter();
            }

        }
    }


    private void handleDeleteAddressCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            DeleteCustomerAddressMutation.Data data = (DeleteCustomerAddressMutation.Data) response.data();
            if(data.customerAddressDelete().customerUserErrors().size()>0){
                MessageUtil.showSnackbarMessage(rootView, data.customerAddressDelete().customerUserErrors().get(0).message());
            }else {
                if (Validations.isObjectNotEmptyAndNull(data)) {
                    MessageUtil.showSnackbarMessage(rootView, "Address deleted successfully!");

                    for (int i = 0; i<addressDTOList.size(); i++){
                        if(addressDTOList.get(i).getAddressId().equals(data.customerAddressDelete().deletedCustomerAddressId())){
                            addressDTOList.remove(i);
                        }
                    }
                    addressDTOList.clear();
                    setAdapter();
                    getCustomerAddresses();
                }
            }

        }
    }

    private void handleDefaultAddressCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            UpdateDefaultCustomerAddressMutation.Data data = (UpdateDefaultCustomerAddressMutation.Data) response.data();
            if(data.customerDefaultAddressUpdate().customerUserErrors().size()>0){
                MessageUtil.showSnackbarMessage(rootView, data.customerDefaultAddressUpdate().customerUserErrors().get(0).message());
            }else {
                if (Validations.isObjectNotEmptyAndNull(data)) {
                    if (Validations.isStringNotEmptyAndNull(data.customerDefaultAddressUpdate().customer().defaultAddress().id()))
                    {
                        MessageUtil.showSnackbarMessage(rootView, "Default Address set successfully!");
                    }
                }
            }

        }
    }

    private void parseAddresses(GetCustomerAddressesQuery.Customer customer) {
        if(Validations.isObjectNotEmptyAndNull(customer)) {
            for (GetCustomerAddressesQuery.Edge addreses : customer.addresses().edges()) {
                addressDTOList.add(new AddressDTO(addreses.node().id(), addreses.node().firstName(), addreses.node().lastName(),
                        addreses.node().company(), addreses.node().address1(), addreses.node().address2(), addreses.node().city(),
                        addreses.node().province(), addreses.node().country(), addreses.node().zip(), addreses.node().phone(),
                        addreses.cursor()));
            }
        }
    }

    private void setAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                recyclerView.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
            }
        });
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
