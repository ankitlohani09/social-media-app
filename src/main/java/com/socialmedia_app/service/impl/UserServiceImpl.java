package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.SocialMediaAccountDTO;
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
import com.socialmedia_app.utils.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final InfluencerRepository influencerRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, InfluencerRepository influencerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.influencerRepository = influencerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final SocialMediaServiceImpl socialMediaServiceImpl;

    public UserServiceImpl(SocialMediaServiceImpl socialMediaServiceImpl) {
        this.socialMediaServiceImpl = socialMediaServiceImpl;
    }

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
        User userEntity = getUserFromDbByUserID(id);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        BeanUtils.copyProperties(userDTO,userEntity);
        this.userRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity,userResponseDTO);
        return userResponseDTO;
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserFromDbByUserID(id);
        userRepository.delete(user);
    }

    private User getUserFromDbByUserID(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("User not found with this id "+ userId);
        }
        return optUser.get();
    }

    @Override
    public UserDTO unFollowInfluencer(Long userId, Long influencerId) {
        UserDTO userResponseDTO = new UserDTO();
        User user = getUserFromDbByUserID(userId);
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
        User user = getUserFromDbByUserID(userId);
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
        User user = getUserFromDbByUserID(userId);
        List<Influencer> followedInfluencers = user.getFollowedInfluencers();
        if (followedInfluencers.isEmpty()) {
            throw new NoDataFoundException("User Not followed influencers");
        } else {
            return followedInfluencers;
        }
    }

    @Override
    public List<Feed> getFeedsByUserAndPlatform(Long userId, String platform) {
        User user = getUserFromDbByUserID(userId);
        List<Feed> feeds = new ArrayList<>();
        if (user != null) {
            List<Influencer> followedInfluencers = user.getFollowedInfluencers();
            if (followedInfluencers.isEmpty()) {
                throw new NoDataFoundException("User Not followed influencers");
            }
            for (Influencer influencer : followedInfluencers) {
                if (isInfluencerActiveOnPlatform(influencer, platform)) {
                    for (Feed feed : influencer.getFeeds()) {
                        if (feed.getPlatform().equalsIgnoreCase(platform)) {
                            feeds.add(feed);
                        }
                    }
                }
            }
        }
        if (user == null) {
            throw new UserNotFoundException("User Not Found");
        }
        return feeds;
    }

    private boolean isInfluencerActiveOnPlatform(Influencer influencer, String selectedPlatform) {
        SocialMediaAccountDTO account = socialMediaServiceImpl.getSocialMediaAccByInfluencerId(influencer.getId());
        if (selectedPlatform.equalsIgnoreCase(Constants.FACEBOOK)) {
            return account.isFacebookAc();
        } else if (selectedPlatform.equalsIgnoreCase(Constants.TWITTER)) {
            if (!account.isTwitterAc()) {
                throw new NoDataFoundException("Twitter Account Not Found for this influencer: "+influencer.getId());
            }
            return true;
        } else if (selectedPlatform.equalsIgnoreCase(Constants.INSTAGRAM)) {
            return account.isInstagramAc();
        } else {
            throw new IllegalArgumentException("Please type valid or full platform name. Unsupported platform: "+selectedPlatform);
        }
    }
}
