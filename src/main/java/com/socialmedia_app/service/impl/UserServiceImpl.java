package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.exception.DataAlreadyExistException;
import com.socialmedia_app.exception.NoDataFoundException;
import com.socialmedia_app.exception.UserAlreadyExistException;
import com.socialmedia_app.exception.UserNotFoundException;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.User;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.UserRepository;
import com.socialmedia_app.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InfluencerRepository influencerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> all = userRepository.findAll();
        List<UserDTO> userResponseDTOList = new ArrayList<>();
        for (User user : all) {
            UserDTO userResponseDTO = new UserDTO();
            BeanUtils.copyProperties(user, userResponseDTO);
            userResponseDTOList.add(userResponseDTO);
        }
        return userResponseDTOList;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserDTO userResponseDTO = new UserDTO();
        User existUser = userRepository.findByEmail(email);
        if (existUser == null) {
            throw new UserNotFoundException("User not found with this email: "+email);
        }
        BeanUtils.copyProperties(existUser,userResponseDTO);
        return userResponseDTO;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User existUser = userRepository.findByEmail(userDTO.getEmail());
        if (existUser != null) {
            throw new UserAlreadyExistException("User with this email already exists");
        }
        UserDTO userResponseDTO = new UserDTO();
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        userRepository.save(user);
        BeanUtils.copyProperties(user, userResponseDTO);
        return userResponseDTO;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserDTO userResponseDTO = new UserDTO();
        User userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            BeanUtils.copyProperties(userDTO,userEntity);
            this.userRepository.save(userEntity);
            BeanUtils.copyProperties(userEntity,userResponseDTO);
            return userResponseDTO;
        } else {
            throw new UserNotFoundException("User not found with this id "+ id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        User existUser = userRepository.findById(id).get();
        if (existUser == null) {
            throw new UserNotFoundException("User not found with this id "+ id);
        }
        userRepository.delete(existUser);
    }

    @Override
    public UserDTO unFollowInfluencer(Long userId, Long influencerId) {
        UserDTO userResponseDTO = new UserDTO();
        User user = userRepository.findById(userId).orElse(null);
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        List<Influencer> followedInfluencers = user.getFollowedInfluencers();
        if (followedInfluencers != null) {
            followedInfluencers.remove(influencer);
        }
        userRepository.save(user);
        BeanUtils.copyProperties(user, userResponseDTO);
        return userResponseDTO;
    }

    @Override
    public UserDTO followInfluencer(Long userId, Long influencerId) {
        UserDTO userResponseDTO = new UserDTO();
        User user = userRepository.findById(userId).orElse(null);
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        List<Influencer> followedInfluencers = user.getFollowedInfluencers();
        if (followedInfluencers == null) {
            followedInfluencers = new ArrayList<>();
            user.setFollowedInfluencers(followedInfluencers);
        }
        if (followedInfluencers.contains(influencer)) {
            throw new DataAlreadyExistException("User Already followed this influencer");
        } else {
            followedInfluencers.add(influencer);
            userRepository.save(user);
        }
        BeanUtils.copyProperties(user, userResponseDTO);
        return userResponseDTO;
    }

    @Override
    public List<Influencer> getFollowedInfluencers(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Influencer> followedInfluencers = user.getFollowedInfluencers();
            if (followedInfluencers.isEmpty()) {
                throw new NoDataFoundException("User Not followed influencers");
            } else {
                return followedInfluencers;
            }
        } else {
            throw new UserNotFoundException("User Not Found");
        }
    }

    @Override
    public List<Feed> getFeedsByUserAndPlatform(Long userId, String platform) {
        User user = userRepository.findById(userId).orElse(null);
        List<Feed> feeds = new ArrayList<>();
        if (user != null) {
            for (Influencer influencer : user.getFollowedInfluencers()) {
                for (Feed feed : influencer.getFeeds()) {
                    if (feed.getPlatform().equalsIgnoreCase(platform)) {
                        feeds.add(feed);
                    }
                }
            }
        }
        return feeds;
    }
}
