package com.socialmedia_app.controller;

import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.model.*;
import com.socialmedia_app.service.UserService;
import com.socialmedia_app.utils.Role;
import com.socialmedia_app.utils.Theme;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/byTheme")
    public ResponseEntity<List<User>> findUsersByTheme(@RequestParam("theme") Theme theme) {
        List<User> users = userService.findUsersByTheme(theme);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/byRole")
    public ResponseEntity<List<User>> findUsersByRole(@RequestParam("role") Role role) {
        List<User> users = userService.findUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get")
    public UserDTO getUser(@RequestBody String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping("/create")
    public UserDTO createUser(@RequestBody UserDTO userRequestDTO) {
        return userService.registerUser(userRequestDTO);
    }

    @PutMapping("/update/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userRequestDTO) {
        return userService.updateUser(id,userRequestDTO);
    }

    @DeleteMapping("/delete/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{userId}/unfollow/{influencerId}")
    public ResponseEntity<UserDTO> unFollowInfluencer(@PathVariable Long userId, @PathVariable Long influencerId) {
        UserDTO user = userService.unFollowInfluencer(userId, influencerId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{userId}/follow/{influencerId}")
    public ResponseEntity<UserDTO> followInfluencer(@PathVariable Long userId, @PathVariable Long influencerId) {
        UserDTO user = userService.followInfluencer(userId, influencerId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{userId}/feeds/{platform}")
    public ResponseEntity<List<Feed>> getFeedsByUserAndPlatform(@PathVariable Long userId, @PathVariable String platform) {
        List<Feed> feeds = userService.getFeedsByUserAndPlatform(userId, platform);
        if (!feeds.isEmpty()) {
            return ResponseEntity.ok(feeds);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/getFollowedInfluencers")
    public ResponseEntity<List<Influencer>> getFollowedInfluencers(@PathVariable Long userId) {
        List<Influencer> influencers = userService.getFollowedInfluencers(userId);
            return ResponseEntity.ok(influencers);
    }
}
