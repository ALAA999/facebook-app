package com.example.acer.facebookapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class splash_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static FloatingActionButton fab;
    public static DrawerLayout drawer;
    public static ArrayList<Users> All_Users;
    public static DatabaseReference databaseReference_users;
    public static Users current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);

        preferences = getSharedPreferences("Data", MODE_PRIVATE);
        editor = preferences.edit();
        All_Users = new ArrayList<>();
        databaseReference_users = FirebaseDatabase.getInstance().getReference("Users");
        upload_Allusers();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        splashscreenfragment fragment = new splashscreenfragment();
        FragmentUtil.addFragment(splash_activity.this, fragment, R.id.content_splash);
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void upload_Allusers() {
        databaseReference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                All_Users.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users p = dataSnapshot1.getValue(Users.class);
                    All_Users.add(p);
                }
                setNavDate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void setNavDate() {
        for (int i = 0; i < All_Users.size(); i++) {
            if (splash_activity.preferences.getString("username_id", "").equals(All_Users.get(i).getId())) {
                Glide.with(this).load(All_Users.get(i).getImg_id()).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ImageView) findViewById(R.id.username_navimg)));
                ((TextView) findViewById(R.id.username_nav)).setText(All_Users.get(i).getName());
                current_user = All_Users.get(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.write_post) {
            writepost();
        } else if (id == R.id.change_img) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 18);
        } else if (id == R.id.log_out) {
            editor.clear();
            editor.commit();
            finish();
            startActivity(new Intent(splash_activity.this, splash_activity.class));
        } else if (id == R.id.change_name) {
            change_name();
        } else if (id == R.id.change_pass) {
            change_pass();

        } else if (id == R.id.showmy_page) {
            Intent i = new Intent(getApplication(), Show_User_page.class);
            i.putExtra("id", splash_activity.preferences.getString("username_id", ""));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void writepost() {
        final Dialog dilog = new Dialog(this);
        dilog.setContentView(R.layout.write_post);
        dilog.setCancelable(true);
        dilog.setTitle("Write A Post.");
        dilog.show();
        (dilog.findViewById(R.id.img_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 17);
            }
        });
        (dilog.findViewById(R.id.publihs)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post_msg = ((EditText) dilog.findViewById(R.id.posttxt)).getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                if (post_msg.isEmpty() && UploadingData.uri == null) {
                    Toast.makeText(splash_activity.this, "Write Something.", Toast.LENGTH_SHORT).show();
                } else {
                    UploadingData.AddPost(dilog, post_msg, UploadingData.uri, currentTime);
                }
            }
        });
    }

    public void change_name() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(splash_activity.this);
        alertDialog.setTitle("CHANGE NAME");
        alertDialog.setIcon(R.drawable.ic_text_fields_black_24dp);
        final EditText input = new EditText(this);
        input.setHint("Enter New Mane");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < News_Fragment.post_list.size(); ++j) {
                    Log.e("c1", current_user.getName());
                    Log.e("c2", News_Fragment.post_list.get(j).getPost_ownername());
                    if (current_user.getName().equals(News_Fragment.post_list.get(j).getPost_ownername())) {
                        News_Fragment.post_list.get(j).setPost_ownername(input.getText().toString());
                        UploadingData.update_post(News_Fragment.post_list.get(j));
                    }
                }
                current_user.setName(input.getText().toString());
                editor.putString("username", input.getText().toString());
                editor.commit();
                UploadingData.update_user(current_user);
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void change_pass() {
        final Dialog alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.change_pass);
        alertDialog.setTitle("CHANGE PASSWORD");
        final EditText old = alertDialog.findViewById(R.id.old_Passowrd);
        final EditText new_pass = alertDialog.findViewById(R.id.new_Password);
        final EditText conform_pass = alertDialog.findViewById(R.id.confom_Password);
        final Button ok = alertDialog.findViewById(R.id.ok);
        final Button no = alertDialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_Pass = new_pass.getText().toString();
                String conform_Pass = conform_pass.getText().toString();
                String Old = old.getText().toString();
                if (new_Pass.equals("") || conform_Pass.equals("") || Old.equals("")) {
                    Toast.makeText(splash_activity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                } else {
                    if (old.getText().toString().equals(current_user.getPassword())) {
                        if (new_Pass.equals(conform_Pass)) {
                            if (new_Pass.length() >= 5) {
                                current_user.setPassword(new_Pass);
                                UploadingData.update_user(current_user);
                                alertDialog.dismiss();
                                Toast.makeText(splash_activity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(splash_activity.this, "Lenght must be at least 5", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(splash_activity.this, "No Match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(splash_activity.this, "Old Password isn't Correct", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 17) {
            UploadingData.uri = data.getData();
            Toast.makeText(this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 18) {
            UploadingData.uri = data.getData();
            Toast.makeText(this, "تم تغيير الصورة", Toast.LENGTH_SHORT).show();
            current_user.setImg_id(UploadingData.uri.toString());
            UploadingData.update_user(current_user);
        }
    }
}
