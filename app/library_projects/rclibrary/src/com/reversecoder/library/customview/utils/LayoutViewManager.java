package com.reversecoder.library.customview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reversecoder.library.customview.event.OnSingleClickListener;

/**
 * Created by Rashed on 7/30/2016.
 */
public class LayoutViewManager {

    private final Activity activity;
    private View view;

    public LayoutViewManager(Activity activity) {
        this.activity = activity;
    }

    public LayoutViewManager id(int viewId) {
        view = activity.findViewById(viewId);
        return this;
    }

    public View view(int viewId) {
        view = activity.findViewById(viewId);
        return view;
    }

    public View getView() {
        if (view != null) {
            return view;
        }
        return null;
    }

    public LayoutViewManager image(int resId) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
        }
        return this;
    }

    public LayoutViewManager visible() {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public LayoutViewManager gone() {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public LayoutViewManager invisible() {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public LayoutViewManager setOnClickListener(View.OnClickListener onClickListener) {
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        return this;
    }

    public LayoutViewManager setOnSingleClickListener(OnSingleClickListener onSingleClickListener) {
        if (view != null) {
            view.setOnClickListener(onSingleClickListener);
        }
        return this;
    }

    public LayoutViewManager setText(CharSequence text) {
        if (view != null && view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    public LayoutViewManager setVisibility(int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
        return this;
    }

    private void size(boolean width, int n, boolean dip) {
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (n > 0 && dip) {
                n = dip2pixel(activity, n);
            }
            if (width) {
                lp.width = n;
            } else {
                lp.height = n;
            }
            view.setLayoutParams(lp);
        }
    }

    public void height(int height, boolean dip) {
        size(false, height, dip);
    }

    public int dip2pixel(Context context, float n) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n, context.getResources().getDisplayMetrics());
        return value;
    }

    public float pixel2dip(Context context, float n) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = n / (metrics.densityDpi / 160f);
        return dp;
    }
}
