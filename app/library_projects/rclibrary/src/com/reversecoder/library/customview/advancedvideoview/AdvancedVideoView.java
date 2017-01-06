package com.reversecoder.library.customview.advancedvideoview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.reversecoder.library.R;

import java.io.IOException;

/**
 * This is a custom videoview. Here any scale types are added. Anyone can set different scale types from layout or via programmatically.
 * The main advantage of this videoview is that it removes black screen from each video loop.
 * Also play pause and resume function is added in this videoview
 *
 * usages:
 * ------
 * Uri mSource = Uri.parse(filePath);
 * setDataSource(mSource);
 * setLooping(true);
 * setScalableType(ScalableType.FIT_CENTER);
 * -----
 *
 * @author Md. Rashadul Alam
 */
public class AdvancedVideoView extends TextureView {

    private MediaPlayer mMediaPlayer;
    private Uri mSource;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private MediaPlayer.OnErrorListener mErrorListener;
    private MediaPlayer.OnInfoListener mOnInfoListener;
    private boolean isLooping = false;
    private int playedLength = 0;
    private MediaPlayerListener mListener;
    private ScalableType mScalableType = ScalableType.FIT_XY;
    private State mState = State.UNINITIALIZED;
    private boolean mIsDataSourceSet;
    private boolean mIsViewAvailable;
    private boolean mIsVideoPrepared;
    private boolean mIsPlayCalled;

    public AdvancedVideoView(Context context) {
        this(context, null);
        initAdvancedVideoView(context, null);
    }

