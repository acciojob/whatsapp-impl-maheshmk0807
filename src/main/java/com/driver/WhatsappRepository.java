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
        /*
        If the mobile number exists in database, throw "User already exists" exception
        Otherwise, create the user and return "SUCCESS"
        */
        if(userHashMap==null || userHashMap.containsKey(mobile)) {
            //THROW EXCEPTION
            throw new Exception("User already exists");
        }
        else{
            //CREATE USER
            userHashMap.put(name,new User(name,mobile));
            return "SUCCESS";
        }
    }

    public Group createGroup(List<User> users) {
        /*
        The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
         If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
         If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
         Note that a personal chat is not considered a group and the count is not updated for personal chats.
         If group is successfully created, return group.

        For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.
        */
        if(users.size()>2){
            Group group = new Group("Group "+customGroupCount,users.size());//may throw exception if size is 1
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
        /*
         The 'i^th' created message has message id 'i'.
         Return the message id.
         */
        //Message message = new Message(messageId,content);
        messageId++;
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        /*
        Throw "Group does not exist" if the mentioned group does not exist
        Throw "You are not allowed to send message" if the sender is not a member of the group
        If the message is sent successfully, return the final number of messages in that group.
         */
        if(groupUserMap.containsKey(group)){
            if(groupUserMap.get(group).contains(sender)){
                //send message
                if(groupMessageMap.containsKey(group))//No prev msg in group
                {
                    groupMessageMap.get(group).add(message);
                    return groupMessageMap.get(group).size();
                }
                else{ //fetch prev
                    List<Message> msgs = new ArrayList<>();
                    msgs.add(message);
                    return msgs.size();
                }
            }
            else
                throw new Exception("You are not allowed to send message");
        }
        else
            throw new Exception("Group does not exist");
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        /*
        Throw "Group does not exist" if the mentioned group does not exist
        Throw "Approver does not have rights" if the approver is not the current admin of the group
        Throw "User is not a participant" if the user is not a part of the group
        Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
         */
        if(adminMap.containsKey(group)&&groupUserMap.containsKey(group)){
            if(adminMap.get(group).equals(approver)){
                if(groupUserMap.get(group).contains(user)){
                    adminMap.put(group,user);
                    return "success";
                }
                else //not group member
                    throw new Exception("Not group member");
            }
            else ///No Admin
                throw new Exception("You are not admin");
        }
        else //no group found
            throw new Exception("Group not found");
    }
}
