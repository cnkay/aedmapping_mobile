package com.app.aedmapping.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.aedmapping.Constant;
import com.app.aedmapping.R;
import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.Defibrillator.CreateDefibrillatorRequest;
import com.app.aedmapping.Retrofit.User.CreateUserResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDefibFragment extends Fragment {

    @BindView(R.id.txtAddressAdd)
    EditText textAddress;
    @BindView(R.id.txtCityAdd)
    EditText textCity;
    @BindView(R.id.txtCountryAdd)
    EditText textCountry;
    @BindView(R.id.txtDescriptionAdd)
    EditText textDescription;
    @BindView(R.id.txtNameAdd)
    EditText textName;
    @BindView(R.id.txtLatitudeAdd)
    TextView textLatitude;
    @BindView(R.id.txtLongitudeAdd)
    TextView textLongitude;

    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private APIManager apiManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_adddefib, container,
                false);
        // mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        context = getActivity();
        ButterKnife.bind(this, v);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        configureComponenets();
        return v;
    }

    private void configureComponenets() {
        if (!(pref.getString("address", "ERR").equals("ERR")) && !(pref.getString("address","ERR").equals(""))) {
            textAddress.setText(pref.getString("address",null));
        }
        if (!(pref.getString("city", "ERR").equals("ERR")) && !(pref.getString("city","ERR").equals(""))) {
            textCity.setText(pref.getString("city",null));
        }
        if (!(pref.getString("country", "ERR").equals("ERR")) && !(pref.getString("country","ERR").equals(""))) {
            textCountry.setText(pref.getString("country",null));
        }
        if (!(pref.getString("latitude", "ERR").equals("ERR")) && !(pref.getString("latitude","ERR").equals(""))) {
            textLatitude.setText(pref.getString("latitude",null));
        }
        if (!(pref.getString("longitude", "ERR").equals("ERR")) && !(pref.getString("longitude","ERR").equals(""))) {
            textLongitude.setText(pref.getString("longitude",null));
        }
    }
    @OnClick(R.id.buttonSaveDefibrillator)
    void onSave(){
        boolean error=false;
        EditText[] texts = {textCountry,textCity,textAddress,textName,textDescription};

        for(EditText text:texts) {
            if(text.getText()==null) {
                text.setError("This field can not be blank");
                error=true;
            }
            else if (text.getText().toString().trim().equalsIgnoreCase("")) {
                text.setError("This field can not be blank");
                error=true;
            }
        }
        if(!error) {
            apiManager=new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
            apiManager.getServices().addDefib(new CreateDefibrillatorRequest(textName.getText().toString(),
                    textDescription.getText().toString(),textAddress.getText().toString(),
                    textCity.getText().toString(),textCountry.getText().toString(),
                    Double.parseDouble(textLatitude.getText().toString()),Double.parseDouble(textLongitude.getText().toString())))
                    .enqueue(new Callback<CreateUserResponse>() {
                @Override
                public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                    if(response.body().getCode()==200) {
                        Toast.makeText(context, "Defibrillator saved!", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new MapFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm
                                .beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fragment_container, fragment)
                                .commit();
                    }
                    else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CreateUserResponse> call, Throwable t) {

                }
            });
        }

    }


}
