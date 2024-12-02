package com.socialmedia_app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.exception.DataAlreadyExistException;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.model.FollowInformation;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.User;
import com.socialmedia_app.repository.FollowInformationRepo;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.UserRepository;
import com.socialmedia_app.utils.Role;
import com.socialmedia_app.utils.Theme;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowInformationRepo followInformationRepo;
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
    @Mock
    private Feed feedMock;
    @Mock
    private List<Feed> feedsMockList;

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
        userDTO.setTheme(Theme.LIGHT);
        userDTO.setRole(Role.USER);

        influencer = new Influencer();
        influencer.setId(1L);
        influencer.setUsername("influencerOye");
        influencer.setPassword("oye");
        influencer.setEmail("influencerOye@gmail.com");

        feedMock = new Feed();
        feedMock.setId(1L);
        feedMock.setContent("artist");
        feedMock.setPlatform("instagram");
        feedMock.setInfluencerName(influencerDetail);

        feedsMockList = new ArrayList<>();
        feedsMockList.add(feedMock);

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
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encoded12345");
        UserDTO userDetailResponse = userService.updateUser(userDTO.getId(), userDTO);
        assertEquals("suraj", userDetailResponse.getUsername());
        assertEquals("encoded12345", userDetailResponse.getPassword());
    }

    @Test
    void deleteUser() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        userService.deleteUser(userDTO.getId());
        assertNull(null, userDetailEntities.get(0).getUsername());
    }

    @Test
    void unFollowInfluencer() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer1 = influencerDetailEntities.get(0);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        when(influencerRepository.findById(influencer.getId())).thenReturn(Optional.of(influencerDetailEntities.get(0)));
        when(followInformationRepo.findByUserFollowNameAndInfluencerFollowName(anyString(), anyString()))
                .thenReturn(new FollowInformation());

        UserDTO userDetailResponse = new UserDTO();
        assertThrows(DataAlreadyExistException.class, () -> {
            userService.unFollowInfluencer(userDTO.getId(), influencer1.getId());
        });
        assertNull(userDetailResponse.getUsername());
        assertNull(userDetailResponse.getPassword());
    }

    @Test
    void followInfluencer() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer1 = influencerDetailEntities.get(0);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        when(influencerRepository.findById(influencer.getId())).thenReturn(Optional.of(influencerDetailEntities.get(0)));
        when(followInformationRepo.findByUserFollowNameAndInfluencerFollowName(anyString(), anyString()))
                .thenReturn(new FollowInformation());

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
        userDetailEntities.get(0).getFollowedInfluencers().get(0).setFeeds(feedsMockList);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        List<Feed> feedDetails = userService.getFeedsByUserAndPlatform(userDTO.getId(),"instagram");
        assertEquals("instagram", feedDetails.get(0).getPlatform());
        assertEquals(1L, feedDetails.get(0).getId());
    }

    @Test
    void findUsersByTheme() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<>() {});
        userDetailEntities.get(0).setRole(Role.USER);
        userDetailEntities.get(0).setTheme(Theme.LIGHT);
        when(userRepository.findByTheme(userDTO.getTheme())).thenReturn(userDetailEntities);
        List<User> userDetailResponse = userService.findUsersByTheme(userDTO.getTheme());
        assertEquals(Theme.LIGHT, userDetailResponse.get(0).getTheme());
    }

    @Test
    void findUsersByRole() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<>() {});
        userDetailEntities.get(0).setRole(Role.USER);
        when(userRepository.findByRole(userDTO.getRole())).thenReturn(userDetailEntities);
        List<User> userDetailResponse = userService.findUsersByRole(userDTO.getRole());
        assertEquals(Role.USER, userDetailResponse.get(0).getRole());
    }
}