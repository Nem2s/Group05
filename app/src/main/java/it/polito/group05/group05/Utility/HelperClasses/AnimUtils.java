package it.polito.group05.group05.Utility.HelperClasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.R;

/**
 * Created by Marco on 16/05/2017.
 */

public class AnimUtils {

    public static void startActivityWithAnimation (Activity from,Intent intent, Pair<View, String>... pairs) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(from, pairs);
        ActivityCompat.startActivity(from, intent, options.toBundle());
    }

    public static void startActivityForResultWithAnimation (Activity from,Intent intent, int RequestCode, Pair<View, String>... pairs) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(from, pairs);
        ActivityCompat.startActivityForResult(from, intent, RequestCode,  options.toBundle());
    }

    public static void toggleOffView(final View view, Context context) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.start();
    }

    public static void enterRevealAnimation(View view) {
        // previously invisible view
        final View myView = view;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);

        anim.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void exitRevealAndFinish(View view, final AppCompatActivity activity) {
        // previously visible view
        final View myView = view;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                android.view.ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.GONE);
                // Finish the activity after the exit transition completes.
                activity.supportFinishAfterTransition();
            }
        });
        // start the animation
        anim.start();
    }
    public static void exitReveal(View view) {
        // previously visible view
        final View myView = view;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

}
