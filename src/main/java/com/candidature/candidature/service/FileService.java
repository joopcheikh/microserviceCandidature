package com.candidature.candidature.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    // Chemin de base des fichiers de candidature
    private static final String BASE_DIRECTORY = "candidatures/";

    public File createZipFile(List<String> filePaths) throws IOException {
        // Crée un fichier temporaire pour stocker le ZIP
        File zipFile = File.createTempFile("candidature_files", ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String filePath : filePaths) {
                File fileToZip = new File(BASE_DIRECTORY + filePath);

                if (fileToZip.exists()) {
                    try (FileInputStream fis = new FileInputStream(fileToZip)) {
                        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                    }
                }
            }
        }

        return zipFile;
    }
}
