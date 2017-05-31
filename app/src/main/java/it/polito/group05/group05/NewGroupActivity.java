package it.polito.group05.group05;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.AestheticActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mvc.imagepicker.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.Adapter.MemberContactsAdapter;
import it.polito.group05.group05.Utility.Adapter.MemberInvitedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.Interfaces.Namable;


/**

 * Created by Marco on 05/05/2017.

 */


public class NewGroupActivity extends AestheticActivity {

    private static DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("groups");


    private static final String TAG = "Error";

    public  static int REQUEST_FROM_NEW_GROUP, INVITE;

    FloatingActionButton fab;

    CircleImageView iv_new_group;

    EditText et_group_name;
    TextView tv_contacts;
    RecyclerView rv_invited;
    RecyclerView rv_contacts;
    List<UserContact> contacts;
    List<UserContact> local_contacts;
    SwipeRefreshLayout mSwipeLayout;

    private MenuItem mSearchItem;

    public static MenuItem mConfirmItem;

    public static boolean isNameEmpty;

    private Toolbar mToolbar;

    private SearchView searchView;

    private TextView no_people;

    private TextView tv_partecipants;

    private final UserDatabase currentUser = Singleton.getInstance().getCurrentUser();

    private Context context;

    private String[] ids;

    MemberInvitedAdapter invitedAdapter;
    MemberContactsAdapter contactsAdapter;
    private boolean formIsValid = false;
    private Activity activity;


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);


        if(requestCode==INVITE)

        {

            if (resultCode == RESULT_OK) {

                // Get the invitation IDs of all sent messages

                ids= AppInviteInvitation.getInvitationIds(resultCode, data);

                if( ids != null && ids.length > 0) {

                    no_people.setText("You've invited " + ids.length + " people. Let's create your group!");

                    et_group_name.setEnabled(true);

                    iv_new_group.setEnabled(true);

                    tv_partecipants.setVisibility(View.INVISIBLE);

                    if(mSearchItem != null)

                        mSearchItem.setEnabled(true);

                }


            }

        }

        if (bitmap != null && REQUEST_FROM_NEW_GROUP == requestCode) {

            iv_new_group.setImageBitmap(bitmap);

            REQUEST_FROM_NEW_GROUP = -1;

        }

        else {

            iv_new_group.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.group_placeholder));

        }


    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_group);
        activity = this;
        isNameEmpty = true;


        et_group_name = (EditText) findViewById(R.id.group_name_add);

        iv_new_group = (CircleImageView) findViewById(R.id.iv_new_group);

        no_people = (TextView)findViewById(R.id.no_people);

        context = this;
        tv_contacts = (TextView)findViewById(R.id.tv_contacts);
        //mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        tv_partecipants = (TextView)findViewById(R.id.tv_partecipants);

        setSupportActionBar(mToolbar);

        fab = (FloatingActionButton)findViewById(R.id.fab_invite);

