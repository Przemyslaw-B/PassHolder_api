package com.program.passholder.Endpoints.Updates;

import com.program.passholder.Updates.UpdateInfo;
import com.program.passholder.Updates.UpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CheckForUpdateEnpoint {
    @Autowired
    private UpdateService updateService;

    @GetMapping("/update/check")
    public ResponseEntity<Map<String, Object>> checkForUpdate(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest,
            @RequestParam String version) {
        boolean updateAvailable = updateService.checkForUpdate(version);
        UpdateInfo latest = updateService.getUpdate();
        Map<String, Object> data = Map.of(
                "update", updateAvailable,
                "latestVersion", latest.getVersion(),
                "url", latest.getUrl(),
                "sha256", latest.getSha256(),
                "notes", latest.getNotes()
        );
        return ResponseEntity.ok(data);
    }
}
