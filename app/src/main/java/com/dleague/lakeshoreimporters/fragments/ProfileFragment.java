package com.dleague.lakeshoreimporters.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.AddressBookActivity;
import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.activities.ChangePassword;
import com.dleague.lakeshoreimporters.activities.EditCustomerInfo;
import com.dleague.lakeshoreimporters.activities.MyOrdersActivity;
import com.dleague.lakeshoreimporters.activities.StoreCredits;
import com.dleague.lakeshoreimporters.utils.MessageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_NAME;

public class ProfileFragment extends Fragment {

    @BindView(R.id.tv_welcome)
    TextView tvWelcomeUser;

    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container,false);
        ButterKnife.bind(this, rootView);
        tvWelcomeUser.setText("Welcome " + AppSpace.sharedPref.readValue(CUSTOMER_NAME, "to Lakeshore-Importers"));
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.layout_address_book)void setAddressBook(){
        startActivity(new Intent(getActivity(), AddressBookActivity.class));
    }

    @OnClick(R.id.layout_my_orders)void setMyOrders(){
        startActivity(new Intent(getActivity(), MyOrdersActivity.class));
    }

    @OnClick(R.id.layout_edit_profile)void setEditProfile(){
        startActivity(new Intent(getActivity(), EditCustomerInfo.class));
    }

    @OnClick(R.id.layout_store_credit)void setStoreCredits(){
        startActivity(new Intent(getActivity(), StoreCredits.class));
    }

    @OnClick(R.id.layout_change_password)void setChangePassword(){
        if(AppSpace.sharedPref.readValue(CUSTOMER_ID, "0").equals("0")){
            MessageUtil.showSnackbarMessage(rootView, "Something went wrong!");
        }else {
            startActivity(new Intent(getActivity(), ChangePassword.class));
        }
    }

    private void init() {

    }
}
