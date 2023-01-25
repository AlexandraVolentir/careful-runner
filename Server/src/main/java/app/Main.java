package app;

import server.Server;

public class Main {
    public static void start(){
        Server server = new Server();
        server.startServer();
    }

    public static void main(String[] args) {
        start();
    }
}
