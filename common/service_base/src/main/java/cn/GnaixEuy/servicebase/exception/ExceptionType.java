package cn.GnaixEuy.servicebase.exception;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/23
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
public enum ExceptionType {

    /**
     * 枚举业务异常信息
     */
    INNER_ERROR(500, "系统内部错误"),
    UNAUTHORIZED(401, "未登录"),
    BAD_REQUEST(400, "请求错误"),
    FORBIDDEN(403, "无权操作"),
    NOT_FOUND(404, "未找到"),
    DELETE_EXCEPTION(10005, "删除失败"),
    FILE_EMPTY_EXCEPTION(20001, "文件数据为空"),
    CLASS_SUBJECT_ADD_EXCEPTION(20002, "添加课程分类失败"),
    CLASS_INFO_ADD_EXCEPTION(20003, "添加课程信息失败"),
    CLASS_INFO_UPDATE_EXCEPTION(20004, "修改课程信息失败"),
    VIDEO_DELETE_EXCEPTION(20005, "删除视频失败"),
    CHAPTER_INFO_DELETE_EXCEPTION(30005, "不能删除"),
    LOGIN_EXCEPTION(30001, "登录失败"),
    REGISTER_EXCEPTION(30002, "注册失败"),
    USER_NAME_DUPLICATE(40001001, "用户名重复"),
    USER_NOT_FOUND(40401002, "用户不存在"),
    USER_PASSWORD_NOT_MATCH(40001003, "用户名或密码错误"),
    USER_NOT_ENABLED(50001001, "用户未启用"),
    USER_LOCKED(50001002, "用户被锁定"),
    USER_OPEN_ID_NOT_FOUND(40401003, "未找到openId绑定用户"),
    MUSIC_NOT_FOUND(40402001, "歌曲不存在"),
    FILE_NOT_FOUND(40403001, "文件不存在"),
    FILE_NOT_PERMISSION(40303002, "当前用户无权限修改文件"),
    PLAYLIST_NOT_FOUND(40404001, "歌单不存在"),
    ARTIST_NOT_FOUND(40405001, "歌手不存在"),
    ALBUM_NOT_FOUND(40406001, "专辑不存在");


    private final Integer code;
    private final String message;


    ExceptionType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
