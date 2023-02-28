package cn.GnaixEuy.common.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/28
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @Desc: 消息类型
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Getter
@AllArgsConstructor
public enum MessageEnum {
    FOLLOW_YOU(1, "关注"),
    LIKE_VLOG(2, "点赞视频"),
    COMMENT_VLOG(3, "评论视频"),
    REPLY_YOU(4, "回复评论"),
    LIKE_COMMENT(5, "点赞评论");

    public final Integer type;
    public final String value;

}
