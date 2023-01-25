package com.sothawo.mapjfxdemo;

class Client {
    private static final int PORT = 2222;
    /**
     * connect to the server using the localhost
     * and the port number, then perform write/read from server
     */
    public static void establishConnection(String[] args){
        RouteApplication.main(args);
    }

    public static void main(String[] args) {
        establishConnection(args);
    }
}