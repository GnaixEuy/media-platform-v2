package cn.GnaixEuy.controller;

import cn.GnaixEuy.MinIOConfig;
import cn.GnaixEuy.base.BaseInfoProperties;
import cn.GnaixEuy.bo.UpdatedUserBO;
import cn.GnaixEuy.common.enmus.FileTypeEnum;
import cn.GnaixEuy.common.enmus.UserInfoModifyType;
import cn.GnaixEuy.common.utils.MinIOUtils;
import cn.GnaixEuy.common.utils.result.JSONResult;
import cn.GnaixEuy.common.utils.result.ResponseStatusEnum;
import cn.GnaixEuy.pojo.Users;
import cn.GnaixEuy.service.UserService;
import cn.GnaixEuy.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2-new </p>
 * 创建日期： 2023/2/27
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Slf4j
@Api(tags = "UserInfoController 用户信息接口模块")
@RequestMapping("userInfo")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoController extends BaseInfoProperties {

    private final UserService userService;
    private final MinIOConfig minIOConfig;

    @GetMapping("query")
    public JSONResult query(@RequestParam String userId) throws Exception {

        Users user = userService.getUser(userId);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);

        // 我的关注博主总数量
        String myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        // 我的粉丝总数
        String myFansCountsStr = redis.get(REDIS_MY_FANS_COUNTS + ":" + userId);
        // 用户获赞总数，视频博主（点赞/喜欢）总和
//        String likedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String likedVlogerCountsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer likedVlogCounts = 0;
        Integer likedVlogerCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
//        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
//            likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
//        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        usersVO.setMyFollowsCounts(myFollowsCounts);
        usersVO.setMyFansCounts(myFansCounts);
        usersVO.setTotalLikeMeCounts(totalLikeMeCounts);

        return JSONResult.ok(usersVO);
    }

    @PostMapping("modifyUserInfo")
    public JSONResult modifyUserInfo(@RequestBody UpdatedUserBO updatedUserBO,
                                     @RequestParam Integer type)
            throws Exception {

        UserInfoModifyType.checkUserInfoTypeIsRight(type);

        Users newUserInfo = userService.updateUserInfo(updatedUserBO, type);

        return JSONResult.ok(newUserInfo);
    }

    @PostMapping("modifyImage")
    public JSONResult modifyImage(@RequestParam String userId,
                                  @RequestParam Integer type,
                                  MultipartFile file) throws Exception {

        if (type != FileTypeEnum.BGIMG.type && type != FileTypeEnum.FACE.type) {
            return JSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILED);
        }

        String fileName = file.getOriginalFilename();

        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                fileName,
                file.getInputStream());

        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + fileName;


        // 修改图片地址到数据库
        UpdatedUserBO updatedUserBO = new UpdatedUserBO();
        updatedUserBO.setId(userId);

        if (type == FileTypeEnum.BGIMG.type) {
            updatedUserBO.setBgImg(imgUrl);
        } else {
            updatedUserBO.setFace(imgUrl);
        }
        Users users = userService.updateUserInfo(updatedUserBO);

        return JSONResult.ok(users);
    }
}

