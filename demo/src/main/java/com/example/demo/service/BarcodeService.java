package com.example.demo.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class BarcodeService {

    public BufferedImage generateCode128(String value) throws Exception {
        Code128Writer writer = new Code128Writer();
        BitMatrix matrix = writer.encode(
                value,
                BarcodeFormat.CODE_128,
                350,
                80
        );
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public BufferedImage generateEAN13(String value) throws Exception {
        // EAN-13 requires exactly 12-13 numeric digits.
        // Extract digits from the value and pad/truncate as needed.
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() > 12) {
            digits = digits.substring(0, 12);
        }
        // Pad with leading zeros if too short (min 12 digits for EAN-13)
        digits = String.format("%-12s", digits).replace(' ', '0');

        EAN13Writer writer = new EAN13Writer();
        BitMatrix matrix = writer.encode(
                digits,
                BarcodeFormat.EAN_13,
                350,
                80
        );
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
