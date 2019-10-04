/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (C) 2013 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tv.danmaku.ijk.media.player;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Surface;


import java.io.IOException;
import java.lang.ref.WeakReference;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.annotations.AccessedByNative;
import tv.danmaku.ijk.media.player.annotations.CalledByNative;
import tv.danmaku.ijk.media.player.option.AvFormatOption;

/**
 * @author bbcallen
 * <p/>
 * Java wrapper of ffplay.
 */
public final class IjkMediaPlayer implements IjkMediaPlayerConstants {
    private final static String TAG = IjkMediaPlayer.class.getName();

    private ijkPlayerCallBack mijkPlayerCallBack;

    @AccessedByNative
    private long mNativeMediaPlayer;

    @AccessedByNative
    private int mNativeSurfaceTexture;

    @AccessedByNative
    private int mListenerContext;

    private static IjkLibLoader sLocalLibLoader = libName -> System.loadLibrary(libName);

    private static volatile boolean mIsLibLoaded = false;

    public static void loadLibrariesOnce(IjkLibLoader libLoader) {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsLibLoaded) {
                try {
                    libLoader.loadLibrary("kaolafmffmpeg");
                    libLoader.loadLibrary("kaolafmutil");
                    libLoader.loadLibrary("kaolafmsdl");
                    libLoader.loadLibrary("kaolafmplayer");
                    mIsLibLoaded = true;
                } catch (UnsatisfiedLinkError ule) {
                    ule.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    private static volatile boolean mIsNativeInitialized = false;

    private static void initNativeOnce() {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
            }
        }
    }

    public IjkMediaPlayer() {
        this(sLocalLibLoader);
    }


    /**
     * do not loadLibaray
     *
     * @param libLoader
     */
    public IjkMediaPlayer(IjkLibLoader libLoader) {
        initPlayer(libLoader);
    }

    @SuppressLint("CheckResult")
    private void initPlayer(final IjkLibLoader libLoader) {
        Single.fromCallable(() -> {
            loadLibrariesOnce(libLoader);
            return 1;
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(integer -> {
                    initNativeOnce();
                    native_setup(new WeakReference<>(IjkMediaPlayer.this));
                    _setLogInValid();
                    if (mijkPlayerCallBack != null) {
                        mijkPlayerCallBack.message(IjkMediaPlayerConstants.MEDIA_IJK_SO_INIT_SUCCESS, 0, 0, 0);
                    }
                });
    }

    public void setDataSource(String path) throws Exception {
        if (!mIsNativeInitialized) {
            return;
        }
        _setDataSource(path, null, null);
    }

    public void prepare(int needSeek) throws IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _prepareAsync(needSeek, STREAM_MUSIC);
    }

    public void setDuration(int urlDuration, int totalDuration) throws IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _setdurtion(urlDuration, totalDuration);
    }

