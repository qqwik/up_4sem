package bsu.qqwik.chat;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "Output.json";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    public InMemoryMessageStorage() {
        messages = readFile(DEFAULT_PERSISTENCE_FILE);
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
    public boolean addMessage(Message message) {
        for (Message item: messages) {
            if (message.getId().equals(item.getId())) {

                return false;
            }
        }
        messages.add(message);
        writeFile(messages);
        return true;
    }

    @Override
    public boolean updateMessage(Message message) {
        String id = message.getId();
        for (Message item: messages) {
            if (item.getId().compareTo(id) == 0) {
                item.setText(message.getText());
                writeFile(messages);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (Message item: messages) {
            if (item.getId().compareTo(messageId) == 0) {
                messages.remove(item);
                writeFile(messages);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }

    public void writeFile(List<Message> listMessage) {
        try {
            FileWriter writer = new FileWriter(DEFAULT_PERSISTENCE_FILE, false);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(listMessage, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("There was an error writing to the file Output.json");
        }
    }


    public List<Message> readFile(String filename) {
        List<Message> listFileMessage = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type collectionType = new TypeToken<List<Message>>() {
            }.getType();
            listFileMessage = gson.fromJson(bufferedReader, collectionType);
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("There was an error reading from file.");
        }
        return listFileMessage;
    }

}
