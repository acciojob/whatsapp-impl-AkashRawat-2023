package com.driver;

import java.time.LocalDate;
import java.time.ZoneId;
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

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception{
        if(!userMobile.contains(mobile)){
            throw new Exception("User already exist");
        }
        User user = new User(name,mobile);
        userMobile.add(mobile);
        return "User successfully added";
    }
    public Group createGroup(List<User> users){
        String groupName = "";
        if(users.size() > 2){                        // if there are more than 2 users
            customGroupCount ++;                     //group count increases
            groupName = "Group "+ customGroupCount; // Group 1, Group 2
        }else{
            groupName = users.get(1).getName();     //If there are only 2 users than group name is name of 2nd user
        }
        Group group = new Group(groupName, users.size());
        groupUserMap.put(group,users);              //group and list of users
        adminMap.put(group,users.get(0));           // also map admin to user
        return group;
        }
    public int createMessage(String content){
        messageId++;
//        LocalDate currentDate = LocalDate.now();
//        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Message message = new Message(messageId, content);
        return message.getId();
//        messageId++;
//        LocalDate currentDate = LocalDate.now();
//        Message message = new Message(messageId,content,currentDate);
//        return  message.getId();
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        List<User> user = groupUserMap.get(group);
        if(groupUserMap.get(group) == null){
            throw new Exception("Group does not exist");
        } else if (!user.contains(sender)) {
            throw new Exception("You are not allowed to send message");
        }else{

            List<Message> list = groupMessageMap.get(group);
            list.add(message);
            groupMessageMap.put(group,list);    //add message to the group message list map
            return list.size();
        }
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(groupUserMap.get(group) == null){
            throw new Exception("Group does not exist");
        } else if (!adminMap.get(group).getName().equals(approver)) {
            throw new Exception("Approver does not have rights");
        }else{
            List<User> list = groupUserMap.get(group);
            if(!list.contains(user)){
                throw new Exception("User is not a participant");
            }
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }
    public int removeUser(User user) throws Exception{
        return 0;
    }
    public String findMessage(Date start, Date end, int K) throws Exception{
        return "";
    }


}
