package it.polito.group05.group05;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import android.widget.TextView;

import com.mvc.imagepicker.ImagePicker;
import com.pkmmte.view.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import io.codetail.animation.ViewAnimationUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.DB_Manager;
import it.polito.group05.group05.Utility.EventClasses.SelectionChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.TextChangedEvent;
import it.polito.group05.group05.Utility.InvitedAdapter;

public class NewGroup extends AppCompatActivity{

    public  static int REQUEST_FROM_NEW_GROUP;


    CircularImageView iv_new_group;
    MaterialEditText et_group_name;
    RecyclerView rv_invited;
    private Group newgroup;
    private MenuItem mSearchItem;
    public static MenuItem mConfirmItem;
    public static boolean isNameEmpty;
    private Toolbar mToolbar;
    private SearchView searchView;
    private TextView no_people;
    private final User currentUser = Singleton.getInstance().getCurrentUser();
    private InvitedAdapter invitedAdapter;
    private Context context;
    private boolean textIsValid;
    private boolean selectionIsValid;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (bitmap != null && REQUEST_FROM_NEW_GROUP == requestCode) {
            iv_new_group.setImageBitmap(bitmap);
            REQUEST_FROM_NEW_GROUP = -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        isNameEmpty = true;
        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
        et_group_name = (MaterialEditText)findViewById(R.id.group_name_add);
        iv_new_group = (CircularImageView) findViewById(R.id.iv_new_group);
        no_people = (TextView)findViewById(R.id.no_people);
        context = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        invitedAdapter = new InvitedAdapter(currentUser.getContacts(), this);
        LinearLayoutManager invitedManager = new LinearLayoutManager(this);

        rv_invited.setHasFixedSize(true);
        rv_invited.setLayoutManager(invitedManager);
        rv_invited.setAdapter(invitedAdapter);
        //all.addAll(retriveAllPeople());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_invited.getContext(),
                invitedManager.getOrientation());
        rv_invited.addItemDecoration(dividerItemDecoration);

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
                EventBus.getDefault().post(new TextChangedEvent(charSequence.length() > 0));
            }

            @Override
            public void afterTextChanged(Editable editable){
            }
        });




    }

    @Subscribe public void onTextChangedEvent(TextChangedEvent event) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchItem = menu.findItem(R.id.m_search);
        mConfirmItem = menu.findItem(R.id.m_confirm);
        mConfirmItem.setVisible(false);
        searchView = (SearchView)mSearchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                no_people.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ( TextUtils.isEmpty ( newText ) ) {
                    ((InvitedAdapter)rv_invited.getAdapter()).getFilter().filter("");
                    no_people.setVisibility(View.GONE);

                } else {
                    ((InvitedAdapter)rv_invited.getAdapter()).getFilter().filter(newText);
                    if(((InvitedAdapter)rv_invited.getAdapter()).getItemCount() == 0)
                        no_people.setVisibility(View.VISIBLE);
                }
                return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.m_confirm) {
            newgroup = new Group(et_group_name.getText().toString(), new Balance(0, 0), ((BitmapDrawable)iv_new_group.getDrawable()).getBitmap(), Calendar.getInstance().getTime().toString(), 1);
            newgroup.addMember(currentUser);
            for (UserContact u : currentUser.getContacts()) {
                if(u.isSelected()) {
                    newgroup.addMember(u);
                    u.setSelected(false);
                }

            }

            if(!newgroup.getMembers().isEmpty() && !et_group_name.getText().toString().equals("")) {
                Singleton.getInstance().addGroup(newgroup);
                //invitedAdapter.notifyDataSetChanged();
                finish();


            }
            else {
                Snackbar.make(findViewById(R.id.parent_layout), "Missing some Informations!", Snackbar.LENGTH_LONG).show();
                invitedAdapter.notifyDataSetChanged();
                return false;

            }
        }

        DB_Manager.getInstance().PushGroupToDB(newgroup);
        //DB_Manager.getInstance().MonitorOnGroup(newgroup);

        return super.onOptionsItemSelected(item);
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.quantum_grey_600));
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
                        mToolbar.setBackgroundColor(getThemeColor(NewGroup.this, R.attr.colorPrimary));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(getThemeColor(NewGroup.this, R.attr.colorPrimaryDark));
                        }
                    }
                });
                createCircularReveal.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getThemeColor(NewGroup.this, R.attr.colorPrimaryDark));
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


}
