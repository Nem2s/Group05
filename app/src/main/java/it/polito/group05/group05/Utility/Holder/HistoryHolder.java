package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.polito.group05.group05.HistoryFragment;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.HistoryClass;

/**
 * Created by andre on 24-May-17.
 */

public class HistoryHolder extends GeneralHolder  {

    TextView history_text_view;
    TextView when_text_view;

    public HistoryHolder(View itemView) {
        super(itemView);
        this.history_text_view = (TextView) itemView.findViewById(R.id.history_text_view);
        this.when_text_view = (TextView) itemView.findViewById(R.id.when_text_view);
    }

    @Override
    public void setData(Object c, Context context) {

        HistoryClass history = (HistoryClass) c;
        when_text_view.setText(history.getWhen());
        String sourceString = "<b>" + history.getWho() + "</b> " + history.getWhat();
        history_text_view.setText(Html.fromHtml(sourceString));
    }

  /*  public void setData(Object c, Object previous, Context context) {

        HistoryFragment.historyclass history = (HistoryFragment.historyclass) c;
        HistoryFragment.historyclass previoushistory = (HistoryFragment.historyclass) previous;

        when_text_view.setText(((HistoryFragment.historyclass) c).getWhen());
        String sourceString = "<b>" + ((HistoryFragment.historyclass) c).getWho() + "</b> " + ((HistoryFragment.historyclass) c).getWhat();
        history_text_view.setText(Html.fromHtml(sourceString));
    }*/
}
