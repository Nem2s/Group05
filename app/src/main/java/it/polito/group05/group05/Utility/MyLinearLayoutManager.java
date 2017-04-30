package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Marco on 14/04/2017.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
