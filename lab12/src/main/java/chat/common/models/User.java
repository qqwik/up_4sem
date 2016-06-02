package chat.common.models;


public class User {
    private String id;
    private String name;
    private String password;

    private boolean isOnline;


    public User(String name, String password, String id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public User(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", isonline=" + isOnline +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
//        return super.equals(obj);
        User u = (User)obj;
        if(this.name.equals(u.getName())&&this.password.equals(u.getPassword())){
            return true;
        }else{
            return false;
        }
    }
}
