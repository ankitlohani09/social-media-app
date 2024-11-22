package com.socialmedia_app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.FeedDTO;
import com.socialmedia_app.dto.SocialMediaAccountDTO;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerFeedRepository;
import com.socialmedia_app.repository.InfluencerRepository;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class InfluencerFeedServiceImplTest {
    @InjectMocks
    private InfluencerFeedServiceImpl influencerServiceImpl;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private InfluencerFeedRepository influencerFeedRepository;
    @Mock
    private InfluencerRepository influencerRepository;
    @Mock
    private SocialMediaServiceImpl socialMediaServiceImpl;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private FeedDTO feedDTO;
    @Mock
    private SocialMediaAccountDTO  socialMediaAccountDTO;

    private String feedDetail;
    private String influencerDetail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedDTO = new FeedDTO();
        feedDTO.setId(1L);
        feedDTO.setContent("img");
        feedDTO.setPlatform("facebook");

        socialMediaAccountDTO = new SocialMediaAccountDTO();
        socialMediaAccountDTO.setId(1L);
        socialMediaAccountDTO.setFacebookAc(true);
        socialMediaAccountDTO.setTwitterAc(true);
        socialMediaAccountDTO.setInstagramAc(true);

        feedDetail = IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream("feed_detail_response.json"), StandardCharsets.UTF_8);
        influencerDetail = IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream("influencer_detail_response.json"), StandardCharsets.UTF_8);
    }

    @Test
    void createFeed() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Feed> feedList = objectMapper.readValue(feedDetail, new TypeReference<List<Feed>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        influencerDetailEntities.get(0).setFeeds(feedList);
        when(influencerRepository.findById(influencerDetailEntities.get(0).getId())).thenReturn(Optional.ofNullable(influencerDetailEntities.get(0)));
        when(socialMediaServiceImpl.getSocialMediaAccByInfluencerId(influencerDetailEntities.get(0).getId())).thenReturn(socialMediaAccountDTO);
        when(influencerFeedRepository.save(feedList.get(0))).thenReturn(feedList.get(0));
        FeedDTO feedDetailResponse = influencerServiceImpl.createFeed(influencerDetailEntities.get(0).getId(),feedDTO);
        assertEquals("facebook", feedDetailResponse.getPlatform());
        assertEquals("img", feedDetailResponse.getContent());
    }
}