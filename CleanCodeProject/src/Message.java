import java.util.Date;

/**
 * Created by black on 16.02.2016.
 */
public class Message {
    private String id;
    private String message;
    private String author;
    private Date timestamp;


    public Message(String _id,String _message, String _author, long _timestamp){
        this.id=new String(_id);
        this.message= new String(_message);
        this.author = new String(_author);
        this.timestamp = new Date(_timestamp);
    }
    public Message(){}

    public String getId(){
        return id;
    }
    public String getMessage(){
        return message;
    }
    public String getAuthor(){
        return author;
    }
    public long getTimestamp(){
        return timestamp.getTime();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":\"");
        sb.append(id);
        sb.append("\",\"author\":\"");
        sb.append(author);
        sb.append("\",\"timestamp\":");
        sb.append(timestamp);
        sb.append(".\"message\":\"");
        sb.append(message);
        sb.append("\"}");
        return new String(sb);
    }




}
