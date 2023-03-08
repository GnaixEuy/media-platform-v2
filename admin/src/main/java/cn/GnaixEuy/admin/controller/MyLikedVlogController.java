package cn.GnaixEuy.admin.controller;

import cn.GnaixEuy.admin.service.MyLikedVlogService;
import cn.GnaixEuy.admin.utils.ResponseResult;
import cn.GnaixEuy.admin.vo.AdminLikeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/8
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@RestController
@RequestMapping(value = "like")
public class MyLikedVlogController {

    @Autowired
    private MyLikedVlogService myLikedVlogService;

    @GetMapping(value = {"{vlogId}"})
    public ResponseResult<List<AdminLikeInfoVo>> getLikePeopleByVlogId(@PathVariable String vlogId) {
        return ResponseResult.success(this.myLikedVlogService.getLikePeopleByVlogId(vlogId));
    }

}
