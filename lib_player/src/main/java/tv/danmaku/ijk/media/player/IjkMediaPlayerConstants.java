package tv.danmaku.ijk.media.player;

/**
 * @author zhangchao on 2019-05-27.
 */

public interface IjkMediaPlayerConstants {
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;

    public static final int MEDIA_NOP = 0; // interface test message
    public static final int MEDIA_PREPARED = 1;
    public static final int MEDIA_PLAYBACK_COMPLETE = 2;
    public static final int MEDIA_BUFFERING_UPDATE = 3;
    public static final int MEDIA_SEEK_COMPLETE = 4;
    public static final int MEDIA_SET_VIDEO_SIZE = 5;
    public static final int MEDIA_PLAYER_PTS_UPDATE = 6;
    /**
     * 点播预加载时间戳
     */
    public static final int MEDIA_PLAYER_VOD_PTS_PRELOAD_UPDATE = 8;
    public static final int MEDIA_TIMED_TEXT = 99;
    public static final int MEDIA_ERROR = 100;
    public static final int MEDIA_INFO = 200;

    public static final int MEDIA_SET_VIDEO_SAR = 10001;

    /**
     * 来自底层PLAYER错误码
     */
    public static final int MEDIA_ERROR_IJK_PLAYER = -10000;
    public static final int MEDIA_ERROR_IJK_PLAYER_ZERO = 0;
    public static final int MEDIA_CONNECTION_TIMEOUT_ERROR = -110;
    /**
     * 没有 ts 文件造成的错误
     */
    public static final int NO_TS_FILE_ERROR_IJK_PLAYER = 400;
    public static final int NO_M3U8_FILE_IJK_PLAYER = 0;
    /**
     * 播放ID不存在，表示服务器上还没有这个id，表示这个节目从来没有在服务器上出现过
     */
    public static final int NO_ID_SUB_ERROR_IJK_PLAYER = 404;
    public static final int NO_ID_SUB_ERROR_UNKHNOWN_IJK_PLAYER = 403;
    /**
     * 网关错误。这个错误目前没有找到具体出现的场景
     */
    public static final int BAD_GATEWAY_SUB_ERROR_IJK_PLAYER = 502;
    /**
     * 播放的文件不存在。是在拿到索引文件后，却拿不到具体的TS文件，这时通常都是么有推流
     */
    public static final int NO_FILE_SUB_ERROR_IJK_PLAYER = 503;
    /**
     * 域名解析错误
     */
    public static final int DOMAIN_SUB_ERROR_IJK_PLAYER = 700;
    /**
     * 多次请求索引文件相同 多半是服务器没有更新m3u8文件。这个错误码会在连续10次请求到相同索引文件的情况下发出
     */
    public static final int NO_UPDATE_SUB_ERROR_IJK_PLAYER = 701;
    /**
     * 底层播放器断点重试
     */
    public static final int GET_STREAM_FAILED_SUB_ERROR_IJK_PLAYER = -1;

    /**
     * 首次开播seek断线重连子错误码
     */
    public static final int SEEK_GET_STREAM_FAILED_SUB_ERROR_IJK_PLAYER = 1;
    /**
     * 底层播放器因网络断开开始尝试重连
     */
    public static final int GET_STREAM_CONNECTION_SUB_ERROR_IJK_PLAYER = 900;

    /**
     * 底层播放器已经从断网中恢复
     */
    public static final int GET_STREAM_CONNECTED_SUB_ERROR_IJK_PLAYER = 901;

    public static final int STREAM_MUSIC = 3;

    /**
     * 播放器初始化成功
     */
    public static final int MEDIA_IJK_SO_INIT_SUCCESS = 666666;
}
