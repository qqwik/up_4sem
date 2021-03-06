import java.util.Date;


public class Message {
    private String id;
    private String message;
    private String author;
    private Date timestamp;

    public Message() {
    }

    public Message(String _id, String _message, String _author, long _timestamp) {
        this.id = _id;
        this.message = _message;
        this.author = _author;
        this.timestamp = new Date(_timestamp);
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp.getTime();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(author);
        sb.append("(");
        sb.append(timestamp);
        sb.append("):\n");
        sb.append(message);
        return new String(sb);
    }


}
