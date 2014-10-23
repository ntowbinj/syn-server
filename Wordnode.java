import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Wordnode
{
    public String word;
    public ArrayList<Wordnode> neighbors;

    public Wordnode(String word)
    {
        this.word = word;
        this.neighbors = new ArrayList<Wordnode>();
    }

    public Wordnode(){
        this("");
    }

    public String toString()
    {
        return this.word;
    }

    public ArrayList<String> shortestPath(Wordnode dest, int limit)
    {
        HashSet<Wordnode> visited = new HashSet<>();
        HashMap<Wordnode, Wordnode> previous = new HashMap<>();
        LinkedList<Wordnode> q = new LinkedList<Wordnode>();
        previous.put(this, null);
        q.add(this);
        Wordnode curr = null;
        boolean found = false;
        while(!q.isEmpty() && !found)
        {
            curr = q.removeLast();
            visited.add(curr);
            if(curr == dest)
                found = true;
            else
            {
                for(Wordnode n: curr.neighbors)
                {
                    if(!visited.contains(n))
                    {
                        q.addFirst(n);
                        if(!previous.containsKey(n))
                            previous.put(n, curr);
                    }
                }
            }
        }
        if(!found)
            return null;
        ArrayList<String> ret = new ArrayList<>();
        while(curr != null)
        {
            ret.add(curr.word);
            curr = previous.get(curr);
        }
        return ret;
    }

    public List<String> shortestPath(Wordnode dest, int limit, HashSet<Wordnode> avoids)
    {
        HashSet<Wordnode> visited = new HashSet<>();
        HashMap<Wordnode, Wordnode> previous = new HashMap<>();
        LinkedList<Wordnode> q = new LinkedList<Wordnode>();
        previous.put(this, null);
        if(!avoids.contains(this))
            q.add(this);
        Wordnode curr = null;
        boolean found = false;
        while(!q.isEmpty() && !found)
        {
            curr = q.removeLast();
            visited.add(curr);
            if(curr == dest)
                found = true;
            else
            {
                for(Wordnode n: curr.neighbors)
                {
                    if(!visited.contains(n) && !avoids.contains(n))
                    {
                        q.addFirst(n);
                        if(!previous.containsKey(n))
                            previous.put(n, curr);
                    }
                }
            }
        }
        if(!found)
            return null;
        ArrayList<String> ret = new ArrayList<>();
        while(curr != null)
        {
            ret.add(curr.word);
            curr = previous.get(curr);
        }
        return ret;
    }


}
    
