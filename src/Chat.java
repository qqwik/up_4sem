import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by black on 16.02.2016.
 */
public class Chat {
    List<Message> chat;
    public static final File MESSAGE_STORAGE = new File("chatLog.json");

    public Chat() {
        this.chat = new ArrayList<Message>();
    }

    public void readChat() throws IOException {
        byte[] a = Files.readAllBytes(Paths.get(MESSAGE_STORAGE.toURI()));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(a);
        TypeReference<List<Message>> typeRef = new TypeReference<List<Message>>() {
        };
        chat = mapper.readValue(node.traverse(), typeRef);

    }

    public void writeChat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
           mapper.writeValue(MESSAGE_STORAGE, chat);


    }

    public void addMessage() throws IOException {
        String id, msg, author;
        long time;

        Scanner sc = new Scanner(System.in);
        id = UUID.randomUUID().toString();
        System.out.println("message");
        msg = sc.nextLine();
        System.out.println("author");
        author = sc.nextLine();
        time =System.currentTimeMillis();
        chat.add(new Message(id, msg, author, time));
        writeChat();


    }

    public void sortChatChrono() {
        Collections.sort(chat, new ComparatorChrono());
    }

    public void deleteMsg() throws IOException{
        String id;
        System.out.println("Enter id");
        Scanner sc = new Scanner(System.in);
        id = sc.next();
        for (int i = 0; i < chat.size(); i++) {
            if (id.equals(chat.get(i).getId())) {
                chat.remove(i);
            }
        }
        writeChat();
        System.out.println("Deleted");

    }

    public void menu() throws IOException {
        System.out.println("What would you like to do?");
        System.out.println("1.Load chat log");
        System.out.println("2.Write chat log");
        System.out.println("3.Add entry");
        System.out.println("4.Delete entry");
        System.out.println("5.Find id");
        System.out.println("6.Find author");
        System.out.println("7.Find key word");
        System.out.println("8.Find by time");
        System.out.println("9.Find by pattern");
        System.out.println("10.Exit");
        Scanner s = new Scanner(System.in);
        int i = Integer.parseInt((s.nextLine()));
        switch (i) {
            case 1:

                readChat();
                break;
            case 2:

                writeChat();

                break;
            case 3:
                addMessage();
                break;
            case 4:
                deleteMsg();
                break;
            case 5:
                findId();
                break;
            case 6:
                findAuthor();
                break;
            case 7:
                findKeyword();
                break;
            case 8:
                findTimeWindow();
                break;
            case 9:
                findPattern();
                break;
            case 10:
                System.exit(0);

            default:

                throw new IllegalArgumentException("Unacceptable option.");

        }

        menu();
    }


    public void findId() {
        System.out.println("Enter id");
        Scanner sc = new Scanner(System.in);
        String id = sc.next();
        for (int i = 0; i < chat.size(); i++) {
            if (id.equals(chat.get(i).getId())) {
                System.out.println(chat.get(i).toString());
            }
        }
    }

    public void findKeyword() {
        System.out.println("Enter key word");
        Scanner sc = new Scanner(System.in);
        String k = sc.nextLine();
        for (int i = 0; i < chat.size(); i++) {
            if (chat.get(i).getMessage().toLowerCase().contains(k.toLowerCase()) ||
                    chat.get(i).getAuthor().toLowerCase().contains(k.toLowerCase()) ) {
                System.out.println(chat.get(i).toString());
            }
        }
    }

    public void findAuthor() {
        System.out.println("Enter author");
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        for (int i = 0; i < chat.size(); i++) {
            if (chat.get(i).getAuthor().equalsIgnoreCase(a) ) {
                System.out.println(chat.get(i).toString());
            }
        }
    }
    public void findPattern(){
        System.out.println("Enter pattern");
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        Pattern p = Pattern.compile(a);
        for (int i = 0; i < chat.size(); i++) {
            Matcher m = p.matcher(chat.get(i).getMessage());
            if (m.matches() ) {
                System.out.println(chat.get(i).toString());
            }
        }
    }

    public void findTimeWindow(){
        System.out.println("Enter time start");
        Scanner sc = new Scanner(System.in);
        long a = sc.nextLong();
        System.out.println("Enter time end");
        long b =sc.nextLong();

        for (int i = 0; i < chat.size(); i++) {
            if (a<=chat.get(i).getTimestamp() && chat.get(i).getTimestamp()<=b  ) {
                System.out.println(chat.get(i).toString());
            }
        }
    }

    public void writeLog(String message) throws IOException{
        FileWriter fw = new FileWriter("log.txt", true);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        Date date = new Date();
        fw.write(formatter.format(date));
        fw.write(message);
        fw.close();
    }

}