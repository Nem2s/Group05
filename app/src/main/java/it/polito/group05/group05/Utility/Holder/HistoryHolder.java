package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.polito.group05.group05.HistoryFragment;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.HistoryClass;

/**
 * Created by andre on 24-May-17.
 */

public class HistoryHolder extends GeneralHolder  {

    TextView history_text_view;
    TextView when_text_view;
    LinearLayout ll_history;
    RecyclerView internal_rv;
    RecyclerView.Adapter internal_adapter;

    public HistoryHolder(View itemView) {
        super(itemView);
        this.when_text_view = (TextView) itemView.findViewById(R.id.when_text_view);
        this.internal_rv = (RecyclerView) itemView.findViewById(R.id.internal_history_rv);
    }



    @Override
    public void setData(Object c, final Context context) {

        final List<HistoryClass> history = (List<HistoryClass>) c;
        final LinearLayoutManager ll=  new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true);
        internal_rv.setLayoutManager(ll);
        ll.setStackFromEnd(true);
        internal_adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View rootView = LayoutInflater.from(context).inflate(R.layout.history_rec_element,parent,false);
                GeneralHolder holder = new InternalHistoryHolder(rootView);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((GeneralHolder)holder).setData(history.get(position),context);
            }

            @Override
            public int getItemCount() {
                return history.size();
            }
        };
        internal_rv.setAdapter(internal_adapter);

        when_text_view.setText(history.get(0).getWhen());
        if(history.get(0).getWhen().equals("oggi")) {
            GradientDrawable bgShape = (GradientDrawable) internal_rv.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.expenseTabColor));
        }
        else
        {
            GradientDrawable bgShape = (GradientDrawable) internal_rv.getBackground();
            bgShape.setColor(Color.parseColor("#ffffff"));
        }
    }

  /*  public void setData(Object c, Object previous, Context context) {

        HistoryFragment.historyclass history = (HistoryFragment.historyclass) c;
        HistoryFragment.historyclass previoushistory = (HistoryFragment.historyclass) previous;

        when_text_view.setText(((HistoryFragment.historyclass) c).getWhen());
        String sourceString = "<b>" + ((HistoryFragment.historyclass) c).getWho() + "</b> " + ((HistoryFragment.historyclass) c).getWhat();
        history_text_view.setText(Html.fromHtml(sourceString));
    }*/
}
