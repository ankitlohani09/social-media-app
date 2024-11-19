package com.socialmedia_app.dto;

import com.socialmedia_app.model.Influencer;
import lombok.Data;

@Data
public class SocialMediaAccountDTO {
    private Long id;
    private boolean facebookAc;
    private boolean twitterAc;
    private boolean instagramAc;
    private Influencer influencer;
}
