package it.polito.group05.group05.Utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;

import static android.os.Build.VERSION.SDK;

public class ProfileImageActivity extends AppCompatActivity {

    final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_header);
        toolbar.setBackgroundColor(currentGroup.getGroupColor());
        setSupportActionBar(toolbar);

        ArrayList<User> users = new ArrayList<>();


        for(int i = 0;i< 10; i++) {
            User u = new User();
            u.setId("eNiente" + i);
            u.setAdministrator(true);
            u.setCardEnabled(true);
            u.setUser_name("GIALLUME" + i);
            users.add(i, u);
        }


        UserAdapter adapter = new UserAdapter(this, users);
        ListView listView = (ListView)findViewById(R.id.lv_group_members);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
