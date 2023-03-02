package cn.GnaixEuy.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import java.util.Date;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/27
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 评论的视频是哪个作者（vloger）的关联id
     */
    @Column(name = "vloger_id")
    private String vlogerId;

    /**
     * 如果是回复留言，则本条为子留言，需要关联查询
     */
    @Column(name = "father_comment_id")
    private String fatherCommentId;

    /**
     * 回复的那个视频id
     */
    @Column(name = "vlog_id")
    private String vlogId;

    /**
     * 发布留言的用户id
     */
    @Column(name = "comment_user_id")
    private String commentUserId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言的点赞总数
     */
    @Column(name = "like_counts")
    private Integer likeCounts;

    /**
     * 留言时间
     */
    @Column(name = "create_time")
    private Date createTime;

}
