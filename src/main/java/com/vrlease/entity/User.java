package com.vrlease.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* 
* @TableName user
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @ApiModelProperty("主键")
    private Long id;
    /**
    * 手机号码
    */
    @NotBlank(message="[手机号码]不能为空")
    @Size(max= 11,message="编码长度不能超过11")
    @ApiModelProperty("手机号码")
    //@Length(max= 11,message="编码长度不能超过11")
    private String phone;
    /**
    * 密码，加密存储
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("密码，加密存储")
    //@Length(max= 128,message="编码长度不能超过128")
    private String password;
    /**
    * 昵称，默认是用户id
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("昵称，默认是用户id")
    //@Length(max= 32,message="编码长度不能超过32")
    private String username;
    /**
    * 人物头像
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("人物头像")
    //@Length(max= 255,message="编码长度不能超过255")
    private String icon;
    /**
    * 创建时间
    */
    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @NotNull(message="[更新时间]不能为空")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("微信用户唯一标识")
    //微信用户唯一标识
    private String openid;

    private String mail;

    /**
    * 主键
    */

}
