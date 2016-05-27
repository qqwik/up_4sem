package by.bsu.up.chat.logging;

public interface Logger {

    void info(String message);
    void error(String message, Throwable e);

}
