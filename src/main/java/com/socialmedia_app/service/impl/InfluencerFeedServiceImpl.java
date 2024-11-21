package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.FeedDTO;
import com.socialmedia_app.dto.SocialMediaAccountDTO;
import com.socialmedia_app.exception.NoDataFoundException;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerFeedRepository;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.service.InfluencerFeedService;
import com.socialmedia_app.utils.Constants;
import org.springframework.stereotype.Service;

@Service
public class InfluencerFeedServiceImpl implements InfluencerFeedService {

    private final InfluencerRepository influencerRepository;

    private final InfluencerFeedRepository influencerFeedRepository;

    private final SocialMediaServiceImpl socialMediaServiceImpl;

    public InfluencerFeedServiceImpl(InfluencerRepository influencerRepository, InfluencerFeedRepository influencerFeedRepository, SocialMediaServiceImpl socialMediaServiceImpl) {
        this.influencerRepository = influencerRepository;
        this.influencerFeedRepository = influencerFeedRepository;
        this.socialMediaServiceImpl = socialMediaServiceImpl;
    }

    @Override
    public FeedDTO createFeed(Long influencerId, FeedDTO feedDTO) {
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        if (influencer == null) {
            throw new NoDataFoundException("influencer not found");
        }
        SocialMediaAccountDTO socialMediaAccByInfluencerId = socialMediaServiceImpl.getSocialMediaAccByInfluencerId(influencerId);
        if (isValidPlatformInput(feedDTO)) {
            validatePlatformAccess(socialMediaAccByInfluencerId, feedDTO);
            Feed feed = new Feed();
            feed.setContent(feedDTO.getContent());
            feed.setPlatform(feedDTO.getPlatform());
            feed.setInfluencerName(influencer.getUsername());
            feed.setInfluencer(influencer);
            influencer.getFeeds().add(feed);
            influencerFeedRepository.save(feed);
            feedDTO.setId(feed.getId());
            feedDTO.setInfluencer(influencer);
            return feedDTO;
        } else {
            throw new IllegalArgumentException("invalid platform");
        }
    }

    private boolean isValidPlatformInput(FeedDTO feedDTO) {
        return feedDTO.getPlatform().equalsIgnoreCase(Constants.FACEBOOK) ||
                feedDTO.getPlatform().equalsIgnoreCase(Constants.INSTAGRAM) ||
                feedDTO.getPlatform().equalsIgnoreCase(Constants.TWITTER);
    }

    private void validatePlatformAccess(SocialMediaAccountDTO socialMediaAcc, FeedDTO feedDTO) {
        String platform = feedDTO.getPlatform();
        if (!socialMediaAcc.isFacebookAc() && platform.equalsIgnoreCase(Constants.FACEBOOK)) {
            throw new NoDataFoundException("influencer feed cannot be created, because Facebook Account not found");
        }
        if (!socialMediaAcc.isTwitterAc() && platform.equalsIgnoreCase(Constants.TWITTER)) {
            throw new NoDataFoundException("influencer feed cannot be created, because Twitter Account not found");
        }
        if (!socialMediaAcc.isInstagramAc() && platform.equalsIgnoreCase(Constants.INSTAGRAM)) {
            throw new NoDataFoundException("influencer feed cannot be created, because Instagram Account not found");
        }
    }
}
