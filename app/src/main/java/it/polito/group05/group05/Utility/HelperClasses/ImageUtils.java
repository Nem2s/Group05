package it.polito.group05.group05.Utility.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Marco on 11/05/2017.
 */

public class ImageUtils {

    public static void LoadUserImageProfile(CircleImageView cv, Context context, UserDatabase user) {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users")
                        .child(user.getId())
                        .child(user.getiProfile()))
                .asBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(cv);
    }

    public static void LoadMyImageProfile(CircleImageView cv, Context context) {
        UserDatabase user = Singleton.getInstance().getCurrentUser();
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users")
                        .child(user.getId())
                        .child(user.getiProfile()))
                .asBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(cv);
    }

    public static void LoadMyImageProfile(ImageView iv, Context context) {
        UserDatabase user = Singleton.getInstance().getCurrentUser();
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users")
                        .child(user.getId())
                        .child(user.getiProfile()))
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(iv);
    }

    public static void LoadImageGroup(CircleImageView cv, Context context, GroupDatabase currGroup) {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("groups").child(currGroup.getId()).child(currGroup.getPictureUrl()))
                .fitCenter()
                .into(cv);
    }

    public static void LoadImageGroup(ImageView cv, Context context, GroupDatabase currGroup) {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("groups").child(currGroup.getId()).child(currGroup.getPictureUrl()))
                .placeholder(R.drawable.network)
                .centerCrop()
                .into(cv);
    }


    public static List<Integer> getMatColor(String typeColor, Context context, int n)
    {
        List<Integer> result = new ArrayList<>();
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0)
        {
            for(int i = 0; i < n; i++) {
                TypedArray colors = context.getResources().obtainTypedArray(arrayId);
                int index = (int) (Math.random() * colors.length());
                returnColor = colors.getColor(index, Color.BLACK);
                result.add(returnColor);
                colors.recycle();
            }
        }
        return result;
    }
}
