package com.app.aedmapping.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.aedmapping.Constant;
import com.app.aedmapping.Fonts;
import com.app.aedmapping.R;
import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.Report.CreateReportRequest;
import com.app.aedmapping.Retrofit.Report.CreateReportResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment {
    @BindView(R.id.textComment)
    EditText comment;
    @BindView(R.id.txtComment)
    TextView txtComment;
    @BindView(R.id.radioGroup)
    RadioGroup group;
    @BindView(R.id.radioDuplicate)
    RadioButton duplicate;
    @BindView(R.id.radioMissing)
    RadioButton missing;
    @BindView(R.id.radioOther)
    RadioButton other;
    @BindView(R.id.radioOut)
    RadioButton outOfOrder;
    @BindView(R.id.txtMail)
    EditText mail;
    @BindView(R.id.textMail)
    TextView email;
    @BindView(R.id.buttonSaveReport)
    Button report;
    @BindView(R.id.txtReport)
    TextView txtReport;
    @BindView(R.id.txtReportType)
    TextView txtReportType;


    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Fonts fonts;
    private APIManager apiManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container,
                false);
        context = getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        ButterKnife.bind(this,v);
        fonts = new Fonts(getActivity());
        configureComponents();
        return v;
    }

    private void configureComponents() {
        comment.setTypeface(fonts.getMRegular());
        other.setTypeface(fonts.getMRegular());
        outOfOrder.setTypeface(fonts.getMRegular());
        missing.setTypeface(fonts.getMRegular());
        duplicate.setTypeface(fonts.getMRegular());
        report.setTypeface(fonts.getMMedium());
        mail.setTypeface(fonts.getMRegular());
        txtReport.setTypeface(fonts.getMRegular());
        txtReportType.setTypeface(fonts.getMRegular());
        txtComment.setTypeface(fonts.getMRegular());
        email.setTypeface(fonts.getMRegular());
    }

    @OnClick(R.id.buttonSaveReport)
    void save() {
        boolean error = false;
        EditText[] texts = {comment, mail};
        for (EditText text : texts) {
            if (text.getText() == null) {
                text.setError("This field can not be blank");
                error = true;
            } else if (text.getText().toString().trim().equalsIgnoreCase("")) {
                text.setError("This field can not be blank");
                error = true;
            }
        }
        if (!error) {
            String checkedText = "";
            switch (group.getCheckedRadioButtonId()) {
                case R.id.radioDuplicate:
                    checkedText = duplicate.getText().toString();
                    break;
                case R.id.radioMissing:
                    checkedText = missing.getText().toString();
                    break;
                case R.id.radioOut:
                    checkedText = outOfOrder.getText().toString();
                    break;
                case R.id.radioOther:
                    checkedText = other.getText().toString();
                    break;
            }
            CreateReportRequest request = new CreateReportRequest(checkedText, comment.getText().toString(), mail.getText().toString(), Integer.parseInt(pref.getString("selectedId", null)));
            apiManager = new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
            apiManager.getServices().addReport(request).enqueue(new Callback<CreateReportResponse>() {
                @Override
                public void onResponse(Call<CreateReportResponse> call, Response<CreateReportResponse> response) {
                    if (response != null) {
                        if (response.body().getCode() == 200) {
                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            Fragment fragment = new MapFragment();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm
                                    .beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.fragment_container, fragment)
                                    .commit();
                        }
                    } else {
                        Toast.makeText(context, "Network Err", Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<CreateReportResponse> call, Throwable t) {

                }
            });
        }

    }
}
