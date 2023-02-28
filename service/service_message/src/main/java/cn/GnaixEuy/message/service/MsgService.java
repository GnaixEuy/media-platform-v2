package cn.GnaixEuy.message.service;

import cn.GnaixEuy.model.mo.MessageMO;

import java.util.List;
import java.util.Map;

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
public interface MsgService {

    /**
     * 创建消息
     */
    void createMsg(String fromUserId,
                   String toUserId,
                   Integer type,
                   Map msgContent);

    /**
     * 查询消息列表
     */
    List<MessageMO> queryList(String toUserId,
                              Integer page,
                              Integer pageSize);

}

