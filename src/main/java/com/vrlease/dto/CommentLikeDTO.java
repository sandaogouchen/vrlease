package com.vrlease.dto;

import lombok.Data;

@Data
public class CommentLikeDTO {
    private Long likedCommentid;
    private Long userId;
    private Long likedUserId;
}
;