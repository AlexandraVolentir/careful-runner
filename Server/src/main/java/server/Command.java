package server;

import server.Server;
import user.User;

import java.util.Arrays;
import java.util.List;

/**
 * abstract class for commands
 */
public abstract class Command {

    public abstract String execute(User user, Server server, String message);

    public static List<String> getParameter(String message, int startingChar) {
        return Arrays.asList(message.substring(startingChar).trim().split(" "));
    }
}
