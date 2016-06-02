package chat.storage;

import chat.common.models.Message;
import chat.common.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class FileMessageStorage implements MessageStorage {
    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";
    private static final String DEFAULT_PERSISTENCE_FILE_USERS = "users.txt";//"users.srg";

//    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<Message>();
    private List<User> users = new ArrayList<User>();


    public FileMessageStorage(){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Message>>(){}.getType();
        Type collectionType1 = new TypeToken<Collection<User>>(){}.getType();
        Scanner sc;
        try{
             sc = new Scanner(new File(DEFAULT_PERSISTENCE_FILE));
            if(sc.hasNext()){
                StringBuilder sb=new StringBuilder(sc.nextLine());
                while (sc.hasNextLine()){
                    sb.append(sc.nextLine());
                }
                messages = gson.fromJson(sb.toString(),collectionType);
            }


        } catch (FileNotFoundException e){
            try{
                PrintStream ps = new PrintStream(new File(DEFAULT_PERSISTENCE_FILE));
                ps.close();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

//            logger.error("Could not find default messages or users storsge.", e);
        }
        try{
            sc = new Scanner(new File(DEFAULT_PERSISTENCE_FILE_USERS));
            if(sc.hasNext()){
                StringBuilder sb=new StringBuilder(sc.nextLine());
                while (sc.hasNextLine()){
                    sb.append(sc.nextLine());
                }
                users = gson.fromJson(sb.toString(),collectionType1);
            }
        } catch (FileNotFoundException e) {
            try{
                PrintStream ps = new PrintStream(new File(DEFAULT_PERSISTENCE_FILE_USERS));
                ps.close();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

    }




    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public synchronized void addMessage(Message message) {
        messages.add(message);
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE);
            Gson gson = new Gson();
            fw.write(gson.toJson(messages));
            fw.close();
        }catch (IOException e){
//            logger.error("error while adding massage.", e);
            return;
        }

    }

    @Override
    public boolean updateMessage(Message message) {
        int index = contains(message.getId());
        if(index == -1)
            return  false;
        messages.get(index).setText(message.getText());
        Message mesToAdd = new Message();
        mesToAdd.setText("user "+messages.get(index).getAuthor() + " change message ");
        mesToAdd.setAuthor("system");
        UUID uuid = UUID.randomUUID();
        mesToAdd.setId(uuid.toString());
        messages.add(index,mesToAdd);
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE);
            Gson gson = new Gson();
            fw.write(gson.toJson(messages));
            fw.close();
        }catch (IOException e){
//            logger.error("error while updating message.", e);
            return false;
        }
        return true;
    }

//    String uniqueId() {
//        UUID uuid = UUID.randomUUID();
//
//        //var random = Math.random() * Math.random();
//
//        return Math.floor(date * random);
//    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        int index = contains(messageId);
        if(index == -1)
            return  false;
        messages.remove(index);
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE);
            Gson gson = new Gson();
            fw.write(gson.toJson(messages));
            fw.close();
        }catch (IOException e){
//            logger.error("Could not delete message.", e);
            return false;
        }
        return true;
    }
    public synchronized boolean replaceMessage(String messageId, Message newmsg) {
        int index = contains(messageId);
        if(index == -1)
            return  false;
        //messages.remove(index);
        messages.set(index, newmsg);
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE);
            Gson gson = new Gson();
            fw.write(gson.toJson(messages));
            fw.close();
        }catch (IOException e){
//            logger.error("error while changing message.", e);
            return false;
        }
        return true;
    }
    public int contains(String str){
        for (int i = 0; i < messages.size(); i++){
            if(messages.get(i).getId().equals(str)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return messages.size();
    }

    public synchronized List<User> getUsers() {
        return users;
    }
    public int userCounter() {
        return users.size();
    }

    @Override
    public boolean updateUser(User user) {
        int index = isunique(user);
        if(index != -1){
            users.get(index).setName(user.getName());
            try{
                FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE_USERS);
                Gson gson = new Gson();
                fw.write(gson.toJson(users));
                fw.close();
                return true;
            }catch (IOException e){
//                logger.error("error while updating users.", e);
                return false;
            }
        }
        return false;
    }

    @Override
    public void addUser(User user) {
        int index = isunique(user);
        if(index == -1){
            users.add(user);
        }else{
            boolean f= users.get(index).isOnline();
            users.get(index).setIsOnline(!f);
        }
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE_USERS);
            Gson gson = new Gson();
            fw.write(gson.toJson(users));
            fw.close();
        }catch (IOException e){
//            logger.error("Could not add user.", e);
            return;
        }
    }
    int isunique(User user){
        for (int i = 0; i < users.size(); i++){
            if(users.get(i).getId().equals(user.getId())){
                return i;
            }
        }
        return -1;
    }
}