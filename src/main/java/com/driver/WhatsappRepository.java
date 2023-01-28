package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
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


    public int getCustomGroupCount() {
        return customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }
    public String createUser(String name, String mobile) throws Exception {
        if(userHashMap==null || userHashMap.containsKey(mobile)) {
            //THROW EXCEPTION
            throw new Exception("User already exists");
        }
        else{
            //CREATE USER
            userHashMap.put(name,new User(name,mobile));
            return "success";//c
        }
    }

    public Group createGroup(List<User> users) {
        if(users.size()>2){
            Group group = new Group("Group "+customGroupCount+1,users.size());//may throw exception if size is 1
            groupUserMap.put(group,users);
            customGroupCount++;
            adminMap.put(group,users.get(0));
            return group;
        }
        else
        {
            Group group = new Group(users.get(1).getName(),users.size());//may throw exception if size is 1
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }
    }

    public int createMessage(String content) {
        messageId++;
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(groupUserMap.containsKey(group)){
            if(groupUserMap.get(group).contains(sender)){
                //send message
                if(groupMessageMap.containsKey(group))//No prev msg in group
                {
                    groupMessageMap.get(group).add(message);
                    return groupMessageMap.get(group).size()+1;
                }
                else{ //fetch prev
                    List<Message> msgs = new ArrayList<>();
                    msgs.add(message);
                    return msgs.size()+1;
                }
            }
            else
                throw new Exception("You are not allowed to send message");
        }
        else
            throw new Exception("Group does not exist");
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(adminMap.containsKey(group)&&groupUserMap.containsKey(group)){
            if(adminMap.get(group).equals(approver)){
                if(groupUserMap.get(group).contains(user)){
                    adminMap.put(group,user);
                    return "success";
                }
                else //not group member
                    throw new Exception("User is not a participant");//c
            }
            else ///No Admin
                throw new Exception("Approver does not have rights");//
        }
        else //no group found
            throw new Exception("Group does not exist");//c
    }
}
