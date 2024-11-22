package com.socialmedia_app.service.failuremethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.FeedDTO;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerFeedRepository;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.service.impl.InfluencerFeedServiceImpl;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class InfluencerFeedServiceImplFailureTest {

    @InjectMocks
    private InfluencerFeedServiceImpl influencerServiceImpl;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private InfluencerFeedRepository influencerFeedRepository;
    @Mock
    private InfluencerRepository influencerRepository;
    @Mock
    private FeedDTO feedDTO;

    private String feedDetail;
    private String influencerDetail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedDTO = new FeedDTO();
        feedDTO.setId(1L);
        feedDTO.setContent("img");
        feedDTO.setPlatform("fb");

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
        when(influencerFeedRepository.save(feedList.get(0))).thenReturn(feedList.get(0));
        Long influencerId = influencerDetailEntities.get(0).getId();
        assertThrows(RuntimeException.class, () -> {
            influencerServiceImpl.createFeed(influencerId, feedDTO);
        });
        assertEquals("instagram",feedList.get(0).getPlatform());
        assertEquals("reel",feedList.get(0).getContent());
    }
}
