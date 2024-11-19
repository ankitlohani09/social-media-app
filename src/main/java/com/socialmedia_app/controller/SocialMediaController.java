package com.socialmedia_app.controller;

import com.socialmedia_app.dto.SocialMediaAccountDTO;
import com.socialmedia_app.service.SocialMediaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialmedia")
public class SocialMediaController {

    private final SocialMediaService socialMediaService;

    public SocialMediaController(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    @GetMapping("/getAll")
    List<SocialMediaAccountDTO> getAll() {
       return socialMediaService.getAllSocialMediaAccounts();
    }

    @GetMapping("/get/{influencerId}")
    SocialMediaAccountDTO getSocialMediaAccById(@PathVariable Long influencerId) {
        return socialMediaService.getSocialMediaAccByInfluencerId(influencerId);
    }

    @PostMapping("/create/{influencerId}")
    SocialMediaAccountDTO createSocialMediaAccount(@PathVariable Long influencerId ,@RequestBody SocialMediaAccountDTO socialMediaAccountDTO) {
        return socialMediaService.addSocialMediaAccount(influencerId,socialMediaAccountDTO);
    }

    @PutMapping("/update/{influencerId}")
    SocialMediaAccountDTO updateSocialMediaAccount(@PathVariable Long influencerId ,
                                                   @RequestBody SocialMediaAccountDTO socialMediaAccountDTO) {
        return socialMediaService.updateSocialMediaAccount(influencerId, socialMediaAccountDTO);
    }

    @DeleteMapping("/delete/{influencerId}")
    void deleteSocialMediaAccount(@PathVariable Long influencerId) {
        socialMediaService.deleteSocialMediaAccount(influencerId);
    }
}
