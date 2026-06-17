package com.example.demo.repository;

import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProfileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void save_ShouldPersistProfile() {
        // Arrange
        Profile profile = Profile.builder()
                .uuid("test-uuid-123")
                .registrationNumber("2026-ENG-001")
                .type(ProfileType.STUDENT)
                .fullName("John Doe")
                .department("ENG")
                .build();

        // Act
        Profile saved = entityManager.persistAndFlush(profile);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUuid()).isEqualTo("test-uuid-123");
        assertThat(saved.getRegistrationNumber()).isEqualTo("2026-ENG-001");
        assertThat(saved.getFullName()).isEqualTo("John Doe");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void findByUuid_ShouldReturnProfile() {
        // Arrange
        Profile profile = Profile.builder()
                .uuid("unique-uuid-456")
                .registrationNumber("2026-CS-002")
                .type(ProfileType.EMPLOYEE)
                .fullName("Jane Doe")
                .department("CS")
                .build();
        entityManager.persistAndFlush(profile);

        // Act
        Optional<Profile> found = profileRepository.findByUuid("unique-uuid-456");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Jane Doe");
    }

    @Test
    void findByRegistrationNumber_ShouldReturnProfile() {
        // Arrange
        Profile profile = Profile.builder()
                .uuid("uuid-789")
                .registrationNumber("2026-ENG-003")
                .type(ProfileType.STUDENT)
                .fullName("Bob Smith")
                .build();
        entityManager.persistAndFlush(profile);

        // Act
        Optional<Profile> found = profileRepository.findByRegistrationNumber("2026-ENG-003");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Bob Smith");
    }

    @Test
    void existsByRegistrationNumber_ShouldReturnTrue_WhenExists() {
        // Arrange
        Profile profile = Profile.builder()
                .uuid("uuid-111")
                .registrationNumber("2026-CS-004")
                .type(ProfileType.STUDENT)
                .fullName("Alice Wonder")
                .build();
        entityManager.persistAndFlush(profile);

        // Act
        boolean exists = profileRepository.existsByRegistrationNumber("2026-CS-004");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByRegistrationNumber_ShouldReturnFalse_WhenNotExists() {
        // Act
        boolean exists = profileRepository.existsByRegistrationNumber("NONEXISTENT");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void findByType_ShouldReturnProfilesOfGivenType() {
        // Arrange
        Profile student1 = Profile.builder().uuid("u1").registrationNumber("R1").type(ProfileType.STUDENT).fullName("S1").build();
        Profile student2 = Profile.builder().uuid("u2").registrationNumber("R2").type(ProfileType.STUDENT).fullName("S2").build();
        Profile employee = Profile.builder().uuid("u3").registrationNumber("R3").type(ProfileType.EMPLOYEE).fullName("E1").build();
        entityManager.persistAndFlush(student1);
        entityManager.persistAndFlush(student2);
        entityManager.persistAndFlush(employee);

        // Act
        List<Profile> students = profileRepository.findByType(ProfileType.STUDENT);

        // Assert
        assertThat(students).hasSize(2);
        assertThat(students).extracting(Profile::getFullName).containsExactlyInAnyOrder("S1", "S2");
    }

    @Test
    void findByFullNameContainingIgnoreCase_ShouldReturnMatchingProfiles() {
        // Arrange
        Profile p1 = Profile.builder().uuid("u4").registrationNumber("R4").type(ProfileType.USER).fullName("John Smith").build();
        Profile p2 = Profile.builder().uuid("u5").registrationNumber("R5").type(ProfileType.USER).fullName("Johnny Depp").build();
        Profile p3 = Profile.builder().uuid("u6").registrationNumber("R6").type(ProfileType.USER).fullName("Jane Doe").build();
        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(p2);
        entityManager.persistAndFlush(p3);

        // Act
        List<Profile> result = profileRepository.findByFullNameContainingIgnoreCase("john");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Profile::getFullName).containsExactlyInAnyOrder("John Smith", "Johnny Depp");
    }
}