package com.example.demo.controller;

import com.example.demo.dto.ProfileRequest;
import com.example.demo.model.BarcodeType;
import com.example.demo.model.ProfileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProfile_ShouldReturnCreatedProfile() throws Exception {
        ProfileRequest request = ProfileRequest.builder()
                .type(ProfileType.STUDENT)
                .fullName("Test Student")
                .department("ENG")
                .email("test@test.com")
                .phone("012345678")
                .barcodeType(BarcodeType.CODE_128)
                .build();

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fullName").value("Test Student"))
                .andExpect(jsonPath("$.uuid").isString())
                .andExpect(jsonPath("$.registrationNumber").isString())
                .andExpect(jsonPath("$.type").value("STUDENT"));
    }

    @Test
    void getAllProfiles_ShouldReturnList() throws Exception {
        // First create a profile
        ProfileRequest request = ProfileRequest.builder()
                .type(ProfileType.EMPLOYEE)
                .fullName("Test Employee")
                .department("HR")
                .barcodeType(BarcodeType.CODE_128)
                .build();

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Then get all
        mockMvc.perform(get("/api/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))));
    }

    @Test
    void getProfileById_ShouldReturnProfile() throws Exception {
        // Create a profile first
        ProfileRequest request = ProfileRequest.builder()
                .type(ProfileType.STUDENT)
                .fullName("Get Test")
                .department("CS")
                .barcodeType(BarcodeType.CODE_128)
                .build();

        String response = mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/profiles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Get Test"));
    }

    @Test
    void deleteProfile_ShouldReturnNoContent() throws Exception {
        ProfileRequest request = ProfileRequest.builder()
                .type(ProfileType.USER)
                .fullName("Delete Test")
                .department("IT")
                .barcodeType(BarcodeType.CODE_128)
                .build();

        String response = mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/profiles/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProfile_WithoutFullName_ShouldReturnBadRequest() throws Exception {
        ProfileRequest request = ProfileRequest.builder()
                .type(ProfileType.STUDENT)
                .department("ENG")
                .barcodeType(BarcodeType.CODE_128)
                .build();

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}