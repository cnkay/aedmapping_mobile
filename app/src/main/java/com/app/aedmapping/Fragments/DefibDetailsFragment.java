package com.app.aedmapping.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.aedmapping.Fonts;
import com.app.aedmapping.R;
import com.app.aedmapping.Retrofit.APIManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DefibDetailsFragment extends Fragment {

    @BindView(R.id.txtName)
    TextView textName;
    @BindView(R.id.txtDescription)
    TextView textDescription;
    @BindView(R.id.txtAddress)
    TextView textAddress;
    @BindView(R.id.txtCity)
    TextView textCity;
    @BindView(R.id.txtCountry)
    TextView textCountry;
    @BindView(R.id.txtLatitude)
    TextView textLatitude;
    @BindView(R.id.txtLongitude)
    TextView textLongitude;
    @OnClick(R.id.reportButton)
    void reportButton() {
        Fragment fragment = new ReportFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    @BindView(R.id.txt1)
    TextView txt1;
    @BindView(R.id.txt2)
    TextView txt2;
    @BindView(R.id.txt3)
    TextView txt3;
    @BindView(R.id.txt4)
    TextView txt4;
    @BindView(R.id.txt5)
    TextView txt5;
    @BindView(R.id.txt6)
    TextView txt6;
    @BindView(R.id.txt7)
    TextView txt7;

    private APIManager apiManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_defib_details, container,
                false);
        // mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        context = getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        ButterKnife.bind(this, v);
        fillTexts();
        setFonts();
        return v;
    }
    private void setFonts() {
        Fonts fonts = new Fonts(getActivity());

        textName.setTypeface(fonts.getMMedium());
        textDescription.setTypeface(fonts.getMMedium());
        textCountry.setTypeface(fonts.getMMedium());
        textAddress.setTypeface(fonts.getMMedium());
        textCity.setTypeface(fonts.getMMedium());
        textLongitude.setTypeface(fonts.getMMedium());
        textLatitude.setTypeface(fonts.getMMedium());

        txt1.setTypeface(fonts.getMRegular());
        txt2.setTypeface(fonts.getMRegular());
        txt3.setTypeface(fonts.getMRegular());
        txt4.setTypeface(fonts.getMRegular());
        txt5.setTypeface(fonts.getMRegular());
        txt6.setTypeface(fonts.getMRegular());
        txt7.setTypeface(fonts.getMRegular());

    }
    private void fillTexts() {
        if(pref.getString("selectedName",null)!=null) {
            textName.setText(pref.getString("selectedName",null));
        }
        if(pref.getString("selectedAddress",null)!=null) {
            textAddress.setText(pref.getString("selectedAddress",null));
        }
        if(pref.getString("selectedCity",null)!=null) {
            textCity.setText(pref.getString("selectedCity",null));
        }
        if(pref.getString("selectedCountry",null)!=null) {
            textCountry.setText(pref.getString("selectedCountry",null));
        }
        if(pref.getString("selectedDescription",null)!=null) {
            textDescription.setText(pref.getString("selectedDescription",null));
        }
        if(pref.getString("selectedLatitude",null)!=null) {
            textLatitude.setText(pref.getString("selectedLatitude",null));
        }
        if(pref.getString("selectedLongitude",null)!=null) {
            textLongitude.setText(pref.getString("selectedLongitude",null));
        }

    }

}
