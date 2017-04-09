package it.polito.group05.group05;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mvc.imagepicker.ImagePicker;
import com.pkmmte.view.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;

import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.InvitedAdapter;

public class NewGroup extends AppCompatActivity {

    public  static int REQUEST_FROM_NEW_GROUP;


    CircularImageView iv_new_group;
    Button addButton;
    MaterialEditText et_group_name;
    RecyclerView rv_invited;

    private Group newgroup;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        Intent intent = ImagePicker.getPickImageIntent(this, "Select Image:");
        if (bitmap != null && REQUEST_FROM_NEW_GROUP == requestCode) {
            iv_new_group.setImageBitmap(bitmap);
            REQUEST_FROM_NEW_GROUP = -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        rv_invited = (RecyclerView)findViewById(R.id.invited_people_list);
        addButton = (Button)findViewById(R.id.add_to_group_button);
        et_group_name = (MaterialEditText)findViewById(R.id.group_name_add);
        iv_new_group = (CircularImageView) findViewById(R.id.iv_new_group);

        final User currentUser = Singleton.getInstance().getCurrentUser();
        final InvitedAdapter invitedAdapter = new InvitedAdapter(currentUser.getContacts(), this);
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
                ImagePicker.pickImage(getParent(), "Select Image:");
                REQUEST_FROM_NEW_GROUP = ImagePicker.PICK_IMAGE_REQUEST_CODE;

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newgroup = new Group(et_group_name.getText().toString(), new Balance(0, 0), ((BitmapDrawable)iv_new_group.getDrawable()).getBitmap(), Calendar.getInstance().getTime().toString(), 1);
                newgroup.addMember(currentUser);
                Singleton.getInstance().createRandomListUsers(61, getApplicationContext(), newgroup);
                for (UserContact u : currentUser.getContacts()) {
                    if(u.isSelected()) {
                        newgroup.addMember(u);
                        u.setSelected(false);
                    }

                }

                if(newgroup.getMembers().isEmpty() || et_group_name.getText().toString().equals("")) {
                    Snackbar.make(view, "Missing some Informations!", Snackbar.LENGTH_LONG).show();

                }
                else {
                    Singleton.getInstance().addGroup(newgroup);
                    finish();
                }
                invitedAdapter.notifyDataSetChanged();
            }
        });


        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(isOpen)
                    addButton.setVisibility(View.INVISIBLE);
                else
                    addButton.setVisibility(View.VISIBLE);
            }
        });
    }
}
