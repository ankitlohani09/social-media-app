package com.socialmedia_app.service;

import com.socialmedia_app.dto.InfluencerDTO;

import java.util.List;

public interface InfluencerService {
    List<InfluencerDTO> getAllInfluencers();
    InfluencerDTO getInfluencerById(Long id);
    InfluencerDTO createInfluencer(InfluencerDTO influencer);
    InfluencerDTO updateInfluencer(Long id, InfluencerDTO influencer);
    void deleteInfluencer(Long id);
}
