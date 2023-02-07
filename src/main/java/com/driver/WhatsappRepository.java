package com.driver;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;
    private HashMap<String,User> userHashMap;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
        this.userHashMap=new HashMap<>();
    }
    public String createUser(String name, String mobile) throws Exception {
        if(userHashMap==null || userHashMap.containsKey(mobile)) {
            //THROW EXCEPTION
            throw new Exception();
        }
        else{
            //CREATE USER
            userHashMap.put(mobile,new User(name,mobile));
            return "SUCCESS";
        }
    }

    public Group createGroup(List<User> users) {
        Group group;
        if(users.size()>2){
            customGroupCount++;
            group = new Group("Group " + customGroupCount, users.size());
            groupUserMap.put(group,users);
        }
        else
        {
            group = new Group(users.get(1).getName(), users.size());
            groupUserMap.put(group,users);
        }
        adminMap.put(group,users.get(0));
        return group;
    }

    public int createMessage(String content) {
        Message msg = new Message(messageId,content);
        messageId++;
        return messageId;
    }

    public int sendMassage(Message message, User sender, Group group) throws Exception {
        if(groupUserMap.containsKey(group)){
            List<User> users = groupUserMap.get(group);
            for(User u:users){
                if(Objects.equals(u.getMobile(), sender.getMobile()) && Objects.equals(u.getName(), sender.getName())){
                    List<Message>messages = new ArrayList<>();
                    if(!groupMessageMap.containsKey(group)){
                        messages.add(message);
                        groupMessageMap.put(group,messages);
                    }
                    else {
                        messages = groupMessageMap.get(group);
                        messages.add(message);
                        groupMessageMap.put(group,messages);
                    }
                    return messages.size();
                }
            }
            throw new Exception("You are not allowed to send message");
        }
        throw new Exception("Group does not exist");
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(adminMap.containsKey(group) && groupUserMap.containsKey(group)){
            if(adminMap.get(group)==approver){
                List<User> users = groupUserMap.get(group);
                for (User u: users){
                    if(u==user){
                        adminMap.put(group,user);
                        return "SUCCESS";
                    }
                }
                throw new Exception("User is not a participant");
            }
            throw new Exception("Approver does not have rights");
        }
        throw new Exception("Group does not exist");
    }
}

