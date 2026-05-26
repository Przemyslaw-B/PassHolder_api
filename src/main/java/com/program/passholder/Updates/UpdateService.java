package com.program.passholder.Updates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;

@Service
public class UpdateService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File versionFile = new File("Updates/version.json");
    private final File updateFile = new File("Updates/app-latest.exe");

    public UpdateInfo getUpdate() {
        try {
            return mapper.readValue(versionFile, UpdateInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read update file", e);
        }
    }

    public void setUpdate(UpdateInfo updateInfo) {
        try {
            mapper.writeValue(versionFile, updateInfo);
        } catch (Exception e) {
            throw new RuntimeException("Cannot write update file", e);
        }
    }

    public Boolean checkForUpdate(String clientVersion) {
        if (clientVersion == null || clientVersion.isEmpty()) {
            return false;
        }
        UpdateInfo serverVersion = getUpdate();
        return isNewerVersion(serverVersion.getVersion(), clientVersion);
    }

    private boolean isNewerVersion(String server, String client) {
        String[] s = server.split("\\.");
        String[] c = client.split("\\.");
        int length = Math.max(s.length, c.length);
        for (int i = 0; i < length; i++) {
            int sv = i < s.length ? Integer.parseInt(s[i]) : 0;
            int cv = i < c.length ? Integer.parseInt(c[i]) : 0;
            if (sv > cv) return true;
            if (sv < cv) return false;
        }
        return false;
    }

    public Resource downloadUpdate() {
        if (!updateFile.exists()) {
            throw new RuntimeException("Update file not found");
        }
        return new FileSystemResource(updateFile);
    }

}
