package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.transition.Transition;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

public class GroupActivity extends AppCompatActivity {


    FragmentManager mFragmentManager;
    BottomBar navigation;
    FloatingActionButton fab;
    NestedScrollView mNestedScrollView;
    Toolbar mToolbar;
    GroupDatabase currentGroup = Singleton.getInstance().getmCurrentGroup();
    CircleImageView cv_group;
    TextView tv_groupname;
    TextView tv_members;
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
            navigation = (BottomBar) findViewById(R.id.navigation);
            if (getIntent().getStringExtra("message") == null) {
                mFragmentManager = getSupportFragmentManager();
                navigation.setDefaultTab(R.id.navigation_expenses);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.fragment_container, ExpenseFragment.newInstance());
                transaction.commit();

            } else {
                navigation.setDefaultTab(R.id.navigation_chat);
                mFragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.fragment_container, ChatFragment.newInstance());
                transaction.commit();

            }
            if (getIntent().getStringExtra("type") != null)
                if (getIntent().getStringExtra("type").equals("paymentRequest")) {
                    final String eid = getIntent().getStringExtra("expenseId");
                    final String uid = getIntent().getStringExtra("requestFromId");
                    final String gid = getIntent().getStringExtra("groupId");
                    final String debit = getIntent().getStringExtra("expenseDebit");
                    final Double dd = Double.parseDouble(getIntent().getStringExtra("expenseDebit").substring(1));
                    AlertDialog d = new AlertDialog.Builder(this).setTitle("Confirm the Payment")
                            .setMessage("Have you received € " + String.format("%.2f", dd) + " for " + getIntent().getStringExtra("expenseName") + "by " + getIntent().getStringExtra("requestFrom") + " ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    DB_Manager.getInstance().payDone(gid, eid, uid, dd);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DB_Manager.getInstance().payUnDone(gid, eid, uid);
                                    dialog.cancel();
                                }
                            })
                            .show();


                }

            mToolbar = (Toolbar) findViewById(R.id.toolbar);

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
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                    tv_groupname.setText(currentGroup.getName());
                    fab.hide();
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    fab.show();

                    fillNameMembersList();


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

            fillNameMembersList();
        }


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
        for (String s : currentGroup.getMembers().keySet()) {
            FirebaseDatabase.getInstance().getReference("users").child(s).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                    if (tv_members.getText().toString().equals(""))
                        tv_members.setText(u.getName());
                    else
                        tv_members.append(", " + u.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

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
