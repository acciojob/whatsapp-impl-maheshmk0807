package com.driver;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository = new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        return whatsappRepository.createUser(name, mobile);
    }

    public Group createGroup(List<User> users) {
        return whatsappRepository.createGroups(users);
    }

    public int createMessage(String content) {
        return whatsappRepository.createMsg(content);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        return whatsappRepository.Sendmsg(message, sender, group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        return whatsappRepository.changeAdmin(approver, user, group);
    }
    public String findMessage(Date start, Date end, int k) {
        return null;
    }

    public int removeUser(User user) {
        return 0;
    }
}
