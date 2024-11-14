package com.vrlease.service.Impl;

import cn.hutool.json.JSONUtil;
import com.vrlease.dto.CommentAddDTO;
import com.vrlease.dto.CommentLikeDTO;
import com.vrlease.entity.Comment;
import com.vrlease.mapper.CommentMapper;
import com.vrlease.service.CommentService;
import com.vrlease.websocket.WebSocketServer;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private WebSocketServer webSocketServer;

    // 添加消息队列存储
    private final Map<Long, BlockingQueue<Comment>> messageQueues = new ConcurrentHashMap<>();

    @Override
    public String addComment(CommentAddDTO commentAddDTO) {

        if(commentAddDTO.getParentId() == null){//新开一楼
            Comment comment = new Comment(null, commentAddDTO.getPosterId(), commentAddDTO.getDeviceId(), commentAddDTO.getImages(), null, commentAddDTO.getScore(), null, null, commentAddDTO.getWords(), commentAddDTO.getVideo(), null, null, null);
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());
            commentMapper.addcomment(comment);
            return "success";
        }
        else{//回复
            Comment comment = new Comment(null, commentAddDTO.getPosterId(), commentAddDTO.getDeviceId(), commentAddDTO.getImages(), null, commentAddDTO.getScore(), null, null, commentAddDTO.getWords(), commentAddDTO.getVideo(), null, null, null);
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());
            comment.setParentId(Long.valueOf(commentAddDTO.getParentId()));
            comment.setRootId(Long.valueOf(commentAddDTO.getRootId()));
            commentMapper.addcomment(comment);
            //楼回复数+1
            commentMapper.plusRepliesCount(comment.getRootId());
            //通知被回复者和楼主有新消息

            // 发送通知给被回复者
            sendNotification(comment, Long.valueOf(commentAddDTO.getParentId()));
            // 发送通知给楼主
            if (!commentAddDTO.getRootId().equals(commentAddDTO.getParentId())) {
                sendNotification(comment, Long.valueOf(commentAddDTO.getRootId()));
            }

            return "success";
        }
    }

    // 添加发送通知的方法
    private void sendNotification(Comment comment, Long receiverId) {
        BlockingQueue<Comment> queue = messageQueues.computeIfAbsent(receiverId, 
                k -> new LinkedBlockingQueue<>());
        queue.offer(comment);
    }

    // 添加长轮询获取消息的方法
    public String pollMessages(Long userId, long timeout) {
        List<Comment> messages = new ArrayList<>();
        BlockingQueue<Comment> queue = messageQueues.get(userId);
        
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
            messageQueues.put(userId, queue);
        }
        
        try {
            Comment message = queue.poll(timeout, TimeUnit.MILLISECONDS);
            if (message != null) {
                messages.add(message);
                // 一次性获取队列中的所有消息
                queue.drainTo(messages);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Map map = new HashMap();
        map.put("messages", messages);
        return JSONUtil.toJsonStr(map);
    }

    @Override
    @ApiOperation("没写完")
    public String like(CommentLikeDTO commentLikeDTO) {
        Long likedid = commentLikeDTO.getLikedCommentid();
        Long userId = commentLikeDTO.getUserId();
        Long likedUserId = commentLikeDTO.getLikedUserId();
        commentMapper.addlike(likedid);

        return "success";
    }
}
