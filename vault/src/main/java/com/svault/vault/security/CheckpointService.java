package com.svault.vault.security;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class CheckpointService {
    
    private static final String CHECKPOINT_DIR =
            System.getProperty("user.dir") + "/auditCheckpoints/";

    public void writeCheckpoint(String hash) {

        try {
            File dir = new File(CHECKPOINT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            File file = new File(CHECKPOINT_DIR + "checkpoint_" + timestamp + ".txt");

            FileWriter writer = new FileWriter(file);

            writer.write("timestamp=" + timestamp + "\n");
            writer.write("hash=" + hash + "\n");

            writer.close();

            System.out.println("CHECKPOINT SAVED: " + file.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Failed to write checkpoint file", e);
        }
    }
}
