package cn.GnaixEuy.message.controller;

import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.message.service.MsgService;
import cn.GnaixEuy.model.mo.MessageMO;
import cn.GnaixEuy.properties.BaseInfoProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@Slf4j
@Api(tags = "MsgController 消息功能模块的接口")
@RequestMapping(value = {"msg"})
@RestController
public class MsgController extends BaseInfoProperties {

    @Autowired
    private MsgService msgService;

    @GetMapping("list")
    public JSONResult<List<MessageMO>> list(@RequestParam String userId,
                                            @RequestParam Integer page,
                                            @RequestParam Integer pageSize) {
        // mongodb 从0分页，区别于数据库
        if (page == null) {
            page = COMMON_START_PAGE_ZERO;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        List<MessageMO> list = msgService.queryList(userId, page, pageSize);
        return JSONResult.ok(list);
    }
}

