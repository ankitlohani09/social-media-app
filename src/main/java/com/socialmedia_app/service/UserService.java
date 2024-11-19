package com.socialmedia_app.service;

import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.exception.DataAlreadyExistException;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.Feed;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String email);

    UserDTO registerUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userRequestDTO);

    void deleteUser(Long id);

    UserDTO unFollowInfluencer(Long userId, Long influencerId);

    UserDTO followInfluencer(Long userId, Long influencerId) throws DataAlreadyExistException;

    List<Influencer> getFollowedInfluencers(Long userId);

    List<Feed> getFeedsByUserAndPlatform(Long userId, String platform);
}
