package it.polito.group05.group05.Utility.Holder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.GroupDetailsActivity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

/**
 * Created by Marco on 17/05/2017.
 */

public class MemberGroupDetailsHeaderHolder extends GeneralHolder {
    private CircleImageView cv_userImage;
    private TextView tv_userName;
    private Button buttonLeave;

    public MemberGroupDetailsHeaderHolder(View itemView) {
        super(itemView);

        this.cv_userImage = (CircleImageView) itemView.findViewById(R.id.cv_userimage);
        this.tv_userName = (TextView) itemView.findViewById(R.id.tv_user_name);
        this.buttonLeave = (Button) itemView.findViewById(R.id.button_leave);
    }

    @Override
    public void setData(Object c, final Context context) {
        if (!(c instanceof UserDatabase)) return;
        final UserDatabase user = Singleton.getInstance().getCurrentUser();
        ImageUtils.LoadMyImageProfile(cv_userImage, context);
        tv_userName.setText("You");

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("expenses")
                        .child(Singleton.getInstance().getmCurrentGroup().getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double value = 0;
                                int n = 0;
                                MaterialDialog.Builder builder;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    ExpenseDatabase e = data.getValue(ExpenseDatabase.class);
                                    if (e.getMembers().containsKey(user.getId())) {
                                        n++;
                                        value += e.getMembers().get(user.getId());
                                    }
                                }
                                if (n > 0 && value != 0) {
                                    builder =
                                            new MaterialDialog.Builder(context)
                                                    .title("Error")
                                                    .content("You cannot leave the group.\n" +
                                                            "You have " + n + " pending " + (n > 1 ? "Expenses " : "Expense ") +
                                                            "for a total of " + (value < 0 ? -value + " €'s debit" : value + " €'s credit"))
                                                    .negativeText("Cancel")
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).canceledOnTouchOutside(false);


                                } else {
                                    builder =
                                            new MaterialDialog.Builder(context)
                                                    .title("Leaving Group")
                                                    .content("Are you sure?")
                                                    .negativeText("Cancel")
                                                    .positiveColor(context.getResources().getColor(R.color.colorSecondaryText))
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            FirebaseDatabase.getInstance().getReference("expenses")
                                                                    .child(Singleton.getInstance().getmCurrentGroup().getId())
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                                ExpenseDatabase e = data.getValue(ExpenseDatabase.class);
                                                                                if (e.getMembers().containsKey(user.getId())) {
                                                                                    data.child("members").child(user.getId()).getRef().removeValue();
                                                                        }
                                                                            }
                                                                            ((Activity) context).onBackPressed();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                            FirebaseDatabase.getInstance().getReference("groups")
                                                                    .child(Singleton.getInstance().getmCurrentGroup().getId())
                                                                    .child("members")
                                                                    .child(user.getId())
                                                                    .removeValue();
                                                            FirebaseDatabase.getInstance().getReference("users")
                                                                    .child(user.getId())
                                                                    .child("userGroups")
                                                                    .child(Singleton.getInstance().getmCurrentGroup().getId())
                                                                    .removeValue();

                                                        }
                                                    })
                                                    .positiveText("Yes");
                                }
                                builder.show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        });

    }
}
