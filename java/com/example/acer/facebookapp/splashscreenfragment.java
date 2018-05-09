package com.example.acer.facebookapp;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class splashscreenfragment extends Fragment {


    public splashscreenfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splashscreenfragment, container, false);
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (!splash_activity.preferences.getString("username", "").equals("")) {
                    Main_screen login_screen = new Main_screen();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.content_splash, login_screen);
                    transaction.commit();
                } else {
                    LoginAndSignup_screen LoginAndSignup_screen = new LoginAndSignup_screen();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.content_splash, LoginAndSignup_screen);
                    transaction.commit();
                }
            }
        }.start();
        return view;
    }

}
