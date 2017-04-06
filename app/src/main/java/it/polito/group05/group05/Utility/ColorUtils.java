package it.polito.group05.group05.Utility;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 03/04/2017.
 */

public class ColorUtils {

    public enum type {DOMINANT, VIBRANT, MUTED};
    public enum bright {LIGHT, DARK, NORMAL};

    private static class PaletteUtils {
        private Bitmap image;
        private List<View> elements;
        private Palette.Swatch swatch;
        private type type;
        private bright bright;
        public boolean anim;

        public PaletteUtils() {
            elements = new ArrayList<>();
            anim = false;

        }

        public void add(View view) {
            elements.add(view);
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public List<View> getElements() {
            return elements;
        }

        public void setElements(List<View> elements) {
            this.elements = elements;
        }

        public ColorUtils.type getType() {
            return type;
        }

        public void setType(ColorUtils.type type) {
            this.type = type;
        }

        public void build() {

            switch (bright) {
                case LIGHT:
                    if(type != null) {
                        if(type.equals(ColorUtils.type.MUTED))
                            swatch = Palette.from(image).generate().getLightMutedSwatch();
                        else if(type.equals(ColorUtils.type.VIBRANT))
                            swatch = Palette.from(image).generate().getLightVibrantSwatch();
                        else {
                            swatch = Palette.from(image).generate().getDominantSwatch();
                        }
                    }if(swatch == null){
                        swatch = Palette.from(image).generate().getLightVibrantSwatch();
                        if (swatch == null)
                            swatch = Palette.from(image).generate().getLightMutedSwatch();
                        if (swatch == null)
                            swatch = Palette.from(image).generate().getDominantSwatch();
                    }
                    break;
                case DARK:
                    if(type != null) {
                        if (type.equals(ColorUtils.type.MUTED))
                            swatch = Palette.from(image).generate().getDarkMutedSwatch();
                        else if (type.equals(ColorUtils.type.VIBRANT))
                            swatch = Palette.from(image).generate().getDarkVibrantSwatch();
                        else {
                            swatch = Palette.from(image).generate().getDominantSwatch();
                        }
                    } if(swatch == null){
                        swatch = Palette.from(image).generate().getDarkVibrantSwatch();
                        if (swatch == null)
                            swatch = Palette.from(image).generate().getDarkMutedSwatch();
                        if (swatch == null)
                            swatch = Palette.from(image).generate().getDominantSwatch();
                    }
                    break;
                case NORMAL:
                    if(type != null) {
                        if(type.equals(ColorUtils.type.MUTED))
                            swatch = Palette.from(image).generate().getMutedSwatch();
                        else if(type.equals(ColorUtils.type.VIBRANT))
                            swatch = Palette.from(image).generate().getVibrantSwatch();
                        else {
                            swatch = Palette.from(image).generate().getDominantSwatch();
                        }
                    } if(swatch == null){
                        swatch = Palette.from(image).generate().getVibrantSwatch();
                        if (swatch == null)
                            swatch = Palette.from(image).generate().getMutedSwatch();
                        if (swatch == null)
                            swatch = Palette.from(image).generate().getDominantSwatch();
                    }
                    break;
            }

            if(swatch != null) {
                for (final View view : elements) {

                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(swatch.getBodyTextColor());
                    }
                    else if(view instanceof Toolbar) {
                        if(anim) {
                            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(), swatch.getRgb());
                            animator.setDuration(1000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                                    ((Toolbar) view).setTitleTextColor(swatch.getBodyTextColor());
                                }
                            });
                            animator.start();
                        } else {
                            view.setBackgroundColor(swatch.getRgb());
                            ((Toolbar) view).setTitleTextColor(swatch.getBodyTextColor());
                        }
                    }
                    else if (view != null) {
                        if(anim) {
                            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(), swatch.getRgb());
                            animator.setDuration(1000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                                }
                            });
                            animator.start();

                        } else {
                            view.setBackgroundColor(swatch.getRgb());
                        }
                    }
                }
            }
        }
    }

    public static class PaletteBuilder {
        private PaletteUtils p;

        public PaletteBuilder() {
            p = new PaletteUtils();
        }

        public PaletteBuilder load(Bitmap image) {
            p.setImage(image);
            return this;
        }
        public PaletteBuilder set(View view) {
            p.add(view);
            return this;
        }

        public void build() {
            p.build();
        }

        public PaletteBuilder method(type type) {
            p.setType(type);
            return this;
        }
        public PaletteBuilder brighter(bright bright) {
            p.bright = bright;
            return this;
        }
        public PaletteBuilder anim(){
            p.anim = true;
            return this;
        }

    }
}
