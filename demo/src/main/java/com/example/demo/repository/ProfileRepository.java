package com.example.demo.repository;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUuid(String uuid);

    Optional<Profile> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByEmail(String email);

    List<Profile> findByType(ProfileType type);

    List<Profile> findByDepartmentContainingIgnoreCase(String department);

    List<Profile> findByFullNameContainingIgnoreCase(String fullName);
}