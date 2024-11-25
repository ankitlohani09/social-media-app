package com.socialmedia_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socialmedia_app.utils.Role;
import com.socialmedia_app.utils.Theme;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE influencer SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
public class Influencer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private Theme theme;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "influencer")
    @JsonIgnore
    private List<Feed> feeds;
}
