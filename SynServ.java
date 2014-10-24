import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SynServ {

    static Thesaurus t;
    static final String NO_SUCH_WORD_MESSAGE = "error: one or both of the words entered above is not an English adjective";
    static final String NO_PATH_EXISTS_MESSAGE = "No path exists.  If you have words in the words to avoid list, try removing some.";

    public static void main(String[] args) throws Exception {
        t = new Thesaurus("./thesaurus.json");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/demo", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String response = "{'message': 'internal server error'}";
            OutputStream os = ex.getResponseBody();
            Scanner scan = new Scanner(ex.getRequestBody()).useDelimiter("\\A");
            String json = scan.hasNext() ? scan.next() : "";
            int code = 200;
            try{
                JSONObject j = new JSONObject(json);
                String start, end;
                start = j.getString("origin");
                end = j.getString("destination");
                JSONArray avoids = new JSONArray();
                JSONObject responseJson = new JSONObject();
                if(j.has("avoids")){
                    avoids = j.getJSONArray("avoids");
                }
                try{
                    responseJson.put("path", t.synpath(start, end, avoids));
                }
                catch (Thesaurus.NoSuchWordException e){
                    responseJson.put("message", NO_SUCH_WORD_MESSAGE);
                }
                catch (Thesaurus.NoPathExistsException e){
                    responseJson.put("message", NO_PATH_EXISTS_MESSAGE);
                }
                response = responseJson.toString();
            } 
            catch (JSONException e) { //doesn't happen
                e.printStackTrace();
                code = 500;
            }
            ex.sendResponseHeaders(code, response.length());
            os.write(response.getBytes());
            os.close();
        }
    }

}
