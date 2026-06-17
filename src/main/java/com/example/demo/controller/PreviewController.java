package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/preview")
@RequiredArgsConstructor
public class PreviewController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    public String preview(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        return "idcard-preview";
    }
}