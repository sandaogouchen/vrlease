package com.vrlease.dto;

import lombok.Data;

@Data
public class CommentAddDTO {
    private Long posterId;
    private Long deviceId;
    private String images;
    private String words;
    private String video;
    private Long parentId;
    private Long rootId;
    private Integer score;

}
