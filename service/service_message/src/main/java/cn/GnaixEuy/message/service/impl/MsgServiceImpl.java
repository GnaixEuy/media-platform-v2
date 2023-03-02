package cn.GnaixEuy.message.service.impl;

import cn.GnaixEuy.common.enmus.MessageEnum;
import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.message.client.UsersFeignClient;
import cn.GnaixEuy.message.repository.MessageRepository;
import cn.GnaixEuy.message.service.MsgService;
import cn.GnaixEuy.model.mo.MessageMO;
import cn.GnaixEuy.model.pojo.Users;
import cn.GnaixEuy.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.GnaixEuy.properties.BaseInfoProperties.REDIS_FANS_AND_VLOGGER_RELATIONSHIP;

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
@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private RedisUtils redis;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UsersFeignClient usersFeignClient;

    @Override
    public void createMsg(String fromUserId,
                          String toUserId,
                          Integer type,
                          Map msgContent) {

        JSONResult userBaseInfoById = usersFeignClient.getUserBaseInfoById(fromUserId);
        Users fromUser = null;
        if (userBaseInfoById.getSuccess()) {
            fromUser = JSON.parseObject(JSON.toJSONString(userBaseInfoById.getData()), new TypeReference<Users>() {
            });
        }
        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(fromUserId);
        messageMO.setFromNickname(fromUser.getNickname());
        messageMO.setFromFace(fromUser.getFace());
        messageMO.setToUserId(toUserId);
        messageMO.setMsgType(type);
        if (msgContent != null) {
            messageMO.setMsgContent(msgContent);
        }
        messageMO.setCreateTime(new Date());
        messageRepository.save(messageMO);
    }

    @Override
    public List<MessageMO> queryList(String toUserId,
                                     Integer page,
                                     Integer pageSize) {
        Pageable pageable = PageRequest.of(page,
                pageSize,
                Sort.Direction.DESC,
                "createTime");
        List<MessageMO> list = messageRepository
                .findAllByToUserIdEqualsOrderByCreateTimeDesc(toUserId,
                        pageable);
        for (MessageMO msg : list) {
            // 如果类型是关注消息，则需要查询我之前有没有关注过他，用于在前端标记“互粉”“互关”
            if (msg.getMsgType() != null && msg.getMsgType() == MessageEnum.FOLLOW_YOU.type) {
                Map map = msg.getMsgContent();
                if (map == null) {
                    map = new HashMap();
                }

                String relationship = this.redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + msg.getToUserId() + ":" + msg.getFromUserId());
                if (StringUtils.isNotBlank(relationship) && relationship.equalsIgnoreCase("1")) {
                    map.put("isFriend", true);
                } else {
                    map.put("isFriend", false);
                }
                msg.setMsgContent(map);
            }
        }
        return list;
    }
}

