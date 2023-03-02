package cn.GnaixEuy.vlog.dao;

import cn.GnaixEuy.model.pojo.Vlog;
import cn.GnaixEuy.model.vo.IndexVlogVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
@Mapper
public interface VlogMapper extends BaseMapper<Vlog> {

    Page<IndexVlogVO> getIndexVlogList(Page page, @Param("paramMap") Map<String, Object> map);

    List<IndexVlogVO> getVlogDetailById(@Param("paramMap") Map<String, Object> map);

    Page<IndexVlogVO> getMyLikedVlogList(Page page, @Param("paramMap") Map<String, Object> map);

    Page<IndexVlogVO> getMyFollowVlogList(Page page, @Param("paramMap") Map<String, Object> map);

    Page<IndexVlogVO> getMyFriendVlogList(Page page, @Param("paramMap") Map<String, Object> map);

}