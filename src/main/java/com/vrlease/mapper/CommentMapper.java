package com.vrlease.mapper;

import com.vrlease.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.Size;

@Mapper
public interface CommentMapper {

    void addcomment(Comment comment);

    void plusRepliesCount(@Size(max= -1,message="编码长度不能超过-1") Long id);

    void addlike(Long id);
}
