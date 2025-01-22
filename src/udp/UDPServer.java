package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {

    private DatagramSocket socket;
    private byte[] buffer;

    public UDPServer(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.buffer = new byte[256];
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                String message;
                if(received.equals("LOGIN")){
                    message="Logged in";
                    System.out.println("User logged in");
                }
                else if( received.equals("LOGOUT")){
                    message="Logged out";
                    System.out.println("User logged out");
                }
                else{

                    message= "echo-"+received;
                    System.out.println("User message: "+received);
                }

                byte[] responseBuffer= message.getBytes();
                packet = new DatagramPacket(responseBuffer, responseBuffer.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        String port=System.getenv("SERVER_PORT");
//        UDPServer server = new UDPServer(Integer.parseInt(port));
        UDPServer server = new UDPServer(7000);
        server.start();
    }
}

