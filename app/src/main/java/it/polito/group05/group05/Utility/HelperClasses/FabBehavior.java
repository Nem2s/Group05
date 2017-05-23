package it.polito.group05.group05.Utility.HelperClasses;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by Marco on 22/05/2017.
 */

public class FabBehavior extends FloatingActionButton.Behavior {

    public FabBehavior() {
    }

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutLinearInInterpolator();

    private int mTotalDy = 0;
    private boolean isAnimating = false;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            updateFabTranslationForSnackbar(child, dependency);
        }
        return false;
    }

    private void updateFabTranslationForSnackbar(FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        ViewCompat.setTranslationY(child, translationY);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        mTotalDy = dyConsumed < 0 && mTotalDy > 0 || dyConsumed > 0 && mTotalDy < 0 ? 0 : mTotalDy;
        mTotalDy += dyConsumed;
        updateFloatingActionMenu(child);
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, final FloatingActionButton child, View target, float velocityX, float velocityY) {

        if (Math.abs(velocityY) < Math.abs(velocityX)) return false;


        if (velocityY < 0 && isScaleX(child, 0f)) {
            /*
                Velocity is negative, we are flinging up
             */
            scaleTo(child, 1f, null);
            child.setClickable(true);
        } else if (velocityY > 0) {
              /*
                Velocity is positive, we are flinging down
             */

        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    private void updateFloatingActionMenu(final FloatingActionButton child) {
        int totalHeight = child.getHeight();
        // Only one fab.
        updateFirstFloatingActionButton(child, totalHeight);
    }


    private void updateFirstFloatingActionButton(View firstFab, int totalHeight) {
        if (mTotalDy > 0 && isScaleX(firstFab, 1f)) {
            firstFab.setClickable(false);
            scaleTo(firstFab, 0f, null);
        } else if (mTotalDy < 0 && Math.abs(mTotalDy) >= totalHeight && isScaleX(firstFab, 0f)) {
            scaleTo(firstFab, 1f, null);
            firstFab.setClickable(true);
        }
    }

    private void scaleTo(View floatingActionButton, float value, Runnable runnable) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(floatingActionButton)
                .scaleX(value).scaleY(value).setDuration(100)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        if (runnable != null) {
            viewPropertyAnimatorCompat.withEndAction(runnable);
        }
        viewPropertyAnimatorCompat.start();
    }

    private boolean isScaleX(View v, float value) {
        return ViewCompat.getScaleX(v) == value;
    }


    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout && ViewCompat.getTranslationY(child) != 0.0F) {
            ViewCompat.animate(child).translationY(0.0F).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener((ViewPropertyAnimatorListener) null);
        }

    }
}
