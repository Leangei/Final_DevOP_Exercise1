package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.model.Template;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfGenerationService {

    private final QRCodeService qrCodeService;
    private final BarcodeService barcodeService;
    private final FileStorageService fileStorageService;

    public PdfGenerationService(QRCodeService qrCodeService, BarcodeService barcodeService, FileStorageService fileStorageService) {
        this.qrCodeService = qrCodeService;
        this.barcodeService = barcodeService;
        this.fileStorageService = fileStorageService;
    }

    public byte[] generate(Profile profile) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Template tmpl = profile.getTemplate();

        // Colors
        DeviceRgb primaryColor = new DeviceRgb(
                tmpl != null ? hexToRgb(tmpl.getPrimaryColor()) : new java.awt.Color(29, 78, 216)
        );
        DeviceRgb secondaryColor = new DeviceRgb(
                tmpl != null ? hexToRgb(tmpl.getSecondaryColor()) : new java.awt.Color(224, 231, 255)
        );
        DeviceRgb textColor = new DeviceRgb(
                tmpl != null ? hexToRgb(tmpl.getTextColor()) : new java.awt.Color(17, 24, 39)
        );

        // Header
        Paragraph header = new Paragraph(tmpl != null ? tmpl.getOrganizationName() : "ID CARD")
                .setFontColor(primaryColor)
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(header);

        if (tmpl != null && tmpl.getTagline() != null && !tmpl.getTagline().isEmpty()) {
            document.add(new Paragraph(tmpl.getTagline())
                    .setFontColor(textColor)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        document.add(new Paragraph("\n"));

        // Profile Info Table
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        // Left column - Photo
        Cell photoCell = new Cell();
        if (profile.hasPhoto()) {
            try {
                // Load photo from the uploads directory
                Path photoPath = fileStorageService.load(profile.getPhotoFileName());
                byte[] photoBytes = Files.readAllBytes(photoPath);
                ImageData imageData = ImageDataFactory.create(photoBytes);
                Image photo = new Image(imageData).scaleToFit(120, 150);
                photoCell.add(photo);
            } catch (Exception e) {
                photoCell.add(new Paragraph("[Photo]"));
            }
        } else {
            photoCell.add(new Paragraph("[No Photo]"));
        }
        table.addCell(photoCell);

        // Right column - Details
        Cell detailsCell = new Cell();
        detailsCell.add(new Paragraph(profile.getFullName())
                .setFontColor(textColor)
                .setFontSize(16)
                .setBold());
        detailsCell.add(new Paragraph("Reg No: " + profile.getRegistrationNumber())
                .setFontColor(textColor)
                .setFontSize(10));
        detailsCell.add(new Paragraph("Type: " + profile.getType())
                .setFontColor(textColor)
                .setFontSize(10));
        if (profile.getDepartment() != null) {
            detailsCell.add(new Paragraph("Dept: " + profile.getDepartment())
                    .setFontColor(textColor)
                    .setFontSize(10));
        }
        if (profile.getTitle() != null) {
            detailsCell.add(new Paragraph("Title: " + profile.getTitle())
                    .setFontColor(textColor)
                    .setFontSize(10));
        }
        if (profile.getEmail() != null) {
            detailsCell.add(new Paragraph("Email: " + profile.getEmail())
                    .setFontColor(textColor)
                    .setFontSize(10));
        }
        table.addCell(detailsCell);

        document.add(table);

        document.add(new Paragraph("\n"));

        // QR Code
        if (profile.getUuid() != null) {
            try {
                BufferedImage qrBufferedImage = qrCodeService.generateQRCode(
                        "http://localhost:8082/api/profiles/" + profile.getUuid()
                );
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(qrBufferedImage, "PNG", baos);
                ImageData qrData = ImageDataFactory.create(baos.toByteArray());
                Image qrImage = new Image(qrData).scaleToFit(100, 100);
                document.add(qrImage.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER));
            } catch (Exception e) {
                // Skip QR if error
            }
        }

        // Barcode
        if (profile.getRegistrationNumber() != null) {
            try {
                BufferedImage barcodeBufferedImage;
                if (profile.getBarcodeType() == com.example.demo.model.BarcodeType.EAN_13) {
                    barcodeBufferedImage = barcodeService.generateEAN13(profile.getRegistrationNumber());
                } else {
                    barcodeBufferedImage = barcodeService.generateCode128(profile.getRegistrationNumber());
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(barcodeBufferedImage, "PNG", baos);
                ImageData barcodeData = ImageDataFactory.create(baos.toByteArray());
                Image barcodeImage = new Image(barcodeData).scaleToFit(200, 50);
                document.add(barcodeImage.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER));
            } catch (Exception e) {
                // Skip barcode if error
            }
        }

        // Footer
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Issue Date: " + (profile.getIssueDate() != null ? profile.getIssueDate() : "N/A"))
                .setFontColor(textColor)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Expiry Date: " + (profile.getExpiryDate() != null ? profile.getExpiryDate() : "N/A"))
                .setFontColor(textColor)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER));

        document.close();
        return output.toByteArray();
    }

    private java.awt.Color hexToRgb(String hex) {
        if (hex == null || hex.isEmpty()) {
            return new java.awt.Color(0, 0, 0);
        }
        try {
            return java.awt.Color.decode(hex);
        } catch (NumberFormatException e) {
            return new java.awt.Color(0, 0, 0);
        }
    }
}