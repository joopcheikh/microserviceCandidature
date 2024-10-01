package com.candidature.candidature.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files/candidature")
public class FileController {

    private final String fileBasePath = "candidatures/";

    @GetMapping("/download")
public ResponseEntity<Resource> getFile(@RequestParam String filePath) {
    try {
        // Remplacer les barres obliques inverses par des barres obliques
        filePath = filePath.replace("\\", "/");
        System.out.println(filePath); //candidatures/Sosthène Mounsamboté_stanislas_6/ENOA/avis de remboursement Patrice Christ (1).pdf
        Path path = Paths.get(fileBasePath + filePath).toAbsolutePath().normalize();
        System.out.println("Attempting to load file from path: " + path.toString()); // Debugging

        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            System.out.println("File not found: " + path.toString()); // Debugging
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        e.printStackTrace(); // Log l'erreur
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

}
