package com.svault.vault.util;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
     private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyAdmins(String message) {

        messagingTemplate.convertAndSend("/topic/admin-alerts", message);
    }
    
}
