package com.socialmedia_app.controller;

import com.socialmedia_app.dto.FeedDTO;
import com.socialmedia_app.dto.InfluencerDTO;
import com.socialmedia_app.service.InfluencerFeedService;
import com.socialmedia_app.service.InfluencerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/influencer")
public class InfluencerController {

    private final InfluencerService influencerService;

    private final InfluencerFeedService influencerFeedService;

    public InfluencerController(InfluencerService influencerService, InfluencerFeedService influencerFeedService) {
        this.influencerService = influencerService;
        this.influencerFeedService = influencerFeedService;
    }

    @GetMapping("/getAll")
    public List<InfluencerDTO> getAllInfluencers() {
        return influencerService.getAllInfluencers();
    }

    @GetMapping("/getAll/{id}")
    public InfluencerDTO getInfluencerById(@PathVariable Long id) {
        return influencerService.getInfluencerById(id);
    }

    @PostMapping("/create")
    public InfluencerDTO createInfluencer(@RequestBody InfluencerDTO influencer) {
        return influencerService.createInfluencer(influencer);
    }

    @PutMapping("/update/{id}")
    public InfluencerDTO updateInfluencer(@PathVariable Long id, @RequestBody InfluencerDTO influencer) {
        return influencerService.updateInfluencer(id, influencer);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteInfluencer(@PathVariable Long id) {
        influencerService.deleteInfluencer(id);
    }

    @PostMapping("/createFeed/{influencerId}")
    public ResponseEntity<FeedDTO> createFeed(@PathVariable Long influencerId, @RequestBody FeedDTO feedDTO) {
        FeedDTO feedDTO1 = influencerFeedService.createFeed(influencerId, feedDTO);
        if (feedDTO1 != null) {
            return ResponseEntity.ok(feedDTO1);
        }
        return ResponseEntity.badRequest().build();
    }
}
