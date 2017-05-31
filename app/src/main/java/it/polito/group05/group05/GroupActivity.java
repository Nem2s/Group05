package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

import static it.polito.group05.group05.R.id.view;

public class GroupActivity extends AestheticActivity {


    FragmentManager mFragmentManager;
    BottomNavigationView bottomView;
    FloatingActionButton fab;
    NestedScrollView mNestedScrollView;
    Toolbar mToolbar;
    GroupDatabase currentGroup = Singleton.getInstance().getmCurrentGroup();
    CircleImageView cv_group;
    TextView tv_groupname;
    TextView tv_members;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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


            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomView = (BottomNavigationView) findViewById(R.id.navigation);
            bottomView.setSelectedItemId(R.id.navigation_expenses);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            cv_group = (CircleImageView) findViewById(R.id.cv_groupImage);
            tv_groupname = (TextView) findViewById(R.id.tv_group_name);
            tv_members = (TextView) findViewById(R.id.tv_members);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, ExpenseFragment.newInstance());
            transaction.commit();
            initializeUI();

        } else {
            bottomView.setSelectedItemId(R.id.navigation_chat);
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, ChatFragment.newInstance());
            transaction.commit();

        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(mToolbar);


        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tv_members.setSelected(true);
                switch (item.getItemId()) {
                    case R.id.navigation_expenses:
                        replaceWithExpenseFragment();
                        //  animateAppAndStatusBar(getBackgroundColor(mToolbar), getResources().getColor(R.color.expenseTabColor), mToolbar.getX(), mToolbar.getHeight());
                        break;
                    case R.id.navigation_chat:
                        replaceWithChatFragment();
                        //animateAppAndStatusBar(getBackgroundColor(mToolbar), getResources().getColor(R.color.colorPrimaryLight), mToolbar.getWidth() / 2, mToolbar.getHeight());
                        break;
                    case R.id.navigation_history:
                        replaceWithHistoryFragment();
                        //Toast.makeText(getApplicationContext(), "To be implmented...", Toast.LENGTH_SHORT).show();
                        //changeToolbarColor(getBackgroundColor(mToolbar), getResources().getColor(R.color.historyTabColor));
                        //   animateAppAndStatusBar(getBackgroundColor(mToolbar), getResources().getColor(R.color.expenseTabColor), mToolbar.getX(), mToolbar.getHeight());

                        break;
                }

                return true;
            }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.show();
        supportFinishAfterTransition();
    }

    private void initializeUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    transition.removeTarget(R.id.toolbar);
                    transition.removeTarget(R.id.navigation);
                    ImageUtils.LoadImageGroup(cv_group, getApplicationContext(), currentGroup);
                }

                @Override
                public void onTransitionEnd(Transition transition) {

                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

           getWindow().getSharedElementExitTransition().addListener(new Transition.TransitionListener() {
               @Override
               public void onTransitionStart(Transition transition) {

               }

               @Override
               public void onTransitionEnd(Transition transition) {

               }

               @Override
               public void onTransitionCancel(Transition transition) {

               }

               @Override
               public void onTransitionPause(Transition transition) {

               }

               @Override
               public void onTransitionResume(Transition transition) {

               }
           });
            scheduleStartPostponedTransition(cv_group);

        } else {
            fab.show();
            ImageUtils.LoadImageGroup(cv_group, getApplicationContext(), currentGroup);


        }
        fillNameMembersList();
        tv_groupname.setText(currentGroup.getName());

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

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    private void fillNameMembersList() {
        tv_members.setText("");
        for(String s : currentGroup.getMembers().keySet()) {
            FirebaseDatabase.getInstance().getReference("users").child(s).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                    if(tv_members.getText().toString().equals(""))
                        tv_members.setText(u.getName());
                    else
                        tv_members.append(", " +u.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            tv_members.setSelected(true);
        }

    }

    private void replaceWithExpenseFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ExpenseFragment.newInstance())
                .commit();
        fab.show();

    }





    private void replaceWithChatFragment() {
        fab.hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ChatFragment.newInstance())
                .commit();
    }

    private void replaceWithHistoryFragment() {
        fab.hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, HistoryFragment.newInstance())
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


    }
}
