package com.socialmedia_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_entity")
@SQLDelete(sql = "UPDATE user_entity SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @Size(min = 10, max = 10)
    private String phone;
    private boolean isDeleted;

    @ManyToMany
    private List<Influencer> followedInfluencers;
}
