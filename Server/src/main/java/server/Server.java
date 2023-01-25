package server;

import server.ThreadHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import utils.Reader;

/**
 * the server of the app
 */
public class Server {

    private int nrOfOpenConnections = 0;
    private final int PORT = 2222;
    private volatile boolean serverStopped;
    private List<ThreadHandler> threadHandlerList;

    public Server(){
        threadHandlerList = new ArrayList<>();
    }

    /**
     * listen to the incoming connections
     * for each client start a new thread and give it a
     * function to execute
     */
    public void startServer() {
        Reader.readData("src/main/resources/paths.csv");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(240000);
            while (!serverStopped) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected to server" + clientSocket.getInetAddress().getHostAddress());
                ThreadHandler threadHandler = new ThreadHandler(clientSocket, this);
                Thread thread = new Thread(threadHandler);
                threadHandlerList.add(threadHandler);
                thread.start();
                increaseNrOfOpenConnections();
            }
        } catch (SocketException e){
            System.out.println("Client exited the server");
        } catch (IOException e) {
            System.out.println("input/output error");
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("I/O exception");
                }
            }
        }
    }

    public void resetNrOfOpenConnections(int nrOfOpenConnections) {
        this.nrOfOpenConnections = -1;
    }

    public void increaseNrOfOpenConnections() {
        this.nrOfOpenConnections++;
    }

    public void decreaseNrOfOpenConnections() {
        this.nrOfOpenConnections--;
    }

    public void endServer() {
        if(nrOfOpenConnections == 0) {
            System.exit(0);
        }
    }

    public void update() {
        if(!serverStopped) {
            serverStopped = true;
        }
        endServer();
    }

    public int getNrOfOpenConnections() {
        return nrOfOpenConnections;
    }

    public int getPORT() {
        return PORT;
    }

    public List<ThreadHandler> getThreadHandlerList() {
        return threadHandlerList;
    }
}