package com.socialmedia_app.repository;

import com.socialmedia_app.model.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepository extends JpaRepository<Influencer, Long> {
    Influencer findByEmail(String email);
    Influencer getById(Long id);
}
