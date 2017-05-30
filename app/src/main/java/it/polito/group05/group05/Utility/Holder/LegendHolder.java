package it.polito.group05.group05.Utility.Holder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.GroupDetailsActivity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;

/**
 * Created by Marco on 23/05/2017.
 */

public class LegendHolder extends GeneralHolder {


    private final FloatingActionButton fab_group_color;
    private TextView tv_groupName;
    private TextView tv_groupBalance;
    private GroupDatabase g;
    private int color;
    private Context context;
    private GestureDetector detector;

    public LegendHolder(View itemView) {
        super(itemView);
        fab_group_color = (FloatingActionButton) itemView.findViewById(R.id.fab_group_color);
        tv_groupName = (TextView) itemView.findViewById(R.id.tv_group_name);
        tv_groupBalance = (TextView) itemView.findViewById(R.id.tv_group_balance);
        detector = new GestureDetector(context, new GestureTap());
    }

    @Override
    public void setData(Object c, Context context) {


    }

    public View getFab() {
        return (View) fab_group_color;
    }

    public void setData(Object groupDatabase, Integer integer, Context context) {
        g = (GroupDatabase) groupDatabase;
        color = integer;
        this.context = context;
        fab_group_color.setBackgroundTintList(ColorStateList.valueOf(color));
        tv_groupName.setText(g.getName());

        float v = Float.valueOf(g.getMembers().get(Singleton.getInstance().getCurrentUser().getId()).toString());
        String s = String.format("%.2f", v);
        String res;
        if (v > 0) {
            res = "+" + s + " €";
            tv_groupBalance.setText(res);
            tv_groupBalance.setTextColor(context.getResources().getColor(R.color.green_400));
        } else {
            res = s + " €";
            tv_groupBalance.setText(res);
            tv_groupBalance.setTextColor(context.getResources().getColor(R.color.red_400));
        }

        itemView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }


        });
    }

    private void animateAndLaunchActivity(int cx, int cy, final int color) {

        float finalRadius = (float) Math.hypot(cx, cy);
        final Intent i = new Intent(context, GroupDetailsActivity.class);
        i.putExtra("Color", color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = android.view.ViewAnimationUtils.createCircularReveal(

                    ((Activity) context).findViewById(R.id.toggle_reveal),
                    (int) cx,
                    (int) cy, 0,
                    finalRadius
            );
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ((Activity) context).findViewById(R.id.toggle_reveal).setBackgroundColor(color);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    context.startActivity(i);

                }
            });
            animator.setStartDelay(200);
            animator.setDuration(250);
            animator.start();
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    ((Activity) context).findViewById(R.id.toggle_reveal),
                    (int) cx,
                    (int) cy, 0,
                    finalRadius);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ((Activity) context).findViewById(R.id.toggle_reveal).setBackgroundColor(color);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    context.startActivity(i);

                }
            });
        }
    }

    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Singleton.getInstance().setmCurrentGroup(g);
            float cx = e.getRawX();
            float cy = e.getRawY();

            animateAndLaunchActivity((int) cx, (int) cy, color);
            return true;
        }
    }
}
