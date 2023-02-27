package cn.GnaixEuy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/28
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentVO {
    private String id;
    private String commentId;
    private String vlogerId;
    private String fatherCommentId;
    private String vlogId;
    private String commentUserId;
    private String commentUserNickname;
    private String commentUserFace;
    private String content;
    private Integer likeCounts;
    private String replyedUserNickname;
    private Date createTime;
    private Integer isLike = 0;
}
