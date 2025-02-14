package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    public Server(int port){
        this.port=port;
    }

    @Override
    public void run() {
        System.out.println("SERVER: starting");
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SERVER: started");
        System.out.println("SERVER: waiting for connections");
        while (true){
            Socket socket=null;
            try {
                // mora da se izvrsi ova za povrzuvanje
                socket=serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("SERVER: new client");
            // workerot zapocnuva so rabota na serverot
            new Worker(socket).start();
        }
    }

    public static void main(String[] args) {
        Server server=new Server(7000);
        server.start();
    }
}