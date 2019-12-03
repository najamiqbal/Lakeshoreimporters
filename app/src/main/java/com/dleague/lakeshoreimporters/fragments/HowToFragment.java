package com.dleague.lakeshoreimporters.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.adapters.PlayerVideoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HowToFragment extends Fragment {

    @BindView(R.id.recyclerview_how_to)
    RecyclerView recyclerView;
    private View rootView;
    private final String API_KEY = "AIzaSyC4R4btZO76h6mQRHJK2XWP09BI-cBDIEs";
    private String video1 = "KeUXSnEy6iI";
    private String video2 = "hdQMk3dri2Q";
    private String video3 = "VJ-JYDDxzPY";
    private String video4 = "pX8Y2bK5vKA";
    private String video5 = "zdBvL_XyAcc";
    private String video6 = "yICf6knP5EY";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_how_to,container,false);
        ButterKnife.bind(this, rootView);
        init();
        recyclerView.setHasFixedSize(true);
        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        PlayerVideoAdapter adapter=new PlayerVideoAdapter(getContext());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
    }
}
