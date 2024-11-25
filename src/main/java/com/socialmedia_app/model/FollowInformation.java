package com.socialmedia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followInformationId;

    private LocalDateTime followedDateTime;

    private LocalDateTime updateFollowDateTime;

    private String userFollowName;

    private String influencerFollowName;

    private boolean isFollowed;
}