    public AdvancedVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initAdvancedVideoView(context, attrs);
    }

    public AdvancedVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAdvancedVideoView(context, attrs);
    }

    private void initAdvancedVideoView(Context context, AttributeSet attrs) {
        initPlayer();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.scaleStyle, 0, 0);
            if (a != null) {
                int scaleType = a.getInt(R.styleable.scaleStyle_scalableType, ScalableType.FIT_XY.ordinal());
                a.recycle();
                setScalableType(ScalableType.values()[scaleType]);
            }
        } else {
            setScalableType(ScalableType.FIT_XY);
        }
        setSurfaceTextureListener(surfaceTextureListener);
    }

    private void initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            reset();
        }
        mIsVideoPrepared = false;
        mIsPlayCalled = false;
        setState(State.UNINITIALIZED);
    }

    public Uri getDataSource() {
        return mSource;
    }

    public void setDataSource(Uri source) {
        mSource = source;
        initPlayer();
        try {
            mMediaPlayer.setDataSource(getContext(), mSource);
            mIsDataSourceSet = true;
            prepare();
        } catch (IOException e) {
            Log.d("AdvancedVideoView", e.getMessage());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // release resources on detach
        if (mMediaPlayer != null) {
            release();
            mMediaPlayer = null;
        }
        super.onDetachedFromWindow();
    }


    /**
     * Play or resume video. Video will be played as soon as view is available and media player is
     * prepared.
     * <p>
     * If video is stopped or ended and play() method was called, video will start over.
     */
    public void play() {
        if (!mIsDataSourceSet) {
            Log.d("AdvancedVideoView", "play() was called but data source was not set.");
            return;
        }

        mIsPlayCalled = true;

        if (!mIsVideoPrepared) {
            Log.d("AdvancedVideoView", "play() was called but video is not prepared yet, waiting.");
            return;
        }

        if (!mIsViewAvailable) {
            Log.d("AdvancedVideoView", "play() was called but view is not available yet, waiting.");
            return;
        }

        if (getState() == State.PLAY) {
            Log.d("AdvancedVideoView", "play() was called but video is already playing.");
            return;
        }

        if (getState() == State.PAUSE) {
            Log.d("AdvancedVideoView", "play() was called but video is paused, resuming.");
            setState(State.PLAY);
            seekTo(playedLength);
            start();
            return;
        }

        if (getState() == State.END || getState() == State.STOP) {
            Log.d("AdvancedVideoView", "play() was called but video already ended, starting over.");
            setState(State.PLAY);
            seekTo(0);
            start();
            return;
        }

        setState(State.PLAY);
        start();
    }

    /**
     * Pause video. If video is already paused, stopped or ended nothing will happen.
     */
    public void pause() {
        if (getState() == State.PAUSE) {
            Log.d("AdvancedVideoView", "pause() was called but video already paused.");
            return;
        }

        if (getState() == State.STOP) {
            Log.d("AdvancedVideoView", "pause() was called but video already stopped.");
            return;
        }

        if (getState() == State.END) {
            Log.d("AdvancedVideoView", "pause() was called but video already ended.");
            return;
        }

        setState(State.PAUSE);
        if (isPlaying()) {
            mMediaPlayer.pause();
            playedLength = mMediaPlayer.getCurrentPosition();
        }
    }

    /**
     * Stop video (pause and seek to beginning). If video is already stopped or ended nothing will
     * happen.
     */
    public void stop() {
        if (getState() == State.STOP) {
            Log.d("AdvancedVideoView", "stop() was called but video already stopped.");
            return;
        }

        if (getState() == State.END) {
            Log.d("AdvancedVideoView", "stop() was called but video already ended.");
            return;
        }

        setState(State.STOP);
        if (isPlaying()) {
            mMediaPlayer.pause();
            seekTo(0);
        }
    }

    private void scaleVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0) {
            return;
        }

        Size viewSize = new Size(getWidth(), getHeight());
        Size videoSize = new Size(videoWidth, videoHeight);
        ScaleManager scaleManager = new ScaleManager(viewSize, videoSize);
        Matrix matrix = scaleManager.getScaleMatrix(mScalableType);
        if (matrix != null) {
            setTransform(matrix);
        }
    }

    private void prepare() {
        try {
            setLooping(isLooping);
            setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            setOnCompletionListener(mCompletionListener);

            setOnBufferingUpdateListener(mBufferingUpdateListener);
            setOnErrorListener(mErrorListener);
            setOnInfoListener(mOnInfoListener);

            // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
            // creating MediaPlayer
            prepareAsync();

            // Play video when the media source is ready for playback.
            setOnPreparedListener(onPreparedListener);

        } catch (IllegalArgumentException e) {
            Log.d("AdvancedVideoView", e.getMessage());
        } catch (SecurityException e) {
            Log.d("AdvancedVideoView", e.getMessage());
        } catch (IllegalStateException e) {
            Log.d("AdvancedVideoView", e.toString());
        }
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getPlayedLength() {
        return playedLength;
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
        mMediaPlayer.setLooping(looping);
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void prepareAsync() {
        mMediaPlayer.prepareAsync();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void destroyPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            release();
            mMediaPlayer = null;
        }
    }

    public void reset() {
        mMediaPlayer.reset();
    }

    public void release() {
        mMediaPlayer.release();
    }

    /**
     * Enumeration for this videoview
     */
    public enum PivotPoint {
        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM
    }

    public enum State {
        UNINITIALIZED, PLAY, STOP, PAUSE, END
    }

    public enum ScalableType {
        NONE,

        FIT_XY,
        FIT_START,
        FIT_CENTER,
        FIT_END,

        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM,

        LEFT_TOP_CROP,
        LEFT_CENTER_CROP,
        LEFT_BOTTOM_CROP,
        CENTER_TOP_CROP,
        CENTER_CROP,
        CENTER_BOTTOM_CROP,
        RIGHT_TOP_CROP,
        RIGHT_CENTER_CROP,
        RIGHT_BOTTOM_CROP,

        START_INSIDE,
        CENTER_INSIDE,
        END_INSIDE
    }


    public void setScalableType(ScalableType scalableType) {
        mScalableType = scalableType;
    }

    public void setState(State mState) {
        this.mState = mState;
    }

    public State getState() {
        return mState;
    }

    public ScalableType getScalableType() {
        return mScalableType;
    }

    /**
     * Interfaces for videoview
     */
    public interface MediaPlayerListener {
        public void onVideoPrepared();

        public void onVideoEnd();
    }

    /**
     * Set all listener
     */

    SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Surface surface = new Surface(surfaceTexture);
            mMediaPlayer.setSurface(surface);
            mIsViewAvailable = true;
            if (mIsDataSourceSet && mIsPlayCalled && mIsVideoPrepared) {
                Log.d("AdvancedVideoView", "View is available and play() was called.");
                play();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            scaleVideoSize(width, height);
        }
    };

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            setState(State.END);
            Log.d("AdvancedVideoView", "Video has ended.");
            if (mListener != null) {
                mListener.onVideoEnd();
            }
            Log.d("AdvancedVideoView", "Video looping " + isLooping());
            if (isLooping()) {
                setState(State.PLAY);
                play();
            }
        }
    };

    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mIsVideoPrepared = true;
            if (mIsPlayCalled && mIsViewAvailable) {
                Log.d("AdvancedVideoView", "Player is prepared and play() was called.");
                play();
            }
            if (mListener != null) {
                mListener.onVideoPrepared();
            }
        }
    };

    public void setMediaPlayerListener(MediaPlayerListener listener) {
        mListener = listener;
    }


    public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener) {
        mMediaPlayer.setOnVideoSizeChangedListener(listener);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        mMediaPlayer.setOnPreparedListener(listener);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mMediaPlayer.setOnCompletionListener(listener);
    }

    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener) {
        mBufferingUpdateListener = listener;
        mMediaPlayer.setOnBufferingUpdateListener(listener);
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        mErrorListener = listener;
        mMediaPlayer.setOnErrorListener(listener);
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener listener) {
        mOnInfoListener = listener;
        mMediaPlayer.setOnInfoListener(listener);
    }

    /**
     * Model class for video size
     */
    public class Size {

        private int mWidth;
        private int mHeight;

        public Size(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }

    /**
     * Helper class for video scaling
     */
    public class ScaleManager {

        private Size mViewSize;
        private Size mVideoSize;

        public ScaleManager(Size viewSize, Size videoSize) {
            mViewSize = viewSize;
            mVideoSize = videoSize;
        }

        public Matrix getScaleMatrix(ScalableType scalableType) {
            switch (scalableType) {
                case NONE:
                    return getNoScale();

                case FIT_XY:
                    return fitXY();
                case FIT_CENTER:
                    return fitCenter();
                case FIT_START:
                    return fitStart();
                case FIT_END:
                    return fitEnd();

                case LEFT_TOP:
                    return getOriginalScale(PivotPoint.LEFT_TOP);
                case LEFT_CENTER:
                    return getOriginalScale(PivotPoint.LEFT_CENTER);
                case LEFT_BOTTOM:
                    return getOriginalScale(PivotPoint.LEFT_BOTTOM);
                case CENTER_TOP:
                    return getOriginalScale(PivotPoint.CENTER_TOP);
                case CENTER:
                    return getOriginalScale(PivotPoint.CENTER);
                case CENTER_BOTTOM:
                    return getOriginalScale(PivotPoint.CENTER_BOTTOM);
                case RIGHT_TOP:
                    return getOriginalScale(PivotPoint.RIGHT_TOP);
                case RIGHT_CENTER:
                    return getOriginalScale(PivotPoint.RIGHT_CENTER);
                case RIGHT_BOTTOM:
                    return getOriginalScale(PivotPoint.RIGHT_BOTTOM);

                case LEFT_TOP_CROP:
                    return getCropScale(PivotPoint.LEFT_TOP);
                case LEFT_CENTER_CROP:
                    return getCropScale(PivotPoint.LEFT_CENTER);
                case LEFT_BOTTOM_CROP:
                    return getCropScale(PivotPoint.LEFT_BOTTOM);
                case CENTER_TOP_CROP:
                    return getCropScale(PivotPoint.CENTER_TOP);
                case CENTER_CROP:
                    return getCropScale(PivotPoint.CENTER);
                case CENTER_BOTTOM_CROP:
                    return getCropScale(PivotPoint.CENTER_BOTTOM);
                case RIGHT_TOP_CROP:
                    return getCropScale(PivotPoint.RIGHT_TOP);
                case RIGHT_CENTER_CROP:
                    return getCropScale(PivotPoint.RIGHT_CENTER);
                case RIGHT_BOTTOM_CROP:
                    return getCropScale(PivotPoint.RIGHT_BOTTOM);

                case START_INSIDE:
                    return startInside();
                case CENTER_INSIDE:
                    return centerInside();
                case END_INSIDE:
                    return endInside();

                default:
                    return null;
            }
        }

        private Matrix getMatrix(float sx, float sy, float px, float py) {
            Matrix matrix = new Matrix();
            matrix.setScale(sx, sy, px, py);
            return matrix;
        }

        private Matrix getMatrix(float sx, float sy, PivotPoint pivotPoint) {
            switch (pivotPoint) {
                case LEFT_TOP:
                    return getMatrix(sx, sy, 0, 0);
                case LEFT_CENTER:
                    return getMatrix(sx, sy, 0, mViewSize.getHeight() / 2f);
                case LEFT_BOTTOM:
                    return getMatrix(sx, sy, 0, mViewSize.getHeight());
                case CENTER_TOP:
                    return getMatrix(sx, sy, mViewSize.getWidth() / 2f, 0);
                case CENTER:
                    return getMatrix(sx, sy, mViewSize.getWidth() / 2f, mViewSize.getHeight() / 2f);
                case CENTER_BOTTOM:
                    return getMatrix(sx, sy, mViewSize.getWidth() / 2f, mViewSize.getHeight());
                case RIGHT_TOP:
                    return getMatrix(sx, sy, mViewSize.getWidth(), 0);
                case RIGHT_CENTER:
                    return getMatrix(sx, sy, mViewSize.getWidth(), mViewSize.getHeight() / 2f);
                case RIGHT_BOTTOM:
                    return getMatrix(sx, sy, mViewSize.getWidth(), mViewSize.getHeight());
                default:
                    throw new IllegalArgumentException("Illegal PivotPoint");
            }
        }

        private Matrix getNoScale() {
            float sx = mVideoSize.getWidth() / (float) mViewSize.getWidth();
            float sy = mVideoSize.getHeight() / (float) mViewSize.getHeight();
            return getMatrix(sx, sy, PivotPoint.LEFT_TOP);
        }

        private Matrix getFitScale(PivotPoint pivotPoint) {
            float sx = (float) mViewSize.getWidth() / mVideoSize.getWidth();
            float sy = (float) mViewSize.getHeight() / mVideoSize.getHeight();
            float minScale = Math.min(sx, sy);
            sx = minScale / sx;
            sy = minScale / sy;
            return getMatrix(sx, sy, pivotPoint);
        }

        private Matrix fitXY() {
            return getMatrix(1, 1, PivotPoint.LEFT_TOP);
        }

        private Matrix fitStart() {
            return getFitScale(PivotPoint.LEFT_TOP);
        }

        private Matrix fitCenter() {
            return getFitScale(PivotPoint.CENTER);
        }

        private Matrix fitEnd() {
            return getFitScale(PivotPoint.RIGHT_BOTTOM);
        }

        private Matrix getOriginalScale(PivotPoint pivotPoint) {
            float sx = mVideoSize.getWidth() / (float) mViewSize.getWidth();
            float sy = mVideoSize.getHeight() / (float) mViewSize.getHeight();
            return getMatrix(sx, sy, pivotPoint);
        }

        private Matrix getCropScale(PivotPoint pivotPoint) {
            float sx = (float) mViewSize.getWidth() / mVideoSize.getWidth();
            float sy = (float) mViewSize.getHeight() / mVideoSize.getHeight();
            float maxScale = Math.max(sx, sy);
            sx = maxScale / sx;
            sy = maxScale / sy;
            return getMatrix(sx, sy, pivotPoint);
        }

        private Matrix startInside() {
            if (mVideoSize.getHeight() <= mViewSize.getWidth()
                    && mVideoSize.getHeight() <= mViewSize.getHeight()) {
                // video is smaller than view size
                return getOriginalScale(PivotPoint.LEFT_TOP);
            } else {
                // either of width or height of the video is larger than view size
                return fitStart();
            }
        }

        private Matrix centerInside() {
            if (mVideoSize.getHeight() <= mViewSize.getWidth()
                    && mVideoSize.getHeight() <= mViewSize.getHeight()) {
                // video is smaller than view size
                return getOriginalScale(PivotPoint.CENTER);
            } else {
                // either of width or height of the video is larger than view size
                return fitCenter();
            }
        }

        private Matrix endInside() {
            if (mVideoSize.getHeight() <= mViewSize.getWidth()
                    && mVideoSize.getHeight() <= mViewSize.getHeight()) {
                // video is smaller than view size
                return getOriginalScale(PivotPoint.RIGHT_BOTTOM);
            } else {
                // either of width or height of the video is larger than view size
                return fitEnd();
            }
        }
    }
}