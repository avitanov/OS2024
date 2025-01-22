package Ex3;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

public class Server extends Thread{

    private Set<String> words;
    private String WORDS_FILE;
    private int port;

    public Server(int port, String WORDS_FILE) {
        words=new TreeSet<String>();
        this.WORDS_FILE = WORDS_FILE;
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Shared Resource Server: staring...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Shared Resource Server: started!");
        System.out.println("Shared Resource Server: waiting for connections...");

        while (true) {
            Socket socket = null;
            try {
                //accept metodot e blokiracki
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("SERVER: new client");
            new Worker(socket, new File(WORDS_FILE),this.words).start();
        }
    }

    public static void main(String[] args) {
        String serverPort = "7391";
        Server server = new Server(Integer.parseInt(serverPort),
                System.getenv("WORDS_FILE"));
        server.start();
    }
}
