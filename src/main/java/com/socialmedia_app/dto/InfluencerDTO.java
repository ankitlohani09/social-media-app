package com.socialmedia_app.dto;

import com.socialmedia_app.model.Feed;
import lombok.Data;

import java.util.List;

@Data
public class InfluencerDTO {
    private Long id;
    private String username;
    private String password;
    private String email;

    private List<Feed> feeds;
}
