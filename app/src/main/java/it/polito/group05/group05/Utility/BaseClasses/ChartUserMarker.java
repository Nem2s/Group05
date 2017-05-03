package it.polito.group05.group05.Utility.BaseClasses;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.ImageUtils;

/**
 * Created by Marco on 09/04/2017.
 */

public class ChartUserMarker extends MarkerView {
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

    public ChartUserMarker(Context context, int layoutResource) {
        super(context, layoutResource);

        cv = (ImageView) findViewById(R.id.iv_user_image);
        tv_name = (TextView)findViewById(R.id.tv_user_name);
        tv_balance = (TextView)findViewById(R.id.tv_user_balance);
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
        UserDatabase user = (UserDatabase) e.getData();
        if(e.getY() > 0)
            color = "#3EA055";
        else
            color = "#C11B17";
        balance = "<font color=" + color + ">Currently: " + e.getY() + " € </font>";
        tv_balance.setText(Html.fromHtml(balance), TextView.BufferType.SPANNABLE);
        tv_name.setText(user.getName());

        //cv.setImageBitmap(ImageUtils.getRoundedRectBitmap(user.getProfile_image(), 48));
        super.refreshContent(e, highlight);
    }
}
