package com.socialmedia_app.service.impl;

import com.socialmedia_app.dto.SocialMediaAccountDTO;
import com.socialmedia_app.exception.NoDataFoundException;
import com.socialmedia_app.model.Influencer;
import com.socialmedia_app.model.SocialMediaAccount;
import com.socialmedia_app.repository.InfluencerRepository;
import com.socialmedia_app.repository.SocialMediaRepository;
import com.socialmedia_app.service.SocialMediaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SocialMediaServiceImpl implements SocialMediaService {

    private final SocialMediaRepository socialMediaRepository;

    private final InfluencerRepository influencerRepository;

    public SocialMediaServiceImpl(SocialMediaRepository socialMediaRepository, InfluencerRepository influencerRepository) {
        this.socialMediaRepository = socialMediaRepository;
        this.influencerRepository = influencerRepository;
    }

    @Override
    public List<SocialMediaAccountDTO> getAllSocialMediaAccounts() {
        List<SocialMediaAccount> all = socialMediaRepository.findAll();
        List<SocialMediaAccountDTO> socialMediaAccountDTOList = new ArrayList<>();
        for (SocialMediaAccount socialMediaAccount : all) {
            SocialMediaAccountDTO socialMediaAccountDTO = new SocialMediaAccountDTO();
            BeanUtils.copyProperties(socialMediaAccount, socialMediaAccountDTO);
            socialMediaAccountDTOList.add(socialMediaAccountDTO);
        }
        return socialMediaAccountDTOList;
    }

    @Override
    public SocialMediaAccountDTO getSocialMediaAccByInfluencerId(Long influencerId) {
        Optional<SocialMediaAccount> socialMediaAccountOpt = socialMediaRepository.findByInfluencerId(influencerId);
        SocialMediaAccount socialMediaAccount;
        if (socialMediaAccountOpt.isEmpty()) {
            throw new NoDataFoundException("Social media account not found for this influencer");
        } else {
            socialMediaAccount = socialMediaAccountOpt.get();
        }
        SocialMediaAccountDTO socialMediaAccountDTO = new SocialMediaAccountDTO();
        socialMediaAccountDTO.setId(socialMediaAccount.getId());
        socialMediaAccountDTO.setFacebookAc(socialMediaAccount.isFacebookAc());
        socialMediaAccountDTO.setTwitterAc(socialMediaAccount.isTwitterAc());
        socialMediaAccountDTO.setInstagramAc(socialMediaAccount.isInstagramAc());
        socialMediaAccountDTO.setInfluencer(socialMediaAccount.getInfluencer());
        return socialMediaAccountDTO;
    }

    @Override
    public SocialMediaAccountDTO addSocialMediaAccount(Long influencerId, SocialMediaAccountDTO socialMediaAccountDTO) {
        SocialMediaAccount socialMediaAccount = new SocialMediaAccount();
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        if (influencer != null) {
            socialMediaAccountDTO.setInfluencer(influencer);
            BeanUtils.copyProperties(socialMediaAccountDTO, socialMediaAccount);
            socialMediaRepository.save(socialMediaAccount);
            return socialMediaAccountDTO;
        }
        return null;
    }

    @Override
    public SocialMediaAccountDTO updateSocialMediaAccount(Long influencerId, SocialMediaAccountDTO socialMediaAccountDTO) {
        Influencer influencer = influencerRepository.findById(influencerId).orElse(null);
        if (influencer != null) {
            SocialMediaAccount socialMediaAccount1 = socialMediaRepository.findById(influencerId).orElseThrow(() -> new RuntimeException("SocialMedia Accounts not found"));
            socialMediaAccountDTO.setInfluencer(influencer);
            socialMediaAccountDTO.setId(socialMediaAccount1.getId());
            BeanUtils.copyProperties(socialMediaAccountDTO, socialMediaAccount1);
            socialMediaRepository.save(socialMediaAccount1);
            return socialMediaAccountDTO;
        }
        return null;
    }

    @Override
    public void deleteSocialMediaAccount(Long influencerId) {
        SocialMediaAccount socialMediaAccount = socialMediaRepository.findById(influencerId).orElseThrow(() -> new RuntimeException("SocialMedia Accounts not found"));
        socialMediaRepository.delete(socialMediaAccount);
    }
}
