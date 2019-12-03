package com.dleague.lakeshoreimporters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dleague.lakeshoreimporters.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsFragment extends Fragment {

    @BindView(R.id.tv_faq_a1)
    TextView tvA1;
    @BindView(R.id.tv_faq_a2)
    TextView tvA2;
    @BindView(R.id.tv_faq_a3)
    TextView tvA3;
    @BindView(R.id.tv_faq_a4)
    TextView tvA4;
    @BindView(R.id.tv_faq_a5)
    TextView tvA5;
    @BindView(R.id.tv_faq_a6)
    TextView tvA6;
    @BindView(R.id.tv_faq_a7)
    TextView tvA7;
    @BindView(R.id.tv_faq_a8)
    TextView tvA8;
    @BindView(R.id.tv_faq_a9)
    TextView tvA9;
    @BindView(R.id.tv_faq_a10)
    TextView tvA10;

    View rootView;
    boolean isA1Visible, isA2Visible, isA3Visible, isA4Visible, isA5Visible, isA6Visible, isA7Visible, isA8Visible, isA9Visible, isA10Visible;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_us,container,false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.tv_faq_q1)void setQ1(){
        if(isA1Visible){
            isA1Visible = false;
            tvA1.setVisibility(View.GONE);
        }else{
            isA1Visible = true;
            tvA1.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q2)void setQ2(){
        if(isA2Visible){
            isA2Visible = false;
            tvA2.setVisibility(View.GONE);
        }else{
            isA2Visible = true;
            tvA2.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q3)void setQ3(){
        if(isA3Visible){
            isA3Visible = false;
            tvA3.setVisibility(View.GONE);
        }else{
            isA3Visible = true;
            tvA3.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q4)void setQ4(){
        if(isA4Visible){
            isA4Visible = false;
            tvA4.setVisibility(View.GONE);
        }else{
            isA4Visible = true;
            tvA4.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q5)void setQ5(){
        if(isA5Visible){
            isA5Visible = false;
            tvA5.setVisibility(View.GONE);
        }else{
            isA5Visible = true;
            tvA5.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q6)void setQ6(){
        if(isA6Visible){
            isA6Visible = false;
            tvA6.setVisibility(View.GONE);
        }else{
            isA6Visible = true;
            tvA6.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q7)void setQ7(){
        if(isA7Visible){
            isA7Visible = false;
            tvA7.setVisibility(View.GONE);
        }else{
            isA7Visible = true;
            tvA7.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q8)void setQ8(){
        if(isA8Visible){
            isA8Visible = false;
            tvA8.setVisibility(View.GONE);
        }else{
            isA8Visible = true;
            tvA8.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q9)void setQ9(){
        if(isA9Visible){
            isA9Visible = false;
            tvA9.setVisibility(View.GONE);
        }else{
            isA9Visible = true;
            tvA9.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_faq_q10)void setQ10(){
        if(isA10Visible){
            isA10Visible = false;
            tvA10.setVisibility(View.GONE);
        }else{
            isA10Visible = true;
            tvA10.setVisibility(View.VISIBLE);
        }
    }

    private void init() {

    }
}
