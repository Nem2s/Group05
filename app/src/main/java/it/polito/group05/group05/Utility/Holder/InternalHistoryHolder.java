package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.HistoryClass;

/**
 * Created by andre on 25-May-17.
 */

public class InternalHistoryHolder extends GeneralHolder {

    TextView history_text_view;
    LinearLayout ll_history;
    ImageView history_image;
    FrameLayout divider_history;
    TextView history_time;

    public InternalHistoryHolder(View itemView) {
        super(itemView);
        this.history_text_view = (TextView) itemView.findViewById(R.id.history_text_view);
        this.ll_history = (LinearLayout) itemView.findViewById(R.id.ll_history);
        this.history_image = (ImageView) itemView.findViewById(R.id.history_image);
        this.divider_history = (FrameLayout) itemView.findViewById(R.id.divider_history);
        this.history_time = (TextView) itemView.findViewById(R.id.history_time);

    }

    @Override
    public void setData(Object c, Context context) {
        HistoryClass history = (HistoryClass) c;
        String sourceString = "<b>" + history.getWho() + "</b> " + history.getWhat();
        history_text_view.setText(Html.fromHtml(sourceString));
        switch (history.getType()) {
            case 0:
                history_image.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
                break;
            case 1:
                history_image.setImageResource(R.drawable.ic_fiber_new_black_24dp);

                break;
            case 2:
                history_image.setImageResource(R.drawable.ic_person_add_black_24dp);
                break;
            default:
                break;
        }
    }

    public void setData(Object c, Context context, boolean last) {
        HistoryClass history = (HistoryClass) c;
        String sourceString = "<b>" + history.getWho() + "</b> " + history.getWhat();
        history_text_view.setText(Html.fromHtml(sourceString));
        switch (history.getType()) {
            case 0:
                history_image.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
                break;
            case 1:
                history_image.setImageResource(R.drawable.ic_fiber_new_black_24dp);

                break;
            case 2:
                history_image.setImageResource(R.drawable.ic_person_add_black_24dp);
                break;
            default:
                break;
        }
        Date date = new Date(history.getWhen());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        history_time.setText(time);
        history_image.post(new Runnable() {
            @Override
            public void run() {
                history_image. setColorFilter(
                        Aesthetic.get().textColorSecondary().take(1).blockingFirst()
                );
            }
        });

        if (last) divider_history.setVisibility(View.GONE);
    }
}
