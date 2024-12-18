package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.InfluencerDTO;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.service.InfluencerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfluencerServiceImpl implements InfluencerService {

    private final InfluencerRepository influencerRepository;

    public InfluencerServiceImpl(InfluencerRepository influencerRepository) {
        this.influencerRepository = influencerRepository;
    }

    @Override
    public List<InfluencerDTO> getAllInfluencers() {
        List<Influencer> influencerList = influencerRepository.findAll();
        List<InfluencerDTO> influencerResponseDTOList = new ArrayList<>();
        for (Influencer influencer : influencerList) {
            InfluencerDTO responseDTO = new InfluencerDTO();
            BeanUtils.copyProperties(influencer, responseDTO);
            influencerResponseDTOList.add(responseDTO);
        }
        return influencerResponseDTOList;
    }

    @Override
    public InfluencerDTO getInfluencerById(Long id) {
        Influencer influencer = influencerRepository.findById(id).orElseThrow(() -> new RuntimeException("influencer not found with this id: "+id));
        InfluencerDTO influencerResponseDTO = new InfluencerDTO();
        BeanUtils.copyProperties(influencer, influencerResponseDTO);
        return influencerResponseDTO;
    }

    @Override
    public InfluencerDTO createInfluencer(InfluencerDTO influencerDTO) {
        Influencer influencer = new Influencer();
        BeanUtils.copyProperties(influencerDTO, influencer);
        influencer.setTheme(influencerDTO.getTheme());
        influencer.setRole(influencerDTO.getRole());
        influencer.setCreatedBy(influencer.getUsername());
        influencer.setCreatedDate(LocalDateTime.now());
        influencer.setLastModifiedBy(influencer.getUsername());
        influencer.setLastModifiedDate(LocalDateTime.now());
        influencerRepository.save(influencer);
        InfluencerDTO influencerResponseDTO = new InfluencerDTO();
        BeanUtils.copyProperties(influencer, influencerResponseDTO);
        return influencerResponseDTO;
    }

    @Override
    public InfluencerDTO updateInfluencer(Long id, InfluencerDTO influencerDTO) {
        InfluencerDTO influencerResponseDTO = new InfluencerDTO();
        Influencer influencer = influencerRepository.findById(id).orElseThrow(() -> new RuntimeException("influencer not found"));
        influencer.setEmail(influencerDTO.getEmail());
        influencer.setUsername(influencerDTO.getUsername());
        influencer.setPassword(influencerDTO.getPassword());
        influencer.setTheme(influencerDTO.getTheme());
        influencer.setRole(influencerDTO.getRole());
        influencer.setLastModifiedBy(influencer.getUsername());
        influencer.setLastModifiedDate(LocalDateTime.now());
        this.influencerRepository.save(influencer);
        BeanUtils.copyProperties(influencer, influencerResponseDTO);
        return influencerResponseDTO;
    }

    @Override
    public void deleteInfluencer(Long id) {
        Influencer influencer = influencerRepository.findById(id).orElseThrow(() -> new RuntimeException("Influencer not found"));
        influencerRepository.delete(influencer);
    }
}
