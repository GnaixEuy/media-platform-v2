package cn.GnaixEuy.fans.service.impl;

import cn.GnaixEuy.common.enmus.MessageEnum;
import cn.GnaixEuy.common.enmus.YesOrNo;
import cn.GnaixEuy.common.utils.PageResultUtil;
import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.fans.client.MessageFeignClient;
import cn.GnaixEuy.fans.dao.FansMapper;
import cn.GnaixEuy.fans.service.FansService;
import cn.GnaixEuy.model.bo.feign.CreateMsgBo;
import cn.GnaixEuy.model.pojo.Fans;
import cn.GnaixEuy.model.vo.FansVO;
import cn.GnaixEuy.model.vo.VlogerVO;
import cn.GnaixEuy.utils.RedisUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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
public class FansServiceImpl extends ServiceImpl<FansMapper, Fans> implements FansService {

    @Autowired
    private RedisUtils redis;

    @Autowired
    private MessageFeignClient messageFeignClient;

    @Override
    @Transactional
    public void doFollow(String myId, String vlogerId) {
        Fans fans = new Fans();
        fans.setFanId(myId);
        fans.setVlogerId(vlogerId);
        // 判断对方是否关注我，如果关注我，那么双方都要互为朋友关系
        Fans vloger = queryFansRelationship(vlogerId, myId);
        if (vloger != null) {
            fans.setIsFanFriendOfMine(YesOrNo.YES.type);
            vloger.setIsFanFriendOfMine(YesOrNo.YES.type);
            this.baseMapper.updateById(vloger);
        } else {
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
        }
        this.baseMapper.insert(fans);
        // 系统消息：关注
        this.messageFeignClient.createMsg(
                new CreateMsgBo(
                        myId,
                        vlogerId,
                        MessageEnum.FOLLOW_YOU.getType(),
                        null)
        );
    }

    public Fans queryFansRelationship(String fanId, String vlogerId) {
        List<Fans> list = this.baseMapper.selectList(Wrappers.<Fans>lambdaQuery().eq(Fans::getVlogerId, vlogerId).eq(Fans::getFanId, fanId));
        Fans fan = null;
        if (list != null && list.size() > 0 && !list.isEmpty()) {
            fan = list.get(0);
        }
        return fan;
    }

    @Transactional
    @Override
    public void doCancel(String myId, String vlogerId) {
        // 判断我们是否朋友关系，如果是，则需要取消双方的关系
        Fans fan = queryFansRelationship(myId, vlogerId);
        if (fan != null && fan.getIsFanFriendOfMine() == YesOrNo.YES.type) {
            // 抹除双方的朋友关系，自己的关系删除即可
            Fans pendingFan = queryFansRelationship(vlogerId, myId);
            pendingFan.setIsFanFriendOfMine(YesOrNo.NO.type);
            this.baseMapper.updateById(pendingFan);
        }
        // 删除自己的关注关联表记录
        this.baseMapper.deleteById(fan);
    }

    @Override
    public boolean queryDoIFollowVloger(String myId, String vlogerId) {
        Fans vloger = queryFansRelationship(myId, vlogerId);
        return vloger != null;
    }

    @Override
    public PagedGridResult queryMyFollows(String myId, Integer page, Integer pageSize) {
        Page<VlogerVO> vlogerVOPage = this.baseMapper.queryMyFollows(new Page<>(page, pageSize), new HashMap() {{
            this.put("myId", myId);
        }});
        return PageResultUtil.setterPagedGrid(vlogerVOPage);
    }

    @Override
    public PagedGridResult queryMyFans(String myId, Integer page, Integer pageSize) {

        /**
         * <判断粉丝是否是我的朋友（互粉互关）>
         * 普通做法：
         * 多表关联+嵌套关联查询，这样会违反多表关联的规范，不可取，高并发下回出现性能问题
         *
         * 常规做法：
         * 1. 避免过多的表关联查询，先查询我的粉丝列表，获得fansList
         * 2. 判断粉丝关注我，并且我也关注粉丝 -> 循环fansList，获得每一个粉丝，再去数据库查询我是否关注他
         * 3. 如果我也关注他（粉丝），说明，我俩互为朋友关系（互关互粉），则标记flag为true，否则false
         *
         * 高端做法：
         * 1. 关注/取关的时候，关联关系保存在redis中，不要依赖数据库
         * 2. 数据库查询后，直接循环查询redis，避免第二次循环查询数据库的尴尬局面
         */
        Page<FansVO> fansVOPage = this.baseMapper.queryMyFans(
                new Page(page, pageSize),
                new HashMap() {{
                    this.put("myId", myId);
                }}
        );
        for (FansVO f : fansVOPage.getRecords()) {
            String relationship = this.redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + f.getFanId());
            if (StringUtils.isNotBlank(relationship) && relationship.equalsIgnoreCase("1")) {
                f.setFriend(true);
            }
        }
        return PageResultUtil.setterPagedGrid(fansVOPage);
    }

}
