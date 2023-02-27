package cn.GnaixEuy.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

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
@ToString
@Document(value = "message")
public class MessageMO {

    @Id
    private String id;                  // 消息主键id

    @Field("fromUserId")
    private String fromUserId;          // 消息来自的用户id
    @Field("fromNickname")
    private String fromNickname;        // 消息来自的用户昵称
    @Field("fromFace")
    private String fromFace;            // 消息来自的用户头像

    @Field("toUserId")
    private String toUserId;            // 消息发送到某对象的用户id

    @Field("msgType")
    private Integer msgType;             // 消息类型 枚举
    @Field("msgContent")
    private Map msgContent;              // 消息内容

    @Field("createTime")
    private Date createTime;            // 消息创建时间
}