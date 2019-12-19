package com.dleague.lakeshoreimporters.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.AddressBookActivity;
import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.activities.CartActivity;
import com.dleague.lakeshoreimporters.activities.ChangePassword;
import com.dleague.lakeshoreimporters.activities.EditCustomerInfo;
import com.dleague.lakeshoreimporters.activities.MainActivity;
import com.dleague.lakeshoreimporters.activities.MyOrdersActivity;
import com.dleague.lakeshoreimporters.activities.SignInActivity;
import com.dleague.lakeshoreimporters.activities.StoreCredits;
import com.dleague.lakeshoreimporters.activities.WarrantyClaimActivity;
import com.dleague.lakeshoreimporters.utils.MessageUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_NAME;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tv_welcome)
    TextView tvWelcomeUser;
    Button my_bag,btn_logout;
    LinearLayout l_policies,l_binrequests,l_faq,l_rateus,l_warrenty;
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

/*    @OnClick(R.id.layout_store_credit)void setStoreCredits(){
        startActivity(new Intent(getActivity(), StoreCredits.class));
    }*/

    @OnClick(R.id.layout_change_password)void setChangePassword(){
        if(AppSpace.sharedPref.readValue(CUSTOMER_ID, "0").equals("0")){
            MessageUtil.showSnackbarMessage(rootView, "Something went wrong!");
        }else {
            startActivity(new Intent(getActivity(), ChangePassword.class));
        }
    }

    private void init() {
        my_bag=rootView.findViewById(R.id.my_bag);
        btn_logout=rootView.findViewById(R.id.btn_logout);
        l_binrequests=rootView.findViewById(R.id.layout_binrequests);
        l_faq=rootView.findViewById(R.id.layout_faq);
        l_policies=rootView.findViewById(R.id.layout_policies);
        l_rateus=rootView.findViewById(R.id.layout_rateus);
        l_warrenty=rootView.findViewById(R.id.layout_warrenty);
        my_bag.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        l_warrenty.setOnClickListener(this);
        l_rateus.setOnClickListener(this);
        l_policies.setOnClickListener(this);
        l_faq.setOnClickListener(this);
        l_binrequests.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_bag:
                startActivity(new Intent(getContext(), CartActivity.class));
                break;
            case R.id.btn_logout:
                logoutUser();
                break;
            case R.id.layout_binrequests:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(4);
                break;
            case R.id.layout_faq:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(5);
                break;
            case R.id.layout_rateus:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getContext().getPackageName())));

                break;
            case R.id.layout_policies:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(10);
                break;
            case R.id.layout_warrenty:
                startActivity(new Intent(getContext(), WarrantyClaimActivity.class));
                break;
        }
    }

    private void logoutUser() {
        AppSpace.sharedPref.writeValue(SESSION, "0");
        AppSpace.sharedPref.writeValue(CUSTOMER_ID, "0");
        AppSpace.sharedPref.writeValue(CUSTOMER_EMAIL, "0");
        AppSpace.sharedPref.writeValue(CUSTOMER_NAME, "0");
        startActivity(new Intent(getContext(), SignInActivity.class));
        getActivity().finish();
    }
}
