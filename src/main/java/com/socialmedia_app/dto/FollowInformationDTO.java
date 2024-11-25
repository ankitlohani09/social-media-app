package com.socialmedia_app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowInformationDTO {

    private Long followInformationId;

    private LocalDateTime followedDateTime;

    private LocalDateTime updateFollowDateTime;

    private String userFollowName;

    private String influencerFollowName;

    private boolean isFollowed;

}
