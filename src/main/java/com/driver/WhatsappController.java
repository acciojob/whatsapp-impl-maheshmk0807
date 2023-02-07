package com.driver;

import java.util.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/whatsapp")
public class WhatsappController {
    WhatsappService whatsappService = new WhatsappService();
    @PostMapping("/add-user")
    public String createUser(@RequestParam("name") String name,@RequestParam("mobile") String mobile) throws Exception {
        return whatsappService.createUser(name, mobile);
    }

    @PostMapping("/add-group")
    public Group createGroup(@RequestBody List<User> users){
        return whatsappService.createGroup(users);
    }

    @PostMapping("/add-message")
    public int createMessage(@RequestParam String content){
        return whatsappService.createMessage(content);
    }

    @PutMapping("/send-message")
    public int sendMessage(@RequestBody Message message,@RequestBody User sender,@RequestBody Group group) throws Exception{
        return whatsappService.sendMessage(message, sender, group);
    }
    @PutMapping("/change-admin")
    public String changeAdmin(@RequestBody User approver,@RequestBody User user,@RequestBody Group group) throws Exception{
        return whatsappService.changeAdmin(approver, user, group);
    }
}