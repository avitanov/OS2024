package Ex3;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Thread{
    private int serverPort;
    public Client(int serverPort){
        this.serverPort=serverPort;
    }

    @Override
    public void run() {
        Socket socket=null;
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try {
            socket=new Socket(InetAddress.getLocalHost(),serverPort);

            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("HANDSHAKE\n");
            writer.write("TEST\n");
            writer.write("DVA\n");
            writer.write("TRI\n");
            writer.write("CETIRI\n");
            writer.write("STOP\n\n");
            writer.flush();

            String line;
            while((line=reader.readLine())!=null){
                System.out.println("Client recieved:"+line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if( reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if( writer!=null){
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Client client=new Client(7391);
        client.start();
    }
}