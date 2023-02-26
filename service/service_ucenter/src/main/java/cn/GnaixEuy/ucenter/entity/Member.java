package cn.GnaixEuy.ucenter.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/26
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Data
public class Member {
    @ApiModelProperty(value = "会员id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdDateTime;
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedDateTime;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "性别 1 女，2 男")
    private Integer sex;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "用户签名")
    private String sign;
    @ApiModelProperty(value = "地区")
    private String area;
    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "是否禁用 1（true）已禁用，  0（false）未禁用")
    private Boolean isDisabled;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

}
