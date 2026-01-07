package org.akash.sofvalidator.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RarExtractorService {

    public List<String> extractFileNames(File zipFile) throws IOException {
        List<String> fileNames = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                if (!entry.isDirectory()) {
                    // 1️⃣ remove path (if any)
                    String name = new File(entry.getName()).getName();

                    // 2️⃣ normalize (.gpg removal)
                    name = normalizeFileName(name);

                    fileNames.add(name);
                }
            }
        }
        return fileNames;
    }

    // 🔹 helper method
    private String normalizeFileName(String name) {
        if (name.endsWith(".gpg")) {
            return name.substring(0, name.length() - 4);
        }
        return name;
    }
}
