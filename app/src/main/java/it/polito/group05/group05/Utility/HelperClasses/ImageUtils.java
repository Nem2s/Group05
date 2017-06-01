package it.polito.group05.group05.Utility.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.afollestad.aesthetic.Aesthetic;
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
        Glide.with(getApplicationContext())
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
                .centerCrop()
                .into(cv);
    }

    public static void LoadImageGroup(ImageView cv, Context context, GroupDatabase currGroup) {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("groups").child(currGroup.getId()).child(currGroup.getPictureUrl()))
                .centerCrop()
                .into(cv);
    }


    public static List<Integer> getMatColor(String typeColor, Context context, int n) {
        List<Integer> result = new ArrayList<>();
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            for (int i = 0; i < n; i++) {
                TypedArray colors = context.getResources().obtainTypedArray(arrayId);
                int index = (int) (Math.random() * colors.length());
                returnColor = colors.getColor(index, Color.BLACK);
                result.add(returnColor);
                colors.recycle();
            }
        }
        return result;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

    public static boolean isLightDarkActionBar() {
        return Aesthetic.get().isDark().take(1).blockingFirst() == isColorDark(Aesthetic.get().colorPrimary().take(1).blockingFirst());
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}