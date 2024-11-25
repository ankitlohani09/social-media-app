package com.socialmedia_app.repository;

import com.socialmedia_app.model.FollowInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowInformationRepo extends JpaRepository<FollowInformation, Long> {
    FollowInformation findByUserFollowNameAndInfluencerFollowName(String userFollowName, String influencerFollowName);
}
