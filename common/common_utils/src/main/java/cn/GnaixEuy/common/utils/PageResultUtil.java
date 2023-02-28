package cn.GnaixEuy.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
public class PageResultUtil {
    public static PagedGridResult setterPagedGrid(Page<?> page) {
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(page.getRecords());
        gridResult.setPage((int) page.getCurrent());
        gridResult.setRecords(page.getTotal());
        gridResult.setTotal(page.getPages());
        return gridResult;
    }
}
