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
        EAN13Writer writer = new EAN13Writer();
        BitMatrix matrix = writer.encode(
                value,
                BarcodeFormat.EAN_13,
                350,
                80
        );
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}