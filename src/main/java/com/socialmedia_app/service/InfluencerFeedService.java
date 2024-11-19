package com.socialmedia_app.service;

import com.socialmedia_app.dto.FeedDTO;

public interface InfluencerFeedService {
    FeedDTO createFeed(Long influencerId, FeedDTO feedDTO);
}
