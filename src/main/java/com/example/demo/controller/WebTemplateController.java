package com.example.demo.controller;

import com.example.demo.model.Template;
import com.example.demo.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web/templates")
@RequiredArgsConstructor
public class WebTemplateController {

    private final TemplateService templateService;

    @GetMapping
    public String list(Model model) {
        List<Template> templates = templateService.findAll();
        model.addAttribute("templates", templates);
        return "template-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("template", new Template());
        model.addAttribute("action", "/web/templates");
        return "template-form";
    }

    @PostMapping
    public String create(@ModelAttribute Template template, RedirectAttributes redirectAttributes) {
        templateService.create(template);
        redirectAttributes.addFlashAttribute("success", "Template created successfully");
        return "redirect:/web/templates";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            templateService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Template deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete template");
        }
        return "redirect:/web/templates";
    }
}