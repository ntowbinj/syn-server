import org.json.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Thesaurus{

    public static class NoSuchWordException extends Exception{
        public NoSuchWordException(){
            super();
        }
    }

    public static class NoPathExistsException extends Exception{
        public NoPathExistsException(){
            super();
        }
    }

    //Map<String, Wordnode> t;
    Trie<Wordnode> t;
    String path = "";

    public Thesaurus(String path) {
        System.out.println("loading json from" + path);
        this.path = path;
        //this.t = new HashMap<String, Wordnode>();
        this.t = new Trie<Wordnode>();
        StringBuffer txt = new StringBuffer();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(path));
            String line;
            while((line = reader.readLine()) != null){
                txt.append(line);
            }
            JSONObject j = new JSONObject(txt.toString());
            Iterator<?> it = j.keys();
            while(it.hasNext()){
                String k = (String) it.next();
                if(!t.containsKey(k)){
                    t.put(k, new Wordnode(k));
                }
                Wordnode cur = t.get(k);
                JSONArray syns = j.getJSONArray(k);
                for(int i = 0; i<syns.length(); i++){
                    String syn = syns.getString(i);
                    if(!syn.equals(cur.word)){
                        if(!t.containsKey(syn)){
                            t.put(syn, new Wordnode(syn));
                        }
                        Wordnode synnode = t.get(syn);
                        if(!cur.neighbors.contains(synnode))
                            cur.neighbors.add(synnode);
                        //only if bidirectional
                        //if(!synnode.neighbors.contains(cur))
                            //synnode.neighbors.add(cur);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if (reader != null) reader.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public List<Wordnode> getSynonyms(String word){
        Wordnode node = t.get(word);
        return node.neighbors;
    }

    public List<String> synpath(String start, String end, JSONArray avoids)
        throws NoSuchWordException, NoPathExistsException, JSONException{
        Wordnode origin = t.get(start);
        Wordnode dest = t.get(end);
        JSONObject retjson = new JSONObject();
        List<String> result = null;
        if(origin == null || dest == null){
            throw new NoSuchWordException();
        }
        else{
            if(avoids.length() == 0)
                result = origin.shortestPath(dest, 0);
            else{
                Set<Wordnode> avoidsSet = new HashSet<>();
                for(int i = 0; i<avoids.length(); i++){
                    avoidsSet.add(t.get(avoids.getString(i)));
                }
                result = origin.shortestPath(dest, 0, avoidsSet);
            }
            if(result == null){
                throw new NoPathExistsException();
            }
        }
        return result;
    }
}
