package Ex3;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Worker extends Thread{

    private Socket socket;
    private File WORDS_FILE;
    private Set<String> words;

    private static Semaphore counterSemaphore = new Semaphore(1);

    public Worker(Socket socket, File WORDS_FILE, Set<String> words) {
        this.socket = socket;
        this.WORDS_FILE = WORDS_FILE;
        this.words=words;
    }
    private void logWord(String word, String ip, LocalDateTime time) throws FileNotFoundException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.WORDS_FILE, true)));;
        try {
            String line=word+"-"+ip+"  "+time+ "\n";
            writer.append(line);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void execute() throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        String line = null;
        List<String> lines=new ArrayList<>();
        try {
            while(!(line=socketReader.readLine()).equals("")){
                lines.add(line);
            }
            if(lines.get(0).equals("HANDSHAKE")){
                writer.write("Logged In "+ this.socket.getInetAddress()+"\n");
                for(int i=1;i<lines.size();i++){
                    if(lines.get(i).equals("STOP")){
                        counterSemaphore.acquire();
                        writer.write("WORDS: "+this.words.size()+"\n");
                        counterSemaphore.release();
                        writer.write("LOGGED OUT\n\n");
                        break;
                    }
                    else{
                        counterSemaphore.acquire();
                        if(this.words.contains(lines.get(i))){
                            writer.write(lines.get(i)+"-IMA\n");
                        }
                        else{
                            this.words.add(lines.get(i));
                            this.logWord(lines.get(i), String.valueOf(this.socket.getInetAddress()),LocalDateTime.now());
                            writer.write(lines.get(i)+"-NEMA\n");
                        }
                        counterSemaphore.release();
                    }
                }
            }
            else{
                writer.write("NOT LOGGED IN\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.flush();
                writer.close();
                socketReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
