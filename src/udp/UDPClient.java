package udp;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {

    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort, String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

        try {
            socket.send(packet);

            byte[] receiveBuffer = new byte[256];
            packet = new DatagramPacket(receiveBuffer, receiveBuffer.length,address,serverPort);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String port=System.getenv("SERVER_PORT");
        String name=System.getenv("SERVER_NAME");
        UDPClient client = new UDPClient(name, Integer.parseInt(port), "VASIL");
        client.start();
    }
}
