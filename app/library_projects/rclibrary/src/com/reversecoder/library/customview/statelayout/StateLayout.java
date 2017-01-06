package com.reversecoder.library.customview.statelayout;

/*
* Basic usage:
* -----------
* StateLayout mStateLayout = (StateLayout) findViewById(R.id.sl_layout_state);
*  mStateLayout.setStateLayoutView(StateLayout.VIEW_STATE.VIEW_LOADING);
*
* Customize:
* ---------
*  mStateLayout.setLoadingMessage("It's loading.");
*
*  mStateLayout.setContentView(R.layout.state_layout_content);
*
*  mStateLayout.setEmptyImage(R.drawable.ic_empty);
* */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.reversecoder.library.R;


public class StateLayout extends FrameLayout {
    public static final String TAG = "STATE_LAYOUT";
    public static boolean DEBUG = false;
    private ViewGroup mContentView;
    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;
    private ViewGroup mErrorView;
    private int mContentViewId;
    private int mLoadingViewId;
    private int mEmptyViewId;
    private int mErrorViewId;
    private ViewGroup mStateLayout;
    private VIEW_STATE mDefViewState = VIEW_STATE.DEFAULT;

    private boolean mClipToPadding;
    private int mPadding;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;

    private boolean isDefaultContentView = false;
    private boolean isDefaultEmptyView = false;
    private boolean isDefaultLoadingView = false;
    private boolean isDefaultErrorView = false;


    public enum VIEW_STATE {

        DEFAULT(5), VIEW_CONTENT(1), VIEW_EMPTY(2), VIEW_ERROR(3), VIEW_LOADING(4);
        private int id;

        VIEW_STATE(int id) {
            this.id = id;
        }

        public int getViewStateValue() {
            return id;
        }

        public VIEW_STATE getViewState() {
            VIEW_STATE mViewState = null;
            switch (id) {
                case 5:
                    mViewState = DEFAULT;
                case 1:
                    mViewState = VIEW_CONTENT;
                    break;
                case 2:
                    mViewState = VIEW_EMPTY;
                    break;
                case 3:
                    mViewState = VIEW_ERROR;
                    break;
                case 4:
                    mViewState = VIEW_LOADING;
                    break;
            }
            return mViewState;
        }

        public VIEW_STATE setViewState(int id) {
            this.id = id;
            return this;
        }
    }

    public ViewGroup getStateLayout() {
        return mStateLayout;
    }

    public ViewGroup getContentView() {
        return mContentView;
    }

