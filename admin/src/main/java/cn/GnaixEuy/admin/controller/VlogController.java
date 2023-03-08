package cn.GnaixEuy.admin.controller;

import cn.GnaixEuy.admin.service.VlogService;
import cn.GnaixEuy.admin.utils.ResponseResult;
import cn.GnaixEuy.model.pojo.Vlog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/6
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@RestController
@RequestMapping(value = {"vlog"})
public class VlogController {

    @Autowired
    private VlogService vlogService;

    @GetMapping(value = {""})
    public ResponseResult<Page<Vlog>> page(@PageableDefault(sort = {"createdTime"}) Page<Vlog> page) {
        return ResponseResult.success(this.vlogService.page(page));
    }

    @GetMapping(value = {"search/{vlogerId}"})
    public ResponseResult<Page<Vlog>> search(@PageableDefault(sort = {"createdTime"}) Page<Vlog> page, @PathVariable String vlogerId) {
        return ResponseResult.success(this.vlogService.page(
                page,
                Wrappers.<Vlog>lambdaQuery()
                        .eq(Vlog::getVlogerId, vlogerId))
        );
    }

    @PutMapping(value = {"recommend/{id}/{recommendNumber}"})
    public ResponseResult<String> recommendVlog(@PathVariable String id, @PathVariable String recommendNumber) {
        Vlog vlog = this.vlogService.getById(id);
        vlog.setRecommend(Integer.valueOf(recommendNumber));
        this.vlogService.updateById(vlog);
        return ResponseResult.success("更新成功");
    }

    @DeleteMapping(value = {"{vlogId}"})
    public ResponseResult<String> delete(@PathVariable String vlogId) {
        this.vlogService.removeById(vlogId);
        return ResponseResult.success("移除成功");
    }

    @PutMapping(value = {"/privat/{vlogId}"})
    private ResponseResult<String> privat(@PathVariable String vlogId) {
        Vlog vlog = this.vlogService.getById(vlogId);
        if (vlog.getIsPrivate() == 0) {
            vlog.setIsPrivate(1);
        } else {
            vlog.setIsPrivate(0);
        }
        this.vlogService.updateById(vlog);
        return ResponseResult.success("更新成功");
    }

}
