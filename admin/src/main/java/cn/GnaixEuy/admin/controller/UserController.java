package cn.GnaixEuy.admin.controller;


import cn.GnaixEuy.admin.service.UsersService;
import cn.GnaixEuy.admin.utils.ResponseResult;
import cn.GnaixEuy.model.pojo.Users;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping(value = {""})
    public ResponseResult<Page<Users>> search(@PageableDefault(sort = {"createdTime"}) Page page) {
        return ResponseResult.success(this.usersService.page(page));
    }

    @GetMapping(value = {"id/{id}"})
    public ResponseResult<Users> findUserById(@PathVariable String id) {
        return ResponseResult.success(this.usersService.getById(id));
    }

    @GetMapping(value = {"search/{type}/{nickname}"})
    public ResponseResult<Page<Users>> findUserLikeByType(@PageableDefault(sort = {"createdTime"}) Page page, @PathVariable String nickname, @PathVariable String type) {
        QueryWrapper<Users> usersWrapper = new QueryWrapper<>();
        switch (type) {
            case "name":
                usersWrapper.like("nickname", nickname);
                break;
            case "phone":
                usersWrapper.like("mobile", nickname);
                break;
            default:
                usersWrapper.like("nickname", nickname);
        }
        return ResponseResult.success(this.usersService.page(
                page,
                usersWrapper
        ));
    }


    @DeleteMapping(value = {"delete/{userId}"})
    public ResponseResult<String> deleteUser(@PathVariable String userId) {
        System.out.println(userId);
        this.usersService.removeById(userId);
        return ResponseResult.success("删除成功");
    }


}
