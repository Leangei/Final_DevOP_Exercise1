package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final RegistrationNumberService registrationService;

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
    }

    public Profile create(Profile profile) {
        profile.setUuid(UUID.randomUUID().toString());
        profile.setRegistrationNumber(
                registrationService.generate(profile.getDepartment()));
        return profileRepository.save(profile);
    }

    public Profile update(Long id, Profile request) {
        Profile profile = findById(id);
        profile.setFullName(request.getFullName());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());
        profile.setDepartment(request.getDepartment());
        profile.setTitle(request.getTitle());
        profile.setType(request.getType());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setExpiryDate(request.getExpiryDate());
        profile.setBloodGroup(request.getBloodGroup());
        profile.setPhotoFileName(request.getPhotoFileName());
        profile.setPhotoContentType(request.getPhotoContentType());
        profile.setBarcodeType(request.getBarcodeType());
        if (request.getTemplate() != null) {
            profile.setTemplate(request.getTemplate());
        }
        return profileRepository.save(profile);
    }

    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}