import org.json.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Thesaurus
{
    //Trie<Wordnode> t;
    HashMap<String, Wordnode> t;
    String path = "";

    public Thesaurus(String path) 
    {
        System.out.println("loading json from" + path);
        this.path = path;
        this.t = new HashMap<String, Wordnode>();
        StringBuffer txt = new StringBuffer();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while((line = reader.readLine()) != null)
            {
                txt.append(line);
            }
            JSONObject j = new JSONObject(txt.toString());
            Iterator<?> it = j.keys();
            while(it.hasNext())
            {
                String k = (String) it.next();
                if(!t.containsKey(k)){
                    t.put(k, new Wordnode(k));
                }
                Wordnode cur = t.get(k);
                JSONArray syns = j.getJSONArray(k);
                for(int i = 0; i<syns.length(); i++)
                {
                    String syn = syns.getString(i);
                    if(!syn.equals(cur.word))
                    {
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null) reader.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public List<Wordnode> getSynonyms(String word)
    {
        Wordnode node = t.get(word);
        return node.neighbors;
    }

    public String synpath(String start, String end, JSONArray avoids)
    {
        try{
            Wordnode origin = t.get(start);
            Wordnode dest = t.get(end);
            JSONObject retjson = new JSONObject();
            if(origin == null || dest == null)
            {
                retjson.put("message", "error: one or both of the words entered above is not an English adjective").toString();
            }
            else
            {
                List result;
                if(avoids.length() == 0)
                    result = origin.shortestPath(dest, 0);
                else
                {
                    HashSet<Wordnode> avoidsSet = new HashSet<>();
                    for(int i = 0; i<avoids.length(); i++)
                    {
                        avoidsSet.add(t.get(avoids.getString(i)));
                    }
                    result = origin.shortestPath(dest, 0, avoidsSet);
                }
                if(result == null)
                {
                    retjson.put("message", "No path exists.  If you have words in the words to avoid list, try removing some.").toString();
                }
                else
                    retjson.put("path", result).toString();
            }
            return retjson.toString();
        }
        catch(Exception e) {
           StringWriter sw = new StringWriter();
           PrintWriter pw = new PrintWriter(sw);
           e.printStackTrace(pw);
           return sw.toString(); 
        }
    }


    /*public static void main(String[] args)
    {
        Thesaurus t = new Thesaurus("thesaurus.json");
        //System.out.println(t.t.getFirst().node);
        Wordnode first = t.t.getFirst().node;
        LinkedList<LinkedList<Wordnode>> subs = new LinkedList<>();
        while(first != null)
        {
            System.out.println(first);
            LinkedList<Wordnode> sub = new LinkedList<>();
            HashSet<Wordnode> visited = new HashSet<>();
            LinkedList<Wordnode> q = new LinkedList<>();
            q.addFirst(first);
            while(!q.isEmpty())
            {
                Wordnode curr = q.removeLast();
                System.out.println(curr);
                visited.add(curr);
                t.t.remove(curr);
                for(Wordnode n: curr.neighbors)
                {
                    if(!visited.contains(n))
                        q.addFirst(n);
                }
            }
            for(Wordnode n: visited)
                sub.add(n);
            subs.add(sub);
            first = t.t.getFirst().node;
        }
        int count = 0;
        for(LinkedList<Wordnode> sub: subs)
        {
            System.out.println(count);
            System.out.println("------------------");
            for(Wordnode n: sub)
                System.out.println(n.word);
        }
    }*/
}



        
