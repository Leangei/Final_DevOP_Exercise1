package com.example.demo.controller;

import com.example.demo.model.BarcodeType;
import com.example.demo.model.Profile;
import com.example.demo.service.BarcodeService;
import com.example.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/barcode")
@RequiredArgsConstructor
public class BarcodeController {

    private final BarcodeService barcodeService;
    private final ProfileService profileService;

    @GetMapping("/{profileId}")
    public ResponseEntity<byte[]> getBarcode(@PathVariable Long profileId) {
        try {
            Profile profile = profileService.findById(profileId);
            BufferedImage barcodeImage;

            if (profile.getBarcodeType() == BarcodeType.EAN_13) {
                barcodeImage = barcodeService.generateEAN13(profile.getRegistrationNumber());
            } else {
                barcodeImage = barcodeService.generateCode128(profile.getRegistrationNumber());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeImage, "PNG", baos);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate barcode", e);
        }
    }
}