package it.polito.group05.group05;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.klinker.android.sliding.SlidingActivity;
import com.mvc.imagepicker.ImagePicker;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

public class SettingActivity extends SlidingActivity {

    CircleImageView cv;
    TextInputEditText phone;
    TextInputEditText name;

    private static final int COMING_FROM_BALANCE_ACTIVITY = 123;
    private static int CUSTOM_THEME_OPTION = 0;
    private static int PREDEFINED_THEME_OPTION = 0;
    public static int REQUEST_FROM_NEW_USER;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");

        if (bitmap != null && REQUEST_FROM_NEW_USER == requestCode) {
            cv.setImageBitmap(bitmap);
            //currentUser.setProfile_image(bitmap);
            //DB_Manager.getInstance().photoMemoryUpload(1, currentUser.getId(), bitmap);
            String uuid = UUID.randomUUID().toString();
            Singleton.getInstance().getCurrentUser().setiProfile(uuid);
            DB_Manager.getInstance().setContext(this)
                    .imageProfileUpload(1, Singleton.getInstance().getCurrentUser().getId(), uuid, bitmap);
            FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userInfo").child("iProfile").setValue(uuid);
            REQUEST_FROM_NEW_USER = -1;
        }
    }

    @Override
    public void init(Bundle bundle) {

        this.enableFullscreen();
        setContent(R.layout.activity_setting);
        cv = (CircleImageView) findViewById(R.id.profile_image);
        phone = (TextInputEditText) findViewById(R.id.name);
        name = (TextInputEditText) findViewById(R.id.number);
        phone.setText(Singleton.getInstance().getCurrentUser().getTelNumber());
        phone.setEnabled(false);
        name.setEnabled(false);
        final SlidingActivity o = this;
        try {
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference("users")
                            .child(Singleton.getInstance().getCurrentUser().getId())
                            .child(Singleton.getInstance().getCurrentUser().getiProfile()))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            o.setImage(resource);
                        }
                    })

            ;
        } catch (Exception e) {


        }
        name.setText(Singleton.getInstance().getCurrentUser().getName());
        final Activity c = this;
        setFab(R.color.accent, R.drawable.ic_mode_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFab(R.color.colorAccent, R.drawable.ic_action_tick_white, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (phone.getText().length() == 0 || name.getText().length() == 0) {
                            Toast.makeText(c, "Invalid Text", Toast.LENGTH_LONG);
                            return;
                        }
                        String s = phone.getText().toString();
                        s = s.trim();
                        if (s.length() > 10 || s.length() < 9) {
                            Toast.makeText(c, "Invalid Number", Toast.LENGTH_LONG);
                            return;
                        }

                        FirebaseDatabase.getInstance().getReference("usersNumber").child(Singleton.getInstance().getCurrentUser().getTelNumber()).removeValue();
                        if (!s.equals(Singleton.getInstance().getCurrentUser().getName())) {
                            Singleton.getInstance().getCurrentUser().setTelNumber(s);
                            FirebaseDatabase.getInstance().getReference("usersNumber").child(Singleton.getInstance().getCurrentUser().getTelNumber()).removeValue();
                            FirebaseDatabase.getInstance().getReference("usersNumber").child(Singleton.getInstance().getCurrentUser().getTelNumber()).setValue(Singleton.getInstance().getCurrentUser().getId());
                            FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userInfo").child("telNumber").setValue(Singleton.getInstance().getCurrentUser().getTelNumber());

                        }
                        if (!name.getText().toString().equals(Singleton.getInstance().getCurrentUser().getName())) {

                            Singleton.getInstance().getCurrentUser().setName(name.getText().toString());
                            FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userInfo").child("name").setValue(Singleton.getInstance().getCurrentUser().getName());
                        }
                        finish();


                    }
                });
                phone.setEnabled(true);
                name.setEnabled(true);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePicker.pickImage(c, "Select Image:");
                        REQUEST_FROM_NEW_USER = ImagePicker.PICK_IMAGE_REQUEST_CODE;
                    }
                });


            }
        });


    }
}
