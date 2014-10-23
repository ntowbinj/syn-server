import java.util.*;

public class Trie<T>{


    T node = null;
    HashMap<Character, Trie<T>> children = new HashMap<>();

    public T add(String word, T node)
    {
        if(word.isEmpty())
        {
            this.node = node;
            return this.node;
        }
        else
        {
            char c = word.charAt(0);
            Trie<T> child;
            if(!children.containsKey(c))
            {
                children.put(c, new Trie<T>());
            }
            word = word.substring(1, word.length());
            child = children.get(c);
            return child.add(word, node);
        }
    }

    public boolean remove(T n)
    {
        if(this.node != null && this.node.equals(n))
        {
            node = null;
            return true;
        }
        else{
            for(Character c: children.keySet())
            {
                if(children.get(c).remove(n))
                    return true;
            }
            return false;
        }
    }

    public T get(String word)
    {
        if(word.isEmpty())
        {
            return this.node;
        }
        else
        {
            if(children.containsKey(word.charAt(0)))
            {
                Trie<T> child = children.get(word.charAt(0));
                return child.get(word.substring(1, word.length()));
            }
            else
                return null;
        }
    }

    public Trie<T> getFirst()
    {
        if(this.node != null)
            return this;
        else
        {
            for(Character c : children.keySet())
            {
                Trie<T> result = children.get(c).getFirst();
                if(result != null)
                    return result;
            }
            return null;
        }
    }
            
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        if(this.node != null)
        {
            ret.append(this.node.toString() + "\n");
        }
        for(char k: children.keySet())
        {
            ret.append(children.get(k).toString());
        }
        return ret.toString();
    }
}





        
            






