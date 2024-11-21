package com.socialmedia_app.dto;

import com.socialmedia_app.model.Influencer;
import lombok.Data;

@Data
public class FeedDTO {
    private Long id;
    private String content;
    private String platform;
    private Influencer influencer;
}
