package cn.GnaixEuy.common.enmus;

import cn.GnaixEuy.common.exceptions.GraceException;
import cn.GnaixEuy.common.utils.result.ResponseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2-new </p>
 * 创建日期： 2023/2/27
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Getter
@AllArgsConstructor
public enum UserInfoModifyType {
    NICKNAME(1, "昵称"),
    NUM(2, "id号"),
    SEX(3, "性别"),
    BIRTHDAY(4, "生日"),
    LOCATION(5, "所在地"),
    DESC(6, "简介");

    public final Integer type;
    public final String value;


    public static void checkUserInfoTypeIsRight(Integer type) {
        if (type != UserInfoModifyType.NICKNAME.type &&
                type != UserInfoModifyType.NUM.type &&
                type != UserInfoModifyType.SEX.type &&
                type != UserInfoModifyType.BIRTHDAY.type &&
                type != UserInfoModifyType.LOCATION.type &&
                type != UserInfoModifyType.DESC.type) {
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
        }
    }
}

