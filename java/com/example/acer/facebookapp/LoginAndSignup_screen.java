package com.example.acer.facebookapp;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrlplusz.anytextview.AnyEditTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginAndSignup_screen extends Fragment {
    boolean shown = false;
    View view;

    public LoginAndSignup_screen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login_screen, container, false);
        TextView textView2 = view.findViewById(R.id.welcome);
        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font3.otf");
        textView2.setTypeface(myTypeface);
        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View views) {
                EditText u = (view.findViewById(R.id.user_name));
                EditText p = (view.findViewById(R.id.password));
                String id = null;
                String username = u.getText().toString();
                String password = p.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "All Fields Required", Toast.LENGTH_SHORT).show();
                } else {
                    boolean found = false;
                    for (int i = 0; i < splash_activity.All_Users.size(); i++) {
                        if (username.equals(splash_activity.All_Users.get(i).getName())
                                && password.equals(splash_activity.All_Users.get(i).getPassword())) {
                            id = splash_activity.All_Users.get(i).getId();
                            found = true;
                        }
                    }
                    if (found) {
                        if (((CheckBox) view.findViewById(R.id.keep_logedin)).isChecked()) {
                            splash_activity.editor.putString("username", username);
                            splash_activity.editor.putString("username_id", id);
                            splash_activity.editor.commit();
                        }
                        startActivity(new Intent(getActivity(), splash_activity.class));
                        getActivity().finish();
                    } else {
                        u.setText("");
                        ((TextView) view.findViewById(R.id.wrong_data)).setText("Wrong Passwrod or Username");
                    }
                }
            }
        });

        final EditText pass = view.findViewById(R.id.password);
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (shown) {
                            pass.setTransformationMethod(new PasswordTransformationMethod());
                            shown = false;
                        } else {
                            pass.setTransformationMethod(null);
                            shown = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        TextView signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dilog = new Dialog(getActivity());
                dilog.setContentView(R.layout.signupdialog);
                dilog.setCancelable(true);
                dilog.setTitle("SignUp");
                dilog.show();
                ((AnyEditTextView) dilog.findViewById(R.id.new_password)).addTextChangedListener(new TextWatcher() {
                    TextView txt = dilog.findViewById(R.id.bad_pass);

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() >= 5) {
                            txt.setText("Great Password!");
                            txt.setTextColor(getResources().getColor(R.color.Green));
                        }
                        if (charSequence.length() >= 1 && charSequence.length() <= 4) {
                            txt.setText("Bad Password!");
                            txt.setTextColor(getResources().getColor(R.color.Red));
                        }
                        if (charSequence.length() > 8) {
                            txt.setText("Long Password!");
                            txt.setTextColor(getResources().getColor(R.color.Orange));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() < 1) {
                            txt.setText("");
                        }
                    }
                });
                dilog.findViewById(R.id.new_uploadimg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(intent, 17);
                    }
                });
                dilog.findViewById(R.id.new_signupbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final String new_username = ((EditText) dilog.findViewById(R.id.new_username)).getText().toString();
                            final String new_password = ((EditText) dilog.findViewById(R.id.new_password)).getText().toString();
                            final String coform_password = ((EditText) dilog.findViewById(R.id.new_passwordconform)).getText().toString();
                            if (UploadingData.uri == null || new_username.isEmpty() || new_password.isEmpty()) {
                                Toast.makeText(dilog.getContext(), "All Fields Required with image!", Toast.LENGTH_SHORT).show();
                            } else if (new_password.equals(coform_password)) {
                                UploadingData.AddUser(dilog, UploadingData.uri, new_username, new_password, getActivity());
                                startActivity(new Intent(getActivity(), splash_activity.class));
                                getActivity().finish();
                            } else {
                                ((TextView) dilog.findViewById(R.id.conform_text)).setText("Not Matched.");
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
        return view;
    }
}