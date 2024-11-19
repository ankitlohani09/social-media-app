package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.InfluencerDTO;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.service.InfluencerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InfluencerServiceImpl implements InfluencerService {

    @Autowired
    private InfluencerRepository influencerRepository;

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
