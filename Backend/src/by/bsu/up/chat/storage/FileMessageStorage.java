package by.bsu.up.chat.storage;


import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;


public class FileMessageStorage implements MessageStorage {
    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();


    public FileMessageStorage(){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Message>>(){}.getType();
        try{
            Scanner sc = new Scanner(new File(DEFAULT_PERSISTENCE_FILE));
            if(sc.hasNext()){
                StringBuilder sb=new StringBuilder(sc.nextLine());
                while (sc.hasNextLine()){
                    sb.append(sc.nextLine());
                }
                messages = gson.fromJson(sb.toString(),collectionType);
            }


        } catch (FileNotFoundException e){

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
    public void addMessage(Message message) {
        messages.add(message);
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE);
            Gson gson = new Gson();
            fw.write(gson.toJson(messages));
            fw.close();
        }catch (IOException e){
            return;
        }

    }

    @Override
    public boolean updateMessage(Message message) {
        int index = contains(message.getId());
        if(index == -1)
            return  false;
        messages.get(index).setText(message.getText());
        try{
            FileWriter fw = new FileWriter(DEFAULT_PERSISTENCE_FILE);
            Gson gson = new Gson();
            fw.write(gson.toJson(messages));
            fw.close();
        }catch (IOException e){
            return false;
        }
        return true;
    }

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
}
