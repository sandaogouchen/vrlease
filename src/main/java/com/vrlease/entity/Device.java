package com.vrlease.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 
* @TableName device
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @ApiModelProperty("主键")
    private Long id;
    /**
    * 商品
    */
    @NotBlank(message="[商品]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("商品")
    //@Length(max= 128,message="编码长度不能超过128")
    private String name;
    /**
    * 品牌
    */
    @NotBlank(message="[品牌]不能为空")
    @Size(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty("品牌")
    //@Length(max= -1,message="编码长度不能超过-1")
    private String brandId;
    /**
    * 商铺图片，多个图片以','隔开
    */
    @Size(max= 1024,message="编码长度不能超过1024")
    @ApiModelProperty("商铺图片，多个图片以','隔开")
    //@Length(max= 1,024,message="编码长度不能超过1,024")
    private String images;
    /**
    * 销量
    */
    @ApiModelProperty("销量")
    private Integer sold;
    /**
    * 评论数量
    */
    @ApiModelProperty("评论数量")
    private Integer comments;
    /**
    * 评分，1~5分，乘10保存，避免小数
    */
    @ApiModelProperty("评分，1~5分，乘10保存，避免小数")
    private Integer score;
    /**
    * 营业时间，例如 10:00-22:00
    */

    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("剩余设备数量")
    private Integer number;
    /**
    * 主键
    */
//    private void setId(Long id){
//    this.id = id;
//    }
//
//    /**
//    * 商品
//    */
//    private void setName(String name){
//    this.name = name;
//    }
//
//    /**
//    * 品牌
//    */
//    private void setBrandId(String brandId){
//    this.brandId = brandId;
//    }
//
//    /**
//    * 商铺图片，多个图片以','隔开
//    */
//    private void setImages(String images){
//    this.images = images;
//    }
//
//    /**
//    * 销量
//    */
//    private void setSold(Integer sold){
//    this.sold = sold;
//    }
//
//    /**
//    * 评论数量
//    */
//    private void setComments(Integer comments){
//    this.comments = comments;
//    }
//
//    /**
//    * 评分，1~5分，乘10保存，避免小数
//    */
//    private void setScore(Integer score){
//    this.score = score;
//    }
//
//    /**
//    * 营业时间，例如 10:00-22:00
//    */
//    private void setOpenHours(String openHours){
//    this.openHours = openHours;
//    }
//
//    /**
//    * 创建时间
//    */
//    private void setCreateTime(Date createTime){
//    this.createTime = createTime;
//    }
//
//    /**
//    * 更新时间
//    */
//    private void setUpdateTime(Date updateTime){
//    this.updateTime = updateTime;
//    }
//
//
//    /**
//    * 主键
//    */
//    private Long getId(){
//    return this.id;
//    }
//
//    /**
//    * 商品
//    */
//    private String getName(){
//    return this.name;
//    }
//
//    /**
//    * 品牌
//    */
//    private String getBrandId(){
//    return this.brandId;
//    }
//
//    /**
//    * 商铺图片，多个图片以','隔开
//    */
//    private String getImages(){
//    return this.images;
//    }
//
//    /**
//    * 销量
//    */
//    private Integer getSold(){
//    return this.sold;
//    }
//
//    /**
//    * 评论数量
//    */
//    private Integer getComments(){
//    return this.comments;
//    }
//
//    /**
//    * 评分，1~5分，乘10保存，避免小数
//    */
//    private Integer getScore(){
//    return this.score;
//    }
//
//    /**
//    * 营业时间，例如 10:00-22:00
//    */
//    private String getOpenHours(){
//    return this.openHours;
//    }
//
//    /**
//    * 创建时间
//    */
//    private Date getCreateTime(){
//    return this.createTime;
//    }
//
//    /**
//    * 更新时间
//    */
//    private Date getUpdateTime(){
//    return this.updateTime;
//    }

}
