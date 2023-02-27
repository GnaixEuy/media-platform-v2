package cn.GnaixEuy.common.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @Desc: 性别 枚举
 * <p>项目： media-v2-new </p>
 * 创建日期： 2023/2/27
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Getter
@AllArgsConstructor
public enum Sex {
    WOMAN(0, "女"), MAN(1, "男"), SECRET(2, "保密");

    public final Integer type;
    public final String value;
}

