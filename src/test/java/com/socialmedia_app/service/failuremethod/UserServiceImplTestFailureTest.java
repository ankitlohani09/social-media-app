package com.socialmedia_app.service.failuremethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.User;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.UserRepository;
import com.socialmedia_app.service.impl.UserServiceImpl;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceImplTestFailureTest {
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
    private String userDetailFailure;

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
        userDetailFailure = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("user_detail_failure.json"), StandardCharsets.UTF_8);
        influencerDetail = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("influencer_detail_response.json"), StandardCharsets.UTF_8);
    }

    @Test
    void getUserByEmailTest() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByEmail("");
        });
        assertEquals("User not found with this email: ", exception.getMessage());
    }

    @Test
    void updateUser() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userDTO.getId(), userDTO);
        });
        assertEquals("ankitlohani", userDetailEntities.get(0).getUsername());
        assertEquals("test", userDetailEntities.get(0).getPassword());
    }

    @Test
    void unFollowInfluencer() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer1 = influencerDetailEntities.get(0);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
//        when(influencerRepository.findById(influencer.getId())).thenReturn(Optional.of(influencerDetailEntities.get(0)));

        UserDTO userDetailResponse = userService.unFollowInfluencer(userDTO.getId(), influencer1.getId());
        assertEquals("ankitlohani", userDetailEntities.get(0).getUsername());
        assertEquals("test", userDetailEntities.get(0).getPassword());
    }

    @Test
    void followInfluencer() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetailFailure, new TypeReference<List<User>>() {});
        List<Influencer> influencerDetailEntities = objectMapper.readValue(influencerDetail, new TypeReference<List<Influencer>>() {});
        Influencer influencer1 = influencerDetailEntities.get(0);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        when(influencerRepository.findById(influencer.getId())).thenReturn(Optional.of(influencerDetailEntities.get(0)));

        UserDTO userDetailResponse = userService.followInfluencer(userDTO.getId(), influencer1.getId());
        assertEquals("ankitlohani", userDetailEntities.get(0).getUsername());
        assertEquals("test", userDetailEntities.get(0).getPassword());
    }

    @Test
    void getFollowedInfluencers() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<User> userDetailEntities = objectMapper.readValue(userDetail, new TypeReference<List<User>>() {});
//        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userDetailEntities.get(0)));
        assertThrows(RuntimeException.class, () -> {
            userService.getFollowedInfluencers(userDTO.getId());
        });
        assertEquals("ankitlohani", userDetailEntities.get(0).getUsername());
        assertEquals("test", userDetailEntities.get(0).getPassword());
    }


}