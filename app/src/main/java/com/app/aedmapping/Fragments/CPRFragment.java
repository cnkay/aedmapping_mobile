package com.app.aedmapping.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.app.aedmapping.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CPRFragment extends Fragment {
    boolean isHasDefib;
    @BindView(R.id.buttonYes)
    Button buttonYes;
    @BindView(R.id.buttonNo)
    Button buttonNo;
    @BindView(R.id.textHaveYouGot)
    TextView textHave;
    @BindView(R.id.nextButton)
    Button nextButton;
    @BindView(R.id.cprImageView)
    ImageView cprImageView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.playVideoButton)
    ImageButton playVideoButton;
    @BindView(R.id.videoView)
    VideoView videoView;
    ArrayList<Integer> images = new ArrayList<>();
    int index = -1;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_cpr, container,
                false);
        // mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        context = getActivity();
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.buttonYes)
    void yes() {
        isHasDefib = true;
        startShow();
    }

    @OnClick(R.id.buttonNo)
    void no() {
        isHasDefib = false;
        startShow();
    }

    @OnClick(R.id.playVideoButton)
    void playVideo() {
        Uri uri = Uri.parse("");
        if (index == 1)
            uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.call911);
        if (index == 3)
            uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.handposition);
        if (index == 4)
            uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.push);
        if (index == 6)
            uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.aed);
        videoView.setVideoURI(uri);
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
    }


    @OnClick(R.id.nextButton)
    void nextImage() {
        index++;
        if(index==0)
            constraintLayout.setBackgroundResource(R.color.colorCPRBackground);
        if (index == images.size()) {
            Toast.makeText(context, "DONE!", Toast.LENGTH_LONG).show();
            index = -1;
        } else {
            if (index > 0 && index < 7 && index != 2 && index!=5) {
                playVideoButton.setVisibility(View.VISIBLE);
                if(index==6){
                    constraintLayout.setBackgroundResource(R.color.colorWhite);
                }
                else {
                    constraintLayout.setBackgroundResource(R.color.colorCPRBackground);
                }
            } else {
                videoView.setVisibility(View.INVISIBLE);
                playVideoButton.setVisibility(View.INVISIBLE);
            }
            cprImageView.setImageResource(images.get(index));

        }
    }

    private void startShow() {
        invisibleComponents();
        if (isHasDefib) {
            images.add(R.drawable.cpr_first);
            images.add(R.drawable.cpr_second);
            images.add(R.drawable.cpr_third);
            images.add(R.drawable.cpr_fourth);
            images.add(R.drawable.cpr_fifth);
            images.add(R.drawable.cpr_sixth);
            images.add(R.drawable.aed);
        } else {
            images.add(R.drawable.cpr_first);
            images.add(R.drawable.cpr_second);
            images.add(R.drawable.cpr_third);
            images.add(R.drawable.cpr_fourth);
            images.add(R.drawable.cpr_fifth);
            images.add(R.drawable.cpr_sixth);
        }
        cprImageView.setImageResource(images.get(0));
    }

    private void invisibleComponents() {
        linearLayout.setVisibility(View.GONE);
        textHave.setVisibility(View.GONE);
        cprImageView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        constraintLayout.setBackgroundResource(R.color.colorCPRBackground);
    }
}
