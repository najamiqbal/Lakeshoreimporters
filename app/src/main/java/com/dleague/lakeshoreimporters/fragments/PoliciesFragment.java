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

import com.dleague.lakeshoreimporters.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoliciesFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_policies,container,false);
        ButterKnife.bind(this, rootView);
        init();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_privacy_policies)void setBtn1(){
        String url = "https://cdn.shopify.com/s/files/1/1789/3041/files/Privacy.pdf?11805586885961940909";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.btn_return_n_refund_policies)void setBtn2(){
        String url = "https://cdn.shopify.com/s/files/1/1789/3041/files/July_28_2018.pdf?15204325667735187003";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.btn_libility_policies)void setBtn3(){
        String url = "https://cdn.shopify.com/s/files/1/1789/3041/files/Liability.pdf?2499277594556807824";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void init() {
    }
}
