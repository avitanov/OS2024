package tcp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker extends Thread{
    private Socket socket;
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

            //TODO impelement HTTP Protocol
            // Tuka prifakame Get Request
            WebRequest webRequest=WebRequest.of(reader);
            System.out.println(webRequest.command+" "+ webRequest.url);


            // Vrakame odgovor na GET REQUESTOT
            writer.write("HTTP/1.1 200 OK\n");
            writer.write("Content-Type: text/html\n\n");

            writer.write("Hello Client!" + webRequest.header.get("User-Agent"));
            writer.write("\n");
            writer.flush();

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

    public static class WebRequest{
        private String command;
        private String url;
        private String version;
        private Map<String,String> header;
        private WebRequest(String command,String url,String version,Map<String,String> header){
            this.command=command;
            this.url=url;
            this.version=version;
            this.header=header;
        }
        public static WebRequest of(BufferedReader br) throws IOException {
            List<String> input=new ArrayList<>();
            String line;
            while(!(line=br.readLine()).equals("")){
                input.add(line);
            }
            String[] args=input.get(0).split(" ");
            String command=args[0];
            String url=args[1];
            String version=args[2];
            Map<String,String> headers=new HashMap<>();

            for(int i=1;i<input.size();i++){
                args=input.get(i).split(":");
                headers.put(args[0],args[1]);
            }
            return new WebRequest(command,url,version,headers);
        }
    }


}
