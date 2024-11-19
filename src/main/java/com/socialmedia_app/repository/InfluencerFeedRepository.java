package com.socialmedia_app.repository;

import com.socialmedia_app.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerFeedRepository extends JpaRepository<Feed, Long> {
//    List<Feed> findByInfluencerIdAndPlatform(Long influencerId, String platform);
}
