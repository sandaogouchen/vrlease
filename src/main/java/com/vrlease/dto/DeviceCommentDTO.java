package com.vrlease.dto;

import lombok.Data;

@Data
public class DeviceCommentDTO {
    private String words;
    private Long deviceId;
    private Long userId;
    private Integer score;
    private String images;
    private String video;
    private String parentId;
    private String replyUserId;
    private String rootId;

}
