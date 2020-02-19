package com.app.aedmapping.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.aedmapping.R;

import butterknife.ButterKnife;

public class EmergencyFragment extends Fragment {
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emergency, container,
                false);
        context = getActivity();
        ButterKnife.bind(this, v);

        return v;
    }
}
