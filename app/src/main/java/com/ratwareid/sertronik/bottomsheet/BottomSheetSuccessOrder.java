package com.ratwareid.sertronik.bottomsheet;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetSuccessOrder extends BottomSheetDialogFragment implements View.OnClickListener {

    private Button buttonOk;

    public BottomSheetSuccessOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bottom_sheet_success_order, container, false);

        this.initWidgets(v);

        return v;
    }

    private void initWidgets(View view) {
        buttonOk = view.findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonOk)){
            this.dismiss();
            this.startActivity(new Intent(getActivity(), HomeActivity.class));
        }
    }
}
