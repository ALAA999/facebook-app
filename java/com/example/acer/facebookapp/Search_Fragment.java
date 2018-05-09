package com.example.acer.facebookapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search_Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Users> user_searched;
    Search_RecyclerAdapter search_recyclerAdapter;
    View view;

    public Search_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_, container, false);
        setadapter(splash_activity.All_Users);
        findAutoCompleteTextView();
        return view;
    }

    public void findAutoCompleteTextView() {
        ((AutoCompleteTextView) view.findViewById(R.id.search)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                user_searched = new ArrayList<>();
                if (charSequence.length() == 0) {
                    setadapter(splash_activity.All_Users);
                } else {
                    for (int j = 0; j < splash_activity.All_Users.size(); j++) {
                        if (splash_activity.All_Users.get(j).getName().contains(charSequence)) {
                            user_searched.add(splash_activity.All_Users.get(j));
                        }
                    }//once you just clear all field and it dosen't show anu user this means that you need onstart methode
                    setadapter(user_searched);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setadapter(ArrayList<Users> users) {
        recyclerView = view.findViewById(R.id.search_rv);
        RecyclerView.LayoutManager mlayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mlayout);
        search_recyclerAdapter = new Search_RecyclerAdapter(users, getActivity());
        recyclerView.setAdapter(search_recyclerAdapter);
    }
}
