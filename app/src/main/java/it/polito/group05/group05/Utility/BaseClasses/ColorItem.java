package it.polito.group05.group05.Utility.BaseClasses;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;

/**
 * Created by Marco on 29/05/2017.
 */

public class ColorItem extends AbstractItem<ColorItem, ColorItem.ViewHolder> {

    int primaryColor;
    int accentColor;
    String themeName;


    public ColorItem(int primaryColor, int accentColor, String themeName) {
        this.primaryColor = primaryColor;
        this.accentColor = accentColor;
        this.themeName = themeName;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
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
        return R.layout.color_item_sample;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        ((GradientDrawable)holder.color_accent.getBackground()).setColor(accentColor);
        ((GradientDrawable)holder.color_primary.getBackground()).setColor(primaryColor);
        holder.theme_name.setText(themeName);
    }



    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView theme_name;
        protected View color_primary;
        protected View color_accent;

        public ViewHolder(View view) {
            super(view);
            this.theme_name = (TextView)view.findViewById(R.id.theme_name);
            this.color_primary = (View)view.findViewById(R.id.primary_color);
            this.color_accent = (View)view.findViewById(R.id.accent_color);

        }
    }
}
