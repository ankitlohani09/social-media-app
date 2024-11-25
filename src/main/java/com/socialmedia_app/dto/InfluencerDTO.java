package com.socialmedia_app.dto;

import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.Role;
import com.socialmedia_app.model.Theme;
import lombok.Data;

import java.util.List;

@Data
public class InfluencerDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean isDeleted;
    private Theme theme;
    private Role role;
    private List<Feed> feeds;
}
