package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.exception.DataAlreadyExistException;
import com.socialmedia_app.exception.NoDataFoundException;
import com.socialmedia_app.exception.UserAlreadyExistException;
import com.socialmedia_app.exception.UserNotFoundException;
import com.socialmedia_app.model.FollowInformation;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.User;
import com.socialmedia_app.model.Feed;
import com.socialmedia_app.repository.FollowInformationRepo;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.UserRepository;
import com.socialmedia_app.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final InfluencerRepository influencerRepository;

    private final PasswordEncoder passwordEncoder;

    private final FollowInformationRepo followInformationRepo;

    public UserServiceImpl(UserRepository userRepository, InfluencerRepository influencerRepository, PasswordEncoder passwordEncoder, FollowInformationRepo followInformationRepo) {
        this.userRepository = userRepository;
        this.influencerRepository = influencerRepository;
        this.passwordEncoder = passwordEncoder;
        this.followInformationRepo = followInformationRepo;
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

    private Influencer getInfluencerFromDbByInfluencerID(Long influencerId) {
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        if (influencer == null) {
            throw new NoDataFoundException("Influencer not found with this id "+ influencerId);
        } else {
            return influencer;
        }
    }

    @Override
    public UserDTO unFollowInfluencer(Long userId, Long influencerId) {
        UserDTO userResponseDTO = new UserDTO();
        User user = getUserFromDbByUserID(userId);
        Influencer influencer = getInfluencerFromDbByInfluencerID(influencerId);
        List<Influencer> followedInfluencers = user.getFollowedInfluencers();
        if (isUserAlreadyUnFollowInfluencer(userId,influencerId)) {
            throw new DataAlreadyExistException("User already unfollow this influencer");
        }

        if (followedInfluencers != null) {
            followInformationOperation(userId, influencerId);
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
        Influencer influencer = getInfluencerFromDbByInfluencerID(influencerId);
        List<Influencer> followedInfluencers = user.getFollowedInfluencers();
        if (followedInfluencers == null) {
            followedInfluencers = new ArrayList<>();
            user.setFollowedInfluencers(followedInfluencers);
        }
        if (isUserAlreadyFollowInfluencer(userId,influencerId)) {
            throw new DataAlreadyExistException("User Already followed this influencer");
        } else {
            followInformationOperation(userId, influencerId);
            followedInfluencers.add(influencer);
            userRepository.save(user);
        }
        BeanUtils.copyProperties(user, userResponseDTO);
        return userResponseDTO;
    }

    private void followInformationOperation(Long userId, Long influencerId) {
        User user = getUserFromDbByUserID(userId);
        Influencer influencer = getInfluencerFromDbByInfluencerID(influencerId);

        FollowInformation existingFollowInfo = followInformationRepo.findByUserFollowNameAndInfluencerFollowName(user.getUsername(), influencer.getUsername());

        if (existingFollowInfo == null) {
            FollowInformation followInformation = new FollowInformation();
            followInformation.setUserFollowName(user.getUsername());
            followInformation.setInfluencerFollowName(influencer.getUsername());
            followInformation.setFollowed(true);
            followInformation.setFollowedDateTime(LocalDateTime.now());
            followInformation.setUpdateFollowDateTime(LocalDateTime.now());
            followInformationRepo.save(followInformation);
        }
        if (existingFollowInfo != null) {
            boolean followed = existingFollowInfo.isFollowed();
            if (followed) {
                existingFollowInfo.setFollowed(false);
                existingFollowInfo.setUpdateFollowDateTime(LocalDateTime.now());
                followInformationRepo.save(existingFollowInfo);
            } else {
                existingFollowInfo.setFollowed(true);
                existingFollowInfo.setFollowedDateTime(LocalDateTime.now());
                existingFollowInfo.setUpdateFollowDateTime(LocalDateTime.now());
                followInformationRepo.save(existingFollowInfo);
            }
        }
    }

    private boolean isUserAlreadyFollowInfluencer(Long userId, Long influencerId) {
        User user = getUserFromDbByUserID(userId);
        Influencer influencer = getInfluencerFromDbByInfluencerID(influencerId);
        return user.getFollowedInfluencers().contains(influencer);
    }

    private boolean isUserAlreadyUnFollowInfluencer(Long userId, Long influencerId) {
        User user = getUserFromDbByUserID(userId);
        Influencer influencer = getInfluencerFromDbByInfluencerID(influencerId);
        return !user.getFollowedInfluencers().contains(influencer);
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
        List<Influencer> followedInfluencers = user.getFollowedInfluencers();
        if (followedInfluencers.isEmpty()) {
            throw new NoDataFoundException("User Not followed influencers");
        }
        for (Influencer influencer : followedInfluencers) {
                for (Feed feed : influencer.getFeeds()) {
                    if (feed.getPlatform().equalsIgnoreCase(platform)) {
                        feed.setInfluencer(influencer);
                        feed.setInfluencerName(influencer.getUsername());
                        feeds.add(feed);
                    }
                }
        }
        return feeds;
    }
}
