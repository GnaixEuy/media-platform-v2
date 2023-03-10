package cn.GnaixEuy.users.controller;

import cn.GnaixEuy.common.enmus.FileTypeEnum;
import cn.GnaixEuy.common.enmus.ResponseStatusEnum;
import cn.GnaixEuy.common.enmus.UserInfoModifyType;
import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.config.MinIOConfig;
import cn.GnaixEuy.model.bo.UpdatedUserBO;
import cn.GnaixEuy.model.pojo.Users;
import cn.GnaixEuy.model.vo.UsersVO;
import cn.GnaixEuy.properties.BaseInfoProperties;
import cn.GnaixEuy.users.service.UserService;
import cn.GnaixEuy.utils.MinIOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

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
@Api(tags = "UserInfoController 用户信息接口模块")
@RequestMapping(value = {"userInfo"})
@RestController
public class UserInfoController extends BaseInfoProperties {

    @Autowired

    private UserService userService;
    @Autowired
    private MinIOConfig minIOConfig;

    @GetMapping(value = {"query"})
    public JSONResult query(@RequestParam String userId) {
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
        int myFollowsCounts = 0;
        int myFansCounts = 0;
        int likedVlogCounts = 0;
        int likedVlogerCounts = 0;
        int totalLikeMeCounts;
        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.parseInt(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.parseInt(myFansCountsStr);
        }
//        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
//            likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
//        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.parseInt(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;
        usersVO.setMyFollowsCounts(myFollowsCounts);
        usersVO.setMyFansCounts(myFansCounts);
        usersVO.setTotalLikeMeCounts(totalLikeMeCounts);
        return JSONResult.ok(usersVO);
    }

    @PostMapping(value = {"modifyUserInfo"})
    public JSONResult modifyUserInfo(@RequestBody UpdatedUserBO updatedUserBO,
                                     @RequestParam Integer type) {
        UserInfoModifyType.checkUserInfoTypeIsRight(type);
        Users newUserInfo = userService.updateUserInfo(updatedUserBO, type);
        return JSONResult.ok(newUserInfo);
    }

    @PostMapping(value = {"modifyImage"})
    public JSONResult modifyImage(@RequestParam String userId,
                                  @RequestParam Integer type,
                                  MultipartFile file) throws Exception {
        if (!Objects.equals(type, FileTypeEnum.BACKGROUND_IMG.type) && !Objects.equals(type, FileTypeEnum.FACE.type)) {
            return JSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILED);
        }
        String fileName = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                fileName,
                file.getInputStream());
        String imgUrl = minIOConfig.getFileHost() + "/"
                + minIOConfig.getBucketName() + "/"
                + fileName;
        // 修改图片地址到数据库
        UpdatedUserBO updatedUserBO = new UpdatedUserBO();
        updatedUserBO.setId(userId);
        if (Objects.equals(type, FileTypeEnum.BACKGROUND_IMG.type)) {
            updatedUserBO.setBgImg(imgUrl);
        } else {
            updatedUserBO.setFace(imgUrl);
        }
        Users users = userService.updateUserInfo(updatedUserBO);
        return JSONResult.ok(users);
    }
}
