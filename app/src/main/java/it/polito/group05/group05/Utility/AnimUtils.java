package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import it.polito.group05.group05.R;

/**
 * Created by Marco on 30/03/2017.
 */

public class AnimUtils {

    public static void toggleOn(final FloatingActionButton fab, int duration, Context context){
        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        if(fab.getVisibility() == View.INVISIBLE) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setStartOffset(duration);

            fab.startAnimation(animation);
        }
    }
    public static void toggleOff(final FloatingActionButton fab, int duration, Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        animation.setDuration(duration);
        if(fab.getVisibility() == View.VISIBLE) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                        fab.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            fab.startAnimation(animation);

        }
    }
}
