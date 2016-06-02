package by.bsu.up.chat.common.models;

import java.io.Serializable;

/**
 * Created by семён on 11.04.2016.
 */
public class User implements Serializable {
    private String id;
    private boolean isOnline;
    private String name;

    public String getId() {
        return id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name ='"+name+'\''+
                ", id='" + id + '\'' +
                ", isonline=" + isOnline +
                '}';
    }
}
