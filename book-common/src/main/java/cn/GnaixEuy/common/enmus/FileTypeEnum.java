package cn.GnaixEuy.common.enmus;

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
 * @Desc: 文件类型 枚举
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    BGIMG(1, "用户背景图"),
    FACE(2, "用户头像");

    public final Integer type;
    public final String value;

}

