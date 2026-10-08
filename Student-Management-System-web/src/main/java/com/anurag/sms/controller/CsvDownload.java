package com.anurag.sms.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * A CSV file download, shared by the student (E19) and fee (E20) exports.
 */
final class CsvDownload {

    // Without a byte order mark, Excel reads a CSV in the PC's legacy code
    // page and garbles anything outside ASCII (accented names, the ₹ sign).
    // The importer strips it again.
    private static final String BOM = "﻿";

    private static final MediaType TEXT_CSV = new MediaType("text", "csv", StandardCharsets.UTF_8);

    private CsvDownload() {
    }

    static ResponseEntity<byte[]> of(String fileName, String csv) {

        return ResponseEntity.ok()
                .contentType(TEXT_CSV)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(fileName).build().toString())
                .body((BOM + csv).getBytes(StandardCharsets.UTF_8));
    }
}
