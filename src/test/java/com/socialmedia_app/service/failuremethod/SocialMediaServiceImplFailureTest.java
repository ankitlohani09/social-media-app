package com.socialmedia_app.service.failuremethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.SocialMediaAccountDTO;
import com.socialmedia_app.exception.NoDataFoundException;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.SocialMediaAccount;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.SocialMediaRepository;
import com.socialmedia_app.service.impl.SocialMediaServiceImpl;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SocialMediaServiceImplFailureTest {
    @InjectMocks
    private SocialMediaServiceImpl socialMediaServiceImpl;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private SocialMediaRepository socialMediaRepository;
    @Mock
    private InfluencerRepository influencerRepository;
    @Mock
    private SocialMediaAccountDTO socialMediaAccountDTO;

    private String socialMediaDetail;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Influencer influencerMock = new Influencer();
        influencerMock.setId(1L);
        influencerMock.setUsername("influencerOye");
        influencerMock.setPassword("oye");
        influencerMock.setEmail("influencerOye@gmail.com");

        socialMediaAccountDTO = new SocialMediaAccountDTO();
        socialMediaAccountDTO.setFacebookAc(true);
        socialMediaAccountDTO.setInstagramAc(true);
        socialMediaAccountDTO.setTwitterAc(false);
        socialMediaAccountDTO.setInfluencer(influencerMock);

        socialMediaDetail = IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream("social_media_detail_response.json"), StandardCharsets.UTF_8);
    }

    @Test
    void getSocialMediaAccByInfluencerIdFailure() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<SocialMediaAccount> socialMediaAccountEntities = objectMapper.readValue(socialMediaDetail, new TypeReference<List<SocialMediaAccount>>() {});
        assertThrows(NoDataFoundException.class, () -> {
            socialMediaAccountDTO = socialMediaServiceImpl.getSocialMediaAccByInfluencerId(socialMediaAccountEntities.get(0).getInfluencer().getId());
        });
        if (socialMediaAccountDTO != null) {
          assertEquals("influencerOye", socialMediaAccountDTO.getInfluencer().getUsername());
            assertEquals("test", socialMediaAccountEntities.get(0).getInfluencer().getPassword());
      }
    }

    @Test
    void addSocialMediaAccount() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<SocialMediaAccount> socialMediaAccountEntities = objectMapper.readValue(socialMediaDetail, new TypeReference<List<SocialMediaAccount>>() {});
        when(socialMediaRepository.save(socialMediaAccountEntities.get(0))).thenReturn(socialMediaAccountEntities.get(0));
        socialMediaServiceImpl.addSocialMediaAccount(socialMediaAccountEntities.get(0).getInfluencer().getId(),socialMediaAccountDTO);
        assertEquals("modi", socialMediaAccountEntities.get(0).getInfluencer().getUsername());
        assertEquals("test", socialMediaAccountEntities.get(0).getInfluencer().getPassword());
    }

    @Test
    void updateSocialMediaAccount() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<SocialMediaAccount> socialMediaAccountEntities = objectMapper.readValue(socialMediaDetail, new TypeReference<List<SocialMediaAccount>>() {});
//        when(influencerRepository.findById(socialMediaAccountEntities.get(0).getInfluencer().getId())).thenReturn(Optional.ofNullable(socialMediaAccountEntities.get(0).getInfluencer()));
        when(socialMediaRepository.findById(socialMediaAccountEntities.get(0).getInfluencer().getId())).thenReturn(Optional.ofNullable(socialMediaAccountEntities.get(0)));
        when(socialMediaRepository.save(socialMediaAccountEntities.get(0))).thenReturn(socialMediaAccountEntities.get(0));
        socialMediaServiceImpl.updateSocialMediaAccount(socialMediaAccountEntities.get(0).getInfluencer().getId(), socialMediaAccountDTO);
        assertEquals("modi", socialMediaAccountEntities.get(0).getInfluencer().getUsername());
        assertEquals("test", socialMediaAccountEntities.get(0).getInfluencer().getPassword());
    }
}
