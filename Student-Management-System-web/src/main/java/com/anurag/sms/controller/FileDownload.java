package com.anurag.sms.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * File downloads: the CSV exports (E19, E20) and the PDFs (F7, F8).
 */
final class FileDownload {

    // Without a byte order mark, Excel reads a CSV in the PC's legacy code
    // page and garbles anything outside ASCII (accented names, the ₹ sign).
    // The importer strips it again.
    private static final String BOM = "﻿";

    private static final MediaType TEXT_CSV = new MediaType("text", "csv", StandardCharsets.UTF_8);

    private FileDownload() {
    }

    static ResponseEntity<byte[]> csv(String fileName, String csv) {
        return attachment(fileName, TEXT_CSV, (BOM + csv).getBytes(StandardCharsets.UTF_8));
    }

    static ResponseEntity<byte[]> pdf(String fileName, byte[] pdf) {
        return attachment(fileName, MediaType.APPLICATION_PDF, pdf);
    }

    private static ResponseEntity<byte[]> attachment(String fileName, MediaType type, byte[] body) {

        return ResponseEntity.ok()
                .contentType(type)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(fileName).build().toString())
                .body(body);
    }
}
