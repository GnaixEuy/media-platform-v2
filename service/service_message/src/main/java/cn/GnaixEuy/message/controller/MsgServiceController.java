package cn.GnaixEuy.message.controller;

import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.message.service.MsgService;
import cn.GnaixEuy.model.bo.feign.CreateMsgBo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/1
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Slf4j
@Api(tags = "MsgController 消息功能模块的接口")
@RequestMapping(value = {"msg/feign"})
@RestController
public class MsgServiceController {
    @Autowired
    private MsgService msgService;

    @PostMapping(value = {"createMsg"})
    public JSONResult createMsg(CreateMsgBo createMsgBo) {
        this.msgService.createMsg(
                createMsgBo.getFromUserId(),
                createMsgBo.getToUserId(),
                createMsgBo.getType(),
                createMsgBo.getMsgContent()
        );
        return JSONResult.ok();
    }
}
