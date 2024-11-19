package com.socialmedia_app.service;

import com.socialmedia_app.dto.SocialMediaAccountDTO;

import java.util.List;

public interface SocialMediaService {
    List<SocialMediaAccountDTO> getAllSocialMediaAccounts();
    SocialMediaAccountDTO getSocialMediaAccByInfluencerId(Long influencerId);
    SocialMediaAccountDTO addSocialMediaAccount(Long influencerId, SocialMediaAccountDTO socialMediaAccount);
    SocialMediaAccountDTO updateSocialMediaAccount(Long influencerId,SocialMediaAccountDTO socialMediaAccount);
    void deleteSocialMediaAccount(Long influencerId);
}
