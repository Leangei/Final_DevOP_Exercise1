package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/web/batch")
@RequiredArgsConstructor
public class WebBatchController {

    private final ProfileService profileService;

    @GetMapping
    public String batchForm(Model model) {
        List<Profile> profiles = profileService.findAll();
        model.addAttribute("profiles", profiles);
        return "batch-generation";
    }

    @PostMapping
    public String generateBatch(@RequestParam(value = "profileIds", required = false) List<Long> profileIds, Model model) {
        List<Profile> profiles = profileService.findAll();
        model.addAttribute("profiles", profiles);
        if (profileIds == null || profileIds.isEmpty()) {
            model.addAttribute("error", "Please select at least one profile");
        } else {
            model.addAttribute("selectedIds", profileIds);
            model.addAttribute("generateUrl", "/api/pdf/batch");
        }
        return "batch-generation";
    }
}