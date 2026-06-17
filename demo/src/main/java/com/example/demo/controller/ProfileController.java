package com.example.demo.controller;

import com.example.demo.dto.ProfileRequest;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.model.Profile;
import com.example.demo.model.Template;
import com.example.demo.service.ProfileService;
import com.example.demo.service.TemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<ProfileResponse>> getAll() {
        List<ProfileResponse> responses = profileService.findAll()
                .stream()
                .map(ProfileResponse::fromProfile)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> get(@PathVariable Long id) {
        Profile profile = profileService.findById(id);
        return ResponseEntity.ok(ProfileResponse.fromProfile(profile));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) {
        Profile profile = mapToEntity(request);
        Profile saved = profileService.create(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfileResponse.fromProfile(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(@PathVariable Long id, @Valid @RequestBody ProfileRequest request) {
        Profile profile = mapToEntity(request);
        Profile updated = profileService.update(id, profile);
        return ResponseEntity.ok(ProfileResponse.fromProfile(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Profile mapToEntity(ProfileRequest request) {
        Profile profile = new Profile();
        profile.setType(request.getType());
        profile.setFullName(request.getFullName());
        profile.setDepartment(request.getDepartment());
        profile.setTitle(request.getTitle());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());
        profile.setBloodGroup(request.getBloodGroup());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setIssueDate(request.getIssueDate());
        profile.setExpiryDate(request.getExpiryDate());
        profile.setPhotoFileName(request.getPhotoFileName());
        profile.setPhotoContentType(request.getPhotoContentType());
        profile.setBarcodeType(request.getBarcodeType() != null ? request.getBarcodeType() : com.example.demo.model.BarcodeType.CODE_128);

        if (request.getTemplateId() != null) {
            Template template = templateService.get(request.getTemplateId());
            profile.setTemplate(template);
        }

        return profile;
    }
}