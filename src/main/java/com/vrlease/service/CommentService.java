package com.vrlease.service;

import com.vrlease.dto.CommentAddDTO;
import com.vrlease.dto.CommentLikeDTO;

public interface CommentService {
    String addComment(CommentAddDTO commentAddDTO);

    String pollMessages(Long userId, long timeout);

    String like(CommentLikeDTO commentLikeDTO);
}
