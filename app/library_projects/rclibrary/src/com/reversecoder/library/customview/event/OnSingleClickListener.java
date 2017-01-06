package com.reversecoder.library.customview.event;

/**
 * Created by Rashed on 5/10/2016.
 */

import android.util.Log;
import android.view.View;

/**
 * Implementation of {@link OnClickListener} that ignores subsequent clicks that happen too quickly after the first one.<br/>
 * To use this class, implement {@link #onSingleClick(View)} instead of {@link OnClickListener#onClick(View)}.
 */
public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final String TAG = OnSingleClickListener.class.getSimpleName();

    private static final long MIN_DELAY_MS = 3000;

    private long mLastClickTime;

    @Override
    public final void onClick(View v) {
        long lastClickTime = mLastClickTime;
        long now = System.currentTimeMillis();
        mLastClickTime = now;
        if (now - lastClickTime < MIN_DELAY_MS) {
            // Too fast: ignore
            Log.d(TAG, "onClick Clicked too quickly: ignored");
        } else {
            // Register the click
            onSingleClick(v);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    public abstract void onSingleClick(View v);

    /**
     * Wraps an {@link OnClickListener} into an {@link OnSingleClickListener}.<br/>
     * The argument's {@link OnClickListener#onClick(View)} method will be called when a single click is registered.
     *
     * @param onClickListener The listener to wrap.
     * @return the wrapped listener.
     */
    public static View.OnClickListener wrap(final View.OnClickListener onClickListener) {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onClickListener.onClick(v);
            }
        };
    }
}