package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.HistoryClass;

/**
 * Created by andre on 25-May-17.
 */

public class InternalHistoryHolder extends GeneralHolder{

    TextView history_text_view;
    LinearLayout ll_history;

    public InternalHistoryHolder(View itemView) {
        super(itemView);
        this.history_text_view = (TextView) itemView.findViewById(R.id.history_text_view);
        this.ll_history = (LinearLayout) itemView.findViewById(R.id.ll_history);
    }

    @Override
    public void setData(Object c, Context context) {
        HistoryClass history = (HistoryClass) c;
        String sourceString = "<b>" + history.getWho() + "</b> " + history.getWhat();
        history_text_view.setText(Html.fromHtml(sourceString));

    }
}
