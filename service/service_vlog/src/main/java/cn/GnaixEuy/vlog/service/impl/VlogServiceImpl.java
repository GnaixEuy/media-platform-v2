package cn.GnaixEuy.vlog.service.impl;

import cn.GnaixEuy.common.enmus.MessageEnum;
import cn.GnaixEuy.common.enmus.YesOrNo;
import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.common.utils.PageResultUtil;
import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.model.bo.VlogBO;
import cn.GnaixEuy.model.bo.feign.CreateMsgBo;
import cn.GnaixEuy.model.pojo.MyLikedVlog;
import cn.GnaixEuy.model.pojo.Vlog;
import cn.GnaixEuy.model.vo.IndexVlogVO;
import cn.GnaixEuy.utils.RedisUtils;
import cn.GnaixEuy.vlog.client.FansFeignClient;
import cn.GnaixEuy.vlog.client.MessageFeignClient;
import cn.GnaixEuy.vlog.dao.MyLikedVlogMapper;
import cn.GnaixEuy.vlog.dao.VlogMapper;
import cn.GnaixEuy.vlog.service.VlogService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.GnaixEuy.properties.BaseInfoProperties.REDIS_USER_LIKE_VLOG;
import static cn.GnaixEuy.properties.BaseInfoProperties.REDIS_VLOG_BE_LIKED_COUNTS;

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
public class VlogServiceImpl extends ServiceImpl<VlogMapper, Vlog> implements VlogService {
    @Autowired
    private RedisUtils redis;

    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;

    @Autowired
    private FansFeignClient fansFeignClient;
    @Autowired
    private MessageFeignClient messageFeignClient;


