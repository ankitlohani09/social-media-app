package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.FeedDTO;
import com.socialmedia_app.exception.NoDataFoundException;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerFeedRepository;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.service.InfluencerFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InfluencerFeedServiceImpl implements InfluencerFeedService {

    @Autowired
    private InfluencerRepository influencerRepository;

    @Autowired
    private InfluencerFeedRepository influencerFeedRepository;

    @Override
    public FeedDTO createFeed(Long influencerId, FeedDTO feedDTO) {
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        if (influencer == null) {
            throw new NoDataFoundException("influencer not found");
        }
        List<Feed> feedList = new ArrayList<>();
        Feed feed = new Feed();
        feed.setContent(feedDTO.getContent());
        feed.setPlatform(feedDTO.getPlatform());
        feedList.add(feed);
        influencer.getFeeds().addAll(feedList);
        influencerFeedRepository.save(feed);
        feedDTO.setId(feed.getId());
        return feedDTO;
    }
}
