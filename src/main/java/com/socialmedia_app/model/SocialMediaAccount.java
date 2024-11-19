package com.socialmedia_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialMediaAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean facebookAc;
    private boolean twitterAc;
    private boolean instagramAc;

    @OneToOne(cascade = CascadeType.ALL)
    private Influencer influencer;
}
