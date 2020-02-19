package com.app.aedmapping.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.aedmapping.Constant;
import com.app.aedmapping.Fonts;
import com.app.aedmapping.R;
import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.User.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespondersFragment extends Fragment {
    Fonts fonts;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.textView2)
    TextView textView2;
    Context context;
    private APIManager apiManager;
    List<String> names=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_responders, container,
                false);
        context=getActivity();
        ButterKnife.bind(this,v);
        findAllResponders();
        return v;
    }
    private void setArrayAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,android.R.id.text1,names);
        listView.setAdapter(adapter);
    }
    private void findAllResponders(){
        apiManager=new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
        fonts=new Fonts(getActivity());
        textView2.setTypeface(fonts.getMMedium());
        apiManager.getServices().findAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.body()!=null) {
                    for (User user:
                         response.body()) {
                        names.add(user.getName());
                    }
                    setArrayAdapter();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