    public void play() throws IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _start();
    }

    public void stop() throws IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _stop();
    }


    public void pause() throws IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _pause();
    }


    public void seek(long msec) throws Exception {
        if (!mIsNativeInitialized) {
            return;
        }
        seekTo(msec);
    }

    public void release() {
        if (!mIsNativeInitialized) {
            return;
        }
        _release();
    }


    public void reset() throws Exception {
        if (!mIsNativeInitialized) {
            return;
        }
        _stop();
    }


    public String getDnsAddress() {
        if (!mIsNativeInitialized) {
            return null;
        }
        return _getDnsAdress();
    }

    public void setMediaVolume(float leftVolume, float rightVolume) {
        if (!mIsNativeInitialized) {
            return;
        }
        setVolume(leftVolume, rightVolume);
    }

    /**
     * 设置IJKPlayer 音量均衡
     *
     * @param active 1 enable loudness normalization  0 disable loudness normalization
     */
    public void setLoudnessNormalization(int active) {
        try {
            _setLoudnessNormalization(active);
        } catch (IllegalStateException ill) {
            ill.printStackTrace();
        }
    }

    public void setAvOption(AvFormatOption option) {
        setAvFormatOption(option.getName(), option.getValue());
    }

    public void setAvFormatOption(String name, String value) {
        _setAvFormatOption(name, value);
    }

    public void setAvCodecOption(String name, String value) {
        _setAvCodecOption(name, value);
    }

    public void setSwScaleOption(String name, String value) {
        _setSwScaleOption(name, value);
    }

    /**
     * @param chromaFourCC AvFourCC.SDL_FCC_RV16 AvFourCC.SDL_FCC_RV32
     *                     AvFourCC.SDL_FCC_YV12
     */
    public void setOverlayFormat(int chromaFourCC) {
        _setOverlayFormat(chromaFourCC);
    }

    /**
     * @param frameDrop =0 do not drop any frame <0 drop as many frames as possible >0
     *                  display 1 frame per `frameDrop` continuous dropped frames,
     */
    public void setFrameDrop(int frameDrop) {
        _setFrameDrop(frameDrop);
    }

    public void setMediaCodecEnabled(boolean enabled) {
        _setMediaCodecEnabled(enabled);
    }

    public void setOpenSLESEnabled(boolean enabled) {
        _setOpenSLESEnabled(enabled);
    }

    public void setAutoPlayOnPrepared(boolean enabled) {
        _setAutoPlayOnPrepared(enabled);
    }

    public void setDnsAddress(String[] values) throws IOException, IllegalArgumentException,
            SecurityException, IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _setDnsAddress(values);
    }

    public void clearDnsAddress() throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _clearDnsAddress();
    }

    /**
     * 2016.10.21
     * 清除代理方法
     */
    public void clearProxyAddress() {
        if (!mIsNativeInitialized) {
            return;
        }
        _clearProxyAdress();
    }

    /**
     * 2016.10.21
     * 设置代理方法
     */
    private void setProxyAddress(String values) throws IOException, IllegalArgumentException,
            SecurityException, IllegalStateException {
        if (!mIsNativeInitialized) {
            return;
        }
        _setProxyAddress(values);
    }


    public Bundle getMediaMeta() {
        return _getMediaMeta();
    }

    public String getVideoCodecInfo() {
        return _getVideoCodecInfo();
    }

    public String getAudioCodecInfo() {
        return _getAudioCodecInfo();
    }

    protected void finalize() {
        native_finalize();
    }

    public void setIjkPlayerCallBack(ijkPlayerCallBack callBack) {
        this.mijkPlayerCallBack = callBack;
    }

    public ijkPlayerCallBack getIjkPlayerCallBack() {
        return mijkPlayerCallBack;
    }

    /**
     * 本地方法调用上层方法.....
     */

    @CalledByNative
    private static void postDataFromNative(Object weakThiz, byte buffer[], int length) {
    }

    @CalledByNative
    private static void postEventFromNative(Object weakThiz, int what,
                                            int arg1, int arg2, Object obj) {
        if (weakThiz == null)
            return;

        @SuppressWarnings("rawtypes")
        IjkMediaPlayer mp = (IjkMediaPlayer) ((WeakReference) weakThiz).get();
        if (mp == null) {
            return;
        }

        if (what == MEDIA_INFO && arg1 == MEDIA_INFO_STARTED_AS_NEXT) {
            try {
                mp.play();
            } catch (IllegalStateException ill) {
                ill.printStackTrace();
            }
        }
        if (mp.getIjkPlayerCallBack() != null) {
            mp.getIjkPlayerCallBack().message(what, arg1, arg2, obj);
        }
    }

    @CalledByNative
    private static String onSelectCodec(Object weakThiz, String mimeType,
                                        int profile, int level) {
        return null;
    }


    @CalledByNative
    private static String onControlResolveSegmentUrl(Object weakThiz,
                                                     int segment) {
        return null;
    }

    @CalledByNative
    private static String onControlResolveSegmentOfflineMrl(Object weakThiz,
                                                            int segment) {
        return null;
    }

    @CalledByNative
    private static int onControlResolveSegmentDuration(Object weakThiz, int segment) {
        return 0;
    }

    @CalledByNative
    private static int onControlResolveSegmentCount(Object weakThiz) {
        return 0;
    }

    public interface ijkPlayerCallBack {
        void message(int what, int arg1, int arg2, Object obj);
    }


    /**
     * 本地方法.....
     */

    /**
     * @throws IOException
     */
    private native void _clearDnsAddress() throws IOException, IllegalArgumentException,
            SecurityException, IllegalStateException;

    /**
     * 1,片花控制有效，0，片花控制无效
     *
     * @param active 是否有片花(广告)
     */
    public native void _setTitileActive(int active) throws IllegalStateException;

    /**
     * 控制IJK动态库中log信息是否可以输出如果调用则不可以输出，默认情况为输出
     */
    private native void _setLogInValid();

    private native long _getAudioFormat();

    private native long _getAudioSampleRate();

    private native long _getAudioChannels();

    private native void _setDataCallBack(long flag) throws IOException,
            IllegalArgumentException, SecurityException, IllegalStateException;

    /*
     * Update the IjkMediaPlayer SurfaceTexture. Call after setting a new
     * display surface.
     */
    private native void _setVideoSurface(Surface surface);

    /**
     * 1 enable loudness normalization  0 disable loudness normalization
     *
     * @param active disable or enable loudness normalization
     */
    private native void _setLoudnessNormalization(int active) throws IllegalStateException;

    private static native final String _getColorFormatName(
            int mediaCodecColorFormat);

    private static native final void native_init();

    private native final void native_setup(Object IjkMediaPlayer_this);

    private native final void native_finalize();

    private native final void native_message_loop(Object IjkMediaPlayer_this);

    private native void _setDnsAddress(String[] values) throws IOException, IllegalArgumentException,
            SecurityException, IllegalStateException;

    /**
     * 2016.07.22新加方法
     *
     * @return
     */
    private native String _getDnsAdress();

    /**
     * 2016.10.21
     * 清除代理方法
     */
    private native void _clearProxyAdress();

    /**
     * 2016.10.21
     * 设置代理方法
     */
    private native void _setProxyAddress(String values);


    /**
     * @param urlduration 参数单位是以秒为精确度的时长
     */
    public native void _setdurtion(int urlduration, int urlduraion_all) throws IllegalStateException;

    private native void _start() throws IllegalStateException;

    private native void _setDataSource(String path, String[] keys,
                                       String[] values) throws IOException, IllegalArgumentException,
            SecurityException, IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native void _setAvFormatOption(String name, String value);

    private native void _setAvCodecOption(String name, String value);

    private native void _setSwScaleOption(String name, String value);

    private native void _setOverlayFormat(int chromaFourCC);

    private native void _setFrameDrop(int frameDrop);

    private native void _setMediaCodecEnabled(boolean enabled);

    private native void _setOpenSLESEnabled(boolean enabled);

    private native void _setAutoPlayOnPrepared(boolean enabled);

    private native Bundle _getMediaMeta();

    private native String _getVideoCodecInfo();

    private native String _getAudioCodecInfo();

    private native void _pause() throws IllegalStateException;

    public native void _prepareAsync(int needSeek, int stream_type_channel) throws IllegalStateException;

    public native void seekTo(long msec) throws IllegalStateException;

    public native boolean isPlaying();

    public native long getCurrentPosition();

    public native long getDuration();

    private native void _release();

    private native void _reset();

    public native void setVolume(float leftVolume, float rightVolume);


}
