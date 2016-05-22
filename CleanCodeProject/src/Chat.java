import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class Chat {
    List<Message> chat;
    public static final File MESSAGE_STORAGE = new File("chatLog.json");

    public Chat() {
        this.chat = new ArrayList<>();
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
        time = System.currentTimeMillis();
        chat.add(new Message(id, msg, author, time));
        writeChat();


    }

    public void sortChatChrono() throws IOException {
        Collections.sort(chat, new ComparatorChrono());
        writeChat();
        for (Message item : chat) {
            System.out.println(item);
        }
    }

    public void deleteMsg() throws IOException {
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
        System.out.println("2.Add entry");
        System.out.println("3.Delete entry");
        System.out.println("4.Search");
        System.out.println("5.View chronologically");
        System.out.println("6.Exit");
        Scanner s = new Scanner(System.in);
        try {
            int i = Integer.parseInt((s.nextLine()));
            switch (i) {
                case 1:

                    readChat();
                    break;
                case 2:
                    addMessage();
                    break;
                case 3:
                    deleteMsg();
                    break;
                case 4:
                    searchMenu();
                    break;
                case 5:
                    sortChatChrono();
                    break;
                case 6:
                    System.exit(0);

                default:

                    throw new IllegalArgumentException("Unacceptable option.");


            }
        }
        catch(NumberFormatException e){
            System.out.println("Please enter number from 1 to 6.");
        }

        menu();
    }

    public void searchMenu() {
        System.out.println("Search by");
        System.out.println("1.id");
        System.out.println("2.author");
        System.out.println("3.keyword");
        System.out.println("4.pattern");
        System.out.println("5.time period");
        System.out.println("6.Exit to Menu");
        System.out.println("6.Exit");
        Scanner s = new Scanner(System.in);
        int i = Integer.parseInt((s.nextLine()));
        switch (i) {
            case 1:

                findId();
                break;
            case 2:
                findAuthor();
                break;
            case 3:
                findByKeyword();
                break;
            case 4:
                findPattern();
                break;
            case 5:
                findTimeWindow();
                break;
            case 6:
                try {
                    menu();
                } catch (IOException e) {
                    System.out.print("Exception. Shutting down");
                    System.exit(0);
                }
                break;
            case 7:
                System.exit(0);
            default:

                throw new IllegalArgumentException("Unacceptable option.");

        }
    }

    public void findId() {
        System.out.println("Enter id");
        Scanner sc = new Scanner(System.in);
        String id = sc.next();
        for (Message item : chat) {
            if (id.equals(item.getId())) {
                System.out.println(item.toString());
            }
        }
    }

    public void findByKeyword() {
        System.out.println("Enter key word");
        Scanner sc = new Scanner(System.in);
        String k = sc.nextLine().toLowerCase();
        for (Message item : chat) {
            if (item.getMessage().toLowerCase().contains(k) ||
                    item.getAuthor().toLowerCase().contains(k)) {
                System.out.println(item.toString());
            }
        }
    }

    public void findAuthor() {
        System.out.println("Enter author");
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        for (Message item : chat) {
            if (item.getAuthor().equalsIgnoreCase(a)) {
                System.out.println(item.toString());
            }
        }
    }

    public void findPattern() {
        System.out.println("Enter pattern");
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        Pattern p = Pattern.compile(a);
        for (Message item : chat) {
            Matcher m = p.matcher(item.getMessage());
            if (m.matches()) {
                System.out.println(item.toString());
            }
        }
    }

    public void findTimeWindow() {
        System.out.println("Enter from DD/MM/YYYY");
        Scanner sc = new Scanner(System.in);
        String fr = sc.nextLine();
        System.out.println("Enter till DD/MM/YYYY");
        String tl = sc.nextLine();

        for (Message item : chat) {
            Date d = new Date(item.getTimestamp());
            if (checkDate(fr, tl, d)) {
                System.out.println(item.toString());
            }
        }
    }

    private boolean checkDate(String sFrom, String sTill, Date now) throws IllegalArgumentException {
        Date from = makeDate(sFrom);
        Date till = makeDate(sTill);

        if (now.after(from) && now.before(till)) {
            return true;
        }
        if (now.equals(from) || now.equals(till)) {
            return true;
        } else {
            return false;
        }
    }

    private Date makeDate(String s) throws IllegalArgumentException {
        int day = Integer.parseInt(s.substring(0, 2));
        int month = Integer.parseInt(s.substring(3, 5));
        int year = Integer.parseInt(s.substring(6, 10));
        GregorianCalendar newGregCal = new GregorianCalendar(year, month - 1, day);
        return new Date(newGregCal.getTimeInMillis());


    }

    public void writeLog(String message) {
        try {
            FileWriter fw = new FileWriter("log.txt", true);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ");
            formatter.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
            Date date = new Date();

            fw.write(formatter.format(date) + " ");
            fw.write(message);
            fw.close();
        } catch (IOException e) {
            System.out.println("Check log file.");
            System.out.println(e.getMessage());
        }
    }
}

