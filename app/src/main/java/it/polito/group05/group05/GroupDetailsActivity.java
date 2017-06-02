package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.aesthetic.NavigationViewMode;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.ui.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import it.polito.group05.group05.Utility.Adapter.GroupDetailsAdapter;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.AnimUtils;
import it.polito.group05.group05.Utility.HelperClasses.AppBarStateChangeListener;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

public class GroupDetailsActivity extends AestheticActivity {

    private static int EXTRA_COLOR = -1;
    private int DEFAULT_COLOR = -1;
    private final int COME_FROM_ADD_MEMBER_ACTIVITY = 123;
    private final GroupDatabase currGroup = Singleton.getInstance().getmCurrentGroup();
    ImageView iv_header;
    CircleImageView cv_back;
    FloatingActionButton fab;
    RecyclerView rv_members;
    GroupDetailsAdapter mAdapter;
    MaterialDialog editDialog;
    ProgressBar pb;
    Toolbar toolbar;
    AppBarLayout appbar;
    LinearLayout root;
    private List<Object> users = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //gestione cambio nome/immagine gruppo.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarLayout.setTitle(Singleton.getInstance().getmCurrentGroup().getName());
        toolbarLayout.setExpandedTitleColor(ImageUtils.isLightDarkActionBar() ?
                Aesthetic.get().textColorPrimary().take(1).blockingFirst() :
                Aesthetic.get().textColorPrimaryInverse().take(1).blockingFirst());
        toolbarLayout.setCollapsedTitleTextColor(ImageUtils.isLightDarkActionBar() ?
                Aesthetic.get().textColorSecondary().take(1).blockingFirst() :
                Aesthetic.get().textColorSecondaryInverse().take(1).blockingFirst());
        toolbarLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editDialog.show();
                return false;
            }
        });

        /*if (Aesthetic.isFirstTime()) {
            int[] colors = Singleton.getInstance().getColors();
            Aesthetic.get()
                    .colorPrimary(colors[1])
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .colorAccent(colors[0])
                    .navigationViewMode(
                            NavigationViewMode.SELECTED_ACCENT
                    )
                    .apply();
        }*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        iv_header = (ImageView) findViewById(R.id.iv_header_group_image);
        iv_header.setVisibility(View.INVISIBLE);
        cv_back = (CircleImageView) findViewById(R.id.cv_backimage);

        rv_members = (RecyclerView) findViewById(R.id.rv_group_members);
        pb = (ProgressBar) findViewById(R.id.pb_loading_members);
        pb.setVisibility(View.INVISIBLE);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        root = (LinearLayout) findViewById(R.id.reveal_root);
        appbar = (AppBarLayout)findViewById(R.id.app_bar);
        final LinearLayoutManager ll = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rv_members.setLayoutManager(ll);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        onStartView(savedInstanceState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(GroupDetailsActivity.this, NewMemberActivity.class);
                //startActivity(i);
            }
        });


        users.add(Singleton.getInstance().getCurrentUser());
        //users.addAll((Singleton.getInstance().getmCurrentGroup().getMembers().values()));
        mAdapter = new GroupDetailsAdapter(this, users);
        rv_members.setAdapter(mAdapter);



    }

    private void onStartView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final Intent i = getIntent();
            final int color = i.getIntExtra("Color", getResources().getColor(R.color.colorPrimary));
            if(!(color == getResources().getColor(R.color.colorPrimary))) {
                cv_back.setVisibility(View.INVISIBLE);
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title("Group Informations")
                        .content("To be implemented...")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        });
                editDialog = builder.build();
                ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            EXTRA_COLOR = color;
                            final int cx = (int) (appbar.getX() + (appbar.getWidth() / 2));
                            final int cy = (int) (appbar.getY() + (appbar.getHeight() / 2));
                            final float finalRadius = (float) Math.hypot(cx, cy);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                /**Reveal Animator**/
                                Animator revealAnimator = android.view.ViewAnimationUtils.createCircularReveal(

                                        root,
                                        (int) cx,
                                        (int) cy, finalRadius,
                                        0
                                );
                                /**ColorAnimator**/
                                final ValueAnimator colorAnimator = ValueAnimator.ofArgb(color, Aesthetic.get().colorPrimary().take(1).blockingFirst());
                                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        findViewById(R.id.reveal_parent).setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                                    }

                                });
                                colorAnimator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        ImageUtils.LoadImageGroup(iv_header, getApplicationContext(), currGroup);
                                        AnimUtils.enterRevealAnimation(iv_header);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                colorAnimator.setDuration(1000);
                                revealAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        root.setBackgroundColor(color);

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        retriveUsers();

                                        root.setVisibility(View.INVISIBLE);
                                        fab.show();
                                        //AnimUtils.enterRevealAnimation(iv_header);
                                    }
                                });
                                revealAnimator.setStartDelay(200);
                                revealAnimator.setDuration(250);
                                revealAnimator.start();
                                colorAnimator.start();
                            } else {
                                Animator revealAnimator = ViewAnimationUtils.createCircularReveal(
                                        root,
                                        (int) cx,
                                        (int) cy, 0,
                                        finalRadius);
                                final ValueAnimator colorAnimator = new ValueAnimator();
                                colorAnimator.setIntValues(color, getResources().getColor(R.color.colorPrimary));
                                colorAnimator.setEvaluator(new ArgbEvaluator());
                                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        findViewById(R.id.reveal_parent).setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                                    }
                                });

                                colorAnimator.setDuration(1000);
                                colorAnimator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        ImageUtils.LoadImageGroup(iv_header, getApplicationContext(), currGroup);
                                        AnimUtils.enterRevealAnimation(iv_header);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                revealAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                        root.setBackgroundColor(color);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        retriveUsers();
                                        root.setVisibility(View.INVISIBLE);
                                        fab.show();
                                        cv_back.setVisibility(View.INVISIBLE);
                                    }
                                });
                                revealAnimator.start();
                                colorAnimator.start();
                            }
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                    });
                }
            } else {
                cv_back.setVisibility(View.VISIBLE);
                initializeUI();
            }

        }
    }

    private void retriveUsers() {
        pb.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("groups").child(currGroup.getId())
                .child("members")
                .orderByKey()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FirebaseDatabase.getInstance().getReference("users").child(dataSnapshot.getKey()).child("userInfo")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        pb.setVisibility(View.GONE);
                                        final UserDatabase newUser = dataSnapshot.getValue(UserDatabase.class);
                                        if (newUser.getId().equals(Singleton.getInstance().getCurrentUser().getId()))
                                            return;
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                int i = mAdapter.addUser(newUser);
                                                int n = mAdapter.getItemCount();
                                                if (i == n)
                                                    mAdapter.notifyItemInserted(n - 1);
                                                else
                                                    mAdapter.notifyItemChanged(i - 1);
                                            }
                                        });
                                        if (pb.getVisibility() == View.GONE) {
                                            rv_members.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference("users").child(dataSnapshot.getKey()).child("userInfo")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        pb.setVisibility(View.GONE);
                                        final UserDatabase newUser = dataSnapshot.getValue(UserDatabase.class);
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                int i = mAdapter.removeUser(newUser);
                                                if (i != -1)
                                                    mAdapter.notifyItemRemoved(i);
                                            }
                                        });
                                        if (pb.getVisibility() == View.GONE) {
                                            rv_members.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    @Override
    public void onBackPressed() {
        AnimUtils.exitReveal(iv_header);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimUtils.exitRevealAndFinish(fab, this);
        } else {
            AnimUtils.toggleOffView(fab, getApplicationContext());
            supportFinishAfterTransition();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initializeUI() {


        ImageUtils.LoadImageGroup(iv_header, getApplicationContext(), currGroup);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Group Informations")
                .content("To be implemented...")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        editDialog = builder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        fab.show();

                        AnimUtils.enterRevealAnimation(iv_header);
                        cv_back.setVisibility(View.INVISIBLE);
                        retriveUsers();
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

                scheduleStartPostponedTransition(cv_back);
            } else {
                fab.show();
                AnimUtils.enterRevealAnimation(iv_header);
                cv_back.setVisibility(View.INVISIBLE);
                retriveUsers();
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

    @Override
    protected void onStop() {
        super.onStop();
    }
}
