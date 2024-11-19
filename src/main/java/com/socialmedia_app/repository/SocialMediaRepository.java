package com.socialmedia_app.repository;

import com.socialmedia_app.model.SocialMediaAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMediaAccount, Long> {
    Optional<SocialMediaAccount> findByInfluencerId(Long influencerId);
}
