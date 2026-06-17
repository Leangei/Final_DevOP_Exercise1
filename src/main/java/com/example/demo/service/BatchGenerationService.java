package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchGenerationService {

    private final ProfileRepository profileRepository;
    private final PdfGenerationService pdfGenerationService;

    public List<byte[]> generateBatch(List<Long> profileIds) {
        List<byte[]> pdfs = new ArrayList<>();
        for (Long id : profileIds) {
            Profile profile = profileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
            try {
                byte[] pdf = pdfGenerationService.generate(profile);
                pdfs.add(pdf);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate PDF for profile id: " + id, e);
            }
        }
        return pdfs;
    }

    public List<byte[]> generateAllByType(com.example.demo.model.ProfileType type) {
        List<Profile> profiles = profileRepository.findByType(type);
        List<byte[]> pdfs = new ArrayList<>();
        for (Profile profile : profiles) {
            try {
                byte[] pdf = pdfGenerationService.generate(profile);
                pdfs.add(pdf);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate PDF for profile: " + profile.getId(), e);
            }
        }
        return pdfs;
    }
}