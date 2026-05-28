package com.program.passholder.Endpoints.UpdateRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/update")
public class GetUpdateEndpoint {
    @Value("${app.updates.path}")
    private String updatesPath;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getUpdate(@PathVariable String filename) throws IOException {
        Path updateDir = Paths.get(updatesPath);
        Path file = updateDir.resolve(filename);
        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
