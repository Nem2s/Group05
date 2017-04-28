package it.polito.group05.group05.Utility;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;

/**
 * Created by Marco on 03/04/2017.
 */

public class ColorUtils {

    public static int returnDarkerColor(int color){
        float ratio = 1.0f - 0.2f;
        int a = (color >> 24) & 0xFF;
        int r = (int) (((color >> 16) & 0xFF) * ratio);
        int g = (int) (((color >> 8) & 0xFF) * ratio);
        int b = (int) ((color & 0xFF) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public enum type {DOMINANT, VIBRANT, MUTED};
    public enum bright {LIGHT, DARK, NORMAL};
    public static Context context;

    public void setContext(Context context) {
        this.context = context;
    }


    private static class PaletteUtils {
        private Bitmap image;
        private List<View> elements;
        private Palette.Swatch swatch;
        private type type;
        private bright bright;
        private Context context;

        private int starterColor;
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

        public int getStarterColor() {
            return starterColor;
        }

        public void setStarterColor(int starterColor) {
            this.starterColor = starterColor;
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        public void build() {
            if(image == null)
                return;
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
                        if(anim) {
                            ValueAnimator animator;
                            if(starterColor != 0)
                                animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(),context.getResources().getColor(R.color.colorPrimaryText));
                            animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(), swatch.getBodyTextColor());
                            animator.setDuration(1000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    ((TextView) view).setTextColor((Integer) valueAnimator.getAnimatedValue());
                                }
                            });
                            animator.start();
                        }else
                            ((TextView) view).setTextColor(swatch.getBodyTextColor());
                    }
                    else if(view instanceof Toolbar) {
                        if(anim) {
                            ValueAnimator animator;
                            if(starterColor != 0) {
                                animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(), view.getSolidColor());
                            }else
                                animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(), swatch.getRgb());
                            animator.setDuration(1000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                                    if(starterColor != 0)
                                        ((Toolbar) view).setTitleTextColor(Color.WHITE);
                                    else
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
                            ValueAnimator animator;
                            if(starterColor != 0) {
                                animator = ValueAnimator.ofObject(new ArgbEvaluator(), swatch.getRgb(), starterColor);
                            }else
                                animator = ValueAnimator.ofObject(new ArgbEvaluator(), view.getSolidColor(), swatch.getRgb());
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

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
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

        public PaletteBuilder comeBack(int color) {
            p.setStarterColor(color);
            return this;
        }

        public PaletteBuilder setContext(Context context) {
            p.context = context;
            return this;
        }

    }
}
