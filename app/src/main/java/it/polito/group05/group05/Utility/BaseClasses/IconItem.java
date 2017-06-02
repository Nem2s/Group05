package it.polito.group05.group05.Utility.BaseClasses;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.HelperClasses.AssetUriLoader;

/**
 * Created by Marco on 01/06/2017.
 */

public class IconItem extends AbstractItem<IconItem, IconItem.ViewHolder> {

    String iconUri;
    char header;
    public static Context context;
    public IconItem(String uri) {
        this.iconUri = uri;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public Context getContext() {
        return context;
    }

    public static void setContext(Context c) {
        context = c;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_icon_layout;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Glide.with(context)
                .using(new AssetUriLoader(context))
                .load(Uri.parse(iconUri))
                .asBitmap()
                .into(holder.img);
    }

    public IconItem withHeader(char c) {
        this.header = c;
        return this;
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView img;

        public ViewHolder(View view) {
            super(view);
            this.img = (CircleImageView)view.findViewById(R.id.cv_icon);

        }
    }
}
