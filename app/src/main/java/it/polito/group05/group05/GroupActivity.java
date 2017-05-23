package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.internal.TextScale;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.lang.reflect.Field;

import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.Adapter.ViewPagerAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ChatDatabase;

public class GroupActivity extends AppCompatActivity {


    FragmentManager mFragmentManager;
    BottomBar navigation;
    FloatingActionButton fab;
    NestedScrollView mNestedScrollView;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, ExpenseFragment.newInstance());
            transaction.commit();

            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            navigation = (BottomBar) findViewById(R.id.navigation);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            setSupportActionBar(mToolbar);
            mToolbar.setBackgroundColor(getResources().getColor(R.color.expenseTabColor));
        }
        mToolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mToolbar.removeOnLayoutChangeListener(this);
                navigation.setOnTabSelectListener(new OnTabSelectListener() {
                    @Override
                    public void onTabSelected(@IdRes int i) {
                        switch (i) {
                            case R.id.navigation_expenses:
                                replaceWithExpenseFragment();
                                animateAppAndStatusBar(getBackgroundColor(mToolbar), getResources().getColor(R.color.expenseTabColor), mToolbar.getX(), mToolbar.getHeight());
                                break;
                            case R.id.navigation_chat:
                                replaceWithChatFragment();
                                animateAppAndStatusBar(getBackgroundColor(mToolbar), getResources().getColor(R.color.colorPrimaryLight), mToolbar.getWidth() / 2, mToolbar.getHeight());
                                break;
                            case R.id.navigation_history:
                                Toast.makeText(getApplicationContext(), "To be implmented...", Toast.LENGTH_SHORT).show();
                                changeToolbarColor(getBackgroundColor(mToolbar), getResources().getColor(R.color.historyTabColor));
                                break;
                        }
                    }
                });
            }
        });
    }

    private void replaceWithExpenseFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ExpenseFragment.newInstance())
                .commit();
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Pair<View, String> p1 = Pair.create((View)appBar, getString(R.string.transition_appbar));
                Pair<View, String> p2 = Pair.create((View)toolbar, getString(R.string.transition_toolbar));
                Pair<View, String> p3 = Pair.create((View)c, getString(R.string.transition_group_image));
                Pair<View, String> p4 = Pair.create((View)tv, getString(R.string.transition_text));
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1, p2, p3, p4);*/

                Intent i = new Intent(GroupActivity.this, Expense_activity.class);

                startActivity(i);
                //startActivity(i, options.toBundle());
            }
        });
    }



    private void replaceWithChatFragment() {
        fab.hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ChatFragment.newInstance())
                .commit();
    }

    private void animateAppAndStatusBar(final int fromColor, final int toColor, float cx, float cy) {
        // get the final radius for the clipping circle
        findViewById(R.id.reveal).setBackgroundColor(fromColor);
        int dx = (int) Math.max(cx, mToolbar.getWidth() - cx);
        int dy = (int) Math.max(cy, mToolbar.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = android.view.ViewAnimationUtils.createCircularReveal(

                    mToolbar,
                    (int) cx,
                    (int) cy, 0,
                    finalRadius
            );
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mToolbar.setBackgroundColor(toColor);
                }
            });
            animator.setStartDelay(200);
            animator.setDuration(250);
            animator.start();
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    mToolbar,
                    (int) cx,
                    (int) cy, 0,
                    finalRadius);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mToolbar.setBackgroundColor(toColor);
                }
            });
            animator.setStartDelay(0);
            animator.setDuration(250);
            animator.start();
        }


    }
    private void changeToolbarColor(int from, int to) {
        ArgbEvaluator evaluator = new ArgbEvaluator();
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(from, to);
        animator.setEvaluator(evaluator);
        animator.setDuration(500);

//animator.setRepeatCount(ValueAnimator.INFINITE);
//animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                //postInvalidate(); if you are animating a canvas
                //View.setBackgroundColor(color); another exampleof where to use
                mToolbar.setBackgroundColor(color);

            }
        });
        animator.start();
    }

    public static int getBackgroundColor(View view) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void replaceWithDetailsFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, GroupDetailsFragment.newInstance())
                .commit();
    }
}
