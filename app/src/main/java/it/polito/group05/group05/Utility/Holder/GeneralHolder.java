package it.polito.group05.group05.Utility.Holder;

/**
 * Created by user on 03/05/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
public abstract class GeneralHolder extends RecyclerView.ViewHolder {
    public GeneralHolder(View itemView) {
        super(itemView);
    }
    abstract public void setData(Object c, Context context);
}