    public StateLayout(Context context) {
        super(context);
        initView();
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateLayout);
        try {
            mContentViewId = a.getResourceId(R.styleable.StateLayout_layout_content, 0);
            mEmptyViewId = a.getResourceId(R.styleable.StateLayout_layout_empty, 0);
            mLoadingViewId = a.getResourceId(R.styleable.StateLayout_layout_loading, 0);
            mErrorViewId = a.getResourceId(R.styleable.StateLayout_layout_error, 0);

            mClipToPadding = a.getBoolean(R.styleable.StateLayout_contentClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.StateLayout_contentPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.StateLayout_contentPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.StateLayout_contentPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.StateLayout_contentPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.StateLayout_contentPaddingRight, 0.0f);

            if (a.getInteger(R.styleable.StateLayout_default_view, 0) > 0) {
                mDefViewState.setViewState(a.getInteger(R.styleable.StateLayout_default_view, 0));
                mDefViewState = mDefViewState.getViewState();
            }
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }

        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_state_layout, this);
        mStateLayout = (ViewGroup) v.findViewById(R.id.state_layout);

        mContentView = (ViewGroup) v.findViewById(R.id.content);
        if (mContentViewId != 0) {
            LayoutInflater.from(getContext()).inflate(mContentViewId, mContentView);
            setDefaultContentView(false);
        } else {
            isDefaultContentView = true;
            LayoutInflater.from(getContext()).inflate(R.layout.state_layout_content, mContentView);
            setDefaultContentView(true);
        }
        if (mContentView != null) {
            mContentView.setClipToPadding(mClipToPadding);

            if (mPadding != -1.0f) {
                mContentView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
        }

        mLoadingView = (ViewGroup) v.findViewById(R.id.loading);
        if (mLoadingViewId != 0) {
            LayoutInflater.from(getContext()).inflate(mLoadingViewId, mLoadingView);
            setDefaultLoadingView(false);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.state_layout_loading, mLoadingView);
            setDefaultLoadingView(true);
        }
        mEmptyView = (ViewGroup) v.findViewById(R.id.empty);
        if (mEmptyViewId != 0) {
            LayoutInflater.from(getContext()).inflate(mEmptyViewId, mEmptyView);
            setDefaultEmptyView(false);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.state_layout_empty, mEmptyView);
            setDefaultEmptyView(true);
        }

        mErrorView = (ViewGroup) v.findViewById(R.id.error);
        if (mErrorViewId != 0) {
            LayoutInflater.from(getContext()).inflate(mErrorViewId, mErrorView);
            setDefaultErrorView(false);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.state_layout_error, mErrorView);
            setDefaultErrorView(true);
        }

        setStateLayoutView(mDefViewState);
    }

    public void setContentViewPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setEmptyViewPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mEmptyView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setLoadingViewPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mLoadingView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setErrorViewPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mErrorView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setClipToPadding(boolean isClip) {
        mContentView.setClipToPadding(isClip);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mStateLayout.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setContentView(View contentView) {
        mContentView.removeAllViews();
        mContentView.addView(contentView);
        setDefaultContentView(false);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
        setDefaultEmptyView(false);
    }

    public void setLoadingView(View progressView) {
        mLoadingView.removeAllViews();
        mLoadingView.addView(progressView);
        setDefaultLoadingView(false);
    }

    public void setErrorView(View errorView) {
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
        setDefaultErrorView(false);
    }

    public void setContentView(int contentView) {
        mContentView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(contentView, mContentView);
        setDefaultContentView(false);
    }

    public void setEmptyView(int emptyView) {
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
        setDefaultEmptyView(false);
    }

    public void setLoadingView(int progressView) {
        mLoadingView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mLoadingView);
        setDefaultLoadingView(false);
    }

    public void setErrorView(int errorView) {
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
        setDefaultErrorView(false);
    }

    private void hideAll() {
        mEmptyView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        mContentView.setVisibility(View.INVISIBLE);
    }

    public void setStateLayoutView(VIEW_STATE viewState) {
        switch (viewState) {
            case DEFAULT:
//                setContentView(mContentView);
                showView(VIEW_STATE.DEFAULT);
                break;
            case VIEW_CONTENT:
//                setContentView(mContentView);
                showView(VIEW_STATE.VIEW_CONTENT);
                break;

            case VIEW_EMPTY:
//                setEmptyView(mEmptyView);
                showView(VIEW_STATE.VIEW_EMPTY);
                break;

            case VIEW_ERROR:
//                setErrorView(mErrorView);
                showView(VIEW_STATE.VIEW_ERROR);
                break;

            case VIEW_LOADING:
//                setLoadingView(mLoadingView);
                showView(VIEW_STATE.VIEW_LOADING);
                break;
        }
    }

    private void showView(VIEW_STATE viewState) {
        switch (viewState) {
            case DEFAULT:
                mContentView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                break;
            case VIEW_CONTENT:
                mContentView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                break;
            case VIEW_EMPTY:
                mEmptyView.setVisibility(View.VISIBLE);
                mContentView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                break;
            case VIEW_ERROR:
                mErrorView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mContentView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                break;
            case VIEW_LOADING:
                mLoadingView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mContentView.setVisibility(View.GONE);
                break;
        }
    }


    public void showErrorView() {
        log("showErrorView");
        if (mErrorView.getChildCount() > 0) {
            hideAll();
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            showContentView();
        }

    }

    public void showEmptyView() {
        log("showEmptyView");
        if (mEmptyView.getChildCount() > 0) {
            hideAll();
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            showContentView();
        }
    }


    public void showLoadingView() {
        log("showLoadingView");
        if (mLoadingView.getChildCount() > 0) {
            hideAll();
            mLoadingView.setVisibility(View.VISIBLE);
        } else {
            showContentView();
        }
    }

    public void showContentView() {
        log("showContentView");
        hideAll();
        mContentView.setVisibility(View.VISIBLE);
    }

    public View getErrorView() {
        if (mErrorView.getChildCount() > 0) return mErrorView.getChildAt(0);
        return null;
    }

    public View getLoadingView() {
        if (mLoadingView.getChildCount() > 0) return mLoadingView.getChildAt(0);
        return null;
    }

    public View getEmptyView() {
        if (mEmptyView.getChildCount() > 0) return mEmptyView.getChildAt(0);
        return null;
    }

    private static void log(String content) {
        if (DEBUG) {
            Log.i(TAG, content);
        }
    }

    public boolean isDefaultContentView() {
        return isDefaultContentView;
    }

    public boolean isDefaultEmptyView() {
        return isDefaultEmptyView;
    }

    public boolean isDefaultLoadingView() {
        return isDefaultLoadingView;
    }

    public boolean isDefaultErrorView() {
        return isDefaultErrorView;
    }

    public void setDefaultContentView(boolean defaultContentView) {
        isDefaultContentView = defaultContentView;
    }

    public void setDefaultEmptyView(boolean defaultEmptyView) {
        isDefaultEmptyView = defaultEmptyView;
    }

    public void setDefaultLoadingView(boolean defaultLoadingView) {
        isDefaultLoadingView = defaultLoadingView;
    }

    public void setDefaultErrorView(boolean defaultErrorView) {
        isDefaultErrorView = defaultErrorView;
    }

    public void setLoadingMessage(String loadingMessage) {
        if (isDefaultLoadingView()) {
            TextView tvLoading = (TextView) getLoadingView().findViewById(R.id.tv_loading);
            tvLoading.setText(loadingMessage);
        }
    }

    public void setErrorMessage(String errorMessage) {
        if (isDefaultErrorView()) {
            TextView tvError = (TextView) getErrorView().findViewById(R.id.tv_error);
            tvError.setText(errorMessage);
        }
    }

    public void setEmptyMessage(String emptyMessage) {
        if (isDefaultEmptyView()) {
            TextView tvEmpty = (TextView) getEmptyView().findViewById(R.id.tv_empty);
            tvEmpty.setText(emptyMessage);
        }
    }

    public void setErrorImage(int drawableResID) {
        if (isDefaultErrorView()) {
            ImageView ivError = (ImageView) getErrorView().findViewById(R.id.iv_error);
            ivError.setBackgroundResource(drawableResID);
        }
    }

    public void setEmptyImage(int drawableResID) {
        if (isDefaultEmptyView()) {
            ImageView ivEmpty = (ImageView) getEmptyView().findViewById(R.id.iv_empty);
            ivEmpty.setBackgroundResource(drawableResID);
        }
    }
}