/*
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                contacts.clear();

                contacts.addAll(Singleton.getInstance().getRegContactsList().values());

                invitedAdapter.notifyDataSetChanged();

                mSwipeLayout.setRefreshing(false);

            }


        });*/



    }


    /*

    @Subscribe

    public void onTextChangedEvent(TextChangedEvent event) {

        this.textIsValid = event.isValid();

        if(textIsValid && selectionIsValid)

            mConfirmItem.setVisible(true);

        else

            mConfirmItem.setVisible(false);

    }



    @Subscribe public void onSelectionChangedEvent(SelectionChangedEvent event) {

        this.selectionIsValid = event.isValid();

        if(textIsValid && selectionIsValid)

            mConfirmItem.setVisible(true);

        else

            mConfirmItem.setVisible(false);

    }





    @Override

    protected void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();

    }



    @Override

    protected void onStart() {

        super.onStart();

        if(!EventBus.getDefault().isRegistered(this))

            EventBus.getDefault().register(this);

    }

*/

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.new_group_menu, menu);



        mSearchItem = menu.findItem(R.id.m_search);

        mConfirmItem = menu.findItem(R.id.m_confirm);

        mConfirmItem.setVisible(false);

        searchView = (SearchView)mSearchItem.getActionView();


        if(!et_group_name.isEnabled())

            mSearchItem.setEnabled(false);

        else

            mSearchItem.setEnabled(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                return false;

            }

            @Override

            public boolean onQueryTextChange(String newText) {



                if ( TextUtils.isEmpty ( newText ) ) {

                    invitedAdapter.replaceAll(filterRegistered(""));
                    contactsAdapter.replaceAll(filterLocal(""));
                    tv_partecipants.setVisibility(View.VISIBLE);
                    tv_contacts.setVisibility(View.VISIBLE);
                    no_people.setVisibility(View.GONE);

                } else {

                    invitedAdapter.replaceAll(filterRegistered(newText));
                    contactsAdapter.replaceAll(filterLocal(newText));
                    if(((MemberInvitedAdapter)rv_invited.getAdapter()).getItemCount() == 0 &&
                            ((MemberContactsAdapter)rv_contacts.getAdapter()).getItemCount() == 0) {
                        no_people.setVisibility(View.VISIBLE);
                        tv_partecipants.setVisibility(View.GONE);
                        tv_contacts.setVisibility(View.GONE);
                    } else {
                        tv_partecipants.setVisibility(View.VISIBLE);
                        tv_contacts.setVisibility(View.VISIBLE);
                    }

                    return true;
                }

                rv_contacts.smoothScrollToPosition(0);
                rv_invited.smoothScrollToPosition(0);

                return false;
            }

            private List<UserContact> filterLocal(String s) {
                List<UserContact> res = new ArrayList<>();

                for(UserContact u : local_contacts) {
                    if (((Namable)u).getName().toLowerCase().startsWith(s.toString().toLowerCase()))
                        res.add(u);

                }

                return res;
            }
            private List<UserContact> filterRegistered(String s) {
                List<UserContact> res = new ArrayList<>();

                for(UserContact u : contacts) {
                    if (((Namable)u).getName().toLowerCase().startsWith(s.toString().toLowerCase()))
                        res.add(u);

                }
                return res;
            }

        });

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override

            public boolean onMenuItemActionCollapse(MenuItem item) {

                // Called when SearchView is collapsing

                if (mSearchItem.isActionViewExpanded()) {

                    animateSearchToolbar(1, false, false);

                    mConfirmItem.setIcon(R.drawable.ic_action_tick_white);

                }

                return true;

            }


            @Override

            public boolean onMenuItemActionExpand(MenuItem item) {

                // Called when SearchView is expanding

                animateSearchToolbar(1, true, true);

                mConfirmItem.setIcon(R.drawable.ic_action_tick);

                return true;

            }

        });


        return true;

    }


    @Subscribe public void onSelectionChangedEvent(SelectionChangedEvent event) {

        SelectionChangedEvent.getValues(this);

        this.formIsValid = event.isValid();

        if(formIsValid) {

            mConfirmItem.setVisible(true);

        } else

            mConfirmItem.setVisible(false);

    }


    @Override

    protected void onStop() {

        EventBus.getDefault().unregister(this);

        SelectionChangedEvent.resetValues();

        super.onStop();

    }


    @Override

    protected void onStart() {

        super.onStart();

        if(!EventBus.getDefault().isRegistered(this))

            EventBus.getDefault().register(this);

    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.m_confirm) {

            DatabaseReference ref = groupRef.push();

            GroupDatabase group = new GroupDatabase();

            group.setMembers(new HashMap<String,Object>());

            group.setId(ref.getKey());

            group.setName(et_group_name.getText().toString());

            group.setCreator(Singleton.getInstance().getCurrentUser().getId());

            for (UserContact u : contacts) {

                if(u.isSelected()) {

                    group.getMembers().put(u.getId(), 0.0);

                    u.setSelected(false);

                }


            }


            group.getMembers().put(currentUser.getId(), 0.0);


            if(!group.getMembers().isEmpty() && !et_group_name.getText().toString().equals("")) {


                DB_Manager.getInstance().setContext(this).pushNewGroup(group, ((BitmapDrawable)iv_new_group.getDrawable()).getBitmap());

                finish();

            } else {

                Snackbar.make(findViewById(R.id.parent_layout), "Missing some Informations!", Snackbar.LENGTH_LONG).show();

                invitedAdapter.notifyDataSetChanged();

                return false;


            }

        }


        //DB_Manager.getInstance().PushGroupToDB(newgroup);

        //DB_Manager.getInstance().MonitorOnGroup(newgroup);


        return super.onOptionsItemSelected(item);

    }


    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {


        mToolbar.setBackgroundColor(Aesthetic.get().colorPrimary().take(1).blockingFirst());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(Aesthetic.get().colorStatusBar().take(1).blockingFirst());

        }


        if (show) {

            int width = mToolbar.getWidth() -

                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -

                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);

            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,

                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);

            createCircularReveal.setDuration(250);

            createCircularReveal.start();


        } else {


            int width = mToolbar.getWidth() -

                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -

                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);

            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,

                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getMeasuredHeight() / 2, (float) width, 0.0f);

            createCircularReveal.setDuration(250);

            createCircularReveal.addListener(new AnimatorListenerAdapter() {

                @Override

                public void onAnimationEnd(Animator animation) {

                    super.onAnimationEnd(animation);

                    mToolbar.setBackgroundColor(Aesthetic.get().colorPrimary().take(1).blockingFirst());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(Aesthetic.get().colorStatusBar().take(1).blockingFirst());

                    }

                }

            });

            createCircularReveal.start();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                getWindow().setStatusBarColor(Aesthetic.get().colorStatusBar().take(1).blockingFirst());

            }

        }

    }


    private boolean isRtl(Resources resources) {

        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

    }


    private static int getThemeColor(Context context, int id) {

        Resources.Theme theme = context.getTheme();

        TypedArray a = theme.obtainStyledAttributes(new int[]{id});

        int result = a.getColor(0, 0);

        a.recycle();

        return result;

    }

    @Override
    protected void onResume() {
        super.onResume();
        new InitializeViews().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (UserContact u : contacts) {
            if (u.isSelected()) {
                u.setSelected(false);
            }
        }
        finish();
    }

    class InitializeViews extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... voids) {

            contacts = new ArrayList<>(Singleton.getInstance().getRegContactsList().values());
            local_contacts = new ArrayList<>(Singleton.getInstance().getLocalContactsList().values());

            invitedAdapter = new MemberInvitedAdapter(contacts, context);
            contactsAdapter = new MemberContactsAdapter(local_contacts, context);
            Collections.sort(contacts, new Comparator<UserContact>() {
                @Override
                public int compare(UserContact u1, UserContact u2) {
                    return u1.getName().compareTo(u2.getName());
                }
            });
            Collections.sort(local_contacts, new Comparator<UserContact>() {
                @Override
                public int compare(UserContact u1, UserContact u2) {
                    return u1.getName().compareTo(u2.getName());
                }
            });




            return null;
        }

        @Override
        protected void onPreExecute() {
            rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
            rv_contacts = (RecyclerView)findViewById(R.id.contacts_people_list);
            dialog = ProgressDialog.show(activity, "Loading", "Loading contacts...", true, false);
            LinearLayoutManager contactsManager = new LinearLayoutManager(getApplicationContext());
            contactsManager.setItemPrefetchEnabled(true);
            contactsManager.setInitialPrefetchItemCount(25);

            DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(rv_invited.getContext(),

                    contactsManager.getOrientation());

            rv_contacts.addItemDecoration(dividerItemDecoration2);

            LinearLayoutManager invitedManager = new LinearLayoutManager(getApplicationContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_invited.getContext(),

                    invitedManager.getOrientation());

            rv_contacts.setHasFixedSize(true);

            rv_contacts.setLayoutManager(contactsManager);

            rv_invited.setHasFixedSize(true);

            rv_invited.setLayoutManager(invitedManager);
            rv_invited.addItemDecoration(dividerItemDecoration);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            rv_contacts.setAdapter(contactsAdapter);

            rv_invited.setAdapter(invitedAdapter);

            if(invitedAdapter.getItemCount() == 0) {

                Snackbar snackbar = Snackbar.make(findViewById(R.id.parent_layout), "No contacts have ShareCash installed, Start invite your friends!", Snackbar.LENGTH_INDEFINITE)

                        .setAction("ok", new View.OnClickListener() {

                            @Override

                            public void onClick(View view) {

                            }

                        });

                snackbar.show();

                final View snackbarView = snackbar.getView();

                snackbarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override

                    public boolean onPreDraw() {

                        snackbarView.getViewTreeObserver().removeOnPreDrawListener(this);

                        ((CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams()).setBehavior(null);

                        return true;

                    }

                });


                et_group_name.setEnabled(false);

                iv_new_group.setEnabled(false);

                if(contactsAdapter.getItemCount() == 0)
                    no_people.setVisibility(View.VISIBLE);

            }

            rv_contacts.setNestedScrollingEnabled(false);
            rv_invited.setNestedScrollingEnabled(false);


            rv_contacts.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {


                    Intent intent = new AppInviteInvitation.IntentBuilder("")

                            .setMessage("")

                            .setDeepLink(Uri.parse("https://h5uqp.app.goo.gl/"))

                            .build();

                    startActivityForResult(intent, INVITE);


                }

            });


            iv_new_group.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {

                    ImagePicker.pickImage((Activity) context, "Select Image:");

                    REQUEST_FROM_NEW_GROUP = ImagePicker.PICK_IMAGE_REQUEST_CODE;


                }

            });

            /**TROVARE METODO ALTERNATIVO **/

            //checkSelected();


            et_group_name.addTextChangedListener(new TextWatcher() {

                @Override

                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }


                @Override

                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    EventBus.getDefault().post(SelectionChangedEvent.onTextChangedEvent(charSequence.length() > 0));

                }


                @Override

                public void afterTextChanged(Editable editable){


                }

            });

        }
    }



}
