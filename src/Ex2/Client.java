package Ex2;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Thread{
    private int serverPort;
    private String serverName;
    public Client(String serverName,int serverPort){
        this.serverPort=serverPort;
        this.serverName=serverName;
    }

    @Override
    public void run() {
        Socket socket=null;
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try {
            socket=new Socket(InetAddress.getByName(this.serverName),serverPort);

            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("LOGIN\n");
            writer.write("Hello World!\n");
            writer.write("OS NETWORKING!\n");
            writer.write("LOGOUT\n\n");
            writer.flush();

            String line;
            while((line=reader.readLine())!=null){
                System.out.println("Client recieved: "+line);
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
        String port=System.getenv("SERVER_PORT");
        String name=System.getenv("SERVER_NAME");
        if (port == null){
            throw new RuntimeException("Server port should be defined as ENV {SERVER_PORT}.");
        }
        Client client=new Client(name,Integer.parseInt(port));
        client.start();

    }
}
