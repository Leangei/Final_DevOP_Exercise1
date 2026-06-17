package com.example.demo.controller;

import com.example.demo.model.BarcodeType;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileType;
import com.example.demo.model.Template;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.ProfileService;
import com.example.demo.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/web/profiles")
@RequiredArgsConstructor
public class WebProfileController {

    private final ProfileService profileService;
    private final TemplateService templateService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String list(Model model) {
        List<Profile> profiles = profileService.findAll();
        model.addAttribute("profiles", profiles);
        return "profile-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("types", ProfileType.values());
        model.addAttribute("barcodeTypes", BarcodeType.values());
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("action", "/web/profiles");
        return "profile-form";
    }

    @PostMapping
    public String create(@ModelAttribute Profile profile,
                         @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                         @RequestParam(value = "templateId", required = false) Long templateId,
                         RedirectAttributes redirectAttributes) {
        try {
            if (photoFile != null && !photoFile.isEmpty()) {
                String fileName = fileStorageService.store(photoFile);
                profile.setPhotoFileName(fileName);
                profile.setPhotoContentType(photoFile.getContentType());
            }
            if (templateId != null) {
                Template template = templateService.get(templateId);
                profile.setTemplate(template);
            }
            profile.setIssueDate(LocalDate.now());
            profileService.create(profile);
            redirectAttributes.addFlashAttribute("success", "Profile created successfully");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo: " + e.getMessage());
        }
        return "redirect:/web/profiles";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        model.addAttribute("types", ProfileType.values());
        model.addAttribute("barcodeTypes", BarcodeType.values());
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("action", "/web/profiles/" + id);
        return "profile-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Profile profile,
                         @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                         @RequestParam(value = "templateId", required = false) Long templateId,
                         RedirectAttributes redirectAttributes) {
        try {
            if (photoFile != null && !photoFile.isEmpty()) {
                String fileName = fileStorageService.store(photoFile);
                profile.setPhotoFileName(fileName);
                profile.setPhotoContentType(photoFile.getContentType());
            }
            if (templateId != null) {
                Template template = templateService.get(templateId);
                profile.setTemplate(template);
            }
            profileService.update(id, profile);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo: " + e.getMessage());
        }
        return "redirect:/web/profiles";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            profileService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Profile deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete profile: " + e.getMessage());
        }
        return "redirect:/web/profiles";
    }
}