package com.example.demo.service;

import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class RegistrationNumberService {

    private final ProfileRepository profileRepository;

    public String generate(String department) {
        String dept = department == null
                ? "GEN"
                : department.toUpperCase().replaceAll("[^A-Z]", "");

        if (dept.isEmpty()) {
            dept = "GEN";
        }

        // Take first 3 characters
        dept = dept.length() > 3 ? dept.substring(0, 3) : dept;

        long count = profileRepository.findByDepartmentContainingIgnoreCase(dept).size() + 1;

        return Year.now().getValue()
                + "-"
                + dept
                + "-"
                + String.format("%03d", count);
    }
}