package com.apinayami.demo.service;

import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface IBarcodeService {
    byte[] generateQR(Long id) throws IOException, DocumentException;
}
