package com.example.demo.service;

import com.example.demo.model.BarcodeType;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import com.example.demo.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    // Manually mock RegistrationNumberService as it's a concrete class
    private RegistrationNumberService registrationService;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationNumberService(profileRepository);
        profileService = new ProfileService(profileRepository, registrationService);
    }

    @Test
    void findAll_ShouldReturnAllProfiles() {
        Profile profile1 = Profile.builder().id(1L).fullName("John Doe").build();
        Profile profile2 = Profile.builder().id(2L).fullName("Jane Doe").build();
        when(profileRepository.findAll()).thenReturn(Arrays.asList(profile1, profile2));

        List<Profile> result = profileService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFullName()).isEqualTo("John Doe");
        assertThat(result.get(1).getFullName()).isEqualTo("Jane Doe");
        verify(profileRepository).findAll();
    }

    @Test
    void findById_WhenProfileExists_ShouldReturnProfile() {
        Profile profile = Profile.builder().id(1L).fullName("John Doe").build();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Profile result = profileService.findById(1L);

        assertThat(result.getFullName()).isEqualTo("John Doe");
        verify(profileRepository).findById(1L);
    }

    @Test
    void findById_WhenProfileDoesNotExist_ShouldThrowException() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found");
        verify(profileRepository).findById(99L);
    }

    @Test
    void create_ShouldSetUuidAndRegistrationNumberAndSave() {
        Profile profile = Profile.builder()
                .fullName("John Doe")
                .type(ProfileType.STUDENT)
                .department("ENG")
                .build();

        when(profileRepository.findByDepartmentContainingIgnoreCase("ENG")).thenReturn(Arrays.asList());
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Profile result = profileService.create(profile);

        assertThat(result.getUuid()).isNotNull();
        assertThat(result.getRegistrationNumber()).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");
        verify(profileRepository).save(profile);
    }

    @Test
    void update_ShouldUpdateProfileFields() {
        Profile existing = Profile.builder()
                .id(1L)
                .fullName("Old Name")
                .email("old@test.com")
                .build();

        Profile request = Profile.builder()
                .fullName("New Name")
                .email("new@test.com")
                .phone("123456789")
                .department("CS")
                .title("Student")
                .type(ProfileType.STUDENT)
                .barcodeType(BarcodeType.CODE_128)
                .build();

        when(profileRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Profile result = profileService.update(1L, request);

        assertThat(result.getFullName()).isEqualTo("New Name");
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        assertThat(result.getPhone()).isEqualTo("123456789");
        assertThat(result.getDepartment()).isEqualTo("CS");
        assertThat(result.getTitle()).isEqualTo("Student");
        verify(profileRepository).findById(1L);
        verify(profileRepository).save(existing);
    }

    @Test
    void delete_ShouldDeleteProfile() {
        doNothing().when(profileRepository).deleteById(1L);

        profileService.delete(1L);

        verify(profileRepository).deleteById(1L);
    }
}