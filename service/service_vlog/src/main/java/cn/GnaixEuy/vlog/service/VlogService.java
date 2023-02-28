package cn.GnaixEuy.vlog.service;

import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.model.bo.VlogBO;
import cn.GnaixEuy.model.pojo.Vlog;
import cn.GnaixEuy.model.vo.IndexVlogVO;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface VlogService extends IService<Vlog> {

    /**
     * 新增vlog视频
     */
    void createVlog(VlogBO vlogBO);


    /**
     * 查询首页/搜索的vlog列表
     */
    PagedGridResult getIndexVlogList(String userId,
                                     String search,
                                     Integer page,
                                     Integer pageSize);

    /**
     * 根据视频主键查询vlog
     */
    IndexVlogVO getVlogDetailById(String userId, String vlogId);

    /**
     * 用户把视频改为公开/私密的视频
     */
    void changeToPrivateOrPublic(String userId,
                                 String vlogId,
                                 Integer yesOrNo);

    /**
     * 查询用的公开/私密的视频列表
     */
    PagedGridResult queryMyVlogList(String userId,
                                    Integer page,
                                    Integer pageSize,
                                    Integer yesOrNo);

    /**
     * 用户点赞/喜欢视频
     */
    void userLikeVlog(String userId, String vlogId);

    /**
     * 用户取消点赞/喜欢视频
     */
    void userUnLikeVlog(String userId, String vlogId);

    /**
     * 获得用户点赞视频的总数
     */
    Integer getVlogBeLikedCounts(String vlogId);

    /**
     * 查询用户点赞过的短视频
     */
    PagedGridResult getMyLikedVlogList(String userId,
                                       Integer page,
                                       Integer pageSize);

    /**
     * 查询用户关注的博主发布的短视频列表
     */
    PagedGridResult getMyFollowVlogList(String myId,
                                        Integer page,
                                        Integer pageSize);

    /**
     * 查询朋友发布的短视频列表
     */
    PagedGridResult getMyFriendVlogList(String myId,
                                        Integer page,
                                        Integer pageSize);

    /**
     * 根据主键查询vlog
     */
    Vlog getVlog(String id);
}

