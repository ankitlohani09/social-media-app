package com.socialmedia_app.dto;

import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.Role;
import com.socialmedia_app.model.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean isDeleted;
    private Theme theme;
    private Role role;
    private List<Influencer> followedInfluencers;
}
