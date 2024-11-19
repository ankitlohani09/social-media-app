package com.socialmedia_app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.User;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.UserRepository;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private InfluencerRepository influencerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDTO userDTO;
    @Mock
    private Influencer influencer;

    private String userDetail;

    private String influencerDetail;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("suraj");
        userDTO.setPassword("12345");
        userDTO.setEmail("suraj@gmail.com");
        userDTO.setPhone("1234567890");

        Influencer influencer = new Influencer();
        influencer.setId(1L);
        influencer.setUsername("influencerOye");
        influencer.setPassword("oye");
        influencer.setEmail("influencerOye@gmail.com");

        userDetail = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("user_detail_api_response.json"), StandardCharsets.UTF_8);
        influencerDetail = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("influencer_detail_response.json"), StandardCharsets.UTF_8);
    }

    @Test
    void getAllUsers() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findAll()).thenReturn(userDetailEntities);
        List<UserDTO> userDetailResponseList = userService.getAllUsers();
        assertEquals("ankitlohani", userDetailResponseList.get(0).getUsername());
        assertEquals("test", userDetailResponseList.get(0).getPassword());
    }

    @Test
    void getUserByEmail() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(userDetailEntities.get(0));
        UserDTO userDetailResponse = userService.getUserByEmail(userDTO.getEmail());
        assertEquals("ankitlohani", userDetailResponse.getUsername());
        assertEquals("test", userDetailResponse.getPassword());
    }

    @Test
    void registerUser() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        Mockito.when(userRepository.save(userDetailEntities.get(0))).thenReturn(userDetailEntities.get(0));
        UserDTO userDetailResponse = userService.registerUser(userDTO);
        assertEquals("suraj", userDetailResponse.getUsername());
        assertEquals("1234567890", userDetailResponse.getPhone());
    }

    @Test
    void updateUser() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        UserDTO userDetailResponse = userService.updateUser(userDTO.getId(), userDTO);
        assertEquals("suraj", userDetailResponse.getUsername());
        assertEquals("12345", userDetailResponse.getPassword());
    }

    @Test
    void deleteUser() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        userService.deleteUser(userDTO.getId());
    }

    @Test
    void unFollowInfluencer() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer1 = influencerDetailEntities.get(0);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        when(influencerRepository.findById(influencer.getId())).thenReturn(Optional.of(influencerDetailEntities.get(0)));

        UserDTO userDetailResponse = userService.unFollowInfluencer(userDTO.getId(), influencer1.getId());
        assertEquals("ankitlohani", userDetailResponse.getUsername());
        assertEquals("test", userDetailResponse.getPassword());
    }

    @Test
    void followInfluencer() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer1 = influencerDetailEntities.get(0);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        when(influencerRepository.findById(influencer.getId())).thenReturn(Optional.of(influencerDetailEntities.get(0)));
        UserDTO userDetailResponse = userService.followInfluencer(userDTO.getId(), influencer1.getId());
        assertEquals("ankitlohani", userDetailResponse.getUsername());
        assertEquals("test", userDetailResponse.getPassword());
    }

    @Test
    void getFollowedInfluencers() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        List<Influencer> userDetailResponse = userService.getFollowedInfluencers(userDTO.getId());
        assertEquals("jannat", userDetailResponse.get(0).getUsername());
        assertEquals("test", userDetailResponse.get(0).getPassword());
    }

    @Test
    void getFeedsByUserAndPlatform() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        List<Feed> feedDetails = userService.getFeedsByUserAndPlatform(userDTO.getId(),"ig");
        assertEquals("ig", feedDetails.get(0).getPlatform());
        assertEquals(1L, feedDetails.get(0).getId());
    }
}