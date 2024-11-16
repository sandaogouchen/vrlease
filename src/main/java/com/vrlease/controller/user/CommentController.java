package com.vrlease.controller.user;

import com.vrlease.dto.CommentAddDTO;
import com.vrlease.dto.CommentLikeDTO;
import com.vrlease.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/user/comment")
@RestController
@Api("评论")
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/addComment")
    @ApiOperation( "添加,回复评论")
    public String addComment(@RequestBody CommentAddDTO commentAddDTO) {
        return commentService.addComment(commentAddDTO);
    }

    @PostMapping("/checkNew")
    @ApiOperation("获取新消息(长轮询)")
    public String pollMessages(@RequestBody Long userId,@RequestBody long timeout) {
        return commentService.pollMessages(userId, timeout);
    }

    @PostMapping("/like")
    @ApiOperation("点赞")
    public String like(@RequestBody CommentLikeDTO commentLikeDTO) {
        return commentService.like(commentLikeDTO);
    }

}
