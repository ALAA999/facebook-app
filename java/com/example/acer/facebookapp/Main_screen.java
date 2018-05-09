package com.example.acer.facebookapp;


import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_screen extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;


    public Main_screen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        splash_activity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        splash_activity.fab.setVisibility(View.VISIBLE);
        splash_activity.fab.setEnabled(true);
        upload_messeges();
        return view;
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_chrome_reader_mode_black_24dp,
                R.drawable.ic_search_black_24dp,
                R.drawable.ic_notifications_black_24dp
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new News_Fragment());
        adapter.addFrag(new Search_Fragment());
        adapter.addFrag(new Notifications_Fragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    public void upload_messeges() {
        final ArrayList<Messeges> All_Messeges;
        All_Messeges = new ArrayList<>();
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Messeges");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                All_Messeges.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Messeges p = dataSnapshot1.getValue(Messeges.class);
                    if (p.getReciver_id().equals(splash_activity.current_user.getId())) {
                        All_Messeges.add(p);
                    }
                }
                Collections.reverse(All_Messeges);//we will show the last msg recived and check if it was seen or not
                if (All_Messeges.size() != 0 && !All_Messeges.get(0).read) {
                    try {
                        Log.e("msg", All_Messeges.get(0).getMessege() + "");
                        MediaPlayer.create(getActivity(), R.raw.msg_sound).start();
                        splash_activity.fab.setImageBitmap(splash_activity.textAsBitmap("1", 40, Color.WHITE));
                        splash_activity.fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!All_Messeges.get(0).read) {
                                    Snackbar.make(view, "You have unread Messege", Snackbar.LENGTH_LONG)
                                            .setAction("Show Messege", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    splash_activity.fab.setImageResource(android.R.drawable.ic_dialog_email);
                                                    All_Messeges.get(0).setRead(true);
                                                    UploadingData.update_msg(All_Messeges.get(0));
                                                    Intent i = new Intent(getActivity(), SendsMsg_activity.class);
                                                    i.putExtra("id", All_Messeges.get(0).getSender_id());
                                                    startActivity(i);
                                                }
                                            }).show();
                                } else {
                                    Log.e("asd", "asd");
                                    splash_activity.fab.setImageResource(android.R.drawable.ic_dialog_email);
                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}