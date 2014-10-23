import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.io.*;

import org.json.JSONObject;
import org.json.JSONArray;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    static Thesaurus t;

    public static void main(String[] args) throws Exception {
        t = new Thesaurus("./thesaurus.json");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        //server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
        server.setExecutor(Executors.newFixedThreadPool(10)); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String response = "5";
            OutputStream os = ex.getResponseBody();
            if(true){
                StringBuffer jb = new StringBuffer();
                String line = null;
                try 
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ex.getRequestBody()));
                    while((line = reader.readLine()) != null)
                        jb.append(line);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                try 
                {
                    JSONObject j = new JSONObject(jb.toString());
                    String start, end;
                    start = j.getString("origin");
                    end = j.getString("destination");
                    JSONArray avoids = new JSONArray();
                    if(j.has("avoids"))
                    {
                        avoids = j.getJSONArray("avoids");
                    }
                    response = t.synpath(start, end, avoids);
                } catch (Exception e) //doesn't happen
                {
                    e.printStackTrace();
                    response = "{'message': 'internal server error'}";
                }
            }
            ex.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.close();
        }
    }

}