    @Transactional
    @Override
    public void createVlog(VlogBO vlogBO) {
        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBO, vlog);
        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);
        vlog.setDeleted(false);
        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());
        this.baseMapper.insert(vlog);
    }

    @Override
    public PagedGridResult getIndexVlogList(String userId,
                                            String search,
                                            Integer page,
                                            Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)) {
            map.put("search", search);
        }
        Page<IndexVlogVO> indexVlogVOPage = this.baseMapper
                .getIndexVlogList(new Page<>(page, pageSize), map);
        for (IndexVlogVO v : indexVlogVOPage.getRecords()) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();
            if (StringUtils.isNotBlank(userId)) {
                // 用户是否关注该博主
                JSONResult jsonResult = this.fansFeignClient.queryDoIFollowVloger(userId, vlogerId);
                v.setDoIFollowVloger(jsonResult.getData().equals("true"));
                // 判断当前用户是否点赞过视频
                v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
            }
            // 获得当前视频被点赞过的总数
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
            //TODO 评论总数
        }
        return PageResultUtil.setterPagedGrid(indexVlogVOPage);
    }

    private IndexVlogVO setterVO(IndexVlogVO v, String userId) {
        String vlogerId = v.getVlogerId();
        String vlogId = v.getVlogId();
        if (StringUtils.isNotBlank(userId)) {
            // 用户是否关注该博主
            JSONResult jsonResult = this.fansFeignClient.queryDoIFollowVloger(userId, vlogerId);
            v.setDoIFollowVloger((jsonResult.getData().equals("true")));
            // 判断当前用户是否点赞过视频
            v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
        }
        // 获得当前视频被点赞过的总数
        v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        return v;
    }

    @Override
    public Integer getVlogBeLikedCounts(String vlogId) {
        String countsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

    private boolean doILikeVlog(String myId, String vlogId) {

        String doILike = redis.get(REDIS_USER_LIKE_VLOG + ":" + myId + ":" + vlogId);
        return StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1");
    }

    @Override
    public IndexVlogVO getVlogDetailById(String userId, String vlogId) {

        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);
        List<IndexVlogVO> list = this.baseMapper.getVlogDetailById(map);
        if (list != null && list.size() > 0 && !list.isEmpty()) {
            IndexVlogVO vlogVO = list.get(0);
            return setterVO(vlogVO, userId);
        }
        return null;
    }

    @Transactional
    @Override
    public void changeToPrivateOrPublic(String userId,
                                        String vlogId,
                                        Integer yesOrNo) {
        Vlog vlog = this.baseMapper.selectOne(
                Wrappers.<Vlog>lambdaQuery()
                        .eq(Vlog::getId, vlogId)
                        .eq(Vlog::getVlogerId, userId)
        );
        vlog.setIsPrivate(yesOrNo);
        this.baseMapper.updateById(vlog);
    }

    @Override
    public PagedGridResult queryMyVlogList(String userId,
                                           Integer page,
                                           Integer pageSize,
                                           Integer yesOrNo) {
        Page<Vlog> vlogPage = this.page(
                new Page<>(page, pageSize),
                Wrappers
                        .<Vlog>lambdaQuery()
                        .eq(Vlog::getVlogerId, userId)
                        .eq(Vlog::getIsPrivate, yesOrNo)
        );
        return PageResultUtil.setterPagedGrid(vlogPage);
    }

    @Override
    @Transactional
    public void userLikeVlog(String userId, String vlogId) {
        MyLikedVlog likedVlog = new MyLikedVlog();
        likedVlog.setVlogId(vlogId);
        likedVlog.setUserId(userId);
        likedVlog.setCreatedTime(new Date());
        myLikedVlogMapper.insert(likedVlog);
        // 系统消息：点赞短视频
        Vlog vlog = this.getVlog(vlogId);
        Map<String, String> msgContent = new HashMap<>();
        msgContent.put("vlogId", vlogId);
        msgContent.put("vlogCover", vlog.getCover());
        this.messageFeignClient.createMsg(
                new CreateMsgBo(
                        userId,
                        vlog.getVlogerId(),
                        MessageEnum.LIKE_VLOG.type,
                        msgContent));
    }

    @Override
    public Vlog getVlog(String id) {
        return this.getById(id);
    }

    @Override
    @Transactional
    public void userUnLikeVlog(String userId, String vlogId) {
        this.myLikedVlogMapper.delete(Wrappers
                .<MyLikedVlog>lambdaQuery()
                .eq(MyLikedVlog::getVlogId, vlogId)
                .eq(MyLikedVlog::getUserId, userId)
        );
    }

    @Override
    public PagedGridResult getMyLikedVlogList(String userId,
                                              Integer page,
                                              Integer pageSize) {
        Page<IndexVlogVO> indexVlogVOPage = this.baseMapper.getMyLikedVlogList(
                new Page<>(page, pageSize),
                new HashMap() {{
                    this.put("userId", userId);
                }}
        );

        return PageResultUtil.setterPagedGrid(indexVlogVOPage);
    }

    @Override
    public PagedGridResult getMyFollowVlogList(String myId,
                                               Integer page,
                                               Integer pageSize) {
        Page<IndexVlogVO> indexVlogVOPage = this.baseMapper.getMyFollowVlogList(
                new Page(page, pageSize),
                new HashMap() {{
                    this.put("myId", myId);
                }});

        for (IndexVlogVO v : indexVlogVOPage.getRecords()) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();
            if (StringUtils.isNotBlank(myId)) {
                // 用户必定关注该博主
                v.setDoIFollowVloger(true);
                // 判断当前用户是否点赞过视频
                v.setDoILikeThisVlog(doILikeVlog(myId, vlogId));
            }
            // 获得当前视频被点赞过的总数
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }
        return PageResultUtil.setterPagedGrid(indexVlogVOPage);
    }

    @Override
    public PagedGridResult getMyFriendVlogList(String myId,
                                               Integer page,
                                               Integer pageSize) {
        Page<IndexVlogVO> indexVlogVOPage = this.baseMapper.getMyFriendVlogList(
                new Page(page, pageSize),
                new HashMap() {{
                    this.put("myId", myId);
                }}
        );
        for (IndexVlogVO v : indexVlogVOPage.getRecords()) {
            String vlogerId = v.getVlogerId();
            String vlogId = v.getVlogId();
            if (StringUtils.isNotBlank(myId)) {
                // 用户必定关注该博主
                v.setDoIFollowVloger(true);
                // 判断当前用户是否点赞过视频
                v.setDoILikeThisVlog(doILikeVlog(myId, vlogId));
            }
            // 获得当前视频被点赞过的总数
            v.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }
        return PageResultUtil.setterPagedGrid(indexVlogVOPage);
    }
}
