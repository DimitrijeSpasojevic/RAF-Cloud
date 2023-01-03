package rs.raf.rafcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.rafcloud.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MessageController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String test() {
        this.simpMessagingTemplate.convertAndSend("/topic/messages", new Message("Example name", "test"));
        return "ok";
    }
}
