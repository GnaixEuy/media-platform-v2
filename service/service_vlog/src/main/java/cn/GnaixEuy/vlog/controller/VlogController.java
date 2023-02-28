package cn.GnaixEuy.vlog.controller;

import cn.GnaixEuy.common.enmus.YesOrNo;
import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.model.bo.VlogBO;
import cn.GnaixEuy.properties.BaseInfoProperties;
import cn.GnaixEuy.vlog.service.VlogService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "VlogController 短视频相关业务功能的接口")
@RequestMapping("vlog")
@RestController
public class VlogController extends BaseInfoProperties {

    @Autowired
    private VlogService vlogService;

    @PostMapping("publish")
    public JSONResult publish(@RequestBody VlogBO vlogBO) {
        // FIXME 作业，校验VlogBO
        vlogService.createVlog(vlogBO);
        return JSONResult.ok();
    }

    @GetMapping("indexList")
    public JSONResult indexList(@RequestParam(defaultValue = "") String userId,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getIndexVlogList(userId, search, page, pageSize);
        return JSONResult.ok(gridResult);
    }

    @GetMapping("detail")
    public JSONResult detail(@RequestParam(defaultValue = "") String userId,
                             @RequestParam String vlogId) {
        return JSONResult.ok(vlogService.getVlogDetailById(userId, vlogId));
    }


    @PostMapping("changeToPrivate")
    public JSONResult changeToPrivate(@RequestParam String userId,
                                      @RequestParam String vlogId) {
        vlogService.changeToPrivateOrPublic(userId,
                vlogId,
                YesOrNo.YES.type);
        return JSONResult.ok();
    }

    @PostMapping("changeToPublic")
    public JSONResult changeToPublic(@RequestParam String userId,
                                     @RequestParam String vlogId) {
        vlogService.changeToPrivateOrPublic(userId,
                vlogId,
                YesOrNo.NO.type);
        return JSONResult.ok();
    }


    @GetMapping("myPublicList")
    public JSONResult myPublicList(@RequestParam String userId,
                                   @RequestParam Integer page,
                                   @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryMyVlogList(userId,
                page,
                pageSize,
                YesOrNo.NO.type);
        return JSONResult.ok(gridResult);
    }

    @GetMapping("myPrivateList")
    public JSONResult myPrivateList(@RequestParam String userId,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryMyVlogList(userId,
                page,
                pageSize,
                YesOrNo.YES.type);
        return JSONResult.ok(gridResult);
    }

    @GetMapping("myLikedList")
    public JSONResult myLikedList(@RequestParam String userId,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyLikedVlogList(userId,
                page,
                pageSize);
        return JSONResult.ok(gridResult);
    }


    @PostMapping("like")
    public JSONResult like(@RequestParam String userId,
                           @RequestParam String vlogerId,
                           @RequestParam String vlogId) {

        // 我点赞的视频，关联关系保存到数据库
        vlogService.userLikeVlog(userId, vlogId);

        // 点赞后，视频和视频发布者的获赞都会 +1
        redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        redis.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);

        // 我点赞的视频，需要在redis中保存关联关系
        redis.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId, "1");

        return JSONResult.ok();
    }


    @PostMapping("unlike")
    public JSONResult unlike(@RequestParam String userId,
                             @RequestParam String vlogerId,
                             @RequestParam String vlogId) {

        // 我取消点赞的视频，关联关系删除
        vlogService.userUnLikeVlog(userId, vlogId);

        redis.decrement(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        redis.decrement(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        redis.del(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);

        return JSONResult.ok();
    }

    @PostMapping("totalLikedCounts")
    public JSONResult totalLikedCounts(@RequestParam String vlogId) {
        return JSONResult.ok(vlogService.getVlogBeLikedCounts(vlogId));
    }

    @GetMapping("followList")
    public JSONResult followList(@RequestParam String myId,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyFollowVlogList(myId,
                page,
                pageSize);
        return JSONResult.ok(gridResult);
    }

    @GetMapping("friendList")
    public JSONResult friendList(@RequestParam String myId,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyFriendVlogList(myId,
                page,
                pageSize);
        return JSONResult.ok(gridResult);
    }
}
