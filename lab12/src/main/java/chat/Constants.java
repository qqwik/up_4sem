package chat;

public interface Constants {

    String REQUEST_PARAMS_DELIMITER = "&";
    String REQUEST_PARAM_TOKEN = "token";
    String REQUEST_PARAM_USER_RESPONCE = "users";
    String REQUEST_PARAM_MESSAGE_ID = "msgId";

    interface Message {
        String FIELD_ID = "id";
        String FIELD_AUTHOR_ID = "authorId";//authorId
        String FIELD_AUTHOR = "author";
        String FIELD_TIMESTAMP = "timestamp";
        String FIELD_TEXT = "text";
    }
    interface User {
        String FIELD_ID = "id";
        String FIELD_NAME = "name";
        String FIELD_ISONLINE = "isonline";
        String FIELD_PASSWORD = "password";
    }
}
