package com.example.demo.dto;

import com.example.demo.model.BarcodeType;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;
    private String uuid;
    private String registrationNumber;
    private ProfileType type;
    private String fullName;
    private String department;
    private String title;
    private String email;
    private String phone;
    private String bloodGroup;
    private LocalDate dateOfBirth;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String photoFileName;
    private String photoContentType;
    private Long templateId;
    private String templateName;
    private BarcodeType barcodeType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileResponse fromProfile(Profile profile) {
        ProfileResponseBuilder builder = ProfileResponse.builder()
                .id(profile.getId())
                .uuid(profile.getUuid())
                .registrationNumber(profile.getRegistrationNumber())
                .type(profile.getType())
                .fullName(profile.getFullName())
                .department(profile.getDepartment())
                .title(profile.getTitle())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .bloodGroup(profile.getBloodGroup())
                .dateOfBirth(profile.getDateOfBirth())
                .issueDate(profile.getIssueDate())
                .expiryDate(profile.getExpiryDate())
                .photoFileName(profile.getPhotoFileName())
                .photoContentType(profile.getPhotoContentType())
                .barcodeType(profile.getBarcodeType())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt());

        if (profile.getTemplate() != null) {
            builder.templateId(profile.getTemplate().getId());
            builder.templateName(profile.getTemplate().getName());
        }

        return builder.build();
    }
}