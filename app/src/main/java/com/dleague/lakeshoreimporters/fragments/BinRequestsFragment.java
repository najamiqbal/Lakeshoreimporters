package com.dleague.lakeshoreimporters.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.BinRequestForm;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BinRequestsFragment extends Fragment {

    private View rootView;
    private String hostLocation;
    private int code;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bin_requests, container,false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.iv_bin_req_1)void setReq_1(){
        hostLocation = "Crook, Lindsey - Strathmore, AB";
        code = 1;
        launchFormActivity();
    }

    @OnClick(R.id.iv_bin_req_2)void setReq_2(){
        hostLocation = "Mairs, Victoria - Langdon, AB";
        code = 2;
        launchFormActivity();
    }

    @OnClick(R.id.iv_bin_req_3)void setReq_3(){
        hostLocation = "Lazurko, Kyle - Chestermere, AB";
        code = 3;
        launchFormActivity();
    }

    @OnClick(R.id.iv_bin_req_4)void setReq_4(){
        hostLocation = "Peterson, Phyllis - Calgary NE, AB";
        code = 4;
        launchFormActivity();
    }

    @OnClick(R.id.iv_bin_req_5)void setReq_5(){
        hostLocation = "Marth, April - South Calgary, AB";
        code = 5;
        launchFormActivity();
    }

    private void init() {
    }

    private void launchFormActivity(){
        Intent intent = new Intent(getActivity(), BinRequestForm.class);
        intent.putExtra("locationStr", hostLocation);
        intent.putExtra("code", code);
        startActivity(intent);
    }
}
