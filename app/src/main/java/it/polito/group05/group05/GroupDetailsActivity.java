package it.polito.group05.group05.Utility;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.R;

import static android.os.Build.VERSION.SDK;
import static it.polito.group05.group05.R.id.textView;

public class ProfileImageActivity extends AppCompatActivity {

    final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_header);
        customizeToolbar(toolbar);
        setSupportActionBar(toolbar);


        ArrayList<User> users = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, users);
        ListView listView = (ListView)findViewById(R.id.lv_group_members);
        listView.setAdapter(adapter);


        adapter.addAll(currentGroup.getMembers());
    }

    private void customizeToolbar(final Toolbar toolbar) {
        final CircularImageView c = (CircularImageView)findViewById(R.id.iv_group_image);
        final Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int _default = getResources().getColor(R.color.colorPrimary);
                final int _text = 0x000000;
                final int color = palette.getLightVibrantColor(_default);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), _default, color);
                colorAnimation.setDuration(50000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        toolbar.setBackgroundColor(color);
                        toolbar.setTitleTextColor(_text);
                    }

                });

                if(color != _default) {
                    colorAnimation.start();
                }

            }
        };
        Picasso
                .with(getApplicationContext())
                .load(Integer.parseInt(currentGroup.getGroupProfile()))
                .transform(new PicassoRoundTransform())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(paletteListener);
                        c.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setExitSharedElementCallback(new SharedElementCallback() {
                                         @Override
                                         public void onMapSharedElements(List<String> names,
                                                                         Map<String, View> sharedElements) {
                                             sharedElements.put(R.string.transition_string, newSharedElement);
                                         }
                                     }
        supportFinishAfterTransition();
    }
}
