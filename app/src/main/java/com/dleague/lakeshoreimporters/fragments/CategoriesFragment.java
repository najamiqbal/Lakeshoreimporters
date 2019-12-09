package com.dleague.lakeshoreimporters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.MainActivity;

import java.util.Objects;

public class CategoriesFragment extends Fragment implements View.OnClickListener{
    View view;
    RelativeLayout btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.categories_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        btn1=view.findViewById(R.id.btn1);
        btn2=view.findViewById(R.id.btn2);
        btn3=view.findViewById(R.id.btn3);
        btn4=view.findViewById(R.id.btn4);
        btn5=view.findViewById(R.id.btn5);
        btn6=view.findViewById(R.id.btn6);
        btn7=view.findViewById(R.id.btn7);
        btn8=view.findViewById(R.id.btn8);
        btn9=view.findViewById(R.id.btn9);
        btn10=view.findViewById(R.id.btn10);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        //btn1.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(1);
                break;
            case R.id.btn2:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(3);
                break;
            case R.id.btn3:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(20);
                break;
            case R.id.btn4:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(21);
                break;
            case R.id.btn5:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(22);
                break;
            case R.id.btn6:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(23);
                break;
            case R.id.btn7:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(24);
                break;
            case R.id.btn8:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(25);
                break;
            case R.id.btn9:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(26);
                break;
            case R.id.btn10:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(27);
                break;
        }
    }
}
