package com.example.demo.controller;

import com.example.demo.model.Profile;
import com.example.demo.service.BatchGenerationService;
import com.example.demo.service.PdfGenerationService;
import com.example.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final ProfileService profileService;
    private final PdfGenerationService pdfGenerationService;
    private final BatchGenerationService batchGenerationService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        try {
            Profile profile = profileService.findById(id);
            byte[] pdfBytes = pdfGenerationService.generate(profile);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename",
                    "idcard-" + profile.getRegistrationNumber() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<byte[]> downloadBatch(@RequestBody List<Long> profileIds) {
        try {
            List<byte[]> pdfs = batchGenerationService.generateBatch(profileIds);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            for (int i = 0; i < pdfs.size(); i++) {
                Profile profile = profileService.findById(profileIds.get(i));
                ZipEntry entry = new ZipEntry("idcard-" + profile.getRegistrationNumber() + ".pdf");
                zos.putNextEntry(entry);
                zos.write(pdfs.get(i));
                zos.closeEntry();
            }

            zos.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("filename", "idcards-batch.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate batch PDFs", e);
        }
    }
}