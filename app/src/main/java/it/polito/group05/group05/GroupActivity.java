package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.afollestad.aesthetic.AestheticActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

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
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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


        } else {
            bottomView.setSelectedItemId(R.id.navigation_chat);
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, ChatFragment.newInstance());
            transaction.commit();

        }
        if (getIntent().getStringExtra("type") != null) {
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


                                DB_Manager.getInstance().payDone(gid, eid, uid, (-1.00) * dd);
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

        }
        try {
            initializeUI();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        supportFinishAfterTransition();
    }

    private void initializeUI() throws ExecutionException, InterruptedException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    transition.removeTarget(R.id.toolbar);
                    transition.removeTarget(R.id.navigation);
                    //ImageUtils.LoadImageGroup(cv_group, getApplicationContext(), currentGroup);
                    //tv_groupname.setText(currentGroup.getName());
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


            final Context context = this;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GroupDatabase currGroup = Singleton.getInstance().getmCurrentGroup();

                    GlideDrawable bmp = null;
                    try {
                        bmp = Glide.with(context)
                                .using(new FirebaseImageLoader())
                                .load(FirebaseStorage.getInstance().getReference("groups").child(currGroup.getId())
                                        .child(currGroup.getPictureUrl()))
                                .centerCrop()
                                .into(128, 128)
                                .get()
                        ;
                        mToolbar.setLogo(bmp);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            });


            // ImageUtils.LoadImageGroup(cv_group, getApplicationContext(), currentGroup);
            mToolbar.setTitle(currentGroup.getName());
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

        mToolbar.setSubtitle("");
        for(String s : currentGroup.getMembers().keySet()) {
            FirebaseDatabase.getInstance().getReference("users").child(s).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                    if (mToolbar.getSubtitle().toString().equals(""))
                        mToolbar.setSubtitle(u.getName());

                    else
                        mToolbar.setSubtitle(mToolbar.getSubtitle() + "," + u.getName());


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
