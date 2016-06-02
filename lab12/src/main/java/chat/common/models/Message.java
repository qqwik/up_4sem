package chat.common.models;


import java.io.Serializable;

public class Message implements Serializable {

    private String id;
    private String authorId;
    private String author;
    private long timestamp;
    private String text;

    public String getAutId() {
        return authorId;
    }

    public void setAutId(String id) {
        this.authorId = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "authorId ='"+authorId+'\''+
                ", id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                '}';
    }
}
