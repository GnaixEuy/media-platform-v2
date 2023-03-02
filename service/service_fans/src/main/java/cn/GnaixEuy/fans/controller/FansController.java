package cn.GnaixEuy.fans.controller;

import cn.GnaixEuy.common.enmus.ResponseStatusEnum;
import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.fans.client.UsersFeignClient;
import cn.GnaixEuy.fans.service.FansService;
import cn.GnaixEuy.model.pojo.Users;
import cn.GnaixEuy.utils.RedisUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.GnaixEuy.properties.BaseInfoProperties.*;

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
@Api(tags = "FansController 粉丝相关业务功能的接口")
@RequestMapping(value = {"fans"})
@RestController
public class FansController {
    @Autowired
    private RedisUtils redis;
    @Autowired
    private UsersFeignClient usersFeignClient;
    @Autowired
    private FansService fansService;

    @PostMapping(value = {"follow"})
    public JSONResult follow(@RequestParam String myId,
                             @RequestParam String vlogerId) {

        // 判断两个id不能为空
        if (StringUtils.isBlank(myId) || StringUtils.isBlank(vlogerId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        // 判断当前用户，自己不能关注自己
        if (myId.equalsIgnoreCase(vlogerId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }
        Users vloger = null, myInfo = null;
        // 判断两个id对应的用户是否存在
        JSONResult jsonResult = this.usersFeignClient.getUserBaseInfoById(vlogerId);
        if (jsonResult.getSuccess()) {
            vloger = (Users) jsonResult.getData();
        }
        jsonResult = this.usersFeignClient.getUserBaseInfoById(myId);
        if (jsonResult.getSuccess()) {
            myInfo = (Users) jsonResult.getData();
        }

        // fixme: 两个用户id的数据库查询后的判断，是分开好？还是合并判断好？
        if (myInfo == null || vloger == null) {
            return JSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        // 保存粉丝关系到数据库
        fansService.doFollow(myId, vlogerId);

        // 博主的粉丝+1，我的关注+1
        redis.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.increment(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        // 我和博主的关联关系，依赖redis，不要存储数据库，避免db的性能瓶颈
        redis.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId, "1");

        return JSONResult.ok();
    }

    @PostMapping(value = {"cancel"})
    public JSONResult cancel(@RequestParam String myId,
                             @RequestParam String vlogerId) {
        // 删除业务的执行
        fansService.doCancel(myId, vlogerId);
        // 博主的粉丝-1，我的关注-1
        redis.decrement(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.decrement(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);
        // 我和博主的关联关系，依赖redis，不要存储数据库，避免db的性能瓶颈
        redis.del(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId);
        return JSONResult.ok();
    }

    @GetMapping(value = {"queryDoIFollowVloger"})
    public JSONResult queryDoIFollowVloger(@RequestParam String myId,
                                           @RequestParam String vlogerId) {
        return JSONResult.ok(fansService.queryDoIFollowVloger(myId, vlogerId));
    }

    @GetMapping(value = {"queryMyFollows"})
    public JSONResult queryMyFollows(@RequestParam String myId,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize) {
        return JSONResult.ok(
                fansService.queryMyFollows(
                        myId,
                        page,
                        pageSize));
    }

    @GetMapping(value = {"queryMyFans"})
    public JSONResult queryMyFans(@RequestParam String myId,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize) {
        return JSONResult.ok(
                fansService.queryMyFans(
                        myId,
                        page,
                        pageSize));
    }
}
