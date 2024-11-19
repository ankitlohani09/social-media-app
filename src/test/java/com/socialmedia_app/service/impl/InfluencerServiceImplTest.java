package com.socialmedia_app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.InfluencerDTO;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerRepository;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InfluencerServiceImplTest {
    @InjectMocks
    private InfluencerServiceImpl influencerServiceImpl;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private InfluencerRepository influencerRepository;
    @Mock
    private InfluencerDTO influencerDTO;

    private String influencerDetail;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        influencerDTO = new InfluencerDTO();
        influencerDTO.setId(1L);
        influencerDTO.setUsername("mohini");
        influencerDTO.setEmail("mohini@gmail.com");
        influencerDTO.setPassword("mohini");
//        influencerDTO.setFeeds();

        influencerDetail = IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream("influencer_detail_response.json"), StandardCharsets.UTF_8);

    }

    @Test
    void getAllInfluencers() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Influencer> influencerEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        when(influencerRepository.findAll()).thenReturn(influencerEntities);
        List<InfluencerDTO> allInfluencers = influencerServiceImpl.getAllInfluencers();
        assertEquals("jannat", allInfluencers.get(0).getUsername());
        assertEquals("jannat@gmail.com", allInfluencers.get(0).getEmail());
    }

    @Test
    void getInfluencerById() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Influencer> influencerEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        when(influencerRepository.findById(influencerDTO.getId())).thenReturn(Optional.of(influencerEntities.get(0)));
        InfluencerDTO userDetailResponse = influencerServiceImpl.getInfluencerById(influencerDTO.getId());
        assertEquals("jannat", userDetailResponse.getUsername());
        assertEquals("jannat@gmail.com", userDetailResponse.getEmail());
    }

    @Test
    void createInfluencer() {
        Influencer influencer = new Influencer();
        influencer.setUsername("mohini");
        influencer.setEmail("mohini@gmail.com");
        influencer.setPassword("mohini");

        InfluencerDTO influencerDTO = new InfluencerDTO();
        modelMapper.map(influencer, InfluencerDTO.class);
        BeanUtils.copyProperties(influencer, influencerDTO);

        when(influencerRepository.save(influencer)).thenReturn(influencer);
        InfluencerDTO influencerDTO1 = influencerServiceImpl.createInfluencer(influencerDTO);
        assertEquals("mohini", influencerDTO1.getUsername());
        assertEquals("mohini@gmail.com", influencerDTO1.getEmail());
    }

    @Test
    void updateInfluencer() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Influencer> influencerList = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer = influencerList.get(0);
        when(influencerRepository.findById(influencerDTO.getId())).thenReturn(Optional.of(influencer));
        InfluencerDTO influencerDTO1 = influencerServiceImpl.updateInfluencer(influencerList.get(0).getId(), influencerDTO);
        assertEquals("mohini", influencerDTO1.getUsername());
        assertEquals("mohini@gmail.com", influencerDTO1.getEmail());
    }

    @Test
    void deleteInfluencer() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Influencer> influencerListEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        when(influencerRepository.findById(influencerDTO.getId())).thenReturn(Optional.of(influencerListEntities.get(0)));
        influencerServiceImpl.deleteInfluencer(influencerDTO.getId());
    }

}