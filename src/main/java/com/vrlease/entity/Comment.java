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
* @TableName comment
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @ApiModelProperty("主键")
    private Long id;
    /**
    * 评论者
    */
    @NotBlank(message="[评论者]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("评论者")
    //@Length(max= 128,message="编码长度不能超过128")
    private Long posterId;
    /**
    * 设备商品id
    */
    @NotBlank(message="[设备商品id]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("设备商品id")
    //@Length(max= 128,message="编码长度不能超过128")
    private Long deviceId;
    /**
    * 图片
    */
    @Size(max= 1024,message="编码长度不能超过1024")
    @ApiModelProperty("图片")
    //@Length(max= 1,024,message="编码长度不能超过1,024")
    private String images;
    /**
    * 点赞数
    */
    @ApiModelProperty("点赞数")
    private Integer goods;
    /**
    * 评分，1~5分，乘10保存，避免小数
    */
    @ApiModelProperty("评分，1~5分，乘10保存，避免小数")
    private Integer score;
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
    /**
    * 
    */
    @Size(max= 3000,message="编码长度不能超过3000")
    @ApiModelProperty("")
    //@Length(max= 3,000,message="编码长度不能超过3,000")
    private String words;
    /**
    * 
    */
    @Size(max= 300,message="编码长度不能超过300")
    @ApiModelProperty("")
    //@Length(max= 300,message="编码长度不能超过300")
    private String video;
    /**
    * 父评论
    */
    @Size(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty("父评论")
    //@Length(max= -1,message="编码长度不能超过-1")
    private Long parentId;
    /**
    * 根评论id
    */
    @Size(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty("根评论id")
    //@Length(max= -1,message="编码长度不能超过-1")
    private Long rootId;
    /**
    * 评论数
    */
    @ApiModelProperty("评论数")
    private Integer repliesCount;


    /**
    * 主键
    */
//    private void setId(Long id){
//    this.id = id;
//    }
//
//    /**
//    * 评论者
//    */
//    private void setPosterId(String posterId){
//    this.posterId = posterId;
//    }
//
//    /**
//    * 设备商品id
//    */
//    private void setDeviceId(String deviceId){
//    this.deviceId = deviceId;
//    }
//
//    /**
//    * 图片
//    */
//    private void setImages(String images){
//    this.images = images;
//    }
//
//    /**
//    * 点赞数
//    */
//    private void setGoods(Integer goods){
//    this.goods = goods;
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
//    /**
//    *
//    */
//    private void setWords(String words){
//    this.words = words;
//    }
//
//    /**
//    *
//    */
//    private void setVideo(String video){
//    this.video = video;
//    }
//
//    /**
//    * 父评论
//    */
//    private void setParentId(String parentId){
//    this.parentId = parentId;
//    }
//
//    /**
//    * 根评论id
//    */
//    private void setRootId(String rootId){
//    this.rootId = rootId;
//    }
//
//    /**
//    * 评论数
//    */
//    private void setRepliesCount(Integer repliesCount){
//    this.repliesCount = repliesCount;
//    }
//
//    /**
//    * 回复哪条评论的id
//    */
//    private void setReplyUserId(String replyUserId){
//    this.replyUserId = replyUserId;
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
//    * 评论者
//    */
//    private String getPosterId(){
//    return this.posterId;
//    }
//
//    /**
//    * 设备商品id
//    */
//    private String getDeviceId(){
//    return this.deviceId;
//    }
//
//    /**
//    * 图片
//    */
//    private String getImages(){
//    return this.images;
//    }
//
//    /**
//    * 点赞数
//    */
//    private Integer getGoods(){
//    return this.goods;
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
//
//    /**
//    *
//    */
//    private String getWords(){
//    return this.words;
//    }
//
//    /**
//    *
//    */
//    private String getVideo(){
//    return this.video;
//    }
//
//    /**
//    * 父评论
//    */
//    private String getParentId(){
//    return this.parentId;
//    }
//
//    /**
//    * 根评论id
//    */
//    private String getRootId(){
//    return this.rootId;
//    }
//
//    /**
//    * 评论数
//    */
//    private Integer getRepliesCount(){
//    return this.repliesCount;
//    }
//
//    /**
//    * 回复哪条评论的id
//    */
//    private String getReplyUserId(){
//    return this.replyUserId;
//    }

}
