package it.polito.group05.group05.Utility.BaseClasses;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.storage.FirebaseStorage;

import it.polito.group05.group05.R;

/**
 * Created by Marco on 11/05/2017.
 */

public class ChartGroupMarker extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    private ImageView cv;
    private TextView tv_name;
    private TextView tv_balance;
    private CardView card;
    private MPPointF mOffset;

    private Context context;

    public ChartGroupMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        this.context = context;
        cv = (ImageView) findViewById(R.id.iv_group_image);
        tv_name = (TextView)findViewById(R.id.tv_group_name);
        tv_balance = (TextView)findViewById(R.id.tv_group_balance);
        card = (CardView)findViewById(R.id.card);
    }

    @Override
    public void setOffset(MPPointF offset) {
        super.setOffset(offset);
    }

    @Override
    public MPPointF getOffset() {
        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth()), -getHeight());
        }

        return mOffset;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String balance;
        String color;
        GroupDatabase g = (GroupDatabase) e.getData();
        float value;
        try {
            value = Float.valueOf(String.valueOf((long)g.getMembers().get(Singleton.getInstance().getCurrentUser().getId())));

        } catch (ClassCastException ex) {
            value = Float.valueOf(String.valueOf((double)g.getMembers().get(Singleton.getInstance().getCurrentUser().getId())));
        }
        if(value > 0)
            color = "#3EA055";
        else
            color = "#C11B17";
        balance = "<font color=" + color + ">Currently: " + value + " € </font>";
        tv_balance.setText(Html.fromHtml(balance), TextView.BufferType.SPANNABLE);
        tv_name.setText(g.getName());
        Glide.with(context).using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("groups")
                        .child(g.getId())
                        .child(g.getPictureUrl()))
                .placeholder(R.drawable.network)
                .centerCrop()
                .crossFade()
                .into(cv);
        //cv.setImageBitmap(ImageUtils.getRoundedRectBitmap(user.getProfile_image(), 48));
        super.refreshContent(e, highlight);
    }
}
