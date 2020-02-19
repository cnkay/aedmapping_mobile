package com.app.aedmapping.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.aedmapping.Constant;
import com.app.aedmapping.MainActivity;
import com.app.aedmapping.R;
import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.Defibrillator.Defibrillator;
import com.app.aedmapping.Retrofit.Geocoder.AddressComponent;
import com.app.aedmapping.Retrofit.Geocoder.CreateGeocoderResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerDragListener {
    private static final String key = "AIzaSyB9tUX2w31Qnx7v70LBEE1XhytEapBwZX4";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private APIManager apiManager;
    MainActivity mainActivity;
    List<Defibrillator> defibrillators = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private boolean mPermissionDenied = false;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        context = getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        mapConfiguration();
        // mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (pref.getString("floatClicked", null) != null) {
            editor.remove("floatClicked");
            editor.commit();
            mMap = googleMap;
            createDraggableMarker();
            mMap.setOnMarkerDragListener(this);
        } else {
            findAllDefibs();
            mMap = googleMap;

            enableMyLocation();
        }
    /*    for (Defibrillator def:
            defibrillators) {
            Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(def.getLatitude()),Double.parseDouble(def.getLongitude()))).title(def.getName()));
            marker.showInfoWindow();
        }*/
    }

    private void createDraggableMarker() {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble("39.812474"), Double.parseDouble("21.084146")))
                .draggable(true));
        Toast.makeText(getActivity(), "Long click marker and drag/drop", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        String latitude = marker.getPosition().latitude + "";
        String longitude = marker.getPosition().longitude + "";
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.commit();
        apiManager = new APIManager("https://maps.googleapis.com/maps/api/geocode/");
        apiManager.getServices().getAddress(latitude + "," + longitude, key).enqueue(new Callback<CreateGeocoderResponse>() {
            @Override
            public void onResponse(Call<CreateGeocoderResponse> call, Response<CreateGeocoderResponse> response) {
                if (response.body().getResults().size() > 0) {
                    List<AddressComponent> list = response.body().getResults().get(0).getAddressComponents();
                    for (AddressComponent component : list) {
                        if (component.getTypes().get(0).equals("administrative_area_level_1")) {
                            editor.putString("state", component.getLongName());
                        }
                        if (component.getTypes().get(0).equals("country")) {
                            editor.putString("country", component.getLongName());
                        }
                        if (component.getTypes().get(0).equals("locality")) {
                            editor.putString("city", component.getLongName());
                        }
                        if (component.getTypes().get(0).equals("route")) {
                            editor.putString("address", component.getLongName());
                        }
                    }
                    editor.commit();
                    if (pref.getString("state", null) == null && pref.getString("city", null) == null) {
                        for (AddressComponent component : list) {
                            if (component.getTypes().get(0).equals("administrative_area_level_5") || component.getTypes().get(0).equals("administrative_area_level_3")) {
                                editor.putString("city", component.getLongName());
                            }
                        }
                    }
                    editor.commit();
                    Fragment fragment=new AddDefibFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<CreateGeocoderResponse> call, Throwable t) {

            }
        });
    }

    private void findAllDefibs() {
        apiManager = new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
        apiManager.getServices().findAllDefibrillators().enqueue(new Callback<List<Defibrillator>>() {
            @Override
            public void onResponse(Call<List<Defibrillator>> call, Response<List<Defibrillator>> response) {
                if (response.body() != null) {
                    if (response.body().size() == 0) {
                    }
                    else {
                        defibrillators.addAll(response.body());
                        addMap(defibrillators);
                    }

                } else {
                    Toast.makeText(context, "findAllDefibs returned null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Defibrillator>> call, Throwable t) {

            }
        });
    }

    private void addMap(List<Defibrillator> defs) {
        for (Defibrillator def :
                defs) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(def.getLatitude()), Double.parseDouble(def.getLongitude()))).title(def.getName()));
            marker.showInfoWindow();
        }
        mMap.setOnMarkerClickListener(this);
    }

    private void mapConfiguration() {
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((MainActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Double latitude = marker.getPosition().latitude;
        Double longitude = marker.getPosition().longitude;
        Defibrillator selected;
        for (Defibrillator defib :
                defibrillators) {
            if (Double.parseDouble(defib.getLongitude()) == longitude && Double.parseDouble(defib.getLatitude()) == latitude) {
                selected = defib;
                String id= selected.getId();
                String name = selected.getName();
                String description = selected.getDescription();
                String address = selected.getAddress();
                String city = selected.getCity();
                String country = selected.getCountry();
                String latitudeStr = selected.getLatitude();
                String longitudeStr = selected.getLongitude();
                editor = pref.edit();
                editor.putString("selectedId",id);
                editor.putString("selectedName", name);
                editor.putString("selectedDescription", description);
                editor.putString("selectedAddress", address);
                editor.putString("selectedCity", city);
                editor.putString("selectedCountry", country);
                editor.putString("selectedLatitude", latitudeStr);
                editor.putString("selectedLongitude", longitudeStr);
                editor.commit();
                Fragment fragment = new DefibDetailsFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }

        return false;
    }
}
