package it.polito.group05.group05.Utility.Adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.Holder.GeneralHolder;
import it.polito.group05.group05.Utility.Interfaces.Namable;

/**
 * Created by user on 03/05/2017.
 */

public class FirebaseAdapterExtension extends FirebaseRecyclerAdapter implements Filterable {
Context c;
List<Object> data;
    public FirebaseAdapterExtension(Context c,Class mViewClass,
                                    int mModelLayout, Class mViewHolderClass, Query ref, List<Object> data){
        super(mViewClass,mModelLayout,mViewHolderClass,ref);
        this.c = c;
        this.data = data;

    }
    @Override
    public Filter getFilter() {

         return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Object> results = new ArrayList<>();
               /* if (data == null)
                    data = invited;*/
                if (constraint != null) {
                    if (data != null & data.size() > 0) {
                        for (final Object u : data) {
                            if(!(u instanceof Namable)) continue;
                            if (((Namable) u).getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(u);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               // invited = (ArrayList<Object>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {

        data.add(position, model);
        if(!(viewHolder instanceof GeneralHolder)) return;
        ((GeneralHolder)viewHolder).setData(model,c);

    }
}
