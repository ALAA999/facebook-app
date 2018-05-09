package com.example.acer.facebookapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by acer on 2/3/2018.
 */

public class UploadingData {
    public static DatabaseReference databaseReference, databaseReferenceUpdate;
    public static StorageReference storageReference;
    public static ProgressDialog progressDialog;
    public static Uri uri = null;
    static ArrayList<Users> users;


//    public UploadingData(Context context) {
//    progressDialog = new ProgressDialog(context);
//     }

    public static void AddUser(final Dialog dialog, Uri uri, final String new_username, final String new_password, final FragmentActivity context) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        final String id = databaseReference.push().getKey();
        if (uri == null || new_username.isEmpty() || new_password.isEmpty()) {
            Toast.makeText(dialog.getContext(), "All Fields Required with image.", Toast.LENGTH_SHORT).show();
        } else {
            showprogressDialog(dialog, "Signing Up.");
            StorageReference filepath = storageReference.child("images/" + UUID.randomUUID().toString());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Users users = new Users(new_username, id, new_password, taskSnapshot.getDownloadUrl().toString(), null, null);
                    databaseReference.child(id).setValue(users);
                    UploadingData.uri = null;
                    if (((CheckBox) dialog.findViewById(R.id.keep_logedin)).isChecked()) {
                        splash_activity.editor.putString("username", new_username);
                        splash_activity.editor.putString("username_id", id);
                        splash_activity.editor.commit();
                    }
                    progressDialog.dismiss();
                    Toast.makeText(dialog.getContext(), "Great, Signed Up successfully!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(dialog.getContext(), "Scroll Left or right edges to show NavigationView.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(dialog.getContext(), "Sorry, There's a problem.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public static void AddPost(final Dialog dialog, final String post_msg, Uri uri, final Date date) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        final String post_id = databaseReference.push().getKey();
        final String usename_id = splash_activity.preferences.getString("username_id", null);
        final String usename = splash_activity.preferences.getString("username", null);
        String user_img = null;//You have to upload your name from current user and once you change your name change the prefrences
        storageReference = FirebaseStorage.getInstance().getReference();
        showprogressDialog(dialog, "Wrtiting Post.");
        final ArrayList<Comments> comments = new ArrayList<>();
        final ArrayList<String> users_liked = new ArrayList<>();
        user_img = splash_activity.current_user.getImg_id();
        if (uri == null) {
            Post post = new Post(usename_id, date, post_msg, users_liked, comments, post_id, "", usename, user_img, null, null);
            databaseReference.child(post_id).setValue(post);
            progressDialog.dismiss();
        } else {
            StorageReference filepath = storageReference.child("images/" + UUID.randomUUID().toString());
            final String finalUser_img = user_img;
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Post post = new Post(usename_id, date, post_msg, users_liked, comments, post_id,
                            taskSnapshot.getDownloadUrl().toString(), usename, finalUser_img, null, null);
                    databaseReference.child(post_id).setValue(post);
                    progressDialog.dismiss();
                }
            });
        }
        UploadingData.uri = null;
    }

    public static void update_post(Post new_post) {
        try {
            databaseReferenceUpdate = FirebaseDatabase.getInstance().
                    getReference("Post").child(new_post.getPostid() + "");

            Post std = new Post(new_post.getid_owner(), new_post.getTime()
                    , new_post.getMsg(), new_post.getUsers_liked(), new_post.getComments(), new_post.getPostid(),
                    new_post.getImgurl(), new_post.getPost_ownername(), new_post.getPost_owenerimg(), new_post.getNotification_like(), new_post.getNotification_comment());
            databaseReferenceUpdate.setValue(std);
        } catch (Exception e) {
        }
    }

    public static void delete_post(Post post) {
        databaseReferenceUpdate = FirebaseDatabase.getInstance()
                .getReference("Post").child(post.getPostid());
        databaseReferenceUpdate.removeValue();
        try {
            FirebaseStorage.getInstance().getReferenceFromUrl(post.getImgurl()).delete();
        } catch (Exception e) {

        }
    }

    public static void update_user(Users new_user) {
        try {
            databaseReferenceUpdate = FirebaseDatabase.getInstance().
                    getReference("Users").child(new_user.getId() + "");

            Users std = new Users(new_user.getName(), new_user.getId(), new_user.getPassword(), new_user.getImg_id()
                    , new_user.getFrom(), new_user.getTo());
            databaseReferenceUpdate.setValue(std);
        } catch (Exception e) {
        }
    }

    public static void update_msg(Messeges new_user) {
        try {
            databaseReferenceUpdate = FirebaseDatabase.getInstance().
                    getReference("Messeges").child(new_user.getMsg_id() + "");

            Messeges std = new Messeges(new_user.getTime(), new_user.getMessege(), new_user.getSender_id(),
                    new_user.getReciver_id(), new_user.getMsg_id(), new_user.isRead());
            databaseReferenceUpdate.setValue(std);
        } catch (Exception e) {
        }
    }

    public static void send_msg(String msg, String current_userId, String current_usercalledId, Date current_time) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Messeges");
        final String msg_id = databaseReference.push().getKey();
        Messeges messege = new Messeges(current_time, msg, current_userId, current_usercalledId, msg_id, false);
        databaseReference.child(msg_id).setValue(messege);
    }

    public static void showprogressDialog(Dialog dialog, String title) {
        progressDialog = new ProgressDialog(dialog.getContext());
        progressDialog.show();
        progressDialog.setTitle(title);
        progressDialog.setMessage("Please wait...");
        dialog.dismiss();
    }

    public static void update_Note(ArrayList<String> people, Post post) {
        Log.e("size", people.size() + "");
        Notification notification = new Notification(people);
        post.setNotification_like(notification);
        update_post(post);
    }
}
//    public static String getuserData() {
//        final String usename = splash_activity.preferences.getString("username_id", null);
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                users.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Users p = dataSnapshot1.getValue(Users.class);
//                    if (p.getName().equals(usename)) {
//                        users.add(p);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return "";
//    }
