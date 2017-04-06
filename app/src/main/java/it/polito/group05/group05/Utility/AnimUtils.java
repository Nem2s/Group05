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

    public static void toggleOn(final View v, int duration, Context context){
        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        if(v.getVisibility() == View.INVISIBLE) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setStartOffset(duration);

            v.startAnimation(animation);
        }
    }
    public static void toggleOff(final View v, int duration, Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        animation.setDuration(duration);
        if(v.getVisibility() == View.VISIBLE) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animation);

        }
    }

    public static void bounce(View v, Context context) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.06, 50);
        anim.setInterpolator(interpolator);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(anim);



    }

    static class MyBounceInterpolator implements android.view.animation.Interpolator {
        double mAmplitude = 1;
        double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}
