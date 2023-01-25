package server;

import user.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

public class ThreadHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;
    private final User user;
    private final List<String> COMMANDS =  Arrays.asList("register", "login", "friend",
            "send", "read", "exit", "stop", "logout", "properties");
    private String bufferMessages;

    /**
     * constructor for the thread handler
     * @param socket the socket descriptor
     * @param server the server object
     */
    public ThreadHandler(Socket socket, Server server) {
        bufferMessages = "";
        this.clientSocket = socket;
        this.user = new User();
        this.server = server;
    }
    public boolean commandExists(String command){
        for(String listedCommand : COMMANDS){
            if(command.startsWith(listedCommand)) return true;
        }
        return false;
    }


    public String read() {
        String message = bufferMessages;
        bufferMessages = "";
        return message;
    }

    public String send(){
        return "message sent";
    }

    /**
     * analyzes the command given by the user
     * @param rawCommandFromUser the unparsed command from user
     * @return the server response
     */
    public String findRoute(String rawCommandFromUser){
        Command command = new FindRoute();
        return command.execute(user, server, rawCommandFromUser.trim());
    }

    /**
     * read commands from the client thread, execute the command
     * if possible and write back the server response
     */
    @Override
    public void run()
    {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            String line;
            String response;
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.printf("$[client]: %s\n", line);

                response = findRoute(line);
                System.out.println("$[server] " + response);

                if(line.startsWith("stop")) {
                    writer.println("Server stopped...");
                    writer.close();
                    reader.close();
                    clientSocket.close();
                    server.decreaseNrOfOpenConnections();
                    server.update();
                }

                String[] lines = response.split("\n");
                writer.println(lines[0]);
            }
            System.out.println("client disconnected from server");
        }
        catch(SocketException e){
            System.out.println("client disconnected");
        }
        catch (IOException e) {
            System.out.println("disconnecting...");
        }
        finally {
            server.decreaseNrOfOpenConnections();
            server.update();
            System.out.println(server.getNrOfOpenConnections() + " connections");
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                    clientSocket.close();

                }
            }
            catch (IOException e) {
                System.out.println("client exited");
            }
        }
    }

    public void receiveMessage(String name, String message) {
        bufferMessages += name + ": " + message.trim() + "....";
    }

    public String getName() {
        return user.getName();
    }

    public User getUser() {
        return user;
    }
}

