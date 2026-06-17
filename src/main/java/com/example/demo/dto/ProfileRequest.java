package com.example.demo.dto;

import com.example.demo.model.BarcodeType;
import com.example.demo.model.ProfileType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequest {

    @NotNull(message = "Profile type is required")
    private ProfileType type;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String department;

    private String title;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String bloodGroup;

    private LocalDate dateOfBirth;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String photoFileName;

    private String photoContentType;

    private Long templateId;

    private BarcodeType barcodeType;
}