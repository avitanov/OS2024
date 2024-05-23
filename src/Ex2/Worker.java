package Ex2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    private Socket socket;
    private static int counter=0;
    private static final Lock mutex=new ReentrantLock();
    public Worker(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try {
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Tuka prifakame Request
            List<String> input=new ArrayList<>();
            String line;
            while(!(line=reader.readLine()).equals("")){
                input.add(line);
            }

            // Vrakame odgovor na GET REQUESTOT
            if (input.get(0).equals("LOGIN")){
                writer.write("Logged in succesfully\n");

                for (int i=1;i<input.size();i++){
                    if(input.get(i).equals("LOGOUT")){
                        writer.write("Logged out succesfully\n");
                        writer.flush();
                        this.socket.close();
                    }
                    else {
                        mutex.lock();
                        counter+=1;
                        writer.write(counter+".echo-" + input.get(i) + "\n");
                        mutex.unlock();
                    }
                }

                writer.flush();
            }
            else{
                writer.write("You are not logged in!\n");
                writer.flush();
                this.socket.close();
            }





        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                reader.close();
                //Socketot mora da se zatvori za da moze taa porta da se oslobodi
                this.socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
