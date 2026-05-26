package com.program.passholder.Endpoints.Updates;

import com.program.passholder.Updates.UpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GetUpdateEndpoint {
    @Autowired
    UpdateService updateService;

    @GetMapping("/update/download")
    public ResponseEntity<Resource> getUpdate(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest){
        Resource file = updateService.downloadUpdate();
        return ResponseEntity.ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=app-latest.exe")
                .body(file);
    }
}
