package cn.GnaixEuy.users.service.impl;

import cn.GnaixEuy.common.enmus.ResponseStatusEnum;
import cn.GnaixEuy.common.enmus.Sex;
import cn.GnaixEuy.common.enmus.UserInfoModifyType;
import cn.GnaixEuy.common.enmus.YesOrNo;
import cn.GnaixEuy.common.exceptions.GraceException;
import cn.GnaixEuy.common.utils.DateUtil;
import cn.GnaixEuy.common.utils.DesensitizationUtil;
import cn.GnaixEuy.model.bo.UpdatedUserBO;
import cn.GnaixEuy.model.pojo.Users;
import cn.GnaixEuy.users.dao.UsersMapper;
import cn.GnaixEuy.users.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users> implements UserService {

    private static final String USER_FACE1 = "http://0.gravatar.com/avatar/92fdb7007ffba99f4348cbc63fb0813b?s=52&d=http%3A%2F%2F121.43.233.5%2Fwp-content%2Fuploads%2F2021%2F04%2Fu32195065192732186892fm26gp0.jpg&r=g";

    @Override
    public Users queryMobileIsExist(String mobile) {
        return this.baseMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getMobile, mobile));
    }

    @Transactional
    @Override
    public Users createUser(String mobile) {
        Users user = new Users();
        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setNum("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE1);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.SECRET.type);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");
        user.setCanNumBeUpdated(YesOrNo.YES.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        this.baseMapper.insert(user);
        return user;
    }

    @Override
    public Users getUser(String userId) {
        Users user = this.getById(userId);
        if (user == null) {
            GraceException.display(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        return user;
    }

    @Transactional
    @Override
    public Users updateUserInfo(UpdatedUserBO updatedUserBO) {
        Users pendingUser = new Users();
        BeanUtils.copyProperties(updatedUserBO, pendingUser);
        boolean result = this.updateById(pendingUser);
        if (!result) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        return getUser(updatedUserBO.getId());
    }

    @Override
    @Transactional
    public Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type) {
        if (Objects.equals(type, UserInfoModifyType.NICKNAME.type)) {
            Users user = this.baseMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getNickname, updatedUserBO.getNickname()));
            if (user != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
        }
        if (Objects.equals(type, UserInfoModifyType.NUM.type)) {
            Users user = this.baseMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getNum, updatedUserBO.getNum()));
            if (user != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
            Users tempUser = getUser(updatedUserBO.getId());
            if (Objects.equals(tempUser.getCanNumBeUpdated(), YesOrNo.NO.type)) {
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_NUM_ERROR);
            }
            updatedUserBO.setCanNumBeUpdated(YesOrNo.NO.type);
        }
        return updateUserInfo(updatedUserBO);
    }
}
